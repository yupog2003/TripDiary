package com.yupog2003.tripdiary.data;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;

public class DeviceHelper {

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
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

    public static boolean isMobileNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo mobileInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        return (wifiInfo != null && wifiInfo.isConnected()) || (mobileInfo != null && mobileInfo.isConnected());
    }
}
