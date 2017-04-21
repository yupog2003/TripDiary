package com.yupog2003.tripdiary.data;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.yupog2003.tripdiary.TripDiaryApplication;

public class DeviceHelper {

    public static int getScreenWidth(Activity activity) {
        return getDisPlayMetrics(activity).widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        return getDisPlayMetrics(activity).heightPixels;
    }

    private static DisplayMetrics getDisPlayMetrics(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    public static float dpFromPx(Context c, float px) {
        if (c == null)
            return 0;
        return px / c.getResources().getDisplayMetrics().density;
    }

    public static float pxFromDp(Context c, float dp) {
        if (c == null)
            return 0;
        return dp * c.getResources().getDisplayMetrics().density;
    }

    public static float pxFromSp(Context c, float sp) {
        if (c == null)
            return 0;
        return sp * c.getResources().getDisplayMetrics().scaledDensity;
    }

    public static boolean isMobileNetworkAvailable(Context context) {
        return getActiveNetworkType(context) != -1;
    }

    public static int getActiveNetworkType(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = manager.getActiveNetworkInfo();
        if (info != null && info.isConnected()) {
            return info.getType();
        }
        return -1;
    }

    public static void sendGATrack(Application app, String category, String action, String label, Long value) {
        Tracker t = ((TripDiaryApplication) app).getTracker();
        if (t == null) return;
        HitBuilders.EventBuilder builders = new HitBuilders.EventBuilder();
        if (category != null)
            builders.setCategory(category);
        if (action != null)
            builders.setAction(action);
        if (label != null)
            builders.setLabel(label);
        if (value != null)
            builders.setValue(value);
        t.send(builders.build());
    }

    public static void sendGATrack(Activity activity, String category, String action, String label, Long value) {
        sendGATrack(activity.getApplication(), category, action, label, value);
    }

    public static void sendGATrack(Service service, String category, String action, String label, Long value) {
        sendGATrack(service.getApplication(), category, action, label, value);
    }

    public static boolean isGpsEnabled(Context c) {
        LocationManager locationManager = (LocationManager) c.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void getNumColumnsAndWidth(Activity a, int[] result) {
        int screenWidth = getScreenWidth(a);
        if (screenWidth > getScreenHeight(a)) {
            int numColumns = PreferenceManager.getDefaultSharedPreferences(a).getInt("landscapeNumImagesInRow", 5);
            if (numColumns < 1) numColumns = 1;
            if (numColumns > 20) numColumns = 20;
            result[0] = numColumns;
            result[1] = screenWidth / numColumns;
        } else {
            int numColumns = PreferenceManager.getDefaultSharedPreferences(a).getInt("portraitNumImagesInRow", 3);
            if (numColumns < 1) numColumns = 1;
            if (numColumns > 10) numColumns = 10;
            result[0] = numColumns;
            result[1] = screenWidth / numColumns;
        }
    }

    public static boolean isOnMainThread() {
        return Looper.getMainLooper() == Looper.myLooper();
    }

    public static void runOnBackgroundThread(Runnable r) {
        if (isOnMainThread()) {
            Thread t = new Thread(r);
            t.start();
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            r.run();
        }
    }
}
