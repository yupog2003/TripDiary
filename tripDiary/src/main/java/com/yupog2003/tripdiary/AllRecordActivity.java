package com.yupog2003.tripdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.provider.DocumentFile;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yupog2003.tripdiary.data.ColorHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.MyLatLng2;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Record;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.views.POIInfoWindowAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class AllRecordActivity extends MyActivity implements OnInfoWindowClickListener {
    SupportMapFragment mapFragment;
    GoogleMap gmap;
    Record record;
    String[] tripNames;
    static Trip[] trips;
    AnalysisTask analysisTask;
    ArrayList<Marker> markers;
    ProgressBar progressBar;
    public static final int[] colors = new int[]{Color.parseColor("#FF0000"), Color.parseColor("#FF8000"), Color.parseColor("#FFFF00"), Color.parseColor("#00FF00"), Color.parseColor("#00FFFF"), Color.parseColor("#0000FF"), Color.parseColor("#FF00FF")};
    public static final String tag_trip_names = "tripNames";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_record);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
        }
        progressBar = (ProgressBar) findViewById(R.id.analysis_progress);
        mapFragment = SupportMapFragment.newInstance();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.maplayout, mapFragment, "mapFragment");
        ft.commit();
        markers = new ArrayList<>();
        record = new Record();
        record.maxAltitude = -Float.MAX_VALUE;
        record.minAltitude = Float.MAX_VALUE;
        record.maxLatitude = -Float.MAX_VALUE;
        record.minLatitude = Float.MAX_VALUE;
        tripNames = getIntent().getStringArrayExtra(tag_trip_names);
        if (tripNames == null) {
            tripNames = new String[0];
        }
        trips = new Trip[tripNames.length];
        record.num_Trips = tripNames.length;
    }

    @Override
    protected void onResume() {

        super.onResume();
        if (gmap == null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {
                    gmap = googleMap;
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
                View rootView = getLayoutInflater().inflate(R.layout.all_record_statistics, (ViewGroup)findViewById(android.R.id.content), false);
                TextView totalTime = (TextView) rootView.findViewById(R.id.totalTime);
                totalTime.setText(MyCalendar.formatTotalTime(record.totalTime));
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

        String progressFormat;
        BitmapDescriptor bd;

        @Override
        protected void onPreExecute() {
            setProgressBarIndeterminateVisibility(true);
            if (!libraryLoadSuccess) {
                Toast.makeText(AllRecordActivity.this, getString(R.string.failed_to_loadlibrary), Toast.LENGTH_SHORT).show();
            }
            progressBar.setMax(tripNames.length);
            progressFormat = getString(R.string.lifetime_record) + "(%d/%d)";
            float hue = ColorHelper.getMarkerColorHue(getActivity());
            bd = BitmapDescriptorFactory.defaultMarker(hue);
        }

        public void changeProgress(int progress) {
            publishProgress(progress);
        }

        public void showTrack(double[] latitudes, double[] longitudes) {
            publishProgress(latitudes, longitudes);
        }

        @Override
        protected Boolean doInBackground(String... params) {

            if (tripNames == null)
                return false;
            boolean success = true;
            DocumentFile[] tripFiles = TripDiaryApplication.rootDocumentFile.listFiles();
            for (int i = 0; i < tripNames.length; i++) {
                DocumentFile tripFile = FileHelper.findfile(tripFiles, tripNames[i]);
                trips[i] = new Trip(AllRecordActivity.this, tripFile, false);
                publishProgress(i);
                DocumentFile gpxFile = FileHelper.findfile(tripFile, tripNames[i] + ".gpx");
                File tempFile = new File(getCacheDir(), tripNames[i] + ".gpx");
                FileHelper.copyFile(gpxFile, tempFile);
                Record record = new Record();
                if (libraryLoadSuccess) {
                    success &= parse(tempFile.getPath(), record);
                } else {
                    success &= parseJava(tempFile.getPath(), record);
                }
                tempFile.delete();
                AllRecordActivity.this.record.totalTime += record.totalTime;
                AllRecordActivity.this.record.totalDistance += record.totalDistance;
                AllRecordActivity.this.record.totalClimb += record.totalClimb;
                if (AllRecordActivity.this.record.maxAltitude < record.maxAltitude) {
                    AllRecordActivity.this.record.maxAltitude = record.maxAltitude;
                }
                if (AllRecordActivity.this.record.minAltitude > record.minAltitude) {
                    AllRecordActivity.this.record.minAltitude = record.minAltitude;
                }
                if (AllRecordActivity.this.record.maxLatitude < record.maxLatitude) {
                    AllRecordActivity.this.record.maxLatitude = record.maxLatitude;
                }
                if (AllRecordActivity.this.record.minLatitude > record.minLatitude) {
                    AllRecordActivity.this.record.minLatitude = record.minLatitude;
                }
            }

            return success;
        }

        int count = 0;

        @Override
        public void onProgressUpdate(Object... values) {

            if (values[0] instanceof Integer) {
                int value = (Integer) values[0];
                progressBar.setProgress(value);
                setTitle(String.format(progressFormat, value, tripNames.length));
                POI[] pois = trips[value].pois;
                int poisLength = pois.length;
                record.num_POIs += poisLength;
                for (POI poi : pois) {
                    record.num_Pictures += poi.picFiles.length;
                    record.num_Videos += poi.videoFiles.length;
                    record.num_Audios += poi.audioFiles.length;
                    if (markers == null || gmap == null)
                        continue;
                    markers.add(gmap.addMarker(new MarkerOptions()
                            .position(new LatLng(poi.latitude, poi.longitude))
                            .title(trips[value].tripName + "/" + poi.title)
                            .snippet(" " + poi.time.formatInTimezone(trips[value].timezone) + "\n " + poi.diary)
                            .draggable(false)
                            .icon(bd)));
                }
                if (value == tripNames.length - 1) {
                    gmap.setInfoWindowAdapter(new POIInfoWindowAdapter(getActivity(), null, trips));
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
            progressBar.setVisibility(View.GONE);
            setTitle(getString(R.string.lifetime_record));
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

    public native boolean parse(String gpxPath, Record record);

    public native void stop();

    public void onInfoWindowClick(Marker marker) {

        String pointtitle = marker.getTitle();
        String tripName = pointtitle.split("/")[0];
        String poiName = pointtitle.split("/")[1];
        Intent intent = new Intent(AllRecordActivity.this, ViewPointActivity.class);
        intent.putExtra(ViewPointActivity.tag_tripname, tripName);
        intent.putExtra(ViewPointActivity.tag_poiname, poiName);
        intent.putExtra(ViewPointActivity.tag_fromActivity, AllRecordActivity.class.getSimpleName());
        startActivity(intent);

    }

    public static POI getPOI(String tripName, String poiName){
        for (Trip trip: trips){
            if (tripName.equals(trip.tripName)){
                return trip.getPOI(poiName);
            }
        }
        return null;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    boolean stop;
    public static final int altitudeDifferThreshold = 20;

    public boolean parseJava(String gpxPath, Record record) {
        stop = false;
        float totalDistance = 0;
        float totalClimb = 0;
        float maxAltitude = -Float.MAX_VALUE;
        float minAltitude = Float.MAX_VALUE;
        double maxLatitude = -Float.MAX_VALUE;
        double minLatitude = Float.MAX_VALUE;
        long totalSeconds = 0;
        String s;
        MyLatLng2 latlng = new MyLatLng2();

        try {
            if (stop)
                return false;
            MyLatLng2 preLatLng = new MyLatLng2();
            boolean first = true;
            File gpxFile = new File(gpxPath);
            BufferedReader br = new BufferedReader(new FileReader(gpxFile));
            ArrayList<MyLatLng2> track = new ArrayList<>();
            ArrayList<String> timeStrs = new ArrayList<>();
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
                totalSeconds += MyCalendar.getMinusTimeInSecond(MyCalendar.getTime(timeStrs.get(0), MyCalendar.type_gpx), MyCalendar.getTime(timeStrs.get(timeStrs.size() - 1), MyCalendar.type_gpx));
            double[] lats = new double[trackSize];
            double[] lngs = new double[trackSize];
            for (int j = 0; j < trackSize; j++) {
                lats[j] = track.get(j).latitude;
                lngs[j] = track.get(j).longitude;
            }
            onTrackFinished(lats, lngs);
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }

        record.totalTime = totalSeconds;
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
        } catch (Exception | UnsatisfiedLinkError e) {
            e.printStackTrace();
            libraryLoadSuccess = false;
        }
    }
}
