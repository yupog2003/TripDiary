package com.yupog2003.tripdiary;

import android.Manifest;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyImageDecoder;
import com.yupog2003.tripdiary.data.MyImageDownloader;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;

import java.io.File;
import java.util.HashMap;

public class TripDiaryApplication extends Application {

    Tracker appTracker;
    HashMap<String, Trip> trips;
    public static DocumentFile rootDocumentFile;
    public static TripDiaryApplication instance;
    public static final String serverHost = "yupog2003.idv.tw";
    public static final String serverIP = "219.85.61.62";
    public static final String serverURL = "http://" + serverHost + "/TripDiary";
    public static int distance_unit;
    //public static final int unit_km = 0;
    public static final int unit_mile = 1;
    public static int altitude_unit;
    //public static final int unit_m = 0;
    public static final int unit_ft = 1;

    public static GoogleApiClient googleApiClient;

    synchronized public Tracker getTracker() {
        if (appTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.setDryRun(false);
            appTracker = analytics.newTracker("UA-44647804-2");
            appTracker.enableAutoActivityTracking(true);
            appTracker.enableExceptionReporting(true);
            appTracker.enableAdvertisingIdCollection(false);
        }
        return appTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        if (ContextCompat.checkSelfPermission(instance, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            updateRootPath();
        }
        initialImageLoader();
        initialUnit();
        trips = new HashMap<>();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void updateRootPath() {
        String rootPath = PreferenceManager.getDefaultSharedPreferences(instance).getString("rootpath", Environment.getExternalStorageDirectory() + "/TripDiary");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                rootDocumentFile = DocumentFile.fromTreeUri(Uri.parse(rootPath));
            } catch (Exception e) {
                createRootDir(rootPath);
                rootDocumentFile = DocumentFile.fromFile(new File(rootPath));
            }
        } else {
            createRootDir(rootPath);
            rootDocumentFile = DocumentFile.fromFile(new File(rootPath));
        }
        if (rootDocumentFile != null) {
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
    }

    private static boolean createRootDir(String rootPath) {
        File file = new File(rootPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else if (file.isFile()) {
            return file.delete() && file.mkdirs();
        }
        return true;
    }

    private void initialImageLoader() {
        MyImageDecoder imageDecoder = new MyImageDecoder(this, new BaseImageDecoder(false));
        MyImageDownloader imageDownloader = new MyImageDownloader(this);
        int imageloaderMemoryCachePercentage = PreferenceManager.getDefaultSharedPreferences(this).getInt("imageloaderMemoryCachePercentage", 30);
        if (imageloaderMemoryCachePercentage < 1) imageloaderMemoryCachePercentage = 1;
        if (imageloaderMemoryCachePercentage > 99) imageloaderMemoryCachePercentage = 99;
        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(this).imageDownloader(imageDownloader).imageDecoder(imageDecoder).memoryCacheSizePercentage(imageloaderMemoryCachePercentage).build();
        ImageLoader.getInstance().init(conf);
    }

    private static void initialUnit() {
        distance_unit = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(instance).getString("distance_unit", "0"));
        altitude_unit = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(instance).getString("altitude_unit", "0"));
    }

    public void putTrip(Trip trip) {
        if (trip != null && trip.tripName != null)
            trips.put(trip.tripName, trip);
    }

    public void removeTrip(String tripName) {
        if (tripName != null)
            trips.remove(tripName);
    }

    public Trip getTrip(String tripName) {
        if (tripName == null) return null;
        return trips.get(tripName);
    }
}
