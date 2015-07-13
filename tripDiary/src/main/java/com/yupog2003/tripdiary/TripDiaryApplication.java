package com.yupog2003.tripdiary;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.multidex.MultiDex;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.io.IOException;

public class TripDiaryApplication extends Application{
    Tracker appTracker;
    public static String rootPath;
    public static TripDiaryApplication instance;
    synchronized public Tracker getTracker() {
        if (appTracker==null){
            GoogleAnalytics analytics=GoogleAnalytics.getInstance(this);
            appTracker=analytics.newTracker(R.xml.global_tracker);
        }
        return appTracker;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance=this;
        rootPath= PreferenceManager.getDefaultSharedPreferences(this).getString("rootpath", Environment.getExternalStorageDirectory()+"/TripDiary");
        creatRootDir(rootPath);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public static void creatRootDir(String rootPath) {
        File file = new File(rootPath);
        if (!file.exists()) {
            file.mkdirs();
        } else if (file.isFile()) {
            file.delete();
            file.mkdirs();
        }
        File nomedia = new File(rootPath + "/.nomedia");
        try {
            nomedia.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
