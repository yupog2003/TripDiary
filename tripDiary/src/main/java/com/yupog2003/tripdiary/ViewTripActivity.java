package com.yupog2003.tripdiary;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.provider.DocumentFile;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.TrackCache;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.fragments.AllAudioFragment;
import com.yupog2003.tripdiary.fragments.AllPictureFragment;
import com.yupog2003.tripdiary.fragments.AllTextFragment;
import com.yupog2003.tripdiary.fragments.AllVideoFragment;
import com.yupog2003.tripdiary.fragments.ViewCostFragment;
import com.yupog2003.tripdiary.fragments.ViewMapFragment;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class ViewTripActivity extends MyActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    public Trip trip;
    ViewMapFragment viewMapFragment;
    AllAudioFragment allAudioFragment;
    AllTextFragment allTextFragment;
    AllPictureFragment allPictureFragment;
    AllVideoFragment allVideoFragment;
    ViewCostFragment viewCostFragment;
    TextView navigationTripName;
    TextView navigationTripTime;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    RelativeLayout navigationHeader;
    ActionBarDrawerToggle drawerToggle;
    AppBarLayout appBarLayout;
    FrameLayout fragmentLayout;
    CoordinatorLayout coordinatorLayout;
    Mode mode = Mode.map_mode;
    int appBarLayoutDefaultHeight;

    public static final int REQUEST_VIEW_POI = 1;
    public static final String tag_request_updatePOI = "request_update_poi";
    public static final String tag_update_poiNames = "update_poiNames";
    public static final String tag_tripName = "tag_tripname";

    enum Mode {
        map_mode, text_mode, photo_mode, video_mode, audio_mode, money_mode
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            assert getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        String tripName = getIntent().getStringExtra(tag_tripName);
        DeviceHelper.sendGATrack(getActivity(), "Trip", "view", tripName, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(new ActivityManager.TaskDescription(tripName, null, getResources().getColor(R.color.colorPrimary)));
        }
        setTitle(tripName);
        trip = null;
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        drawerLayout.setStatusBarBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        navigationTripName = (TextView) findViewById(R.id.navigationTripName);
        navigationTripTime = (TextView) findViewById(R.id.navigationTripTime);
        navigationHeader = (RelativeLayout) findViewById(R.id.navigationHeader);
        navigationHeader.setOnClickListener(this);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        fragmentLayout = (FrameLayout) findViewById(R.id.fragment);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        allAudioFragment = new AllAudioFragment();
        allVideoFragment = new AllVideoFragment();
        allPictureFragment = new AllPictureFragment();
        allTextFragment = new AllTextFragment();
        viewMapFragment = new ViewMapFragment();
        viewCostFragment = new ViewCostFragment();
        Bundle args = new Bundle();
        args.putString(ViewCostActivity.tag_trip, tripName);
        args.putInt(ViewCostActivity.tag_option, ViewCostActivity.optionTrip);
        viewCostFragment.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment, viewMapFragment, ViewMapFragment.class.getSimpleName());
        ft.add(R.id.fragment, allAudioFragment, AllAudioFragment.class.getSimpleName());
        ft.add(R.id.fragment, allPictureFragment, AllPictureFragment.class.getSimpleName());
        ft.add(R.id.fragment, allTextFragment, AllTextFragment.class.getSimpleName());
        ft.add(R.id.fragment, allVideoFragment, AllVideoFragment.class.getSimpleName());
        ft.add(R.id.fragment, viewCostFragment, ViewCostFragment.class.getSimpleName());
        ft.commit();
        appBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    appBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    appBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                appBarLayoutDefaultHeight = appBarLayout.getHeight();
                setMode(Mode.map_mode);
            }
        });
        setMode(Mode.map_mode);
        new PrepareTripTask().execute(tripName);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(navigationHeader)) {
            new LoadNavigationHeaderBackgroundTask().execute();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.map:
                setMode(Mode.map_mode);
                break;
            case R.id.diary:
                setMode(Mode.text_mode);
                break;
            case R.id.photo:
                setMode(Mode.photo_mode);
                break;
            case R.id.video:
                setMode(Mode.video_mode);
                break;
            case R.id.audio:
                setMode(Mode.audio_mode);
                break;
            case R.id.money:
                setMode(Mode.money_mode);
                break;
        }
        return false;
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
        ft = getSupportFragmentManager().beginTransaction();
        resetAppBar();
        this.mode = mode;
        switch (mode) {
            case map_mode:
                if (trip != null)
                    setTitle(trip.tripName);
                ft.show(viewMapFragment);
                navigationView.getMenu().findItem(R.id.map).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, appBarLayoutDefaultHeight);
                break;
            case text_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.diary));
                ft.show(allTextFragment);
                navigationView.getMenu().findItem(R.id.diary).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, 0);
                break;
            case photo_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.photo));
                ft.show(allPictureFragment);
                navigationView.getMenu().findItem(R.id.photo).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, 0);
                break;
            case video_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.video));
                ft.show(allVideoFragment);
                navigationView.getMenu().findItem(R.id.video).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, 0);
                break;
            case audio_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.sound));
                ft.show(allAudioFragment);
                navigationView.getMenu().findItem(R.id.audio).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, 0);
                break;
            case money_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.cost));
                ft.show(viewCostFragment);
                navigationView.getMenu().findItem(R.id.money).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, appBarLayoutDefaultHeight);
                break;
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else if (mode == Mode.map_mode) {
            super.onBackPressed();
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
        if (trip != null && !trip.dir.exists()) { //trip directory has been deleted. Finish this activity
            finishAndRemoveTask();
        }
        if (trip != null) {
            ((TripDiaryApplication) getApplication()).putTrip(trip);
        }
    }

    private void resetAppBar() {
        if (appBarLayout == null || coordinatorLayout == null) return;
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.onNestedFling(coordinatorLayout, appBarLayout, null, 0, -1000, true);
        }
    }

    @Override
    public void finishAndRemoveTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        } else {
            finish();
        }
    }

    class PrepareTripTask extends AsyncTask<String, Object, Boolean> implements GpxAnalyzerJava.ProgressChangedListener, Trip.ConstructListener {

        LatLngBounds bounds;
        LatLng[] lat;
        ProgressDialog pd;

        @Override
        protected void onPreExecute() {
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
        protected Boolean doInBackground(String... params) {
            String tripName = params[0];
            if (tripName == null) {
                return false;
            }
            DocumentFile tripDir = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName);
            if (tripDir == null) {
                return false;
            }
            trip = new Trip(getApplicationContext(), tripDir, false, this);
            trip.listener = null;
            if (libraryLoadSuccess) {
                trip.getCacheJNI(this);
            } else {
                trip.getCacheJava(this);
            }
            ((TripDiaryApplication) getApplication()).putTrip(trip);
            try {
                if (trip.cache != null) {
                    TrackCache cache = trip.cache;
                    final int latsSize = Math.min(cache.latitudes.length, cache.longitudes.length);
                    lat = new LatLng[latsSize];
                    LatLngBounds.Builder latlngBoundesBuilder = new LatLngBounds.Builder();
                    for (int i = 0; i < latsSize; i++) {
                        lat[i] = new LatLng(cache.latitudes[i], cache.longitudes[i]);
                        latlngBoundesBuilder.include(lat[i]);
                    }
                    bounds = latlngBoundesBuilder.build();
                }
            } catch (Exception e) {
                if (trip != null || trip.cacheFile != null) {
                    trip.cacheFile.delete();
                }
                e.printStackTrace();
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), R.string.track_is_too_long, Toast.LENGTH_SHORT).show();
            }
            return trip != null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            pd.setProgress((int) values[0]);
            pd.setMessage((String) values[1]);
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                viewMapFragment.refresh(trip, lat, bounds, pd);
                if (trip.pois != null) {
                    allTextFragment.refresh();
                    allPictureFragment.refresh();
                    allVideoFragment.refresh();
                    allAudioFragment.refresh();
                    viewCostFragment.refreshData(false);
                }
                if (trip.tripName != null && trip.time != null) {
                    navigationTripName.setText(trip.tripName);
                    navigationTripTime.setText(trip.time.formatInTimezone(trip.timezone));
                    new LoadNavigationHeaderBackgroundTask().execute();
                }
            } else {
                Toast.makeText(getActivity(), R.string.invalid_trip_file, Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
                pd.dismiss();
            }
        }

        String analyzeGpx = getString(R.string.analysis_gpx);

        @Override
        public void onPOICreated(int poi, int total, String poiName) {  //construct poi
            publishProgress(500 * poi / total, poiName);
        }

        @Override
        public void onProgressChanged(long progress, long fileSize) { //analyze gpx
            publishProgress(500 + (int) (500 * progress / fileSize), analyzeGpx);
        }
    }

    class LoadNavigationHeaderBackgroundTask extends AsyncTask<String, String, Bitmap> {

        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.loadbackgroudprocess);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            if (trip == null || trip.pois == null) return null;
            ArrayList<DocumentFile> pics = new ArrayList<>();
            for (POI poi : trip.pois) {
                pics.addAll(Arrays.asList(poi.picFiles));
            }
            if (pics.size() > 0) {
                try {
                    int targetWidth = DeviceHelper.getScreenWidth(getActivity());
                    int targetHeight = (int) DeviceHelper.pxFromDp(getActivity(), 172);
                    DocumentFile pic = pics.get(new Random().nextInt(pics.size()));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeStream(getContentResolver().openInputStream(pic.getUri()), new Rect(0, 0, 0, 0), options);
                    float ratio = Math.min(options.outWidth / targetWidth, options.outHeight / targetHeight);
                    if (ratio < 1) ratio = 1;
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = (int) ratio;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    return BitmapFactory.decodeStream(getContentResolver().openInputStream(pic.getUri()), new Rect(0, 0, 0, 0), options);
                } catch (FileNotFoundException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progressBar.setVisibility(View.GONE);
            if (bitmap != null) {
                ImageView imageView = (ImageView) findViewById(R.id.navigationHeaderBackground);
                imageView.setImageBitmap(bitmap);
            }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_VIEW_POI) {
                Bundle bundle = data.getExtras();
                if (bundle == null) return;
                if (bundle.getBoolean(tag_request_updatePOI, false)) {
                    onPOIUpdate(bundle.getString(tag_update_poiNames, null));
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public void onPOIUpdate(final String poiName) {
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

    @Override
    protected void onDestroy() {
        if (trip != null) {
            ((TripDiaryApplication) getApplication()).removeTrip(trip.tripName);
            trip = null;
        }
        ImageView imageView = (ImageView) findViewById(R.id.navigationHeaderBackground);
        imageView.setImageBitmap(null);
        System.gc();
        super.onDestroy();
    }
}
