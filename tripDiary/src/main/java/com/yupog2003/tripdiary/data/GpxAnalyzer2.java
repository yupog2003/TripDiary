package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.os.Handler;
import android.support.v4.provider.DocumentFile;

import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava.ProgressChangedListener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.TimeZone;

public class GpxAnalyzer2 {

    private TrackCache cache;
    Trip trip;
    Context context;
    Handler contextHandler;
    ProgressChangedListener listener;
    long fileSize;

    public GpxAnalyzer2(Trip trip, Context context, Handler contextHandler) {
        this.trip = trip;
        this.context = context;
        this.contextHandler = contextHandler;
    }

    public boolean analyze() {
        String tripName = trip.tripName;
        cache = new TrackCache();
        DocumentFile cacheFile = trip.cacheFile;
        long cacheFileLength = cacheFile.length();
        boolean cacheExist = cacheFileLength > 0;
        File temp = new File(context.getCacheDir(), "temp");
        boolean success;
        if (cacheExist) {
            fileSize = cacheFileLength;
            FileHelper.copyFile(cacheFile, temp);
            int numOfLines = FileHelper.getNumOfLinesInFile(temp);
            int size = (numOfLines - 9) / 4;
            cache.latitudes = new double[size];
            cache.longitudes = new double[size];
            cache.altitudes = new float[size];
            cache.times = new String[size];
            success = getCache(temp.getPath(), cache);
            temp.delete();
        } else {
            String timezone = MyCalendar.getTripTimeZone(context, tripName);
            int timeZoneOffset = TimeZone.getTimeZone(timezone).getRawOffset() / 1000;
            FileHelper.copyFile(trip.gpxFile, temp);
            fileSize = temp.length();
            success = parse(temp.getPath(), cache, timeZoneOffset);
            FileHelper.copyFile(new File(temp.getPath() + ".cache"), cacheFile);
            new File(temp.getPath() + ".cache").delete();
        }
        return success;

    }

    public void onProgressChanged(long progress) {
        if (listener != null) {
            listener.onProgressChanged(progress, fileSize);
        }
    }

    public TrackCache getCache() {
        return this.cache;
    }

    public void setOnProgressChangedListener(ProgressChangedListener listener) {
        this.listener = listener;
    }

    public static final DecimalFormat doubleFormat = new DecimalFormat("#.###");

    public static String getDistanceString(float kmNumber, String unit) {
        if (TripDiaryApplication.distance_unit == TripDiaryApplication.unit_mile) {
            kmNumber /= 1.6;
            if (unit.equals("km"))
                unit = "mi";
            else if (unit.equals("km/hr"))
                unit = "mph";
        }
        return doubleFormat.format(kmNumber) + unit;
    }

    public static String getAltitudeString(float mNumber, String unit) {
        if (TripDiaryApplication.altitude_unit == TripDiaryApplication.unit_ft) {
            mNumber /= 0.3048;
            if (unit.equals("m"))
                unit = "ft";
        }
        return String.valueOf(mNumber) + unit;
    }

    public native boolean parse(String gpxPath, TrackCache cache, int timezoneOffset);

    public native boolean getCache(String cachePath, TrackCache cache);

    public native void stop();

}
