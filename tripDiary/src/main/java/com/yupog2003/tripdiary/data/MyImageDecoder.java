package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;
import android.support.v4.provider.DocumentFile;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecodingInfo;

import java.io.IOException;

public class MyImageDecoder implements ImageDecoder {
    private final BaseImageDecoder m_imageUriDecoder;
    Bitmap playBitmap;
    Context context;

    public MyImageDecoder(Context context, BaseImageDecoder imageUriDecoder) {
        if (imageUriDecoder == null) {
            throw new NullPointerException("Image decoder can't be null");
        }
        this.context = context;
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inSampleSize = 2;
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
            return makeVideoThumbnail((DocumentFile) info.getExtraForDownloader(), info.getTargetSize().getWidth(), info.getTargetSize().getHeight(), cleanedUriString);
        } else {
            Bitmap bitmap = m_imageUriDecoder.decode(info);
            int decodedWidth = bitmap.getWidth();
            int decodedHeight = bitmap.getHeight();
            int targetWidth = info.getTargetSize().getWidth();
            int targetHeight = info.getTargetSize().getHeight();
            if (decodedWidth > decodedHeight) {
                return Bitmap.createBitmap(bitmap, (decodedWidth - targetWidth) / 2, 0, targetWidth, targetHeight);
            } else {
                return Bitmap.createBitmap(bitmap, 0, (decodedHeight - targetHeight) / 2, targetWidth, targetHeight);
            }
        }
    }

    private Bitmap makeVideoThumbnail(DocumentFile dir, int width, int height, String filePath) {
        if (filePath == null || dir == null) {
            return null;
        }
        try {
            DocumentFile videoFile = FileHelper.findfile(dir, filePath);
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(context, videoFile.getUri());
            Bitmap thumbnail = mmr.getFrameAtTime();
            Bitmap scaledThumb = scaleBitmap(thumbnail, width, height);
            thumbnail.recycle();
            return scaledThumb;
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isVideoUri(String path) {
        String ext = path.substring(path.lastIndexOf(".") + 1);
        String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext.toLowerCase());
        return mimeType.startsWith("video/");
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
        String result = contentUriWithAppendedSize.replaceFirst("_\\d+x\\d+$", "");
        return result;
    }
}