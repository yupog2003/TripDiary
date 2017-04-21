package com.yupog2003.tripdiary.data;

import android.widget.ImageView;

import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.nostra13.universalimageloader.utils.L;

import java.lang.reflect.Field;

public class MyImageViewAware extends ImageViewAware {

    public MyImageViewAware(ImageView imageView) {
        super(imageView);
    }

    public MyImageViewAware(ImageView imageView, boolean checkActualViewSize) {
        super(imageView, checkActualViewSize);
    }

    @Override
    public int getWidth() {
        ImageView imageView = (ImageView) this.viewRef.get();
        if (imageView != null) {
            return getImageViewFieldValue(imageView, "mMaxWidth");
        }
        return 0;
    }

    @Override
    public int getHeight() {
        ImageView imageView = (ImageView) this.viewRef.get();
        if (imageView != null) {
            return getImageViewFieldValue(imageView, "mMaxHeight");
        }
        return 0;
    }

    private static int getImageViewFieldValue(Object object, String fieldName) {
        int value = 0;

        try {
            Field e = ImageView.class.getDeclaredField(fieldName);
            e.setAccessible(true);
            int fieldValue = (Integer) e.get(object);
            if (fieldValue > 0 && fieldValue < 2147483647) {
                value = fieldValue;
            }
        } catch (Exception var5) {
            L.e(var5);
        }

        return value;
    }
}
