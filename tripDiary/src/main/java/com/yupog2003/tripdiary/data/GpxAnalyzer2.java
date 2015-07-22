package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.os.Handler;
import android.support.v4.provider.DocumentFile;

import com.yupog2003.tripdiary.MainActivity;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava.ProgressChangedListener;

import java.io.File;
import java.text.DecimalFormat;
import java.util.TimeZone;

public class GpxAnalyzer2 {

    private TrackCache cache;
    DocumentFile gpxFile;
    Context context;
    Handler contextHandler;
    ProgressChangedListener listener;

    public GpxAnalyzer2(DocumentFile gpxFile, Context context, Handler contextHandler) {
        this.gpxFile = gpxFile;
        this.context = context;
        this.contextHandler = contextHandler;
    }

    public boolean analyze() {
        String tripName = FileHelper.getFileName(gpxFile.getParentFile());
        cache = new TrackCache();
        DocumentFile cacheFile = FileHelper.findfile(gpxFile.getParentFile(), tripName + ".gpx.cache");
        boolean cacheExist = cacheFile != null && cacheFile.length() > 0;
        File temp = new File(context.getCacheDir(), "temp");
        boolean success;
        if (cacheExist) {
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
            FileHelper.copyFile(gpxFile, temp);
            success = parse(temp.getPath(), cache, timeZoneOffset);
            FileHelper.copyFile(new File(temp.getPath() + ".cache"), cacheFile);
            new File(temp.getPath() + ".cache").delete();
        }
        return success;

    }

    public void onProgressChanged(long progress) {
        if (listener != null) {
            listener.onProgressChanged(progress);
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
        if (MainActivity.distance_unit == MainActivity.unit_mile) {
            kmNumber /= 1.6;
            if (unit.equals("km"))
                unit = "mi";
            else if (unit.equals("km/hr"))
                unit = "mph";
        }
        return doubleFormat.format(kmNumber) + unit;
    }

    public static String getAltitudeString(float mNumber, String unit) {
        if (MainActivity.altitude_unit == MainActivity.unit_ft) {
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
