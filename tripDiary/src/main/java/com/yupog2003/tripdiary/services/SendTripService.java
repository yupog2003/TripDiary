package com.yupog2003.tripdiary.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.yupog2003.tripdiary.MainActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyFileBody;
import com.yupog2003.tripdiary.thrift.TripDiary;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.THttpClient;
import org.apache.thrift.transport.TTransportException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class SendTripService extends IntentService {

	static final String url = MainActivity.serverURL + "/uploadTrip.php";
	public static final String filePathTag = "filePath";
	public static final String accountTag = "account";
	public static final String tokenTag = "token";
	public static final String publicTag = "public";
	NotificationCompat.Builder nb;
	int progress = 0;
	File tripFile;

	public SendTripService() {
		super("SendTripService");
		 Auto-generated constructor stub
		nb = new NotificationCompat.Builder(this);
	}

	@Override
	protected void onHandleIntent(Intent intent) {

		final String tripP = intent.getStringExtra(filePathTag);
		final String account = intent.getStringExtra(accountTag);
		final String token = intent.getStringExtra(tokenTag);
		final boolean uploadPublic = intent.getBooleanExtra(publicTag, false);
		if (tripP == null || account == null || token == null)
			return;
		tripFile = new File(tripP);
		try {
			progress = 0;
			updateNotification(tripFile.getName(), getString(R.string.zipping) + "...", progress);
			File zipFile = new File(tripFile.getParentFile().getPath() + "/" + tripFile.getName() + ".zip");
			FileHelper.zip(tripFile, zipFile);
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(url);
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setCharset(Charset.forName("UTF-8"));
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			MyFileBody fileBody = new MyFileBody(zipFile);
			final long fileSize = fileBody.getContentLength();
			fileBody.setListener(new MyFileBody.MyListener() {

				public void progressChange(long progress) {

					SendTripService.this.progress = (int) ((float) progress * 100 / fileSize);
				}
			});
			builder.addPart("uploaded_file", fileBody);
			builder.addPart("account", new StringBody(account, ContentType.TEXT_PLAIN));
			builder.addPart("token", new StringBody(token, ContentType.TEXT_PLAIN));
			post.setEntity(builder.build());
			updateNotification(tripFile.getName(), getString(R.string.uploading) + "...", progress);
			new Thread(new Runnable() {

				public void run() {

					while (progress < 100) {
						try {
							Thread.sleep(100);
						} catch (InterruptedException e) {

							e.printStackTrace();
						}
						updateNotification(tripFile.getName(), getString(R.string.uploading) + "...", progress);
					}
				}
			}).start();
			HttpResponse response = client.execute(post);
			BufferedReader br = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			String s;
			StringBuffer result = new StringBuffer();
			while ((s = br.readLine()) != null) {
				result.append(s + "\n");
			}
			br.close();
			stopForeground(true);
			String resultStr = result.toString();
			if (resultStr.contains("\n") && resultStr.contains("tripname=") && resultStr.contains("trippath=")) {
				final String returnURL = result.substring(0, result.lastIndexOf("\n"));
				final String tripPath = returnURL.substring(returnURL.indexOf("trippath=") + 9);
				if (uploadPublic) {
					try {
						THttpClient transport = new THttpClient(MainActivity.serverURL + "/TripDiaryService_binary.php");
						transport.open();
						TProtocol protocol = new TBinaryProtocol(transport);
						TripDiary.Client client2 = new TripDiary.Client(protocol, protocol);
						client2.toggle_public(token, tripPath, "on");
					} catch (TTransportException e) {

						e.printStackTrace();
					} catch (TException e) {

						e.printStackTrace();
					}
					String publicURL = returnURL + "&public=true";
					Toast.makeText(getApplicationContext(), publicURL, Toast.LENGTH_SHORT).show();
				} else {
					Intent intent1 = new Intent(Intent.ACTION_SEND);
					intent1.setType("text/plain");
					intent1.putExtra(Intent.EXTRA_TEXT, returnURL);
					intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					SendTripService.this.startActivity(intent1);
					Toast.makeText(getApplicationContext(), returnURL, Toast.LENGTH_SHORT).show();
				}
			} else {
				Toast.makeText(SendTripService.this, resultStr, Toast.LENGTH_SHORT).show();
			}
		} catch (UnsupportedEncodingException e) {

			e.printStackTrace();
			stopForeground(true);
		} catch (ClientProtocolException e) {

			e.printStackTrace();
			stopForeground(true);
		} catch (IOException e) {

			e.printStackTrace();
			stopForeground(true);
		}

	}

	private void updateNotification(String contentTitle, String contentText, int progress) {
		if (contentTitle != null)
			nb.setContentTitle(contentTitle);
		if (contentText != null)
			nb.setContentText(contentText);
		nb.setTicker("Upload Trip");
		nb.setSmallIcon(R.drawable.ic_launcher);
		nb.setProgress(100, progress, false);
		startForeground(1, nb.build());
	}
}
