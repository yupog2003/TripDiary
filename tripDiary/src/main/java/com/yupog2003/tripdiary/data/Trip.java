package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.widget.Toast;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;

public class Trip implements Comparable<Trip> {

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
    public MyCalendar time; //in UTC
    public Drawable drawable;
    public ConstructListener listener;

    public Trip(Context context, DocumentFile dir, boolean onlyBasic) {
        this(context, dir, onlyBasic, null);
    }

    public Trip(Context context, DocumentFile dir, boolean onlyBasic, ConstructListener listener) throws NullPointerException {
        this.dir = dir;
        this.context = context;
        this.listener = listener;
        if (context == null || dir == null) {
            throw new NullPointerException();
        } else if (onlyBasic) {
            refreshBasic();
        } else {
            refreshAllFields();
        }
    }


    public static Trip createTrip(Context context, String tripName) {
        DocumentFile dir = TripDiaryApplication.rootDocumentFile.createDirectory(tripName);
        if (dir == null){
            Toast.makeText(context, R.string.storage_error, Toast.LENGTH_SHORT).show();
            return null;
        }
        MyCalendar.updateTripTimeZone(context, tripName, TimeZone.getDefault().getID());
        return new Trip(context, dir, false);
    }

    public void refreshBasic() {
        this.tripName = dir.getName();
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
        this.timezone = MyCalendar.getTripTimeZone(context, tripName);
        this.note = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(noteFile.getInputStream()));
            String s;
            StringBuilder sb = new StringBuilder();
            while ((s = br.readLine()) != null) {
                sb.append(s).append("\n");
            }
            note = sb.toString();
            br.close();
        } catch (NullPointerException | IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        refreshPOIs();
    }

    public void getCacheJava(GpxAnalyzerJava.ProgressChangedListener listener) {
        try {
            GpxAnalyzerJava analyzer = new GpxAnalyzerJava(this, context);
            analyzer.setOnProgressChangedListener(listener);
            if (analyzer.analyze()) {
                cache = analyzer.getCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCacheJNI(GpxAnalyzerJava.ProgressChangedListener listener) {
        try {
            GpxAnalyzer2 analyzer2 = new GpxAnalyzer2(this, context);
            analyzer2.setOnProgressChangedListener(listener);
            if (analyzer2.analyze()) {
                cache = analyzer2.getCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteCache() {
        if (cacheFile != null) {
            cacheFile.delete();
        }
        context.getSharedPreferences("tripTime", 0).edit().remove(tripName).apply();
    }

    public void updateNote(String note) {
        if (note != null) {
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(noteFile.getOutputStream()));
                bw.write(note);
                bw.flush();
                bw.close();
                this.note = note;
            } catch (NullPointerException | IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public POI getPOI(String poiName) {
        if (pois != null && poiName != null) {
            for (POI poi : pois) {
                if (poi == null) continue;
                if (poiName.equals(poi.title)) {
                    return poi;
                }
            }
        }
        return null;
    }

    //If exists, return it. If not, create it.
    public POI createPOI(String poiName, MyCalendar time, double latitude, double longitude, double altitude) {
        POI poi;
        if ((poi = getPOI(poiName)) != null) {
            return poi;
        }
        DocumentFile poiFile = FileHelper.findfile(dir, poiName);
        if (poiFile == null) {
            poiFile = dir.createDirectory(poiName);
        }
        if (poiFile == null) {
            return null;
        }
        poi = new POI(context, poiFile);
        poi.updateBasicInformation(null, time, latitude, longitude, altitude);
        ArrayList<POI> poiList = new ArrayList<>(Arrays.asList(pois));
        poiList.add(poi);
        this.pois = poiList.toArray(new POI[poiList.size()]);
        Arrays.sort(pois);
        return getPOI(poiName);
    }

    public void deletePOI(String poiName) {
        if (poiName == null || pois == null) return;
        ArrayList<POI> poiList = new ArrayList<>(Arrays.asList(pois));
        for (POI poi : poiList) {
            if (poi == null) continue;
            if (poiName.equals(poi.title)) {
                poi.deleteSelf();
                poiList.remove(poi);
                break;
            }
        }
        this.pois = poiList.toArray(new POI[poiList.size()]);
        Arrays.sort(pois);
    }

    public void refreshPOI(String poiName) {
        if (dir == null || pois == null || poiName == null) return;
        for (POI poi : pois) {
            if (poi != null && poiName.equals(poi.title)) {
                poi.updateAllFields();
            }
        }
        Arrays.sort(pois);
    }

    public void refreshPOIs() {
        if (dir != null) {
            DocumentFile[] poiFiles = dir.listFiles(DocumentFile.list_dirs);
            if (poiFiles != null) {
                this.pois = new POI[poiFiles.length];
                for (int i = 0; i < pois.length; i++) {
                    pois[i] = new POI(context, poiFiles[i]);
                    if (listener != null) {
                        listener.onPOICreated(i, pois.length, pois[i].title);
                    }
                }
                Arrays.sort(pois);
                return;
            }
        }
        this.pois = new POI[0];
    }

    public SparseArray<POI> getPOIsInTrackMap() {
        SparseArray<POI> markersMap = new SparseArray<>();
        if (pois == null || cache == null || cache.times == null || timezone == null) {
            return markersMap;
        }
        for (POI poi : pois) {
            int start = 0, end = cache.times.length - 1, middle;
            MyCalendar poiTime = poi.time;
            if (poiTime == null)
                continue;
            poiTime.setTimeZone(timezone);
            while (Math.abs(start - end) > 1) {
                middle = (start + end) / 2;
                MyCalendar time = MyCalendar.getTime(timezone, cache.times[middle], MyCalendar.type_self);
                if (poiTime.after(time)) {
                    start = middle;
                } else {
                    end = middle;
                }
            }
            while (markersMap.get(start) != null) {
                start++;
            }
            start = Math.min(start, cache.times.length - 1);
            markersMap.put(start, poi);
        }
        return markersMap;
    }

    public void renameTrip(String name) {
        if (dir == null || cacheFile == null || gpxFile == null) return;
        SharedPreferences p = context.getSharedPreferences("trip", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = p.edit();
        editor.remove(tripName);
        editor.putString(name, category);
        editor.apply();
        p = context.getSharedPreferences("tripTimezone", Context.MODE_PRIVATE);
        editor = p.edit();
        editor.remove(tripName);
        editor.putString(name, timezone);
        editor.apply();
        gpxFile.renameTo(name + ".gpx");
        cacheFile.renameTo(name + ".gpx.cache");
        dir.renameTo(name);
        refreshAllFields();
    }

    public void setCategory(String category) {
        this.category = category;
        SharedPreferences.Editor editor = context.getSharedPreferences("trip", Context.MODE_PRIVATE).edit();
        editor.putString(tripName, category);
        editor.apply();
    }

    public void deleteSelf() {
        dir.delete();
        context.getSharedPreferences("trip", Context.MODE_PRIVATE).edit().remove(tripName).apply();
        context.getSharedPreferences("tripTimezone", Context.MODE_PRIVATE).edit().remove(tripName).apply();
    }

    @Override
    public int compareTo(@NonNull Trip another) {
        if (this.time == null || another.time == null) {
            return 0;
        } else {
            return time.compareTo(another.time);
        }
    }

    public interface ConstructListener {
        void onPOICreated(int poi, int total, String poiName);
    }

}
