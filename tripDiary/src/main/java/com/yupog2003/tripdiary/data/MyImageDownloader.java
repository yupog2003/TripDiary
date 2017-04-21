package com.yupog2003.tripdiary.data;

import android.content.Context;

import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.data.documentfile.RestDriveDocumentFile;
import com.yupog2003.tripdiary.data.documentfile.WebDocumentFile;

import java.io.IOException;
import java.io.InputStream;

public class MyImageDownloader extends BaseImageDownloader {

    public MyImageDownloader(Context context) {
        super(context);
    }

    @Override
    protected InputStream getStreamFromOtherSource(String imageUri, Object extra) throws IOException {
        try {
            DocumentFile[] files = (DocumentFile[]) extra;
            DocumentFile file = FileHelper.findfile(files, imageUri.substring(imageUri.lastIndexOf("/") + 1));
            if (file instanceof WebDocumentFile && FileHelper.isPicture(file)) { //from web
                return ((WebDocumentFile) file).getThumbInputStream();
            }
            if (file instanceof RestDriveDocumentFile && FileHelper.isPicture(file)) {
                return ((RestDriveDocumentFile) file).getThumbInputStream();
            }
            return file.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return super.getStreamFromOtherSource(imageUri, extra);
    }
}
