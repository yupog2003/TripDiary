package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;

import java.io.IOException;

public class MyImageDecoder implements ImageDecoder {
    private final BaseImageDecoder m_imageUriDecoder;
    Bitmap playBitmap;

    public MyImageDecoder(Context context, BaseImageDecoder imageUriDecoder) {
        if (imageUriDecoder == null) {
            throw new NullPointerException("Image decoder can't be null");
        }
        BitmapFactory.Options o=new BitmapFactory.Options();
        o.inSampleSize=2;
        playBitmap = BitmapFactory.decodeResource(context.getResources(), android.R.drawable.ic_media_play, o);
        m_imageUriDecoder = imageUriDecoder;
    }

    @Override
    public Bitmap decode(ImageDecodingInfo info) throws IOException {
        if (TextUtils.isEmpty(info.getImageKey())) {
            return null;
        }
        String cleanedUriString = cleanUriString(info.getImageKey());
        if (isVideoUri(cleanedUriString)) {
            return makeVideoThumbnail(info.getTargetSize().getWidth(), info.getTargetSize().getHeight(), getVideoFilePath(cleanedUriString));
        } else {
            return m_imageUriDecoder.decode(info);
        }
    }

    private Bitmap makeVideoThumbnail(int width, int height, String filePath) {
        if (filePath == null) {
            return null;
        }
        Bitmap thumbnail = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Video.Thumbnails.MINI_KIND);
        Bitmap scaledThumb = scaleBitmap(thumbnail, width, height);
        thumbnail.recycle();
        return scaledThumb;
    }

    private boolean isVideoUri(String path) {
        String ext = path.substring(path.lastIndexOf(".") + 1);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());
        return mimeType.startsWith("video/");
    }

    private String getVideoFilePath(String path) {
        return Uri.parse(path).getPath();
    }

    private Bitmap scaleBitmap(Bitmap origBitmap, int width, int height) {
        float scale = Math.min(
                ((float) width) / ((float) origBitmap.getWidth()),
                ((float) height) / ((float) origBitmap.getHeight())
        );
        Bitmap result = Bitmap.createScaledBitmap(origBitmap,
                (int) (((float) origBitmap.getWidth()) * scale),
                (int) (((float) origBitmap.getHeight()) * scale),
                false
        );
        int resultWidth = result.getWidth();
        int resultHeight = result.getHeight();
        int playBitmapWidth = playBitmap.getWidth();
        int playBitmapHeight = playBitmap.getHeight();
        if (resultWidth >= playBitmapWidth && resultHeight >= playBitmapHeight) {
            Canvas canvas = new Canvas(result);
            canvas.drawBitmap(playBitmap, (resultWidth - playBitmapWidth) / 2, (resultHeight - playBitmapHeight) / 2, null);
        }
        return result;
    }

    private String cleanUriString(String contentUriWithAppendedSize) {
        // replace the size at the end of the URI with an empty string.
        // the URI will be in the form "content://....._256x256
        return contentUriWithAppendedSize.replaceFirst("_\\d+x\\d+$", "");
    }
}