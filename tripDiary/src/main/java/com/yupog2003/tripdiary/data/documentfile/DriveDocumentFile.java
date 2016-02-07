package com.yupog2003.tripdiary.data.documentfile;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.metadata.CustomPropertyKey;
import com.google.android.gms.drive.query.Filter;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.providers.DriveProvider;

import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.output.NullOutputStream;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DriveDocumentFile extends DocumentFile {

    public DriveId driveId;
    GoogleApiClient googleApiClient;
    String fileName;
    String mimeType;
    boolean isDir;
    boolean isShared;
    long lastModified;
    long fileSize;
    public static final String appRootFolderName = "TripDiary";
    public static final CustomPropertyKey propertyKey_isTrip = new CustomPropertyKey("isTrip", CustomPropertyKey.PUBLIC);

    public DriveDocumentFile(DocumentFile parent, GoogleApiClient googleApiClient, Metadata metadata) {
        super(parent);
        this.googleApiClient = googleApiClient;
        updateFromMetaData(metadata);
    }

    public void updateFromMetaData(Metadata metadata) {
        if (metadata != null) {
            this.driveId = metadata.getDriveId();
            this.fileName = metadata.getTitle();
            this.mimeType = metadata.getMimeType();
            this.isDir = metadata.isFolder();
            this.lastModified = metadata.getModifiedDate().getTime();
            this.fileSize = metadata.getFileSize();
            this.isShared = metadata.isShared();
        }
    }

    @Nullable
    @Override
    public DocumentFile createFile(String mimeType, final String displayName) {
        if (!isDirectory()) return null;
        final ArrayList<DriveDocumentFile> driveDocumentFiles = new ArrayList<>();
        final String realMimeType = FileHelper.getMIMEtype(displayName);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                DriveFolder parentFolder = driveId.asDriveFolder();
                Query query = getNameQuery(displayName);
                DriveApi.MetadataBufferResult metadataBufferResult = parentFolder.queryChildren(googleApiClient, query).await();
                if (metadataBufferResult.getStatus().isSuccess()) {
                    for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                        if (!metadata.isFolder() && metadata.getTitle().equals(displayName)) {
                            driveDocumentFiles.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                            break;
                        }
                    }
                }
                metadataBufferResult.release();
                if (driveDocumentFiles.size() > 0) return;
                MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(displayName).setMimeType(realMimeType).build();
                DriveFolder.DriveFileResult driveFileResult = parentFolder.createFile(googleApiClient, changeSet, null).await();
                if (driveFileResult.getStatus().isSuccess()) {
                    DriveFile file = driveFileResult.getDriveFile();
                    DriveResource.MetadataResult metadataResult = file.getMetadata(googleApiClient).await();
                    if (metadataResult.getStatus().isSuccess()) {
                        Metadata metadata = metadataResult.getMetadata();
                        driveDocumentFiles.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                    }
                }
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        if (driveDocumentFiles.size() > 0) {
            return driveDocumentFiles.get(0);
        }
        return null;
    }

    @Nullable
    @Override
    public DocumentFile createDirectory(final String displayName) {
        if (!isDirectory()) return null;
        final ArrayList<DriveDocumentFile> driveDocumentFiles = new ArrayList<>();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                DriveFolder parentFolder = driveId.asDriveFolder();
                Query query = getNameQuery(displayName);
                DriveApi.MetadataBufferResult metadataBufferResult = parentFolder.queryChildren(googleApiClient, query).await();
                if (metadataBufferResult.getStatus().isSuccess()) {
                    for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                        if (metadata.isFolder() && metadata.getTitle().equals(displayName)) {
                            driveDocumentFiles.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                            break;
                        }
                    }
                }
                metadataBufferResult.release();
                if (driveDocumentFiles.size() > 0) return;
                MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(displayName).build();
                DriveFolder.DriveFolderResult driveFolderResult = parentFolder.createFolder(googleApiClient, changeSet).await();
                if (driveFolderResult.getStatus().isSuccess()) {
                    DriveFolder folder = driveFolderResult.getDriveFolder();
                    DriveResource.MetadataResult metadataResult = folder.getMetadata(googleApiClient).await();
                    if (metadataResult.getStatus().isSuccess()) {
                        Metadata metadata = metadataResult.getMetadata();
                        driveDocumentFiles.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                    }
                }
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        if (driveDocumentFiles.size() > 0) {
            return driveDocumentFiles.get(0);
        }
        return null;
    }

    public String getPath() {
        if (parent != null && parent instanceof DriveDocumentFile) {
            return ((DriveDocumentFile) parent).getPath() + "/" + getName();
        } else {
            return "/" + getName();
        }
    }

    @Override
    public Uri getUri() {
        return new Uri.Builder()
                .scheme("content")
                .authority(DriveProvider.AUTHORITY)
                .path(driveId + getPath())
                .build();
    }

    @Override
    public String getName() {
        return fileName;
    }

    @Override
    public String getType() {
        return mimeType;
    }

    @Override
    public boolean isDirectory() {
        return isDir;
    }

    @Override
    public boolean isFile() {
        return !isDir;
    }

    @Override
    public long lastModified() {
        return lastModified;
    }

    @Override
    public long length() {
        return fileSize;
    }

    @Override
    public boolean canRead() {
        return true;
    }

    @Override
    public boolean canWrite() {
        return true;
    }

    Status status;

    @Override
    public boolean delete() {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                status = driveId.asDriveResource().delete(googleApiClient).await();
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        return status.isSuccess();
    }

    @Override
    public boolean exists() {
        return true;
    }

    boolean renameSuccess;

    @Override
    public boolean renameTo(final String displayName) {
        renameSuccess = false;
        Runnable r = new Runnable() {
            @Override
            public void run() {
                MetadataChangeSet changeSet = new MetadataChangeSet.Builder().setTitle(displayName).build();
                DriveResource.MetadataResult metadataResult = driveId.asDriveResource().updateMetadata(googleApiClient, changeSet).await();
                if (metadataResult.getStatus().isSuccess()) {
                    updateFromMetaData(metadataResult.getMetadata());
                    renameSuccess = true;
                }
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        return renameSuccess;
    }

    InputStream is;

    @NonNull
    @Override
    public InputStream getInputStream() {
        if (isDirectory()) return new NullInputStream(0);
        is = new NullInputStream(0);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                DriveFile file = driveId.asDriveFile();
                DriveApi.DriveContentsResult driveContentsResult = file.open(googleApiClient, DriveFile.MODE_READ_ONLY, null).await();
                if (driveContentsResult.getStatus().isSuccess()) {
                    is = driveContentsResult.getDriveContents().getInputStream();
                }
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        return is;
    }

    OutputStream os;

    @NonNull
    @Override
    public OutputStream getOutputStream() {
        if (isDirectory()) return new NullOutputStream();
        os = new NullOutputStream();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                DriveFile file = driveId.asDriveFile();
                DriveApi.DriveContentsResult driveContentsResult = file.open(googleApiClient, DriveFile.MODE_WRITE_ONLY, null).await();
                if (driveContentsResult.getStatus().isSuccess()) {
                    os = new DriveOutputStream(driveContentsResult.getDriveContents());
                }
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        return os;
    }

    class DriveOutputStream extends OutputStream {

        OutputStream os;
        DriveContents driveContents;

        public DriveOutputStream(DriveContents driveContents) {
            this.driveContents = driveContents;
            if (driveContents != null)
                this.os = driveContents.getOutputStream();
            else
                this.os = new NullOutputStream();
        }

        @Override
        public void write(int oneByte) throws IOException {
            os.write(oneByte);
        }

        @Override
        public void close() throws IOException {
            super.close();
            if (driveContents != null) {
                Runnable r = new Runnable() {
                    @Override
                    public void run() {
                        driveContents.commit(googleApiClient, null).await();
                    }
                };
                DeviceHelper.runOnBackgroundThread(r);
            }
        }
    }


    @NonNull
    @Override
    public DocumentFile[] listFiles(final int list_type) {
        if (!isDirectory()) return new DocumentFile[0];
        final ArrayList<DocumentFile> children = new ArrayList<>();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                DriveFolder folder = driveId.asDriveFolder();
                DriveApi.MetadataBufferResult metadataBufferResult = folder.listChildren(googleApiClient).await();
                if (metadataBufferResult.getStatus().isSuccess()) {
                    switch (list_type) {
                        case list_all:
                            for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                                children.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                            }
                            break;
                        case list_pics:
                            for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                                if (!FileHelper.isPicture(metadata.getTitle())) continue;
                                children.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                            }
                            break;
                        case list_videos:
                            for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                                if (!FileHelper.isVideo(metadata.getTitle())) continue;
                                children.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                            }
                            break;
                        case list_audios:
                            for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                                if (!FileHelper.isAudio(metadata.getTitle())) continue;
                                children.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                            }
                            break;
                        case list_dirs:
                            for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                                if (!metadata.isFolder() || metadata.getTitle().startsWith("."))
                                    continue;
                                children.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                            }
                            break;
                        case list_withoutdots:
                            for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                                if (metadata.getTitle().startsWith(".")) continue;
                                children.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                            }
                            break;
                        case list_memory:
                            for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                                if (!FileHelper.isMemory(metadata.getTitle())) continue;
                                children.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                            }
                            break;
                        default:
                            for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                                children.add(new DriveDocumentFile(DriveDocumentFile.this, googleApiClient, metadata));
                            }
                            break;
                    }
                }
                metadataBufferResult.release();
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        return children.toArray(new DocumentFile[children.size()]);
    }

    @NonNull
    public static DriveDocumentFile[] listTripFiles(final GoogleApiClient googleApiClient) {
        if (googleApiClient == null || !googleApiClient.isConnected())
            return new DriveDocumentFile[0];
        final ArrayList<DriveDocumentFile> children = new ArrayList<>();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                DriveFolder parentFolder = Drive.DriveApi.getRootFolder(googleApiClient);
                Query query = getNameQuery(appRootFolderName);
                DriveApi.MetadataBufferResult metadataBufferResult = parentFolder.queryChildren(googleApiClient, query).await();
                if (metadataBufferResult.getStatus().isSuccess()) {
                    for (Metadata metadata : metadataBufferResult.getMetadataBuffer()) {
                        if (metadata.isFolder() && metadata.getTitle().equals(appRootFolderName)) {
                            DriveFolder rootFolder = metadata.getDriveId().asDriveFolder();
                            Query tripQuery = getTripQuery();
                            DriveApi.MetadataBufferResult result = rootFolder.queryChildren(googleApiClient, tripQuery).await();
                            if (result.getStatus().isSuccess()) {
                                for (Metadata metadata1 : result.getMetadataBuffer()) {
                                    children.add(new DriveDocumentFile(null, googleApiClient, metadata1));
                                }
                            }
                            result.release();
                            break;
                        }
                    }
                }
                metadataBufferResult.release();
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        return children.toArray(new DriveDocumentFile[children.size()]);
    }

    public static Query getNameQuery(String name) {
        return new Query.Builder()
                .addFilter(Filters.eq(SearchableField.TITLE, name))
                .build();
    }

    public static Query getTripQuery() {
        return new Query.Builder()
                .addFilter(getTripFilters()).build();
    }

    public static Filter getTripFilters() {
        return Filters.and(Filters.eq(SearchableField.MIME_TYPE, DriveFolder.MIME_TYPE)
                , Filters.eq(propertyKey_isTrip, "true"));
    }

    public static GoogleApiClient.Builder getDefaultGoogleApiClientBuilder(Context c, String account) {
        return new GoogleApiClient.Builder(c)
                .addApi(Drive.API)
                .addScope(Drive.SCOPE_FILE)
                .setAccountName(account);
    }

}
