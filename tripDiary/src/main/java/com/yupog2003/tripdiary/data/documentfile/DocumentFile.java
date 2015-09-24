package com.yupog2003.tripdiary.data.documentfile;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.DeviceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;

public abstract class DocumentFile {

    DocumentFile parent;

    public DocumentFile(DocumentFile parent) {
        this.parent = parent;
    }

    public DocumentFile getParentFile() {
        return parent;
    }

    @Nullable
    public abstract DocumentFile createFile(String mimeType, String displayName);

    @Nullable
    public abstract DocumentFile createDirectory(String displayName);

    public abstract Uri getUri();

    public abstract String getName();

    public abstract String getType();

    public abstract boolean isDirectory();

    public abstract boolean isFile();

    public abstract long lastModified();

    public abstract long length();

    public abstract boolean canRead();

    public abstract boolean canWrite();

    public abstract boolean delete();

    public abstract boolean exists();

    public abstract boolean renameTo(String displayName);

    @NonNull
    public abstract InputStream getInputStream();

    @NonNull
    public abstract OutputStream getOutputStream();

    public static final int list_all = -1;
    public static final int list_pics = 0;
    public static final int list_videos = 1;
    public static final int list_audios = 2;
    public static final int list_dirs = 3;
    public static final int list_withoutdots = 4;
    public static final int list_memory = 5;

    @NonNull
    public DocumentFile[] listFiles() {
        return listFiles(list_all);
    }

    @NonNull
    public abstract DocumentFile[] listFiles(final int list_type);

    @NonNull
    public String[] listFileNames(int list_type) {
        DocumentFile[] files = listFiles(list_type);
        String[] fileNames = new String[files.length];
        for (int i = 0; i < fileNames.length; i++) {
            fileNames[i] = files[i].getName();
        }
        return fileNames;
    }

    @Nullable
    public DocumentFile findFile(String name) {
        return findFile(listFiles(list_all), name);
    }

    @Nullable
    public static DocumentFile findFile(DocumentFile[] files, String name) {
        if (files == null) return null;
        for (DocumentFile file : files) {
            if (file == null) continue;
            if (name.equals(file.getName())) {
                return file;
            }
        }
        return null;
    }

    @NonNull
    public static DocumentFile fromFile(File file) {
        return new RawDocumentFile(null, file);
    }

    @Nullable
    public static DocumentFile fromTreeUri(Uri uri) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return new TreeDocumentFile(null, DocumentsContract.buildDocumentUriUsingTree(uri, DocumentsContract.getTreeDocumentId(uri)), true);
        }
        return null;
    }

    @Nullable
    public static DocumentFile fromWeb(String email, String tripName, String token) {
        if (email == null || tripName == null || token == null) return null;
        String tripDirJson = WebDocumentFile.postService("getTripWebDocumentFile", TripDiaryApplication.serverURL + "/Trips/" + email + "/" + tripName, token, "email", email, "tripName", tripName);
        if (tripDirJson != null) {
            try {
                JSONObject tripDirObject = new JSONObject(tripDirJson);
                String urlStr = URLDecoder.decode(tripDirObject.getString("self"), WebDocumentFile.charset);
                JSONArray children = tripDirObject.getJSONArray("children");
                return new WebDocumentFile(token, urlStr, null, children, true);
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Nullable
    public static DocumentFile fromDrive(final GoogleApiClient googleApiClient, final DriveId driveId) {
        if (googleApiClient == null || !googleApiClient.isConnected() || driveId == null)
            return null;
        final ArrayList<DriveDocumentFile> result = new ArrayList<>();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                DriveFolder driveFolder = Drive.DriveApi.getFolder(googleApiClient, driveId);
                DriveResource.MetadataResult metadataResult = driveFolder.getMetadata(googleApiClient).await();
                if (metadataResult.getStatus().isSuccess()) {
                    Metadata metadata = metadataResult.getMetadata();
                    result.add(new DriveDocumentFile(null, googleApiClient, metadata.getDriveId(), metadata));
                }
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public static DocumentFile fromRestDrive(final com.google.api.services.drive.Drive service, final String resourceId, final String account) {
        if (service == null || account == null || resourceId == null) return null;
        final ArrayList<RestDriveDocumentFile> result = new ArrayList<>();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                try {
                    com.google.api.services.drive.model.File file = service.files().get(resourceId).execute();
                    result.add(new RestDriveDocumentFile(null, service, file));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        DeviceHelper.runOnBackgroundThread(r);
        if (result.size() > 0) {
            return result.get(0);
        } else {
            return null;
        }
    }

    public static long queryForLong(Context context, Uri self, String column, long defaultValue) {
        final ContentResolver resolver = context.getContentResolver();
        Cursor c = null;
        try {
            c = resolver.query(self, new String[]{column}, null, null, null);
            if (c.moveToFirst() && !c.isNull(0)) {
                return c.getLong(0);
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            return defaultValue;
        } finally {
            closeQuietly(c);
        }
    }

    public static void closeQuietly(AutoCloseable closeable) {
        if (closeable != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

}
