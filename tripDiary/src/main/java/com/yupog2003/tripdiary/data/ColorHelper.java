package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;

import com.yupog2003.tripdiary.R;

public class ColorHelper {

    public static Drawable getColorDrawable(Context context, int sizeInDp, int color) {
        final int size = (int) DeviceHelper.pxFromDp(context, sizeInDp);
        final int shadowSize = (int) DeviceHelper.pxFromDp(context, 1);
        final int shadowColor = Color.argb(64, 0, 0, 0);
        Bitmap bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(color);
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setShadowLayer(shadowSize, 0, 0, shadowColor);
        canvas.drawCircle(size / 2, size / 2, (int) (size * 0.3), paint);
        return new BitmapDrawable(context.getResources(), bitmap);
    }

    public static float getMarkerColorHue(Context c) {
        int markerColor = PreferenceManager.getDefaultSharedPreferences(c).getInt("markercolor", 0xffff0000);
        float[] hsv = new float[3];
        Color.colorToHSV(markerColor, hsv);
        return hsv[0];
    }

    public static Drawable getAlertDrawable(Context c) {
        return getTintDrawable(c, R.drawable.ic_alert, Color.parseColor("#F44336"));
    }

    public static Drawable getAccentTintDrawable(Context c, int id) {
        return getTintDrawable(c, id, ContextCompat.getColor(c, R.color.accent));
    }

    public static Drawable getTintDrawable(Context c, int id, int color) {
        Drawable drawable = ContextCompat.getDrawable(c, id);
        DrawableCompat.setTint(DrawableCompat.wrap(drawable.mutate()), color);
        return drawable;
    }

}
