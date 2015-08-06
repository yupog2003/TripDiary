package com.yupog2003.tripdiary.data;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.Surface;

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
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected());
    }

    public static void sendGATrack(Application app, String category, String action, String label, Long value) {
        Tracker t = ((TripDiaryApplication) app).getTracker();
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

    public static int getRotation(Activity a) {
        if (a == null) return Surface.ROTATION_0;
        DisplayMetrics dm = getDisPlayMetrics(a);
        return dm.widthPixels > dm.heightPixels ? Surface.ROTATION_90 : Surface.ROTATION_0;
    }
}
