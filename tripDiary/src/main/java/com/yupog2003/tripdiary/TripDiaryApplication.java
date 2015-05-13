package com.yupog2003.tripdiary;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class TripDiaryApplication extends Application{
    Tracker appTracker;
    synchronized public Tracker getTracker() {
        if (appTracker==null){
            GoogleAnalytics analytics=GoogleAnalytics.getInstance(this);
            appTracker=analytics.newTracker(R.xml.global_tracker);
        }
        return appTracker;
    }
}
