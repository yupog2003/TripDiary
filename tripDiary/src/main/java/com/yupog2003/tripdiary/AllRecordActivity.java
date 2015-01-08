package com.yupog2003.tripdiary;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava;
import com.yupog2003.tripdiary.data.MyLatLng2;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Record;
import com.yupog2003.tripdiary.data.TimeAnalyzer;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.views.POIInfoWindowAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AllRecordActivity extends MyActivity implements OnInfoWindowClickListener {
    MapFragment mapFragment;
    GoogleMap gmap;
    Record record;
    String rootPath;
    String[] tripPaths;
    Trip[] trips;
    AnalysisTask analysisTask;
    ArrayList<Marker> markers;
    public static final int[] colors = new int[]{Color.parseColor("#FF0000"), Color.parseColor("#FF8000"), Color.parseColor("#FFFF00"), Color.parseColor("#00FF00"), Color.parseColor("#00FFFF"), Color.parseColor("#0000FF"), Color.parseColor("#FF00FF")};
    public static final String tag_trip_paths = "tripPaths";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_record);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
        }
        mapFragment = MapFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.maplayout, mapFragment, "mapFragment");
        ft.commit();
        rootPath = MainActivity.rootPath;
        markers = new ArrayList<Marker>();
        record = new Record();
        tripPaths = getIntent().getStringArrayExtra(tag_trip_paths);
        if (tripPaths == null) {
            tripPaths = new String[0];
        }
        trips = new Trip[tripPaths.length];
        record.num_Trips = tripPaths.length;
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (gmap == null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {

                    gmap = googleMap;
                    gmap.setInfoWindowAdapter(new POIInfoWindowAdapter(AllRecordActivity.this, rootPath));
                    gmap.setOnInfoWindowClickListener(AllRecordActivity.this);
                    gmap.getUiSettings().setZoomControlsEnabled(true);
                    analysisTask = new AnalysisTask();
                    analysisTask.execute();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_all_record, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.statistics:
                AlertDialog.Builder ab = new AlertDialog.Builder(this);
                ab.setTitle(R.string.statistics);
                View rootView = getLayoutInflater().inflate(R.layout.all_record_statistics, null);
                TextView totalTime = (TextView) rootView.findViewById(R.id.totalTime);
                totalTime.setText(record.totalTime);
                TextView totalDistance = (TextView) rootView.findViewById(R.id.distance);
                totalDistance.setText(GpxAnalyzer2.getDistanceString(record.totalDistance / 1000, "km"));
                TextView totalClimb = (TextView) rootView.findViewById(R.id.totalClimb);
                totalClimb.setText(GpxAnalyzer2.getAltitudeString(record.totalClimb, "m"));
                TextView maxAltitude = (TextView) rootView.findViewById(R.id.maxAltitude);
                maxAltitude.setText(GpxAnalyzer2.getAltitudeString(record.maxAltitude, "m"));
                TextView minAltitude = (TextView) rootView.findViewById(R.id.minAltitude);
                minAltitude.setText(GpxAnalyzer2.getAltitudeString(record.minAltitude, "m"));
                TextView maxLatitude = (TextView) rootView.findViewById(R.id.maxLatitude);
                maxLatitude.setText(String.valueOf(record.maxLatitude));
                TextView minLatitude = (TextView) rootView.findViewById(R.id.minLatitude);
                minLatitude.setText(String.valueOf(record.minLatitude));
                TextView numTrips = (TextView) rootView.findViewById(R.id.numTrips);
                numTrips.setText(String.valueOf(record.num_Trips));
                TextView numPOIs = (TextView) rootView.findViewById(R.id.numPOIs);
                numPOIs.setText(String.valueOf(record.num_POIs));
                TextView numPics = (TextView) rootView.findViewById(R.id.numPics);
                numPics.setText(String.valueOf(record.num_Pictures));
                TextView numVideos = (TextView) rootView.findViewById(R.id.numVideos);
                numVideos.setText(String.valueOf(record.num_Videos));
                TextView numAudios = (TextView) rootView.findViewById(R.id.numAudios);
                numAudios.setText(String.valueOf(record.num_Audios));
                ab.setView(rootView);
                ab.show();
                break;
        }
        return true;
    }

    class AnalysisTask extends AsyncTask<String, Object, Boolean> implements OnCancelListener {

        AlertDialog ad;
        TextView message;
        TextView smallMessage;
        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {

            setProgressBarIndeterminateVisibility(true);
            AlertDialog.Builder ab = new AlertDialog.Builder(AllRecordActivity.this);
            ab.setTitle(R.string.analysis_gpx);
            View rootView = getLayoutInflater().inflate(R.layout.classical_progress_dialog, null);
            message = (TextView) rootView.findViewById(R.id.message);
            progressBar = (ProgressBar) rootView.findViewById(R.id.progress);
            progressBar.setMax(tripPaths.length);
            smallMessage = (TextView) rootView.findViewById(R.id.smallmessage);
            ab.setView(rootView);
            ad = ab.create();
            ad.show();
            if (!libraryLoadSuccess) {
                Toast.makeText(AllRecordActivity.this, getString(R.string.failed_to_loadlibrary), Toast.LENGTH_SHORT).show();
            }
        }

        public void changeProgress(int progress) {
            publishProgress(progress);
        }

        public void showTrack(double[] latitudes, double[] longitudes) {
            publishProgress(latitudes, longitudes);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            if (tripPaths == null)
                return false;
            for (int i = 0; i < tripPaths.length; i++) {
                tripPaths[i] = rootPath + "/" + tripPaths[i];
                trips[i] = new Trip(AllRecordActivity.this, new File(tripPaths[i]));
            }
            boolean success;
            if (libraryLoadSuccess) {
                success = parse(rootPath, tripPaths, new int[0], record);
            } else {
                success = parseJava(rootPath, tripPaths, new int[0], record);
            }
            return success;
        }

        int count = 0;

        @Override
        public void onProgressUpdate(Object... values) {

            if (values[0] instanceof Integer) {
                int value = (Integer) values[0];
                if (ad != null && ad.isShowing()) {
                    progressBar.setProgress(value);
                    message.setText(trips[value].tripName);
                    smallMessage.setText(String.valueOf(value) + "/" + String.valueOf(progressBar.getMax()));
                }
                POI[] pois = trips[value].pois;
                record.num_POIs += pois.length;
                for (int j = 0; j < pois.length; j++) {
                    record.num_Pictures += pois[j].picFiles.length;
                    record.num_Videos += pois[j].videoFiles.length;
                    record.num_Audios += pois[j].audioFiles.length;
                    if (markers == null || gmap == null)
                        continue;
                    markers.add(gmap.addMarker(new MarkerOptions().position(new LatLng(pois[j].latitude, pois[j].longitude)).title(trips[value].tripName + "/" + pois[j].title).snippet(" " + TimeAnalyzer.formatInTimezone(pois[j].time, trips[value].timezone) + "\n " + pois[j].diary).draggable(false)));
                }
            } else if (values[0] instanceof double[]) {
                if (gmap != null) {
                    try {
                        double[] latitudes = (double[]) values[0];
                        double[] longitudes = (double[]) values[1];
                        int trackSize = Math.min(latitudes.length, longitudes.length);
                        LatLng[] lats = new LatLng[trackSize / 60];
                        for (int j = 0; j < trackSize / 60; j++) {
                            lats[j] = new LatLng(latitudes[j * 60], longitudes[j * 60]);
                        }
                        gmap.addPolyline(new PolylineOptions().add(lats).color(colors[count % colors.length]).width(5));
                        count++;
                        if (count == colors.length)
                            count = 0;
                        latitudes = null;
                        longitudes = null;
                        lats = null;
                        System.gc();
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        System.gc();
                    }
                } else {
                    gmap = mapFragment.getMap();
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {

            ad.dismiss();
            setProgressBarIndeterminateVisibility(false);
        }

        public void onCancel(DialogInterface dialog) {

            AllRecordActivity.this.stop();
        }

    }

    public void progressChanged(int progress) {
        if (analysisTask != null) {
            analysisTask.changeProgress(progress);
        }
    }

    public void onTrackFinished(double[] latitudes, double[] longitudes) {
        if (analysisTask != null) {
            analysisTask.showTrack(latitudes, longitudes);
        }
    }

    public native boolean parse(String rootPath, String[] tripPaths, int[] timezones, Record record);

    public native void stop();

    public void onInfoWindowClick(Marker marker) {

        String pointtitle = marker.getTitle();
        if (new File(rootPath + "/" + pointtitle).exists()) {
            Intent intent = new Intent(AllRecordActivity.this, ViewPointActivity.class);
            intent.putExtra("path", rootPath + "/" + pointtitle);
            startActivity(intent);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    boolean stop;
    public static final int altitudeDifferThreshold = 20;

    public boolean parseJava(String rootPath, String[] tripPaths, int[] timezones, Record record) {
        stop = false;
        int num_trips = tripPaths.length;
        float totalDistance = 0;
        float totalClimb = 0;
        float maxAltitude = Float.MIN_VALUE;
        float minAltitude = Float.MAX_VALUE;
        double maxLatitude = Float.MIN_VALUE;
        double minLatitude = Float.MAX_VALUE;
        long totalSeconds = 0;
        String s;
        MyLatLng2 latlng = new MyLatLng2();
        for (int i = 0; i < num_trips; i++) {
            try {
                if (stop)
                    return false;
                progressChanged(i);
                MyLatLng2 preLatLng = new MyLatLng2();
                boolean first = true;
                String tripPath = tripPaths[i];
                String tripName = tripPath.substring(tripPath.lastIndexOf("/") + 1);
                String gpxPath = tripPath + "/" + tripName + ".gpx";
                BufferedReader br = new BufferedReader(new FileReader(new File(gpxPath)));
                ArrayList<MyLatLng2> track = new ArrayList<MyLatLng2>();
                ArrayList<String> timeStrs = new ArrayList<String>();
                while ((s = br.readLine()) != null) {
                    if (stop) {
                        return false;
                    }
                    if (s.contains("<trkpt")) {
                        latlng = new MyLatLng2();
                        if (s.indexOf("lat") > s.indexOf("lon")) {
                            latlng.longitude = Double.parseDouble(s.split("\"")[1]);
                            latlng.latitude = Double.parseDouble(s.split("\"")[3]);
                        } else {
                            latlng.latitude = Double.parseDouble(s.split("\"")[1]);
                            latlng.longitude = Double.parseDouble(s.split("\"")[3]);
                        }
                        if (latlng.latitude > maxLatitude) {
                            maxLatitude = latlng.latitude;
                        }
                        if (latlng.latitude < minLatitude) {
                            minLatitude = latlng.latitude;
                        }
                    } else if (s.contains("<ele>")) {
                        float altitude = Float.parseFloat(s.substring(s.indexOf(">") + 1, s.lastIndexOf("<")));
                        latlng.altitude = altitude;
                        if (altitude > maxAltitude)
                            maxAltitude = altitude;
                        if (altitude < minAltitude)
                            minAltitude = altitude;
                    } else if (s.contains("<time>")) {
                        timeStrs.add(s);
                    } else if (s.contains("</trkpt>")) {
                        if (!first) {
                            float altitudeDiffer = latlng.altitude - preLatLng.altitude;
                            if (Math.abs(altitudeDiffer) > altitudeDifferThreshold) {
                                if (altitudeDiffer > 0)
                                    totalClimb += altitudeDiffer;
                            }
                            totalDistance += GpxAnalyzerJava.distFrom(preLatLng.latitude, preLatLng.longitude, latlng.latitude, latlng.longitude);
                        } else {
                            first = false;
                        }
                        preLatLng = latlng;
                        track.add(latlng);
                    }
                }
                br.close();
                int trackSize = track.size();
                if (timeStrs.size() > 0)
                    totalSeconds += TimeAnalyzer.getMinusTimeInSecond(TimeAnalyzer.getTime(timeStrs.get(0), TimeAnalyzer.type_gpx), TimeAnalyzer.getTime(timeStrs.get(timeStrs.size() - 1), TimeAnalyzer.type_gpx));
                double[] latitudes = new double[trackSize];
                double[] longitudes = new double[trackSize];
                for (int j = 0; j < trackSize; j++) {
                    latitudes[j] = track.get(j).latitude;
                    longitudes[j] = track.get(j).longitude;
                }
                onTrackFinished(latitudes, longitudes);
            } catch (FileNotFoundException e) {

                e.printStackTrace();
            } catch (NumberFormatException e) {

                e.printStackTrace();
            } catch (IOException e) {

                e.printStackTrace();
            }
        }
        String totalTime = "";
        long day = totalSeconds / 86400;
        long hour = totalSeconds % 86400 / 3600;
        long min = totalSeconds % 3600 / 60;
        long sec = totalSeconds % 60;
        if (day != 0) {
            totalTime = String.valueOf(day) + "T";
        }
        totalTime += String.valueOf(hour) + ":" + String.valueOf(min) + ":" + String.valueOf(sec);
        record.totalTime = totalTime;
        record.totalDistance = totalDistance;
        record.totalClimb = totalClimb;
        record.maxAltitude = maxAltitude;
        record.minAltitude = minAltitude;
        record.maxLatitude = maxLatitude;
        record.minLatitude = minLatitude;
        return true;

    }

    public static boolean libraryLoadSuccess = false;

    static {
        try {
            System.loadLibrary("TripDiary");
            libraryLoadSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            libraryLoadSuccess = false;
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            libraryLoadSuccess = false;
        }

    }
}
