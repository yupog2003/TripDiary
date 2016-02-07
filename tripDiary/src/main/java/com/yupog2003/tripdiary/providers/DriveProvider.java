package com.yupog2003.tripdiary.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.support.annotation.NonNull;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveId;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.model.File;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DriveProvider extends ContentProvider {

    public static final String AUTHORITY = "com.yupog2003.tripdiary.driveprovider";
    public static java.io.File restDriveCacheDir;

    @Override
    public boolean onCreate() {
        restDriveCacheDir = new java.io.File(getContext().getCacheDir(), "RestDriveFile");
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        return null;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        return FileHelper.getMIMEtype(uri.getPath());
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        return 0;
    }

    ParcelFileDescriptor parcelFileDescriptor;

    @Override
    public ParcelFileDescriptor openFile(final Uri uri, String mode) throws FileNotFoundException {
        try {
            if (uri.getAuthority().equals(AUTHORITY)) {
                final String id = uri.getPathSegments().get(0);
                if (id.startsWith("DriveId:")) {
                    final GoogleApiClient googleApiClient = TripDiaryApplication.googleApiClient;
                    if (googleApiClient == null || !googleApiClient.isConnected()) {
                        return null;
                    }
                    final DriveFile driveFile = DriveId.decodeFromString(id).asDriveFile();
                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            DriveApi.DriveContentsResult result = driveFile.open(googleApiClient, DriveFile.MODE_READ_ONLY, null).await();
                            if (result.getStatus().isSuccess()) {
                                parcelFileDescriptor = result.getDriveContents().getParcelFileDescriptor();
                            }
                        }
                    };
                    DeviceHelper.runOnBackgroundThread(r);
                } else if (id.startsWith("ResourceId:")) {
                    final com.google.api.services.drive.Drive service = TripDiaryApplication.service;
                    if (service == null) return null;
                    final String resourceId = uri.getPathSegments().get(0).substring(11);
                    final java.io.File cache = new java.io.File(getContext().getCacheDir(), resourceId);
                    if (!cache.exists() || cache.length() == 0) {
                        Runnable r = new Runnable() {
                            @Override
                            public void run() {
                                FileOutputStream fos = null;
                                HttpResponse response = null;
                                try {
                                    File file = service.files().get(resourceId).execute();
                                    response = service.getRequestFactory().buildGetRequest(new GenericUrl(file.getDownloadUrl())).execute();
                                    fos = new FileOutputStream(cache);
                                    response.download(fos);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    if (fos != null) {
                                        try {
                                            fos.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    if (response != null) {
                                        try {
                                            response.disconnect();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }

                            }
                        };
                        DeviceHelper.runOnBackgroundThread(r);
                    }
                    parcelFileDescriptor = ParcelFileDescriptor.open(cache, ParcelFileDescriptor.MODE_READ_ONLY);
                }
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return parcelFileDescriptor;
    }
}
