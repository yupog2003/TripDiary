package com.yupog2003.tripdiary.data.documentfile;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.drive.DriveFolder;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.providers.DriveProvider;

import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.output.NullOutputStream;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RestDriveDocumentFile extends DocumentFile {

    String id;
    Drive service;
    File file;
    static final String folderMimeType = DriveFolder.MIME_TYPE;

    public RestDriveDocumentFile(DocumentFile parent, Drive service, File file) {
        super(parent);
        this.service = service;
        this.file = file;
        this.id = file.getId();
    }

    @Nullable
    @Override
    public DocumentFile createFile(String mimeType, String displayName) {
        return null;
    }

    @Nullable
    @Override
    public DocumentFile createDirectory(String displayName) {
        return null;
    }

    public String getPath() {
        if (parent != null && parent instanceof RestDriveDocumentFile) {
            return ((RestDriveDocumentFile) parent).getPath() + "/" + getName();
        } else {
            return "/" + getName();
        }
    }

    @Override
    public Uri getUri() {
        return new Uri.Builder()
                .scheme("content")
                .authority(DriveProvider.AUTHORITY)
                .path("ResourceId:" + id + getPath())
                .build();
    }

    @Override
    public String getName() {
        return file.getTitle();
    }

    @Override
    public String getType() {
        return file.getMimeType();
    }

    @Override
    public boolean isDirectory() {
        return file.getMimeType().equals(folderMimeType);
    }

    @Override
    public boolean isFile() {
        return !file.getMimeType().equals(folderMimeType);
    }

    @Override
    public long lastModified() {
        return file.getModifiedDate().getValue();
    }

    @Override
    public long length() {
        return file.getFileSize();
    }

    @Override
    public boolean canRead() {
        return true;
    }

    @Override
    public boolean canWrite() {
        return false;
    }

    @Override
    public boolean delete() {
        return false;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public boolean renameTo(String displayName) {
        return false;
    }

    private java.io.File thumbCache;

    @NonNull
    public InputStream getThumbInputStream() {
        if (!FileHelper.isPicture(file.getTitle())) return new NullInputStream(0);
        if (thumbCache != null) {
            if (shouldDeleteLocalCache) {
                thumbCache.delete();
                return new NullInputStream(0);
            }
            try {
                return new FileInputStream(thumbCache);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    String thumbnailLink = file.getThumbnailLink();
                    if (thumbnailLink != null && thumbnailLink.length() > 0) {
                        long startTime = System.currentTimeMillis();
                        InputStream is = new URL(thumbnailLink).openStream();
                        thumbCache = new java.io.File(TripDiaryApplication.instance.getCacheDir(), String.valueOf(System.currentTimeMillis()));
                        FileOutputStream fos = new FileOutputStream(thumbCache);
                        FileHelper.copyByStream(is, fos);
                        Log.i("trip", "getThumbInputStream:" + file.getTitle() + ", spend: " + String.valueOf(System.currentTimeMillis() - startTime) + " ms");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        return getInputStreamFromCache(thumbCache);
    }

    private java.io.File localCache;

    @NonNull
    @Override
    public InputStream getInputStream() {
        if (localCache != null) {
            if (shouldDeleteLocalCache) {
                localCache.delete();
                return new NullInputStream(0);
            } else {
                try {
                    return new FileInputStream(localCache);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    String downloadUrl = file.getDownloadUrl();
                    if (downloadUrl != null && downloadUrl.length() > 0) {
                        long startTime = System.currentTimeMillis();
                        HttpResponse response = service.getRequestFactory().buildGetRequest(new GenericUrl(downloadUrl)).executeAsync().get();
                        InputStream is = response.getContent();
                        localCache = new java.io.File(TripDiaryApplication.instance.getCacheDir(), String.valueOf(System.currentTimeMillis()));
                        FileOutputStream fos = new FileOutputStream(localCache);
                        FileHelper.copyByStream(is, fos);
                        Log.i("trip", "getInputStream:" + file.getTitle() + ", spend: " + String.valueOf(System.currentTimeMillis() - startTime) + " ms");
                    }
                } catch (IOException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        return getInputStreamFromCache(localCache);
    }

    private boolean shouldDeleteLocalCache = false;

    public void deleteLocalCache() {
        shouldDeleteLocalCache = true;
        if (localCache != null) {
            if (localCache.delete())
                localCache = null;
        }
        if (thumbCache != null) {
            if (thumbCache.delete()) {
                thumbCache = null;
            }
        }
    }

    @NonNull
    private InputStream getInputStreamFromCache(java.io.File cache) {
        if (cache != null) {
            if (shouldDeleteLocalCache) {
                cache.delete();
                return new NullInputStream(0);
            } else {
                try {
                    return new FileInputStream(cache);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
        return new NullInputStream(0);
    }

    @NonNull
    @Override
    public OutputStream getOutputStream() {
        return new NullOutputStream();
    }

    @NonNull
    @Override
    public DocumentFile[] listFiles(final int list_type) {
        final ArrayList<DocumentFile> children = new ArrayList<>();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    Drive.Files.List request = service.files().list().setQ("'" + id + "' in parents");
                    do {
                        FileList fileList = request.execute();
                        for (File file : fileList.getItems()) {
                            boolean accept;
                            switch (list_type) {
                                case list_all:
                                    accept = true;
                                    break;
                                case list_pics:
                                    accept = FileHelper.isPicture(file.getTitle());
                                    break;
                                case list_videos:
                                    accept = FileHelper.isVideo(file.getTitle());
                                    break;
                                case list_audios:
                                    accept = FileHelper.isAudio(file.getTitle());
                                    break;
                                case list_dirs:
                                    accept = file.getMimeType().equals(folderMimeType) && !file.getTitle().startsWith(".");
                                    break;
                                case list_withoutdots:
                                    accept = !file.getTitle().startsWith(".");
                                    break;
                                case list_memory:
                                    accept = FileHelper.isMemory(file.getTitle());
                                    break;
                                default:
                                    accept = true;
                                    break;
                            }
                            if (accept) {
                                children.add(new RestDriveDocumentFile(RestDriveDocumentFile.this, service, file));
                            }
                        }
                        request.setPageToken(fileList.getNextPageToken());
                    } while (request.getPageToken() != null && request.getPageToken().length() > 0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        return children.toArray(new DocumentFile[children.size()]);
    }
}
