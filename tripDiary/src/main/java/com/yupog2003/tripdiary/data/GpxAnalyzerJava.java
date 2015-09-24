package com.yupog2003.tripdiary.data;

import com.yupog2003.tripdiary.data.documentfile.DocumentFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.TimeZone;

public class GpxAnalyzerJava {
    TrackCache cache;
    Trip trip;
    ProgressChangedListener listener;
    public static final double earthRadius = 6378.1 * 1000;
    public static final int altitudeDifferThreshold = 20;
    long fileSize;

    public GpxAnalyzerJava(Trip trip) {
        this.trip = trip;
        stop = false;
    }

    public boolean analyze() {
        int timeZoneOffset = 0;
        cache = new TrackCache();
        DocumentFile cacheFile = trip.cacheFile;
        long cacheFileLength = cacheFile != null ? cacheFile.length() : 0;
        boolean cacheExist = cacheFileLength > 0;
        if (cacheExist) {
            fileSize = cacheFileLength;
            int numOfLines = FileHelper.getNumOfLinesInFile(cacheFile);
            int size = (numOfLines - 9) / 4;
            cache.latitudes = new double[size];
            cache.longitudes = new double[size];
            cache.altitudes = new float[size];
            cache.times = new String[size];
        } else {
            fileSize = trip.gpxFile.length();
            timeZoneOffset = MyCalendar.getOffset(trip.timezone, trip.gpxFile) / 1000;
        }
        return cacheExist ? getCache(cacheFile, cache) : parse(trip.gpxFile, cache, timeZoneOffset);
    }

    public TrackCache getCache() {
        return this.cache;
    }

    public boolean getCache(DocumentFile cacheFile, TrackCache cache) {
        try {
            InputStream fis = cacheFile.getInputStream();
            InputStreamReader is = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(is);
            String s;
            cache.startTime = br.readLine();
            cache.endTime = br.readLine();
            cache.totalTime = br.readLine();
            cache.distance = Float.parseFloat(br.readLine());
            cache.avgSpeed = Float.parseFloat(br.readLine());
            cache.maxSpeed = Float.parseFloat(br.readLine());
            cache.climb = Float.parseFloat(br.readLine());
            cache.maxAltitude = Float.parseFloat(br.readLine());
            cache.minAltitude = Float.parseFloat(br.readLine());
            int index = 0, count = 0;
            while ((s = br.readLine()) != null) {
                if (stop) {
                    br.close();
                    return false;
                }
                count++;
                if (count == 1250) {
                    onProgressChanged(cacheFile.length() * index / cache.latitudes.length);
                    count = 0;
                }
                cache.latitudes[index] = Double.parseDouble(s);
                s = br.readLine();
                cache.longitudes[index] = Double.parseDouble(s);
                s = br.readLine();
                cache.altitudes[index] = Float.parseFloat(s);
                s = br.readLine();
                cache.times[index] = s;
                index++;
            }
            br.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean parse(DocumentFile gpxFile, TrackCache cache, int timeZoneOffset) {
        try {
            DocumentFile cacheFile = FileHelper.findfile(gpxFile.getParentFile(), gpxFile.getName() + ".cache");
            BufferedReader br = new BufferedReader(new InputStreamReader(gpxFile.getInputStream()));
            String s;
            int count = 0;
            long byteCount = 0;
            ArrayList<MyLatLng2> track = new ArrayList<>();
            MyLatLng2 latlng = null;
            MyLatLng2 preLatLng = null;
            float maxAltitude = -Float.MAX_VALUE;
            float minAltitude = Float.MAX_VALUE;
            ArrayList<Long> times = new ArrayList<>();
            boolean first = true;
            float preAltitude = 0;
            float totalAltitude = 0;
            float distance = 0;
            while ((s = br.readLine()) != null) {
                if (stop) {
                    br.close();
                    return false;
                }
                count++;
                byteCount += s.getBytes().length;
                if (count == 5000) {
                    onProgressChanged(byteCount);
                    count = 0;
                }
                if (s.contains("<trkpt")) {
                    latlng = new MyLatLng2();
                    String[] tokes = s.split("\"");
                    if (s.indexOf("lat") > s.indexOf("lon")) {
                        latlng.longitude = Double.parseDouble(tokes[1]);
                        latlng.latitude = Double.parseDouble(tokes[3]);
                    } else {
                        latlng.latitude = Double.parseDouble(tokes[1]);
                        latlng.longitude = Double.parseDouble(tokes[3]);
                    }
                } else if (s.contains("<ele>")) {
                    float altitude = Float.parseFloat(s.substring(s.indexOf(">") + 1, s.lastIndexOf("<")));
                    latlng.altitude = altitude;
                    if (altitude > maxAltitude)
                        maxAltitude = altitude;
                    if (altitude < minAltitude)
                        minAltitude = altitude;
                } else if (s.contains("<time>")) {
                    MyCalendar time = MyCalendar.getInstance(TimeZone.getTimeZone("UTC"));
                    s = s.substring(s.indexOf(">") + 1, s.lastIndexOf("<"));
                    String year = s.substring(0, s.indexOf("-"));
                    String month = s.substring(s.indexOf("-") + 1, s.lastIndexOf("-"));
                    String day = s.substring(s.lastIndexOf("-") + 1, s.indexOf("T"));
                    String hour = s.substring(s.indexOf("T") + 1, s.indexOf(":"));
                    String minute = s.substring(s.indexOf(":") + 1, s.lastIndexOf(":"));
                    String second = s.substring(s.lastIndexOf(":") + 1, s.indexOf("Z"));
                    time.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute), (int) Double.parseDouble(second));
                    time.setTimeInMillis(time.getTimeInMillis() + timeZoneOffset * 1000);
                    latlng.time = time.formatInTimezone("UTC");
                    times.add(time.getTimeInMillis() / 1000);
                } else if (s.contains("</trkpt>")) {
                    if (!first) {
                        float altitudeDiffer = latlng.altitude - preAltitude;
                        if (Math.abs(altitudeDiffer) > altitudeDifferThreshold) {
                            if (altitudeDiffer > 0)
                                totalAltitude += altitudeDiffer;
                            preAltitude = latlng.altitude;
                        }
                        distance += distFrom(preLatLng.latitude, preLatLng.longitude, latlng.latitude, latlng.longitude);
                    } else {
                        first = false;
                        preAltitude = latlng.altitude;
                    }
                    preLatLng = latlng;
                    track.add(latlng);
                }
            }
            br.close();
            float maxSpeed = 0;
            int trackSize = track.size();
            int timesSize = times.size();
            for (int i = 0; i + 20 < trackSize && i + 20 < timesSize; i += 20) {
                if (stop)
                    return false;
                float dist = distFrom(track.get(i).latitude, track.get(i).longitude, track.get(i + 20).latitude, track.get(i + 20).longitude);
                float seconds = times.get(i + 20) - times.get(i);
                float speed = dist / seconds * 18 / 5;
                if (maxSpeed < speed)
                    maxSpeed = speed;
            }
            if (stop)
                return false;
            long totalSeconds = times.get(times.size() - 1) - times.get(0);
            String totalTime = "";
            long day = totalSeconds / 86400;
            long hour = totalSeconds % 86400 / 3600;
            long min = totalSeconds % 3600 / 60;
            long sec = totalSeconds % 60;
            if (day != 0) {
                totalTime = String.valueOf(day) + "T";
            }
            totalTime += String.valueOf(hour) + ":" + String.valueOf(min) + ":" + String.valueOf(sec);
            float avgSpeed = distance / totalSeconds * 18 / 5;
            cache.startTime = track.get(0).time;
            cache.endTime = track.get(trackSize - 1).time;
            cache.totalTime = totalTime;
            cache.distance = distance;
            cache.avgSpeed = avgSpeed;
            cache.maxSpeed = maxSpeed;
            cache.climb = totalAltitude;
            cache.maxAltitude = maxAltitude;
            cache.minAltitude = minAltitude;
            cache.latitudes = new double[trackSize];
            cache.longitudes = new double[trackSize];
            cache.altitudes = new float[trackSize];
            cache.times = new String[trackSize];
            for (int i = 0; i < trackSize; i++) {
                cache.latitudes[i] = track.get(i).latitude;
                cache.longitudes[i] = track.get(i).longitude;
                cache.altitudes[i] = track.get(i).altitude;
                cache.times[i] = track.get(i).time;
            }
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(cacheFile.getOutputStream()));
            bw.write(cache.startTime + "\n");
            bw.write(cache.endTime + "\n");
            bw.write(cache.totalTime + "\n");
            bw.write(String.valueOf(cache.distance) + "\n");
            bw.write(String.valueOf(cache.avgSpeed) + "\n");
            bw.write(String.valueOf(cache.maxSpeed) + "\n");
            bw.write(String.valueOf(cache.climb) + "\n");
            bw.write(String.valueOf(cache.maxAltitude) + "\n");
            bw.write(String.valueOf(cache.minAltitude) + "\n");
            for (int i = 0; i < trackSize; i++) {
                bw.write(String.valueOf(cache.latitudes[i]) + "\n");
                bw.write(String.valueOf(cache.longitudes[i]) + "\n");
                bw.write(String.valueOf(cache.altitudes[i]) + "\n");
                bw.write(cache.times[i] + "\n");
            }
            bw.flush();
            bw.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static float distFrom(double lat1, double lng1, double lat2, double lng2) { //in meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLng = Math.toRadians(lng2 - lng1);
        double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(dLng / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double dist = earthRadius * c;
        return (float) dist;
    }

    boolean stop;

    public void stop() {
        stop = true;
    }

    public void setOnProgressChangedListener(ProgressChangedListener listener) {
        this.listener = listener;
    }

    public void onProgressChanged(long progress) {
        if (listener != null) {
            listener.onProgressChanged(progress, fileSize);
        }
    }

    public interface ProgressChangedListener {
        void onProgressChanged(long progress, long fileSize);
    }
}