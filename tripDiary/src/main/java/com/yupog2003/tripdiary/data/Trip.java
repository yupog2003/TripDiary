package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseArray;
import android.widget.Toast;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;

import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

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

    @Nullable
    public static Trip createTrip(Context context, String tripName) {
        DocumentFile dir = TripDiaryApplication.rootDocumentFile.createDirectory(tripName);
        if (dir == null) {
            Toast.makeText(context, R.string.storage_error, Toast.LENGTH_SHORT).show();
            return null;
        }
        MyCalendar.updateTripTimeZone(context, tripName, TimeZone.getDefault().getID());
        return new Trip(context, dir, false);
    }

    public void refreshBasic() {
        this.tripName = dir.getName();
        this.time = MyCalendar.getTripTime(tripName);
        this.timezone = MyCalendar.getTripTimeZone(context, tripName);
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
        this.note = "";
        if (noteFile != null) {
            try {
                note = IOUtils.toString(noteFile.getInputStream(), "UTF-8");
            } catch (NullPointerException | IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        ArrayList<DocumentFile> poiFileList = new ArrayList<>();
        for (DocumentFile file : files) {
            if (file.isDirectory() && !file.getName().startsWith(".")) {
                poiFileList.add(file);
            }
        }
        refreshPOIs(poiFileList.toArray(new DocumentFile[poiFileList.size()]));
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

    @NonNull
    public String getDateDurationString() {
        if (cache == null) return "";
        final String startTime = cache.startTime;
        final String endTime = cache.endTime;
        if (startTime == null || endTime == null) {
            return "";
        }
        if (!startTime.contains("T") || !endTime.contains("T")) {
            return startTime;
        }
        String startDate = startTime.substring(0, startTime.indexOf("T"));
        String endDate = endTime.substring(0, endTime.indexOf("T"));
        if (!startDate.contains("-") || !endDate.contains("-")) {
            return startDate;
        }
        String[] startDates = startDate.split("-");
        String[] endDates = endDate.split("-");
        if (startDates.length != 3 || endDates.length != 3) {
            return startDate;
        }
        StringBuilder resultBuilder = new StringBuilder(startDate);
        for (int i = 0; i < 3; i++) {
            if (!startDates[i].equals(endDates[i])) {
                resultBuilder.append("~");
                for (int j = i; j < 3; j++) {
                    resultBuilder.append(endDates[j]).append("-");
                }
                resultBuilder.deleteCharAt(resultBuilder.length() - 1);
                break;
            }
        }
        return resultBuilder.toString();
    }

    public interface GenerateKMxListener {
        void onProgressChanged(int progress, int total);
    }

    @Nullable
    public DocumentFile generateKML(DocumentFile kmlFile, GenerateKMxListener listener) {
        if (pois == null || cache == null || kmlFile == null || note == null)
            return null;
        int lineColor = PreferenceManager.getDefaultSharedPreferences(context).getInt("trackcolor", 0xff6699cc);
        String lineColorInKML = String.format("%02x%02x%02x%02x", Color.alpha(lineColor), Color.blue(lineColor), Color.green(lineColor), Color.red(lineColor));
        int iconColor = PreferenceManager.getDefaultSharedPreferences(context).getInt("markercolor", 0xffff0000);
        String iconColorInKML = String.format("%02x%02x%02x%02x", Color.alpha(iconColor), Color.blue(iconColor), Color.green(iconColor), Color.red(iconColor));
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(kmlFile.getOutputStream()));
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
            bw.write("<Document>\n");
            bw.write("<name>" + tripName + "</name>\n");
            bw.write("<description>" + note + "</description>\n");
            bw.write("<Style id=\"lineColor\">\n");
            bw.write("<LineStyle>\n");
            bw.write("<color>" + lineColorInKML + "</color>\n");
            bw.write("<width>4</width>\n");
            bw.write("</LineStyle>\n");
            bw.write("</Style>\n");
            bw.write("<Style id=\"iconColor\">\n");
            bw.write("<IconStyle>\n");
            bw.write("<color>" + iconColorInKML + "</color>\n");
            bw.write("</IconStyle>\n");
            bw.write("</Style>\n");
            bw.write("<Folder>\n");
            bw.write("<Placemark>\n");
            bw.write("<name>" + tripName + "</name>\n");
            bw.write("<description>Generated by TripDiary</description>\n");
            bw.write("<styleUrl>#lineColor</styleUrl>\n");
            bw.write("<LineString>\n");
            bw.write("<coordinates>");
            int length = Math.min(cache.latitudes.length, cache.longitudes.length);
            length = Math.min(length, cache.altitudes.length);
            final int poiLength = pois.length;
            final int totalLength = poiLength + length;
            double maxLatitude = -Double.MAX_VALUE;
            double minLatitude = Double.MAX_VALUE;
            double maxLongitude = -Double.MAX_VALUE;
            double minLongitude = Double.MAX_VALUE;
            for (int i = 0; i < length; i++) {
                double longitude = cache.longitudes[i];
                double latitude = cache.latitudes[i];
                if (maxLatitude < latitude) {
                    maxLatitude = latitude;
                }
                if (minLatitude > latitude) {
                    minLatitude = latitude;
                }
                if (maxLongitude < longitude) {
                    maxLongitude = longitude;
                }
                if (minLongitude > longitude) {
                    minLongitude = longitude;
                }
                bw.write(String.format(" %f,%f,%f\n", longitude, latitude, cache.altitudes[i]));
                if (listener != null) {
                    listener.onProgressChanged(i, totalLength);
                }
            }
            bw.write("</coordinates>\n");
            bw.write("</LineString>\n");
            bw.write("</Placemark>\n");
            for (int i = 0; i < poiLength; i++) {
                POI poi = pois[i];
                if (poi == null) continue;
                bw.write("<Placemark>\n");
                bw.write("<name>" + poi.title + "</name>\n");
                bw.write("<styleUrl>#iconColor</styleUrl>\n");
                bw.write("<description>" + poi.time.formatInTimezone(timezone) + "<br/>" + poi.diary + "</description>\n");
                bw.write("<Point>\n");
                bw.write("<coordinates>" + String.valueOf(poi.longitude) + "," + String.valueOf(poi.latitude) + "," + String.valueOf(poi.altitude) + "</coordinates>\n");
                bw.write("</Point>\n");
                bw.write("</Placemark>\n");
                if (listener != null) {
                    listener.onProgressChanged(length + i, totalLength);
                }
            }
            bw.write("</Folder>\n");
            bw.write("<LookAt>\n");
            bw.write("<longitude>" + String.valueOf((maxLongitude + minLongitude) / 2) + "</longitude>\n");
            bw.write("<latitude>" + String.valueOf((maxLatitude + minLatitude) / 2) + "</latitude>\n");
            bw.write("<altitude>0</altitude>\n");
            bw.write("<heading>0</heading>\n");
            bw.write("<tilt>45</tilt>\n");
            bw.write("<range>20000</range>\n");
            bw.write("<altitudeMode>clampToGround</altitudeMode>\n");
            bw.write("</LookAt>\n");
            bw.write("</Document>\n");
            bw.write("</kml>");
            bw.flush();
            bw.close();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        return kmlFile;
    }


    @Nullable
    public DocumentFile generateKMZ(DocumentFile kmzFile, GenerateKMxListener listener) {
        try {
            ZipOutputStream zos = new ZipOutputStream(kmzFile.getOutputStream());
            int lineColor = PreferenceManager.getDefaultSharedPreferences(context).getInt("trackcolor", 0xff6699cc);
            String lineColorInKML = String.format("%02x%02x%02x%02x", Color.alpha(lineColor), Color.blue(lineColor), Color.green(lineColor), Color.red(lineColor));
            int iconColor = PreferenceManager.getDefaultSharedPreferences(context).getInt("markercolor", 0xffff0000);
            String iconColorInKML = String.format("%02x%02x%02x%02x", Color.alpha(iconColor), Color.blue(iconColor), Color.green(iconColor), Color.red(iconColor));
            File tempDir = new File(context.getCacheDir(), "kmzcache");
            tempDir.mkdirs();
            File tempKML = new File(tempDir, "doc.kml");
            BufferedWriter bw = new BufferedWriter(new FileWriter(tempKML));
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
            bw.write("<Document>\n");
            bw.write("<name>" + tripName + "</name>\n");
            bw.write("<description>" + note + "</description>\n");
            bw.write("<Style id=\"lineColor\">\n");
            bw.write("<LineStyle>\n");
            bw.write("<color>" + lineColorInKML + "</color>\n");
            bw.write("<width>4</width>\n");
            bw.write("</LineStyle>\n");
            bw.write("</Style>\n");
            bw.write("<Style id=\"iconColor\">\n");
            bw.write("<IconStyle>\n");
            bw.write("<color>" + iconColorInKML + "</color>\n");
            bw.write("</IconStyle>\n");
            bw.write("</Style>\n");
            bw.write("<Folder>\n");
            bw.write("<Placemark>\n");
            bw.write("<name>" + tripName + "</name>\n");
            bw.write("<description>Generated by TripDiary</description>\n");
            bw.write("<styleUrl>#lineColor</styleUrl>\n");
            bw.write("<LineString>\n");
            bw.write("<coordinates>");
            int length = Math.min(cache.latitudes.length, cache.longitudes.length);
            length = Math.min(length, cache.altitudes.length);
            double maxLatitude = -Double.MAX_VALUE;
            double minLatitude = Double.MAX_VALUE;
            double maxLongitude = -Double.MAX_VALUE;
            double minLongitude = Double.MAX_VALUE;
            for (int i = 0; i < length; i++) {
                double longitude = cache.longitudes[i];
                double latitude = cache.latitudes[i];
                if (maxLatitude < latitude) {
                    maxLatitude = latitude;
                }
                if (minLatitude > latitude) {
                    minLatitude = latitude;
                }
                if (maxLongitude < longitude) {
                    maxLongitude = longitude;
                }
                if (minLongitude > longitude) {
                    minLongitude = longitude;
                }
                bw.write(String.format(" %f,%f,%f\n", longitude, latitude, cache.altitudes[i]));
                if (listener != null) {
                    listener.onProgressChanged(i, length);
                }
            }
            bw.write("</coordinates>\n");
            bw.write("</LineString>\n");
            bw.write("</Placemark>\n");
            final int targetPicWidth = 640;
            final int poiLength = pois.length;
            for (int i = 0; i < poiLength; i++) {
                POI poi = pois[i];
                if (listener != null) {
                    listener.onProgressChanged(i * 100, poiLength * 100);
                }
                if (poi == null) continue;
                final int picSize = poi.picFiles.length;
                StringBuilder descriptionBuilder = new StringBuilder();
                descriptionBuilder.append(poi.time.formatInTimezone(timezone)).append("<br/>").append(poi.diary).append("<br/>");
                for (int j = 0; j < picSize; j++) {
                    DocumentFile picFile = poi.picFiles[i];
                    if (picFile == null) continue;
                    InputStream is = picFile.getInputStream();
                    BitmapFactory.Options op = new BitmapFactory.Options();
                    op.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(is, new Rect(0, 0, 0, 0), op);
                    op.inSampleSize = (int) Math.ceil(op.outWidth / targetPicWidth);
                    op.inJustDecodeBounds = false;
                    is = poi.picFiles[j].getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(is, new Rect(0, 0, 0, 0), op);
                    if (bitmap != null) {
                        String fileName = picFile.getName();
                        zos.putNextEntry(new ZipEntry(fileName));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, zos);
                        bitmap.recycle();
                        zos.closeEntry();
                        descriptionBuilder.append("<img src=\"").append(fileName).append("\" width=\"").append(String.valueOf(targetPicWidth)).append("\" /><br/>");
                    }
                    if (listener != null) {
                        listener.onProgressChanged(i * 100 + j * 100 / picSize, poiLength * 100);
                    }
                }
                bw.write("<Placemark>\n");
                bw.write("<name>" + poi.title + "</name>\n");
                bw.write("<styleUrl>#iconColor</styleUrl>\n");
                bw.write("<description>" + descriptionBuilder.toString() + "</description>\n");
                bw.write("<Point>\n");
                bw.write("<coordinates>" + String.valueOf(poi.longitude) + "," + String.valueOf(poi.latitude) + "," + String.valueOf(poi.altitude) + "</coordinates>\n");
                bw.write("</Point>\n");
                bw.write("</Placemark>\n");
            }
            bw.write("</Folder>\n");
            bw.write("<LookAt>\n");
            bw.write("<longitude>" + String.valueOf((maxLongitude + minLongitude) / 2) + "</longitude>\n");
            bw.write("<latitude>" + String.valueOf((maxLatitude + minLatitude) / 2) + "</latitude>\n");
            bw.write("<altitude>0</altitude>\n");
            bw.write("<heading>0</heading>\n");
            bw.write("<tilt>45</tilt>\n");
            bw.write("<range>20000</range>\n");
            bw.write("<altitudeMode>clampToGround</altitudeMode>\n");
            bw.write("</LookAt>\n");
            bw.write("</Document>\n");
            bw.write("</kml>");
            bw.flush();
            bw.close();
            zos.putNextEntry(new ZipEntry(tempKML.getName()));
            byte[] buffer = new byte[4096];
            int count;
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(tempKML));
            while ((count = bis.read(buffer, 0, 4096)) != -1) {
                zos.write(buffer, 0, count);
            }
            zos.flush();
            bis.close();
            zos.closeEntry();
            zos.close();
            FileHelper.deletedir(tempDir.getPath());
        } catch (NullPointerException | IOException | IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
        return kmzFile;
    }

    @Nullable
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
    @Nullable
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
        poi = new POI(context, poiFile, this);
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
            refreshPOIs(dir.listFiles(DocumentFile.list_dirs));
        } else {
            this.pois = new POI[0];
        }
    }

    public void refreshPOIs(@NonNull DocumentFile[] poiFiles) {
        this.pois = new POI[poiFiles.length];
        for (int i = 0; i < pois.length; i++) {
            pois[i] = new POI(context, poiFiles[i], this);
            if (listener != null) {
                listener.onPOICreated(i, pois.length, pois[i].title);
            }
        }
        Arrays.sort(pois);
    }

    @NonNull
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
        if (name == null || name.equals("") || dir == null || cacheFile == null || gpxFile == null)
            return;
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
        context.getSharedPreferences("tripTime", Context.MODE_PRIVATE).edit().remove(tripName).apply();
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
