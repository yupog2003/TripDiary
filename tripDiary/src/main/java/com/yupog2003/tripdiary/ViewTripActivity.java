package com.yupog2003.tripdiary;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava;
import com.yupog2003.tripdiary.data.TrackCache;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.fragments.AllAudioFragment;
import com.yupog2003.tripdiary.fragments.AllPictureFragment;
import com.yupog2003.tripdiary.fragments.AllTextFragment;
import com.yupog2003.tripdiary.fragments.AllVideoFragment;
import com.yupog2003.tripdiary.fragments.ViewCostFragment;
import com.yupog2003.tripdiary.fragments.ViewMapFragment;

public class ViewTripActivity extends MyActivity implements OnClickListener {

    public static String name;
    public static Trip trip;
    public static int rotation;
    public static ViewMapFragment viewMapFragment;
    public static AllAudioFragment allAudioFragment;
    public static AllTextFragment allTextFragment;
    public static AllPictureFragment allPictureFragment;
    public static AllVideoFragment allVideoFragment;
    public static ViewCostFragment viewCostFragment;
    Button diary;
    Button photo;
    Button video;
    Button audio;
    Button map;
    Button money;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Mode mode = Mode.map_mode;
    public static LatLng[] lat;
    public static LatLngBounds bounds;
    public static ProgressDialog pd;

    enum Mode {
        map_mode, text_mode, photo_mode, video_mode, audio_mode, money_mode
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        if (getIntent().getBooleanExtra("stoptrip", false)) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(0);
        }
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        int sWidth = DeviceHelper.getScreenWidth(this);
        int sHeight = DeviceHelper.getScreenHeight(this);
        rotation = sWidth > sHeight ? Surface.ROTATION_90 : Surface.ROTATION_0;
        name = getIntent().getStringExtra("name");
        setTitle(name);
        trip = null;
        lat = null;
        bounds = null;
        pd = null;
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        map = (Button) findViewById(R.id.map);
        map.setOnClickListener(this);
        map.setSelected(true);
        diary = (Button) findViewById(R.id.diary);
        diary.setOnClickListener(this);
        photo = (Button) findViewById(R.id.photo);
        photo.setOnClickListener(this);
        video = (Button) findViewById(R.id.video);
        video.setOnClickListener(this);
        audio = (Button) findViewById(R.id.audio);
        audio.setOnClickListener(this);
        money = (Button) findViewById(R.id.money);
        money.setOnClickListener(this);
        allAudioFragment = new AllAudioFragment();
        allVideoFragment = new AllVideoFragment();
        allPictureFragment = new AllPictureFragment();
        allTextFragment = new AllTextFragment();
        viewMapFragment = new ViewMapFragment();
        viewCostFragment = new ViewCostFragment();
        Bundle args = new Bundle();
        args.putString(ViewCostActivity.tag_trip, name);
        args.putInt(ViewCostActivity.tag_option, ViewCostActivity.optionTrip);
        viewCostFragment.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment, viewMapFragment);
        ft.add(R.id.fragment, allAudioFragment);
        ft.add(R.id.fragment, allPictureFragment);
        ft.add(R.id.fragment, allTextFragment);
        ft.add(R.id.fragment, allVideoFragment);
        ft.add(R.id.fragment, viewCostFragment);
        ft.commit();
        setMode(Mode.map_mode);
        new PrepareTripTask().execute();
    }

    private void setMode(Mode mode) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(viewMapFragment);
        ft.hide(allTextFragment);
        ft.hide(allAudioFragment);
        ft.hide(allPictureFragment);
        ft.hide(allVideoFragment);
        ft.hide(viewCostFragment);
        ft.commit();
        map.setSelected(false);
        diary.setSelected(false);
        photo.setSelected(false);
        video.setSelected(false);
        audio.setSelected(false);
        money.setSelected(false);
        ft = getSupportFragmentManager().beginTransaction();
        this.mode = mode;
        switch (mode) {
            case map_mode:
                if (trip != null)
                    setTitle(trip.tripName);
                ft.show(viewMapFragment);
                map.setSelected(true);
                break;
            case text_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.diary));
                ft.show(allTextFragment);
                diary.setSelected(true);
                break;
            case photo_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.photo));
                ft.show(allPictureFragment);
                photo.setSelected(true);
                break;
            case video_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.video));
                ft.show(allVideoFragment);
                video.setSelected(true);
                break;
            case audio_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.sound));
                ft.show(allAudioFragment);
                audio.setSelected(true);
                break;
            case money_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.cost));
                ft.show(viewCostFragment);
                money.setSelected(true);
                break;
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void onBackPressed() {

        if (mode == Mode.map_mode) {
            finish();
        } else {
            setMode(Mode.map_mode);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return drawerToggle.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void onClick(View v) {
        if (v.equals(map)) {
            setMode(Mode.map_mode);
            drawerLayout.closeDrawers();
        } else if (v.equals(diary)) {
            setMode(Mode.text_mode);
            drawerLayout.closeDrawers();
        } else if (v.equals(photo)) {
            setMode(Mode.photo_mode);
            drawerLayout.closeDrawers();
        } else if (v.equals(video)) {
            setMode(Mode.video_mode);
            drawerLayout.closeDrawers();
        } else if (v.equals(audio)) {
            setMode(Mode.audio_mode);
            drawerLayout.closeDrawers();
        } else if (v.equals(money)) {
            setMode(Mode.money_mode);
            drawerLayout.closeDrawers();
        }
    }

    class PrepareTripTask extends AsyncTask<Integer, Object, LatLng[]> implements GpxAnalyzerJava.ProgressChangedListener, Trip.ConstructListener {

        long fileSize;
        Handler handler;

        public void stop() {
            if (trip != null) {
                trip.stopGetCache();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            handler = new Handler();
            pd = new ProgressDialog(ViewTripActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setMax(1000);
            pd.setCancelable(false);
            pd.show();
            if (!libraryLoadSuccess) {
                Toast.makeText(getActivity(), getString(R.string.failed_to_loadlibrary), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected LatLng[] doInBackground(Integer... params) {
            trip = new Trip(ViewTripActivity.this, FileHelper.findfile(TripDiaryApplication.rootDocumentFile, name), false, this);
            fileSize = ViewTripActivity.trip.cacheFile.length() > 0 ? ViewTripActivity.trip.cacheFile.length() : ViewTripActivity.trip.gpxFile.length();
            if (libraryLoadSuccess) {
                trip.getCacheJNI(getActivity(), handler, this);
            } else {
                trip.getCacheJava(getActivity(), handler, this);
            }
            try {
                if (trip.cache != null) {
                    TrackCache cache = ViewTripActivity.trip.cache;
                    final int latsSize = Math.min(cache.latitudes.length, cache.longitudes.length);
                    lat = new LatLng[latsSize];
                    LatLngBounds.Builder latlngBoundesBuilder = new LatLngBounds.Builder();
                    for (int i = 0; i < latsSize; i++) {
                        lat[i] = new LatLng(cache.latitudes[i], cache.longitudes[i]);
                        latlngBoundesBuilder.include(lat[i]);
                    }
                    bounds = latlngBoundesBuilder.build();
                    return lat;
                }
            } catch (Exception e) {
                trip.cacheFile.delete();
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onProgressUpdate(Object... values) {
            pd.setProgress((int) values[0]);
            pd.setMessage((String) values[1]);
        }

        @Override
        protected void onPostExecute(LatLng[] result) {
            super.onPostExecute(result);
            if (result == null || result.length == 0) {
                Toast.makeText(getActivity(), getString(R.string.invalid_gpx_file), Toast.LENGTH_SHORT).show();
                trip.deleteCache();
                pd.dismiss();
            } else {
                viewMapFragment.refresh();
                allTextFragment.refresh();
                allPictureFragment.refresh();
                allVideoFragment.refresh();
                allAudioFragment.refresh();
                viewCostFragment.refreshData(false);
            }
        }

        String analyzeGpx = getString(R.string.analysis_gpx);

        @Override
        public void onPOICreated(int poi, int total, String poiName) {  //construct poi
            publishProgress(500 * poi / total, poiName);
        }

        @Override
        public void onProgressChanged(long progress) { //analyze gpx
            publishProgress(500 + (int) (500 * progress / fileSize), analyzeGpx);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public static void onPOIUpdate(final String poiName) {
        if (trip != null) {
            if (poiName == null) {
                trip.refreshPOIs();
            } else {
                trip.refreshPOI(poiName);
            }
        }
        if (viewMapFragment != null) {
            viewMapFragment.setPOIs();
        }
        if (allTextFragment != null) {
            allTextFragment.refresh();
        }
        if (allPictureFragment != null) {
            allPictureFragment.refresh();
        }
        if (allVideoFragment != null) {
            allVideoFragment.refresh();
        }
        if (allAudioFragment != null) {
            allAudioFragment.refresh();
        }
        if (viewCostFragment != null) {
            viewCostFragment.refreshData(false);
        }

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
