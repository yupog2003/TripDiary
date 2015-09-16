package com.yupog2003.tripdiary.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.thrift.TripDiary;

import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

public class SendTripService extends IntentService {

    static final String url = TripDiaryApplication.serverURL + "/uploadTrip.php";
    public static final String tripNameTag = "filePath";
    public static final String accountTag = "account";
    public static final String tokenTag = "token";
    public static final String publicTag = "public";

    NotificationCompat.Builder nb;
    long totalBytes = 0;
    long totalUploaded = 0;
    DocumentFile tripFile;

    public SendTripService() {
        super("SendTripService");
        nb = new NotificationCompat.Builder(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        final String tripP = intent.getStringExtra(tripNameTag);
        final String account = intent.getStringExtra(accountTag);
        final String token = intent.getStringExtra(tokenTag);
        final boolean uploadPublic = intent.getBooleanExtra(publicTag, false);
        if (tripP == null || account == null || token == null)
            return;
        tripFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripP);
        final String tripName = tripFile.getName();
        try {
            updateNotification(tripName, getString(R.string.zipping) + "...", 0, 0);
            DocumentFile zipFile = DocumentFile.fromFile(new File(getCacheDir(), tripName + ".zip"));
            zipFile.delete();
            FileHelper.zip(tripFile, zipFile);
            totalBytes = zipFile.length();
            updateNotification(tripName, getString(R.string.uploading) + "...", 0, 0);
            final MultipartUtility multipart = new MultipartUtility(url, "UTF-8");
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (totalUploaded < totalBytes) {
                        updateNotification(tripName, getString(R.string.uploading) + "...", (int) totalUploaded, (int) totalBytes);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            multipart.addFormField("account", account);
            multipart.addFormField("token", token);
            multipart.addFilePart("uploaded_file", zipFile);

            List<String> response = multipart.finish();
            if (response.size() > 0) {
                String resultStr = response.get(0);
                if (resultStr.contains("tripname=") && resultStr.contains("trippath=")) {
                    final String tripPath = resultStr.substring(resultStr.indexOf("trippath=") + 9);
                    if (uploadPublic) {
                        THttpClient transport = new THttpClient(TripDiaryApplication.serverURL + "/TripDiaryService_binary.php");
                        transport.open();
                        TProtocol protocol = new TBinaryProtocol(transport);
                        TripDiary.Client client2 = new TripDiary.Client(protocol, protocol);
                        client2.toggle_public(token, tripPath, "on");
                        resultStr = resultStr + "&public=true";
                    }
                    Intent intent1 = new Intent(Intent.ACTION_SEND);
                    intent1.setType("text/plain");
                    intent1.putExtra(Intent.EXTRA_TEXT, resultStr);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    SendTripService.this.startActivity(intent1);
                }
            }
            zipFile.delete();
            stopForeground(true);
        } catch (IOException | TException e) {
            e.printStackTrace();
            stopForeground(true);
        }
    }

    private void updateNotification(String contentTitle, String contentText, int progress, int total) {
        if (contentTitle != null)
            nb.setContentTitle(contentTitle);
        if (contentText != null) {
            if (total != 0) {
                nb.setContentText(contentText + FileHelper.getSizeProgressString(progress, total));
            } else {
                nb.setContentText(contentText);
            }
        }
        nb.setTicker("Upload Trip");
        nb.setSmallIcon(R.drawable.ic_launcher);
        if (total == 0) total = 1;
        if (progress > total) progress = total;
        nb.setProgress(total, progress, false);
        startForeground(1, nb.build());
    }

    public class MultipartUtility {
        private final String boundary;
        private static final String LINE_FEED = "\r\n";
        private HttpURLConnection httpConn;
        private String charset;
        private OutputStream outputStream;
        private PrintWriter writer;

        public MultipartUtility(String requestURL, String charset) throws IOException {
            this.charset = charset;

            // creates a unique boundary based on time stamp
            boundary = "===" + System.currentTimeMillis() + "===";

            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();
            httpConn.setUseCaches(false);
            httpConn.setDoOutput(true); // indicates POST method
            httpConn.setDoInput(true);
            httpConn.setChunkedStreamingMode(1024);
            httpConn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
            outputStream = httpConn.getOutputStream();
            writer = new PrintWriter(new OutputStreamWriter(outputStream, charset), true);
        }

        /**
         * Adds a form field to the request
         *
         * @param name  field name
         * @param value field value
         */
        public void addFormField(String name, String value) {
            writer.append("--").append(boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"").append(name).append("\"").append(LINE_FEED);
            writer.append("Content-Type: text/plain; charset=").append(charset).append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.append(value).append(LINE_FEED);
            writer.flush();
        }

        /**
         * Adds a upload file section to the request
         *
         * @param fieldName  name attribute in <input type="file" name="..." />
         * @param uploadFile a File to be uploaded
         * @throws IOException
         */
        public void addFilePart(String fieldName, DocumentFile uploadFile)
                throws IOException {
            String fileName = uploadFile.getName();
            writer.append("--").append(boundary).append(LINE_FEED);
            writer.append("Content-Disposition: form-data; name=\"").append(fieldName).append("\"; filename=\"").append(fileName).append("\"").append(LINE_FEED);
            writer.append("Content-Type: ").append(URLConnection.guessContentTypeFromName(fileName)).append(LINE_FEED);
            writer.append("Content-Transfer-Encoding: binary").append(LINE_FEED);
            writer.append(LINE_FEED);
            writer.flush();

            InputStream inputStream = uploadFile.getInputStream();
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
                totalUploaded += bytesRead;
            }
            outputStream.flush();
            inputStream.close();

            writer.append(LINE_FEED);
            writer.flush();
        }

        /**
         * Adds a header field to the request.
         *
         * @param name  - name of the header field
         * @param value - value of the header field
         */
        public void addHeaderField(String name, String value) {
            writer.append(name).append(": ").append(value).append(LINE_FEED);
            writer.flush();
        }

        /**
         * Completes the request and receives response from the server.
         *
         * @return a list of Strings as response in case the server returned
         * status OK, otherwise an exception is thrown.
         * @throws IOException
         */
        public List<String> finish() throws IOException {
            List<String> response = new ArrayList<>();

            writer.append(LINE_FEED).flush();
            writer.append("--").append(boundary).append("--").append(LINE_FEED);
            writer.close();

            // checks server's status code first
            int status = httpConn.getResponseCode();
            if (status == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(httpConn.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    response.add(line);
                }
                reader.close();
                httpConn.disconnect();
            } else {
                throw new IOException("Server returned non-OK status: " + status);
            }
            return response;
        }
    }
}
