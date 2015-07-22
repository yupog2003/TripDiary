package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v4.provider.DocumentFile;
import android.util.Log;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TimeZone;

public class Trip {
    public Context context;
    public DocumentFile dir;
    public DocumentFile gpxFile;
    public DocumentFile cacheFile;
    public DocumentFile noteFile;
    public POI[] pois;
    public TrackCache cache;
    public String note;
    public String tripName;
    public String category;
    public String timezone;
    public GpxAnalyzerJava analyzer;
    public GpxAnalyzer2 analyzer2;
    public MyCalendar time; //in UTC
    public Drawable drawable;
    public ConstructListener listener;

    public Trip(Context context, DocumentFile dir, boolean onlyBasic) {
        this(context, dir, onlyBasic, null);
    }

    public Trip(Context context, DocumentFile dir, boolean onlyBasic, ConstructListener listener) {
        this.dir = dir;
        this.context = context;
        this.listener = listener;
        if (onlyBasic) {
            refreshBasic();
        } else {
            refreshAllFields();
        }
    }


    public static Trip createTrip(Context context, String tripName) {
        DocumentFile dir = TripDiaryApplication.rootDocumentFile.createDirectory(tripName);
        MyCalendar.updateTripTimeZone(context, tripName, TimeZone.getDefault().getID());
        return new Trip(context, dir, false);
    }

    public void refreshBasic() {
        this.tripName = FileHelper.getFileName(dir);
        this.time = MyCalendar.getTripTime(tripName);
        this.category = context.getSharedPreferences("trip", Context.MODE_PRIVATE).getString(tripName, context.getString(R.string.nocategory));
    }

    public void setDrawable(Drawable d) {
        this.drawable = d;
    }

    public void refreshAllFields() {
        refreshBasic();
        DocumentFile[] files = dir.listFiles();
        this.gpxFile = FileHelper.findfile(files, tripName + ".gpx");
        if (gpxFile == null) {
            this.gpxFile = dir.createFile("", tripName + ".gpx");
        }
        this.cacheFile = FileHelper.findfile(files, tripName + ".gpx.cache");
        if (cacheFile == null) {
            this.cacheFile = dir.createFile("", tripName + ".gpx.cache");
        }
        this.noteFile = FileHelper.findfile(files, "note");
        if (noteFile == null) {
            this.noteFile = dir.createFile("", "note");
        }
        timezone = MyCalendar.getTripTimeZone(context, tripName);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(noteFile.getUri())));
            String s;
            StringBuilder sb = new StringBuilder();
            while ((s = br.readLine()) != null) {
                sb.append(s).append("\n");
            }
            note = sb.toString();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        refreshPOIs();
    }

    public void getCacheJava(Context context, Handler handler, GpxAnalyzerJava.ProgressChangedListener listener) {
        analyzer = new GpxAnalyzerJava(gpxFile, context, handler);
        analyzer.setOnProgressChangedListener(listener);
        if (analyzer.analyze()) {
            cache = analyzer.getCache();
        }
        analyzer = null;
    }

    public void getCacheJNI(Context context, Handler handler, GpxAnalyzerJava.ProgressChangedListener listener) {
        try {
            analyzer2 = new GpxAnalyzer2(gpxFile, context, handler);
            analyzer2.setOnProgressChangedListener(listener);
            if (analyzer2.analyze()) {
                cache = analyzer2.getCache();
            }
            analyzer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stopGetCache() {
        if (analyzer != null) {
            analyzer.stop();
        }
    }

    public void deleteCache() {
        cacheFile.delete();
        context.getSharedPreferences("tripTime", 0).edit().remove(tripName).commit();
    }

    public void updateNote(String note) {
        if (note != null) {
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(context.getContentResolver().openOutputStream(noteFile.getUri())));
                bw.write(note);
                bw.flush();
                bw.close();
                this.note = note;
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
    }

    public POI getPOI(String poiName) {
        if (pois != null) {
            for (POI poi : pois) {
                if (poiName.equals(poi.title)) {
                    return poi;
                }
            }
        }
        return null;
    }

    public void refreshPOI(String poiName) {
        if (dir == null || pois == null) return;
        for (int j = 0; j < pois.length; j++) {
            if (poiName.equals(pois[j].title)) {
                pois[j] = new POI(context, dir.findFile(poiName));
                break;
            }
        }
    }

    public void refreshPOIs() {
        if (dir != null) {
            DocumentFile[] poiFiles = FileHelper.listFiles(dir, FileHelper.list_dirs);
            if (poiFiles != null) {
                this.pois = new POI[poiFiles.length];
                for (int i = 0; i < pois.length; i++) {
                    pois[i] = new POI(context, poiFiles[i]);
                    if (listener != null) {
                        listener.onPOICreated(i, pois.length, pois[i].title);
                    }
                }
            } else {
                this.pois = new POI[0];
            }
            Arrays.sort(pois, new Comparator<POI>() {

                @Override
                public int compare(POI lhs, POI rhs) {

                    if (lhs == null || rhs == null || lhs.time == null || rhs.time == null)
                        return 0;
                    if (lhs.time.after(rhs.time))
                        return 1;
                    else if (rhs.time.after(lhs.time))
                        return -1;
                    return 0;
                }
            });
        }
    }

    public void renameTrip(Context context, String name) {
        SharedPreferences p = context.getSharedPreferences("trip", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = p.edit();
        editor.remove(tripName);
        editor.putString(name, category);
        editor.commit();
        p = context.getSharedPreferences("tripTimezone", Context.MODE_PRIVATE);
        editor = p.edit();
        editor.remove(tripName);
        editor.putString(name, timezone);
        editor.commit();
        gpxFile.renameTo(name + ".gpx");
        cacheFile.renameTo(name + ".gpx.cache");
        dir.renameTo(name);
        refreshAllFields();
    }

    public void setCategory(Context context, String category) {
        this.category = category;
        SharedPreferences.Editor editor = context.getSharedPreferences("trip", Context.MODE_PRIVATE).edit();
        editor.putString(tripName, category);
        editor.commit();
    }

    public void deleteSelf() {
        dir.delete();
    }

    public interface ConstructListener {
        void onPOICreated(int poi, int total, String poiName);
    }

}
