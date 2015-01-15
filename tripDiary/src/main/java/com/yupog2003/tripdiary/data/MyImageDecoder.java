package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.text.TextUtils;

import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;
import com.yupog2003.tripdiary.R;

import java.io.File;
import java.io.IOException;

public class MyImageDecoder implements ImageDecoder {
    private final BaseImageDecoder m_imageUriDecoder;
    private final Context context;

    public MyImageDecoder(Context context, BaseImageDecoder imageUriDecoder) {
        if (imageUriDecoder == null) {
            throw new NullPointerException("Image decoder can't be null");
        }
        this.context = context;
        this.m_imageUriDecoder = imageUriDecoder;
    }

    @Override
    public Bitmap decode(ImageDecodingInfo info) throws IOException {
        if (TextUtils.isEmpty(info.getImageKey())) {
            return null;
        }
        String cleanedUriString = cleanUriString(info.getImageKey());
        Uri uri = Uri.parse(cleanedUriString);
        if (isVideoUri(uri)) {
            return makeVideoThumbnail(info.getTargetSize().getWidth(), info.getTargetSize().getHeight(), getVideoFilePath(uri));
        } else {
            return m_imageUriDecoder.decode(info);
        }
    }

    private Bitmap makeVideoThumbnail(int width, int height, String filePath) {
        if (filePath == null) {
            return null;
        }
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(filePath);
        Bitmap b = mmr.getFrameAtTime();
        if (b == null) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_play);
        } else {
            return scaleBitmap(b, width, height);
        }

    }

    private boolean isVideoUri(Uri uri) {
        return uri.getPath() != null && FileHelper.isVideo(new File(uri.getPath()));
    }

    private String getVideoFilePath(Uri uri) {
        return uri.getPath();
    }

    private Bitmap scaleBitmap(Bitmap origBitmap, int width, int height) {
        float scale = Math.min(
                ((float) width) / ((float) origBitmap.getWidth()),
                ((float) height) / ((float) origBitmap.getHeight())
        );
        return Bitmap.createScaledBitmap(origBitmap,
                (int) (((float) origBitmap.getWidth()) * scale),
                (int) (((float) origBitmap.getHeight()) * scale),
                false
        );
    }

    private String cleanUriString(String contentUriWithAppendedSize) {
        // replace the size at the end of the URI with an empty string.
        // the URI will be in the form "content://....._256x256
        return contentUriWithAppendedSize.replaceFirst("_\\d+x\\d+$", "");
    }
}