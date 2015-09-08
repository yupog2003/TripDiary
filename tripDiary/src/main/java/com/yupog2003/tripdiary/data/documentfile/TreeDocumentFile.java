package com.yupog2003.tripdiary.data.documentfile;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.support.annotation.NonNull;

import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.FileHelper;

import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.output.NullOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class TreeDocumentFile extends DocumentFile {

    Context context;
    Uri uri;
    boolean isFile;

    public TreeDocumentFile(TreeDocumentFile parent, Uri uri, boolean isFile) {
        super(parent);
        this.context = TripDiaryApplication.instance;
        this.uri = uri;
        this.isFile = isFile;
    }

    public TreeDocumentFile createFile(String mimeType, String displayName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uri != null) {
            DocumentFile file;
            if ((file = findFile(displayName)) != null) {
                return (TreeDocumentFile) file;
            }
            Uri result = DocumentsContract.createDocument(context.getContentResolver(), uri, mimeType, displayName);
            if (result != null) {
                return new TreeDocumentFile(this, result, true);
            }
        }
        return null;
    }

    @Override
    public TreeDocumentFile createDirectory(String displayName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uri != null) {
            DocumentFile file;
            if ((file = findFile(displayName)) != null) {
                return (TreeDocumentFile) file;
            }
            Uri result = DocumentsContract.createDocument(context.getContentResolver(), uri, DocumentsContract.Document.MIME_TYPE_DIR, displayName);
            if (result != null) {
                return new TreeDocumentFile(this, result, false);
            }
        }
        return null;
    }

    @Override
    public Uri getUri() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uri != null) {
            return uri;
        }
        return null;
    }

    @Override
    public String getName() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uri != null) {
            String path = Uri.decode(uri.toString());
            return path.substring(path.lastIndexOf("/") + 1);
        }
        return null;
    }

    @Override
    public String getType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uri != null) {
            FileHelper.getMIMEtype(Uri.decode(uri.toString()));
        }
        return null;
    }

    @Override
    public boolean isDirectory() {
        return !isFile;
    }

    @Override
    public boolean isFile() {
        return isFile;
    }

    @Override
    public long lastModified() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uri != null) {
            return queryForLong(context, uri, DocumentsContract.Document.COLUMN_LAST_MODIFIED, 0);
        }
        return 0;
    }

    @Override
    public long length() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uri != null) {
            return queryForLong(context, uri, DocumentsContract.Document.COLUMN_SIZE, 0);
        }
        return 0;
    }

    @Override
    public boolean canRead() {
        return true;
    }

    @Override
    public boolean canWrite() {
        return true;
    }

    @Override
    public boolean delete() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uri != null) {
            return DocumentsContract.deleteDocument(context.getContentResolver(), uri);
        }
        return true;
    }

    @Override
    public boolean exists() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uri != null) {
            final ContentResolver resolver = context.getContentResolver();
            Cursor c = null;
            try {
                c = resolver.query(uri, new String[]{DocumentsContract.Document.COLUMN_DOCUMENT_ID}, null, null, null);
                return c.getCount() > 0;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                closeQuietly(c);
            }
        }
        return false;
    }

    @NonNull
    @Override
    public TreeDocumentFile[] listFiles(final int list_type) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uri != null) {
            final ContentResolver resolver = context.getContentResolver();
            final Uri childrenUri = DocumentsContract.buildChildDocumentsUriUsingTree(uri, DocumentsContract.getDocumentId(uri));
            final ArrayList<TreeDocumentFile> results = new ArrayList<>();
            Cursor c = null;
            try {
                c = resolver.query(childrenUri, new String[]{DocumentsContract.Document.COLUMN_DOCUMENT_ID, DocumentsContract.Document.COLUMN_MIME_TYPE}, null, null, null);
                Filter filter = new Filter() {
                    @Override
                    public boolean accept(String name, String mimeType) {
                        switch (list_type) {
                            case list_all:
                                return true;
                            case list_pics:
                                return FileHelper.isPicture(name);
                            case list_videos:
                                return FileHelper.isVideo(name);
                            case list_audios:
                                return FileHelper.isAudio(name);
                            case list_dirs:
                                name = name.substring(name.lastIndexOf("/") + 1);
                                return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && mimeType.equals(DocumentsContract.Document.MIME_TYPE_DIR) && !name.startsWith(".");
                            case list_withoutdots:
                                name = name.substring(name.lastIndexOf("/") + 1);
                                return !name.startsWith(".");
                            case list_memory:
                                return FileHelper.isMemory(name);
                        }
                        return true;
                    }
                };
                while (c.moveToNext()) {
                    final String documentId = c.getString(0);
                    final String mimeType = c.getString(1);
                    if (filter.accept(documentId, mimeType)) {
                        final Uri documentUri = DocumentsContract.buildDocumentUriUsingTree(uri, documentId);
                        results.add(new TreeDocumentFile(this, documentUri, !mimeType.equals(DocumentsContract.Document.MIME_TYPE_DIR)));
                    }
                }
                return results.toArray(new TreeDocumentFile[results.size()]);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                closeQuietly(c);
            }
        }
        return new TreeDocumentFile[0];
    }

    @Override
    public boolean renameTo(String displayName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && uri != null) {
            Uri result = DocumentsContract.renameDocument(context.getContentResolver(), uri, displayName);
            if (result != null) {
                uri = result;
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public InputStream getInputStream() {
        try {
            return context.getContentResolver().openInputStream(getUri());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new NullInputStream(0);
    }

    @NonNull
    @Override
    public OutputStream getOutputStream() {
        try {
            return context.getContentResolver().openOutputStream(getUri());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new NullOutputStream();
    }

}
