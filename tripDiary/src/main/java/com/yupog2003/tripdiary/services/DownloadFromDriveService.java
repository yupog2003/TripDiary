package com.yupog2003.tripdiary.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.data.documentfile.DriveDocumentFile;

public class DownloadFromDriveService extends Service implements GoogleApiClient.ConnectionCallbacks {

    public static final String tag_account = "tag_account";
    public static final String tag_driveId = "tag_driveId";
    DriveId driveId;
    String account;
    GoogleApiClient googleApiClient;
    NotificationCompat.Builder nb;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        driveId = intent.getParcelableExtra(tag_driveId);
        account = intent.getStringExtra(tag_account);
        googleApiClient = DriveDocumentFile.getDefaultGoogleApiClientBuilder(this, account)
                .addConnectionCallbacks(this)
                .build();
        googleApiClient.connect();
        nb = new NotificationCompat.Builder(this);
        nb.setProgress(0, 100, true);
        nb.setTicker(getString(R.string.download_from_google_drive));
        nb.setSmallIcon(R.drawable.ic_cloud);
        startForeground(1, nb.build());
        return START_NOT_STICKY;
    }

    @Override
    public void onConnected(Bundle bundle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DriveDocumentFile driveDocumentFile = (DriveDocumentFile) DocumentFile.fromDrive(googleApiClient, driveId);
                if (driveDocumentFile != null) {
                    String tripName = driveDocumentFile.getName();
                    nb.setContentTitle(getString(R.string.download_from_google_drive) + "-" + tripName);
                    startForeground(1, nb.build());
                    DocumentFile dir = TripDiaryApplication.rootDocumentFile.findFile(tripName);
                    if (dir == null) {
                        downLoadDriveFile(TripDiaryApplication.rootDocumentFile, driveDocumentFile);
                        DocumentFile gpxFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, tripName + ".gpx");
                        if (gpxFile != null) {
                            MyCalendar.updateTripTimeZoneFromGpxFile(DownloadFromDriveService.this, tripName, gpxFile);
                        }
                    }
                }
                stopForeground(true);
                stopSelf();
            }
        }).start();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private void downLoadDriveFile(DocumentFile destDir, DriveDocumentFile driveDocumentFile) {
        if (destDir == null || driveDocumentFile == null) return;
        nb.setContentText(driveDocumentFile.getName());
        startForeground(1, nb.build());
        if (driveDocumentFile.isDirectory()) {
            DocumentFile dir = destDir.createDirectory(driveDocumentFile.getName());
            if (dir == null) return;
            for (DocumentFile childFile : driveDocumentFile.listFiles()) {
                downLoadDriveFile(dir, (DriveDocumentFile) childFile);
            }
        } else {
            DocumentFile file = destDir.createFile("", driveDocumentFile.getName());
            if (file == null) return;
            FileHelper.copyByStream(driveDocumentFile.getInputStream(), file.getOutputStream());
        }
    }
}
