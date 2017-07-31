package com.yupog2003.tripdiary.data.documentfile;

import android.net.Uri;
import android.support.annotation.NonNull;

import com.yupog2003.tripdiary.data.FileHelper;

import org.apache.commons.io.input.NullInputStream;
import org.apache.commons.io.output.NullOutputStream;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class RawDocumentFile extends DocumentFile {

    File file;

    public RawDocumentFile(RawDocumentFile parent, File file) {
        super(parent);
        this.file = file;
    }

    @Override
    public RawDocumentFile createFile(String mimeType, String displayName) {
        if (file != null) {
            File target = new File(file, displayName);
            try {
                if (target.createNewFile()) {
                    return new RawDocumentFile(this, target);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public RawDocumentFile createDirectory(String displayName) {
        if (file != null) {
            File target = new File(file, displayName);
            if (target.isDirectory() || target.mkdirs()) {
                return new RawDocumentFile(this, target);
            }
        }
        return null;
    }

    @Override
    public Uri getUri() {
        if (file != null) {
            return FileHelper.getUriFromFile(file);
        }
        return null;
    }

    @Override
    public String getName() {
        if (file != null) {
            return file.getName();
        }
        return null;
    }

    @Override
    public String getType() {
        if (file != null) {
            if (file.isDirectory()) {
                return null;
            } else {
                return FileHelper.getMimeFromFile(file);
            }
        }
        return null;
    }

    @Override
    public boolean isDirectory() {
        return file != null && file.isDirectory();
    }

    @Override
    public boolean isFile() {
        return file != null && file.isFile();
    }

    @Override
    public long lastModified() {
        if (file != null) {
            return file.lastModified();
        }
        return 0;
    }

    @Override
    public long length() {
        if (file != null) {
            return file.length();
        }
        return 0;
    }

    @Override
    public boolean canRead() {
        return file != null && file.canRead();
    }

    @Override
    public boolean canWrite() {
        return file != null && file.canWrite();
    }

    @Override
    public boolean delete() {
        if (file != null) {
            FileHelper.deleteDir(file);
        }
        return true;
    }

    @Override
    public boolean exists() {
        return file != null && file.exists();
    }

    @NonNull
    @Override
    public RawDocumentFile[] listFiles(final int list_type) {
        if (file != null) {
            File[] files = file.listFiles(getFilterFromListType(list_type));
            if (files == null) {
                return new RawDocumentFile[0];
            }
            RawDocumentFile[] result = new RawDocumentFile[files.length];
            for (int i = 0; i < result.length; i++) {
                result[i] = new RawDocumentFile(this, files[i]);
            }
            return result;
        }
        return new RawDocumentFile[0];
    }

    @NonNull
    private FilenameFilter getFilterFromListType(int list_type) {
        switch (list_type) {
            case list_all:
                return new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String fileName) {
                        return true;
                    }
                };
            case list_pics:
                return new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String fileName) {
                        return FileHelper.isPicture(fileName);
                    }
                };
            case list_videos:
                return new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String fileName) {
                        return FileHelper.isVideo(fileName);
                    }
                };
            case list_audios:
                return new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String fileName) {
                        return FileHelper.isAudio(fileName);
                    }
                };
            case list_dirs:
                return new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String fileName) {
                        return new File(dir, fileName).isDirectory() && !fileName.startsWith(".");
                    }
                };
            case list_withoutdots:
                return new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String fileName) {
                        String realName = fileName.substring(fileName.lastIndexOf("/") + 1);
                        return !realName.startsWith(".");
                    }
                };
            case list_memory:
                return new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String fileName) {
                        return FileHelper.isMemory(fileName);
                    }
                };
            default:
                return new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String fileName) {
                        return true;
                    }
                };
        }
    }

    @Override
    public boolean renameTo(String displayName) {
        if (file != null) {
            File target = new File(file.getParentFile(), displayName);
            if (file.renameTo(target)) {
                file = target;
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
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new NullInputStream(0);
    }

    @NonNull
    @Override
    public OutputStream getOutputStream() {
        try {
            return new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new NullOutputStream();
    }
}
