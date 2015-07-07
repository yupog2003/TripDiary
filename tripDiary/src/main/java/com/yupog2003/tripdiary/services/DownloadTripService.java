package com.yupog2003.tripdiary.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.yupog2003.tripdiary.MainActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.data.FileHelper;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadTripService extends IntentService {

    int totalread = 0;
    int fileSize = 0;
    static final String phpURL = MainActivity.serverURL + "/zipTrip.php";
    NotificationCompat.Builder nb;

    public DownloadTripService() {
        super("DownloadTripService");
        nb = new NotificationCompat.Builder(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String tripPath = intent.getStringExtra("path");
        if (tripPath == null)
            return;
        final String tripName = tripPath.substring(tripPath.lastIndexOf("/") + 1);
        updateNotification(tripName, getString(R.string.zipping) + "...", 0);
        String url = phpURL + "?tripPath=" + tripPath;
        url = url.replace(" ", "%20");
        try {
            HttpURLConnection connection=(HttpURLConnection)new URL(url).openConnection();
            String tripLink= IOUtils.toString(connection.getInputStream());
            tripLink = MainActivity.serverURL + "/" + tripLink;
            tripLink = tripLink.replace(" ", "%20");
            URL tripURL = new URL(tripLink);
            connection = (HttpURLConnection) tripURL.openConnection();
            fileSize = connection.getContentLength();
            InputStream is = connection.getInputStream();
            File zipFile = new File(MainActivity.rootPath + "/" + tripName + ".zip");
            FileOutputStream fos = new FileOutputStream(zipFile);
            byte[] buffer = new byte[4096];
            int read = -1;
            totalread = 0;
            new Thread(new Runnable() {

                public void run() {

                    while (totalread < fileSize) {
                        updateNotification(tripName, getString(R.string.downloading) + "...", (int) (100f * totalread / fileSize));
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {

                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            while ((read = is.read(buffer)) != -1) {
                totalread += read;
                fos.write(buffer, 0, read);
            }
            fos.flush();
            fos.close();
            is.close();
            fileSize = 0;
            updateNotification(tripName, getString(R.string.unzipping) + "...", 100);
            FileHelper.unZip(zipFile.getPath(), MainActivity.rootPath + "/");
        } catch (IOException e) {

            e.printStackTrace();

        }

    }

    private void updateNotification(String contentTitle, String contentText, int progress) {
        if (contentTitle != null)
            nb.setContentTitle(contentTitle);
        if (contentText != null)
            nb.setContentText(contentText);
        nb.setTicker("Download Trip");
        nb.setSmallIcon(R.drawable.ic_launcher);
        nb.setProgress(100, progress, false);
        startForeground(1, nb.build());
    }
}