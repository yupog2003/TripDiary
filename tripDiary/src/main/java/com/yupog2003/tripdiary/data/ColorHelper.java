package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

public class ColorHelper {
    public static Drawable getColorDrawable(Context context, int sizeInDp, int color) {
        int size = (int) DeviceHelper.pxFromDp(context, sizeInDp);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setMaskFilter(new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 2, 3.5f));
        canvas.drawCircle(size / 2, size / 2, (int) (size * 0.3), paint);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static float getMarkerColorHue(Context c) {
        int markerColor = PreferenceManager.getDefaultSharedPreferences(c).getInt("markercolor", 0xffff0000);
        float[] hsv = new float[3];
        Color.colorToHSV(markerColor, hsv);
        return hsv[0];
    }
}
