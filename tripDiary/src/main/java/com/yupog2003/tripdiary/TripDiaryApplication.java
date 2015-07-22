package com.yupog2003.tripdiary;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.support.multidex.MultiDex;
import android.support.v4.provider.DocumentFile;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.yupog2003.tripdiary.data.FileHelper;

import java.io.File;

public class TripDiaryApplication extends Application {
    Tracker appTracker;
    public static DocumentFile rootDocumentFile;
    public static TripDiaryApplication instance;
    public static final String serverURL = "http://219.85.61.62/TripDiary";

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
        DocumentFile settings = FileHelper.findfile(rootDocumentFile, ".settings");
        if (settings == null) {
            rootDocumentFile.createDirectory(".settings");
        }
        DocumentFile nomedia = FileHelper.findfile(rootDocumentFile, ".nomedia");
        if (nomedia == null) {
            rootDocumentFile.createFile("", ".nomedia");
        }
    }

    public static boolean creatRootDir(String rootPath) {
        File file = new File(rootPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else if (file.isFile()) {
            boolean b = file.delete();
            return b & file.mkdirs();
        }
        return true;
    }
}
