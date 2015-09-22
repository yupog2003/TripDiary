package com.yupog2003.tripdiary.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class DownloadTripService extends IntentService {

    int totalRead = 0;
    int fileSize = 0;
    static final String phpURL = TripDiaryApplication.serverURL + "/zipTrip.php";
    NotificationCompat.Builder nb;

    public DownloadTripService() {
        super("DownloadTripService");
        nb = new NotificationCompat.Builder(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String tripPath = intent.getStringExtra("path");
        String token = intent.getStringExtra("token");
        if (tripPath == null || token == null)
            return;
        final String tripName = tripPath.substring(tripPath.lastIndexOf("/") + 1);
        updateNotification(tripName, getString(R.string.zipping) + "...", 0, 0);
        try {
            tripPath = URLEncoder.encode(tripPath, "UTF-8");
            String url = phpURL + "?tripPath=" + tripPath + "&token=" + token;
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            String tripLink = IOUtils.toString(connection.getInputStream(), "UTF-8");
            String zipFileName = tripLink.substring(tripLink.lastIndexOf("/") + 1);
            tripLink = tripLink.replace(zipFileName, URLEncoder.encode(zipFileName, "UTF-8").replace("+", "%20"));
            tripLink = TripDiaryApplication.serverURL + "/" + tripLink;
            URL tripURL = new URL(tripLink);
            connection = (HttpURLConnection) tripURL.openConnection();
            fileSize = connection.getContentLength();
            InputStream is = connection.getInputStream();
            File zipFile = new File(getCacheDir(), tripName + ".zip");
            FileOutputStream fos = new FileOutputStream(zipFile);
            byte[] buffer = new byte[4096];
            int read;
            totalRead = 0;
            new Thread(new Runnable() {

                public void run() {

                    while (totalRead < fileSize) {
                        updateNotification(tripName, getString(R.string.downloading) + "...", totalRead, fileSize);
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
            while ((read = is.read(buffer)) != -1) {
                totalRead += read;
                fos.write(buffer, 0, read);
            }
            fos.flush();
            fos.close();
            is.close();
            updateNotification(tripName, getString(R.string.unzipping) + "...", totalRead, fileSize);
            FileHelper.unZip(zipFile, TripDiaryApplication.rootDocumentFile);
            zipFile.delete();
            updateNotification(tripName, getString(R.string.update_trip_timezone), totalRead, fileSize);
            DocumentFile gpxFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, tripName + ".gpx");
            if (gpxFile != null) {
                MyCalendar.updateTripTimeZoneFromGpxFile(DownloadTripService.this, tripName, gpxFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
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
        nb.setTicker("Download Trip");
        nb.setSmallIcon(R.drawable.ic_launcher);
        if (total == 0) total = 1;
        if (progress > total) progress = total;
        nb.setProgress(total, progress, false);
        startForeground(1, nb.build());
    }
}