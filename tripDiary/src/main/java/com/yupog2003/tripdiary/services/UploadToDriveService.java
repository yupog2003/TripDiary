package com.yupog2003.tripdiary.services;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Query;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.data.documentfile.DriveDocumentFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class UploadToDriveService extends Service implements GoogleApiClient.ConnectionCallbacks {

    public static final String tag_account = "tag_account";
    public static final String tag_tripNames = "tag_tripNames";
    ArrayList<String> tripNames;
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
        tripNames = intent.getStringArrayListExtra(tag_tripNames);
        account = intent.getStringExtra(tag_account);
        nb = new NotificationCompat.Builder(this);
        nb.setProgress(0, 100, true);
        nb.setTicker(getString(R.string.upload_to_google_drive));
        nb.setSmallIcon(R.drawable.ic_cloud);
        googleApiClient = DriveDocumentFile.getDefaultGoogleApiClientBuilder(this, account)
                .addConnectionCallbacks(this)
                .build();
        googleApiClient.connect();
        startForeground(1, nb.build());
        return START_NOT_STICKY;
    }

    @Override
    public void onConnected(Bundle bundle) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                DriveFolder appRootFolder = createFolder(Drive.DriveApi.getRootFolder(googleApiClient), DriveDocumentFile.appRootFolderName);
                if (appRootFolder != null) {
                    DocumentFile[] files = TripDiaryApplication.rootDocumentFile.listFiles(DocumentFile.list_dirs);
                    String uploadToDrive = getString(R.string.upload_to_google_drive);
                    for (String tripName : tripNames) {
                        DocumentFile tripFile = FileHelper.findfile(files, tripName);
                        if (tripFile == null) continue;
                        nb.setContentTitle(uploadToDrive + "-" + tripName);
                        startForeground(1, nb.build());
                        DriveResource resource = uploadLocalFile(appRootFolder, tripFile);
                        if (resource != null) {
                            MetadataChangeSet metadataChangeSet = new MetadataChangeSet.Builder()
                                    .setCustomProperty(DriveDocumentFile.propertyKey_isTrip, "true").build();
                            resource.updateMetadata(googleApiClient, metadataChangeSet).await();
                        }
                        DeviceHelper.sendGATrack(UploadToDriveService.this, "Trip", "upload_to_drive", tripName, null);
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

    @Nullable
    private DriveResource uploadLocalFile(DriveFolder folder, DocumentFile file) {
        if (file.isDirectory()) {
            DriveFolder createdFolder = createFolder(folder, file.getName());
            if (createdFolder != null) {
                nb.setContentText(file.getName());
                startForeground(1, nb.build());
                for (DocumentFile child : file.listFiles()) {
                    uploadLocalFile(createdFolder, child);
                }
            }
            return createdFolder;
        } else {
            DriveFile createdFile = createFileAndUpload(folder, file.getName(), file.getType(), file.getInputStream());
            if (createdFile != null) {
                nb.setContentText(file.getName());
                startForeground(1, nb.build());
            }
            return createdFile;
        }
    }

    @Nullable
    private DriveFolder createFolder(@NonNull DriveFolder parentFolder, @NonNull String folderName) {
        DriveFolder resultFolder = null;
        Query query = DriveDocumentFile.getNameQuery(folderName);
        DriveApi.MetadataBufferResult metadataBufferResult = parentFolder.queryChildren(googleApiClient, query).await();
        if (metadataBufferResult.getStatus().isSuccess()) {
            for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                if (metadata.isFolder() && metadata.getTitle().equals(folderName)) {
                    resultFolder = Drive.DriveApi.getFolder(googleApiClient, metadata.getDriveId());
                    break;
                }
            }
        }
        metadataBufferResult.release();
        if (resultFolder != null) return resultFolder;
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(folderName).build();
        DriveFolder.DriveFolderResult driveFolderResult = parentFolder.createFolder(googleApiClient, changeSet).await();
        if (driveFolderResult.getStatus().isSuccess()) {
            resultFolder = driveFolderResult.getDriveFolder();
        }
        return resultFolder;
    }

    private DriveFile createFile(@NonNull DriveFolder parentFolder, @NonNull String fileName, @NonNull String mimeType) {
        Query query = DriveDocumentFile.getNameQuery(fileName);
        DriveFile resultFile = null;
        DriveApi.MetadataBufferResult metadataBufferResult = parentFolder.queryChildren(googleApiClient, query).await();
        if (metadataBufferResult.getStatus().isSuccess()) {
            for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                if (!metadata.isFolder() && metadata.getTitle().equals(fileName)) {
                    resultFile = Drive.DriveApi.getFile(googleApiClient, metadata.getDriveId());
                    break;
                }
            }
        }
        metadataBufferResult.release();
        if (resultFile != null) return resultFile;
        MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(fileName).setMimeType(mimeType).build();
        DriveFolder.DriveFileResult driveFileResult = parentFolder.createFile(googleApiClient, changeSet, null).await();
        if (driveFileResult.getStatus().isSuccess()) {
            resultFile = driveFileResult.getDriveFile();
        }
        return resultFile;
    }

    private DriveFile createFileAndUpload(@NonNull DriveFolder parentFolder, @NonNull String fileName, @NonNull String mimeType, @Nullable InputStream content) {
        if (content == null) {
            return createFile(parentFolder, fileName, mimeType);
        } else {
            DriveFile resultFile = createFile(parentFolder, fileName, mimeType);
            if (resultFile != null) {
                DriveApi.DriveContentsResult driveContentsResult = resultFile.open(googleApiClient, DriveFile.MODE_WRITE_ONLY, null).await();
                if (driveContentsResult.getStatus().isSuccess()) {
                    OutputStream outputStream = driveContentsResult.getDriveContents().getOutputStream();
                    FileHelper.copyByStream(content, outputStream);
                    driveContentsResult.getDriveContents().commit(googleApiClient, null).await();
                }
            }
            return resultFile;
        }
    }
}
