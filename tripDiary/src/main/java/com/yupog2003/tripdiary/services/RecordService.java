package com.yupog2003.tripdiary.services;

import android.Manifest;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.yupog2003.tripdiary.DummyActivity;
import com.yupog2003.tripdiary.MyActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.RecordActivity;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.TimeZone;

public class RecordService extends Service implements LocationListener, Runnable, SensorEventListener {
    public double latitude;
    public double longitude;
    public double elevation;
    BufferedWriter bw;
    NotificationCompat.Builder nb;
    public static RecordService instance;
    public final long startTime = System.currentTimeMillis() / 1000;
    public long totalTime = 0;
    public double nowSpeed = 0;
    public double totalDistance = 0;
    public float accuracy = -1;
    Location previousLocation;
    public boolean run;
    boolean firstFixed;
    boolean screenOn;
    public boolean hasTask;
    StopTripReceiver stopTripReceiver;
    PauseReceiver pauseReceiver;
    ScreenOnOffReceiver screenOnOffReceiver;
    public Trip trip;
    int recordDuration; //in milliseconds
    int recordDistanceInterval; //in meters
    public int updateDuration; //in milliseconds
    boolean keepforeground;
    long lastFixTime;
    boolean shaketoaddpoi;
    SensorManager sensorManager;
    Handler handler;
    public static final String actionStopTrip = "com.yupog2003.tripdiary.stopTrip";
    public static final String actionPauseTrip = "com.yupog2003.tripdiary.pauseTrip";
    public static final String tag_tripName = "tag_tripName";
    public static final String tag_lastRecordTrip = "tag_lastRecordTrip";
    public static final String tag_onRecording = "tag_onRecording";

    String labelDistance;
    String labelTotalTime;
    String labelVelocity;
    String labelAccuracy;
    String labelAltitude;

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        instance = this;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(RecordService.this);
        String name;
        if (intent != null) {
            name = intent.getStringExtra(tag_tripName);
        } else {
            name = preferences.getString(tag_lastRecordTrip, "");
        }
        DocumentFile tripFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, name);
        if (tripFile == null) {
            Toast.makeText(this, R.string.storage_error, Toast.LENGTH_SHORT).show();
            stopSelf();
            return START_NOT_STICKY;
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            stopSelf();
            return START_NOT_STICKY;
        }
        trip = new Trip(RecordService.this, tripFile, false);
        trip.deleteCache();
        handler = new Handler();
        run = true;
        screenOn = true;
        labelDistance = getString(R.string.distance) + ":";
        labelTotalTime = getString(R.string.total_time) + ":";
        labelVelocity = getString(R.string.velocity) + ":";
        labelAccuracy = getString(R.string.accuracy) + ":";
        labelAltitude = getString(R.string.Altitude) + ":";
        try {
            recordDuration = (int) Double.parseDouble(preferences.getString("record_duration", "1000"));
            recordDuration = Math.max(recordDuration, 200);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            recordDuration = 1000;
        }
        try {
            updateDuration = (int) Double.parseDouble(preferences.getString("update_duration", "1000"));
            updateDuration = Math.max(recordDuration, 200);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            updateDuration = 1000;
        }
        try {
            recordDistanceInterval = (int) Double.parseDouble(preferences.getString("min_distance_record", "20"));
        } catch (NumberFormatException e) {
            e.printStackTrace();
            recordDistanceInterval = 20;
        }
        keepforeground = preferences.getBoolean("keepforeground", false);
        shaketoaddpoi = preferences.getBoolean("shaketoaddpoi", false);
        preferences.edit().putBoolean(tag_onRecording, true).apply();
        preferences.edit().putString(tag_lastRecordTrip, name).apply();
        setupNotification(name);
        if (shaketoaddpoi) {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        }
        if (trip.gpxFile != null && trip.gpxFile.exists()) {
            try {
                if (trip.gpxFile.length() == 0) {
                    bw = new BufferedWriter(new OutputStreamWriter(trip.gpxFile.getOutputStream()));
                    bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\" ?>\n");
                    bw.write("	<gpx>\n");
                    bw.write("	<trk>\n");
                    bw.write("	<trkseg>\n");
                    bw.flush();
                } else {
                    File temp = new File(getCacheDir(), "temp");
                    FileHelper.copyFile(trip.gpxFile, temp);
                    BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(temp)));
                    bw = new BufferedWriter(new OutputStreamWriter(trip.gpxFile.getOutputStream()));
                    String s;
                    while ((s = br.readLine()) != null) {
                        if (s.contains("<?xml") || s.contains("<gpx") || s.contains("<trk>") || s.contains("<trkseg") || s.contains("<trkpt") || s.contains("<ele>") || s.contains("<time>") || s.contains("</trkpt")) {
                            bw.write(s + "\n");
                        }
                        if (s.contains("</gpx>") || s.contains("</trkseg>") || s.contains("</trk>")) {
                            break;
                        }
                    }
                    bw.flush();
                    br.close();
                    temp.delete();
                }
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, recordDuration, 0, this);
            firstFixed = false;
            handler.postDelayed(RecordService.this, updateDuration);
        }
        stopTripReceiver = new StopTripReceiver();
        pauseReceiver = new PauseReceiver();
        screenOnOffReceiver = new ScreenOnOffReceiver();
        IntentFilter screenIntentFilter = new IntentFilter();
        screenIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(stopTripReceiver, new IntentFilter(actionStopTrip));
        registerReceiver(pauseReceiver, new IntentFilter(actionPauseTrip));
        registerReceiver(screenOnOffReceiver, screenIntentFilter);
        Intent i = new Intent(this, RecordActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.putExtra(RecordActivity.tag_tripname, name);
        startActivity(i);
        return START_STICKY;
    }

    private void setupNotification(String name) {
        nb = new NotificationCompat.Builder(this);
        nb.setContentTitle(name);
        nb.setContentText(getString(R.string.click_or_swipe_down_to_view_detail));
        nb.setSmallIcon(R.drawable.ic_satellite);
        nb.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
        nb.setTicker(getString(R.string.Start_Trip));
        Intent pauseIntent = new Intent(actionPauseTrip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            pauseIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
        }
        PendingIntent pausePendingIntent = PendingIntent.getBroadcast(this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nb.addAction(R.drawable.ic_pause, getString(R.string.pause), pausePendingIntent);
        Intent stopIntent = new Intent(actionStopTrip);
        PendingIntent stopPendingIntent = PendingIntent.getBroadcast(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        nb.addAction(R.drawable.ic_stop, getString(R.string.stop), stopPendingIntent);
        Intent i3 = new Intent(this, RecordActivity.class);
        i3.putExtra(RecordActivity.tag_tripname, name);
        PendingIntent pi4 = PendingIntent.getActivity(this, 1, i3, PendingIntent.FLAG_UPDATE_CURRENT);
        nb.setContentIntent(pi4);
        nb.setOngoing(true);
        nb.setStyle(getContent());
        startForeground(1, nb.build());
    }

    private void updateNotification() {
        if (run && screenOn) {
            final long currentTime = System.currentTimeMillis();
            totalTime = currentTime / 1000 - startTime;
            boolean isGpsFix = currentTime - lastFixTime < recordDuration + 2000;
            if (isGpsFix) {
                nb.setSmallIcon(R.drawable.ic_play);
            } else {
                nb.setSmallIcon(R.drawable.ic_satellite);
                accuracy = -1;
            }
            nb.setStyle(getContent());
            startForeground(1, nb.build());
        }
    }

    private NotificationCompat.InboxStyle getContent() {
        String timeExpression = String.valueOf(totalTime / 3600) + ":" + String.valueOf(totalTime % 3600 / 60) + ":" + String.valueOf(totalTime % 3600 % 60);
        NotificationCompat.InboxStyle content = new NotificationCompat.InboxStyle();
        content.addLine(labelDistance + GpxAnalyzer2.getDistanceString((float) totalDistance / 1000, "km"));
        content.addLine(labelTotalTime + timeExpression);
        content.addLine(labelVelocity + GpxAnalyzer2.getDistanceString((float) nowSpeed * 18 / 5, "km/hr"));
        content.addLine(labelAccuracy + (accuracy == -1 ? "∞" : GpxAnalyzer2.getAltitudeString(accuracy, "m")));
        content.addLine(labelAltitude + GpxAnalyzer2.getAltitudeString((float) elevation, "m"));
        return content;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        hasTask = false;
    }

    @Override
    public void onDestroy() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.removeUpdates(RecordService.this);
        }
        if (stopTripReceiver != null) {
            unregisterReceiver(stopTripReceiver);
            stopTripReceiver = null;
        }
        if (pauseReceiver != null) {
            unregisterReceiver(pauseReceiver);
            pauseReceiver = null;
        }
        if (screenOnOffReceiver != null) {
            unregisterReceiver(screenOnOffReceiver);
            screenOnOffReceiver = null;
        }
        if (shaketoaddpoi && sensorManager != null) {
            sensorManager.unregisterListener(RecordService.this);
        }
        run = false;
        instance = null;
        nb = null;
        trip = null;
        if (bw != null) {
            try {
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bw = null;
        }
        handler = null;
        super.onDestroy();
    }

    public class StopTripReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            run = false;
            try {
                if (bw != null) {
                    bw.write("	</trkseg>\n");
                    bw.write("	</trk>\n");
                    bw.write("	</gpx>");
                    bw.flush();
                    bw.close();
                    bw = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            trip.deleteCache();
            stopForeground(true);
            Toast.makeText(getApplicationContext(), getString(R.string.trip_has_been_stopped), Toast.LENGTH_SHORT).show();
            DeviceHelper.sendGATrack(RecordService.this, "Trip", "stop", trip.tripName, null);
            SharedPreferences.Editor preferencesEditor = PreferenceManager.getDefaultSharedPreferences(RecordService.this).edit();
            preferencesEditor.putBoolean(tag_onRecording, false).apply();
            preferencesEditor.remove(RecordActivity.pref_tag_onaddpoi).apply();
            instance = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityManager.AppTask task = MyActivity.findViewTripActivityTask(RecordService.this, trip.tripName);
                if (task != null) {
                    task.finishAndRemoveTask();
                }
            }
            Intent i = new Intent(context, ViewTripActivity.class);
            i.putExtra(ViewTripActivity.tag_tripName, trip.tripName);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            RecordService.this.stopSelf();
        }
    }

    public class PauseReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            run = !run;
            nb = new NotificationCompat.Builder(RecordService.this);
            nb.setContentTitle(trip.tripName);
            nb.setContentText(getString(R.string.click_or_swipe_down_to_view_detail));
            nb.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher));
            nb.setTicker(getString(R.string.Start_Trip));
            if (run) {
                if (ContextCompat.checkSelfPermission(RecordService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, recordDuration, 0, RecordService.this);
                }
                firstFixed = false;
                handler.postDelayed(RecordService.this, updateDuration);
                nb.setSmallIcon(R.drawable.ic_satellite);
                Intent pauseIntent = new Intent(actionPauseTrip);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    pauseIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                }
                PendingIntent pausePendingIntent = PendingIntent.getBroadcast(RecordService.this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                nb.addAction(R.drawable.ic_pause, getString(R.string.pause), pausePendingIntent);
            } else {
                if (ContextCompat.checkSelfPermission(RecordService.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    locationManager.removeUpdates(RecordService.this);
                }
                nb.setSmallIcon(R.drawable.ic_pause);
                Intent pauseIntent = new Intent(actionPauseTrip);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    pauseIntent.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
                }
                PendingIntent pausePendingIntent = PendingIntent.getBroadcast(RecordService.this, 0, pauseIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                nb.addAction(R.drawable.ic_play, getString(R.string.resume), pausePendingIntent);
            }
            Intent stopIntent = new Intent(actionStopTrip);
            PendingIntent stopPendingIntent = PendingIntent.getBroadcast(RecordService.this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            nb.addAction(R.drawable.ic_stop, getString(R.string.stop), stopPendingIntent);
            Intent i3 = new Intent(RecordService.this, RecordActivity.class);
            i3.putExtra(RecordActivity.tag_tripname, trip.tripName);
            PendingIntent pi4 = PendingIntent.getActivity(RecordService.this, 1, i3, PendingIntent.FLAG_UPDATE_CURRENT);
            nb.setContentIntent(pi4);
            nb.setOngoing(true);
            nb.setStyle(getContent());
            startForeground(1, nb.build());
        }
    }

    public class ScreenOnOffReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) return;
            String action = intent.getAction();
            if (action == null) return;
            if (action.equals(Intent.ACTION_SCREEN_ON)) {
                screenOn = true;
                if (keepforeground && DummyActivity.instance != null) {
                    DummyActivity.instance.finishAndRemoveTask();
                }
                handler.postDelayed(RecordService.this, updateDuration);
                if (shaketoaddpoi && sensorManager != null) {
                    sensorManager.registerListener(RecordService.this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_UI);
                }
            } else if (action.equals(Intent.ACTION_SCREEN_OFF)) {
                screenOn = false;
                if (keepforeground && !hasTask) {
                    Intent i = new Intent(context, DummyActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(i);
                }
                if (shaketoaddpoi && sensorManager != null) {
                    sensorManager.unregisterListener(RecordService.this);
                }
            }
        }
    }

    public void onLocationChanged(Location location) {
        if (location == null)
            return;
        lastFixTime = System.currentTimeMillis();
        if (!firstFixed) {
            ((Vibrator) getSystemService(Service.VIBRATOR_SERVICE)).vibrate(200);
            nb.setSmallIcon(R.drawable.ic_play);
            startForeground(1, nb.build());
            firstFixed = true;
        }
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        elevation = location.getAltitude();
        nowSpeed = location.getSpeed();
        accuracy = location.getAccuracy();
        float locationDifference;
        if (previousLocation == null) {
            previousLocation = location;
            locationDifference = -1;
        } else {
            locationDifference = location.distanceTo(previousLocation);
        }
        if (locationDifference >= 0 && locationDifference < recordDistanceInterval)
            return;
        if (locationDifference > 0)
            totalDistance += locationDifference;
        previousLocation = location;
        MyCalendar time = MyCalendar.getInstance(TimeZone.getTimeZone("UTC"));
        try {
            if (run) {
                bw.write("		<trkpt lat=\"" + String.valueOf(latitude) + "\" lon=\"" + String.valueOf(longitude) + "\">\n");
                bw.write("			<ele>" + String.valueOf(elevation) + "</ele>\n");
                bw.write("			<time>" + String.valueOf(time.get(Calendar.YEAR)) + "-" + String.valueOf(time.get(Calendar.MONTH) + 1) + "-" + String.valueOf(time.get(Calendar.DAY_OF_MONTH)) + "T" + String.valueOf(time.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(time.get(Calendar.MINUTE)) + ":" + String.valueOf(time.get(Calendar.SECOND)) + "Z</time>\n");
                bw.write("		</trkpt>\n");
                bw.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    public void run() {
        if (run && screenOn) {
            updateNotification();
            handler.postDelayed(this, updateDuration);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    long lastUpdateSensor = -1;
    long lastAddPOI = -1;
    public static final float SHAKE_THRESHOLD_GRAVITY = 2.7F;
    public static final int min_diffTime_between_add_poi = 5000; // milliseconds

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            long diffTime = System.currentTimeMillis() - lastUpdateSensor;
            if (diffTime > 100) {
                lastUpdateSensor = System.currentTimeMillis();
                float x = event.values[0] / SensorManager.GRAVITY_EARTH;
                float y = event.values[1] / SensorManager.GRAVITY_EARTH;
                float z = event.values[2] / SensorManager.GRAVITY_EARTH;
                float g = (float) Math.sqrt(x * x + y * y + z * z);
                if (g > SHAKE_THRESHOLD_GRAVITY && lastUpdateSensor - lastAddPOI > min_diffTime_between_add_poi) {
                    if (trip == null) return;
                    MyCalendar time = MyCalendar.getInstance(TimeZone.getTimeZone("UTC"));
                    POI poi = trip.createPOI(String.valueOf(System.currentTimeMillis()), time, latitude, longitude, elevation);
                    if (poi == null) return;
                    lastAddPOI = lastUpdateSensor;
                    ((Vibrator) getSystemService(Service.VIBRATOR_SERVICE)).vibrate(200);
                }
            }
        }
    }
}
