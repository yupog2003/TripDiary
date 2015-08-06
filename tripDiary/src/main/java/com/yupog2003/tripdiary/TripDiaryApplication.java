package com.yupog2003.tripdiary;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v4.provider.DocumentFile;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyImageDecoder;
import com.yupog2003.tripdiary.data.MyImageDownloader;
import com.yupog2003.tripdiary.data.Trip;

import java.io.File;

public class TripDiaryApplication extends Application {

    Tracker appTracker;
    Trip trip;
    public static DocumentFile rootDocumentFile;
    public static TripDiaryApplication instance;
    public static final String serverURL = "http://219.85.61.62/TripDiary";
    public static int distance_unit;
    //public static final int unit_km = 0;
    public static final int unit_mile = 1;
    public static int altitude_unit;
    //public static final int unit_m = 0;
    public static final int unit_ft = 1;

    synchronized public Tracker getTracker() {
        if (appTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            appTracker = analytics.newTracker(R.xml.global_tracker);
        }
        return appTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        updateRootPath(PreferenceManager.getDefaultSharedPreferences(this).getString("rootpath", Environment.getExternalStorageDirectory() + "/TripDiary"));
        initialImageLoader();
        initialUnit();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void updateRootPath(String rootPath) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                rootDocumentFile = DocumentFile.fromTreeUri(instance, Uri.parse(rootPath));
            } catch (Exception e) {
                creatRootDir(rootPath);
                rootDocumentFile = DocumentFile.fromFile(new File(rootPath));
            }
        } else {
            creatRootDir(rootPath);
            rootDocumentFile = DocumentFile.fromFile(new File(rootPath));
        }
        DocumentFile[] files = rootDocumentFile.listFiles();
        DocumentFile settings = FileHelper.findfile(files, ".settings");
        if (settings == null) {
            rootDocumentFile.createDirectory(".settings");
        }
        DocumentFile nomedia = FileHelper.findfile(files, ".nomedia");
        if (nomedia == null) {
            rootDocumentFile.createFile("", ".nomedia");
        }
    }

    private static boolean creatRootDir(String rootPath) {
        File file = new File(rootPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else if (file.isFile()) {
            boolean b = file.delete();
            return b & file.mkdirs();
        }
        return true;
    }

    private void initialImageLoader() {
        MyImageDecoder imageDecoder = new MyImageDecoder(this, new BaseImageDecoder(false));
        MyImageDownloader imageDownloader = new MyImageDownloader(this);
        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(this).imageDownloader(imageDownloader).imageDecoder(imageDecoder).build();
        ImageLoader.getInstance().init(conf);
    }

    private static void initialUnit() {
        distance_unit = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(instance).getString("distance_unit", "0"));
        altitude_unit = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(instance).getString("altitude_unit", "0"));
    }

    public void setTrip(Trip trip) {
        this.trip = trip;
    }

    public Trip getTrip() {
        return trip;
    }
}
