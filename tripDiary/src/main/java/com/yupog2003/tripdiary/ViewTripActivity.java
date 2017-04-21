package com.yupog2003.tripdiary;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.api.services.drive.Drive;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.TrackCache;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.data.documentfile.RestDriveDocumentFile;
import com.yupog2003.tripdiary.data.documentfile.WebDocumentFile;
import com.yupog2003.tripdiary.fragments.AllAudioFragment;
import com.yupog2003.tripdiary.fragments.AllPictureFragment;
import com.yupog2003.tripdiary.fragments.AllTextFragment;
import com.yupog2003.tripdiary.fragments.AllVideoFragment;
import com.yupog2003.tripdiary.fragments.ViewCostFragment;
import com.yupog2003.tripdiary.fragments.ViewMapFragment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;

public class ViewTripActivity extends MyActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    public Trip trip;
    String token;
    String email;
    String tripName;
    boolean isPublic;
    ViewMapFragment viewMapFragment;
    AllAudioFragment allAudioFragment;
    AllTextFragment allTextFragment;
    AllPictureFragment allPictureFragment;
    AllVideoFragment allVideoFragment;
    ViewCostFragment viewCostFragment;
    DrawerLayout drawerLayout;
    View navigationHeader;
    ActionBarDrawerToggle drawerToggle;
    FrameLayout fragmentLayout;
    public AppCompatSpinner spinner;
    Mode mode = Mode.map_mode;
    int appBarLayoutDefaultHeight;
    Menu navigationMenu;
    int source;
    DriveId driveId;
    String resourceId;
    Drive service;

    public static final int REQUEST_VIEW_POI = 1;
    public static final String tag_request_updatePOI = "request_update_poi";
    public static final String tag_update_poiNames = "update_poiNames";
    public static final String tag_tripName = "tag_tripname";
    public static final String tag_fromDrive = "tag_fromDrive";
    public static final String tag_driveId = "tag_driveid";
    public static final String tag_fromRestDrive = "tag_fromRestDrive";
    public static final String tag_resourceId = "tag_resourceId";
    public static final int fromLocal = 0;
    public static final int fromWeb = 1;
    public static final int fromDrive = 2;
    public static final int fromRestDrive = 3;

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
        }
        Intent intent = getIntent();
        if (intent == null) {
            finishAndRemoveTask();
            return;
        }
        isPublic = false;
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)) {
            boolean success = tryGetTripInfoFromIntent(intent);
            if (!success) {
                finishAndRemoveTask();
                return;
            }
        } else {
            if (intent.getBooleanExtra(tag_fromDrive, false)) {
                source = fromDrive;
                driveId = intent.getParcelableExtra(tag_driveId);
            } else if (intent.getBooleanExtra(tag_fromRestDrive, false)) {
                source = fromRestDrive;
                resourceId = intent.getStringExtra(tag_resourceId);
            } else {
                source = fromLocal;
            }
            tripName = intent.getStringExtra(tag_tripName);
        }
        DeviceHelper.sendGATrack(getActivity(), "Trip", "view", tripName, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setTaskDescription(new ActivityManager.TaskDescription(tripName, null, ContextCompat.getColor(this, R.color.colorPrimary)));
        }
        setTitle(tripName);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        int colorPrimaryDark = ContextCompat.getColor(this, R.color.colorPrimaryDark);
        drawerLayout.setStatusBarBackgroundColor(Color.argb(128, Color.red(colorPrimaryDark), Color.green(colorPrimaryDark), Color.blue(colorPrimaryDark)));
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        navigationMenu = navigationView.getMenu();
        navigationHeader = navigationView.inflateHeaderView(R.layout.navigation_header_activity_view_trip);
        navigationHeader.setOnClickListener(this);
        fragmentLayout = (FrameLayout) findViewById(R.id.fragment);
        spinner = (AppCompatSpinner) findViewById(R.id.spinner);
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
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                ft.remove(fragment);
            }
        }
        ft.add(R.id.fragment, viewMapFragment, ViewMapFragment.class.getSimpleName());
        ft.add(R.id.fragment, allAudioFragment, AllAudioFragment.class.getSimpleName());
        ft.add(R.id.fragment, allPictureFragment, AllPictureFragment.class.getSimpleName());
        ft.add(R.id.fragment, allTextFragment, AllTextFragment.class.getSimpleName());
        ft.add(R.id.fragment, allVideoFragment, AllVideoFragment.class.getSimpleName());
        ft.add(R.id.fragment, viewCostFragment, ViewCostFragment.class.getSimpleName());
        ft.commit();
        final AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
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
                if (source == fromWeb) {
                    Toast.makeText(getActivity(), R.string.open_in_read_only_mode, Toast.LENGTH_SHORT).show();
                    if (isPublic) {
                        new PrepareTripTask().execute();
                    } else {
                        getAccessToken(email, new OnAccessTokenGotListener() {
                            @Override
                            public void onAccessTokenGot(@NonNull String token) {
                                if (email != null && tripName != null) {
                                    ViewTripActivity.this.token = token;
                                    new PrepareTripTask().execute();
                                }
                            }
                        });
                    }
                } else if (source == fromDrive) {
                    connectToDriveApi(new GoogleApiClient.ConnectionCallbacks() {
                        @Override
                        public void onConnected(Bundle bundle) {
                            new PrepareTripTask().execute();
                        }

                        @Override
                        public void onConnectionSuspended(int i) {

                        }
                    });
                } else if (source == fromRestDrive) {
                    Toast.makeText(getActivity(), R.string.open_in_read_only_mode, Toast.LENGTH_SHORT).show();
                    connectToRestDriveApi(new OnConnectedToRestDriveApiListener() {
                        @Override
                        public void onConnected(Drive service, String account) {
                            ViewTripActivity.this.service = service;
                            ViewTripActivity.this.email = account;
                            new PrepareTripTask().execute();
                        }
                    });
                } else if (source == fromLocal) {
                    new PrepareTripTask().execute();
                }
            }
        });
    }

    private boolean tryGetTripInfoFromIntent(Intent intent) {
        Uri uri = intent.getData();
        if (uri == null) {
            return false;
        }
        String scheme = uri.getScheme();
        String host = uri.getHost();
        if (scheme.equals("http") && (host.equals(TripDiaryApplication.serverHost) || host.equals(TripDiaryApplication.serverIP))) {
            try {
                source = fromWeb;
                String uriStr = URLDecoder.decode(uri.toString(), "UTF-8");
                String tripPath = uriStr.substring(uriStr.indexOf("trippath=") + 9, uriStr.lastIndexOf("&"));
                String[] pathSegments = tripPath.split("/");
                email = pathSegments[1];
                tripName = pathSegments[2];
                isPublic = uriStr.substring(uriStr.lastIndexOf("&public=")).contains("yes");
                if (isPublic) {
                    token = "abc";
                }
                return true;
            } catch (UnsupportedEncodingException | RuntimeException e) {
                e.printStackTrace();
            }
        }
        return false;
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
        resetAppBar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(viewMapFragment);
        ft.hide(allTextFragment);
        ft.hide(allAudioFragment);
        ft.hide(allPictureFragment);
        ft.hide(allVideoFragment);
        ft.hide(viewCostFragment);
        ft.commit();
        ft = getSupportFragmentManager().beginTransaction();
        this.mode = mode;
        switch (mode) {
            case map_mode:
                if (trip != null)
                    setTitle(trip.tripName);
                ft.show(viewMapFragment);
                navigationMenu.findItem(R.id.map).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, appBarLayoutDefaultHeight);
                spinner.setVisibility(View.VISIBLE);
                break;
            case text_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.diary));
                ft.show(allTextFragment);
                navigationMenu.findItem(R.id.diary).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, 0);
                spinner.setVisibility(View.GONE);
                break;
            case photo_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.photo));
                ft.show(allPictureFragment);
                navigationMenu.findItem(R.id.photo).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, 0);
                spinner.setVisibility(View.GONE);
                break;
            case video_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.video));
                ft.show(allVideoFragment);
                navigationMenu.findItem(R.id.video).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, 0);
                spinner.setVisibility(View.GONE);
                break;
            case audio_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.sound));
                ft.show(allAudioFragment);
                navigationMenu.findItem(R.id.audio).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, 0);
                spinner.setVisibility(View.GONE);
                break;
            case money_mode:
                if (trip != null)
                    setTitle(trip.tripName + "-" + getString(R.string.cost));
                ft.show(viewCostFragment);
                navigationMenu.findItem(R.id.money).setChecked(true);
                fragmentLayout.setPadding(0, 0, 0, appBarLayoutDefaultHeight);
                spinner.setVisibility(View.GONE);
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
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        if (appBarLayout == null || coordinatorLayout == null) return;
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.onNestedFling(coordinatorLayout, appBarLayout, null, 0, -1000, true);
        }
    }

    class PrepareTripTask extends AsyncTask<Void, Object, Boolean> implements GpxAnalyzerJava.ProgressChangedListener, Trip.ConstructListener {

        LatLngBounds bounds;
        LatLng[] lat;
        ProgressBar progressBar;
        TextView progressMessage;
        static final int setPOI = 0;
        static final int setTime = 1;

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.loadingTripProgress);
            progressBar.setMax(1000);
            progressBar.setProgress(0);
            progressMessage = (TextView) findViewById(R.id.loadingTripProgressMessage);
            if (!libraryLoadSuccess) {
                Toast.makeText(getActivity(), getString(R.string.failed_to_loadlibrary), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            DocumentFile tripFile = null;
            switch (source) {
                case fromLocal:
                    tripFile = TripDiaryApplication.rootDocumentFile.findFile(tripName);
                    break;
                case fromWeb:
                    tripFile = DocumentFile.fromWeb(email, tripName, token);
                    break;
                case fromDrive:
                    tripFile = DocumentFile.fromDrive(googleApiClient, driveId);
                    break;
                case fromRestDrive:
                    tripFile = DocumentFile.fromRestDrive(service, resourceId, email);
                    break;
            }
            if (tripFile == null) {
                return false;
            }
            trip = new Trip(ViewTripActivity.this, tripFile, false, this);
            trip.listener = null;
            publishProgress(setPOI);
            publishProgress(500, analyzeGpx);
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
                    if (trip.dir instanceof WebDocumentFile && latsSize > 0) {
                        String timeZone = MyCalendar.getTimezoneFromLatLng(cache.latitudes[0], cache.longitudes[0]);
                        if (timeZone == null) timeZone = TimeZone.getDefault().getID();
                        trip.timezone = timeZone;
                        publishProgress(setPOI);
                    }
                    publishProgress(setTime);
                    lat = new LatLng[latsSize];
                    LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
                    for (int i = 0; i < latsSize; i++) {
                        LatLng latLng = new LatLng(cache.latitudes[i], cache.longitudes[i]);
                        lat[i] = latLng;
                        latLngBoundsBuilder.include(latLng);
                    }
                    bounds = latLngBoundsBuilder.build();
                }
            } catch (Exception e) {
                if (trip != null && trip.cacheFile != null) {
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
            int value0 = (int) values[0];
            if (values.length == 2) {
                progressBar.setProgress(value0);
                progressMessage.setText(values[1] + "\n" + String.valueOf(value0 / 10) + "%");
            } else if (value0 == setPOI) {
                if (trip != null && trip.pois != null) {
                    new LoadNavigationHeaderBackgroundTask().execute();
                    allTextFragment.refresh();
                    allPictureFragment.refresh();
                    allVideoFragment.refresh();
                    allAudioFragment.refresh();
                    viewCostFragment.refreshData(false);
                }
            } else if (value0 == setTime) {
                if (trip != null && trip.tripName != null) {
                    setTitle(trip.tripName);
                    String dateDuration = trip.getDateDurationString();
                    TextView navigationTripName = (TextView) findViewById(R.id.navigationTripName);
                    TextView navigationTripTime = (TextView) findViewById(R.id.navigationTripTime);
                    navigationTripName.setText(trip.tripName);
                    navigationTripTime.setText(dateDuration);
                    ActionBar actionBar = getSupportActionBar();
                    if (actionBar != null) {
                        actionBar.setSubtitle(dateDuration);
                    }
                }
            }
        }

        @Override
        protected void onPostExecute(Boolean success) {
            progressBar.setProgress(progressBar.getMax());
            drawerLayout.setStatusBarBackgroundColor(ContextCompat.getColor(ViewTripActivity.this, R.color.colorPrimaryDark));
            findViewById(R.id.fragment).setAlpha(1.0f);
            findViewById(R.id.appbar).setAlpha(1.0f);
            progressMessage.setVisibility(View.GONE);
            if (success) {
                viewMapFragment.initialMap(trip, lat, bounds, progressBar);
            } else {
                Toast.makeText(getActivity(), R.string.invalid_trip_file, Toast.LENGTH_SHORT).show();
                finishAndRemoveTask();
                progressBar.setProgress(0); //prevent progress drawable become 100% next time
                progressBar.setVisibility(View.GONE);
            }
        }

        String analyzeGpx = getString(R.string.analysis_gpx);

        @Override
        public void onPOICreated(int poi, int total, String poiName) {  //construct poi
            publishProgress(500 * (poi + 1) / total, poiName);
        }

        @Override
        public void onProgressChanged(long progress, long fileSize) { //analyze gpx
            publishProgress(500 + (int) (500 * progress / fileSize), analyzeGpx);
        }
    }

    class LoadNavigationHeaderBackgroundTask extends AsyncTask<Void, Void, Bitmap> {

        ProgressBar progressBar;

        @Override
        protected void onPreExecute() {
            progressBar = (ProgressBar) findViewById(R.id.loadbackgroudprocess);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
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
                    BitmapFactory.decodeStream(pic.getInputStream(), new Rect(0, 0, 0, 0), options);
                    float ratio = Math.min(options.outWidth / targetWidth, options.outHeight / targetHeight);
                    if (ratio < 1) ratio = 1;
                    options.inJustDecodeBounds = false;
                    options.inSampleSize = (int) ratio;
                    options.inPreferredConfig = Bitmap.Config.RGB_565;
                    return BitmapFactory.decodeStream(pic.getInputStream(), new Rect(0, 0, 0, 0), options);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }
            return null;
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

    public void deleteLocalCache() {
        if (trip == null) return;
        tryDeleteLocalCache(trip.cacheFile);
        tryDeleteLocalCache(trip.gpxFile);
        tryDeleteLocalCache(trip.noteFile);
        if (trip.pois == null) return;
        for (POI poi : trip.pois) {
            tryDeleteLocalCache(poi.diaryFile);
            tryDeleteLocalCache(poi.basicInformationFile);
            if (poi.picFiles != null) {
                for (DocumentFile file : poi.picFiles) {
                    tryDeleteLocalCache(file);
                }
            }
            if (poi.videoFiles != null) {
                for (DocumentFile file : poi.videoFiles) {
                    tryDeleteLocalCache(file);
                }
            }
            if (poi.audioFiles != null) {
                for (DocumentFile file : poi.audioFiles) {
                    tryDeleteLocalCache(file);
                }
            }
            if (poi.costFiles != null) {
                for (DocumentFile file : poi.costFiles) {
                    tryDeleteLocalCache(file);
                }
            }
        }
    }

    private static void tryDeleteLocalCache(DocumentFile file) {
        if (file != null) {
            if (file instanceof WebDocumentFile) {
                ((WebDocumentFile) file).deleteLocalCache();
            } else if (file instanceof RestDriveDocumentFile) {
                ((RestDriveDocumentFile) file).deleteLocalCache();
            }
        }
    }

    public static boolean libraryLoadSuccess = false;

    static {
        try {
            System.loadLibrary("TripDiary");
            libraryLoadSuccess = true;
        } catch (Exception | Error e) {
            e.printStackTrace();
            libraryLoadSuccess = false;
        }
    }

    @Override
    protected void onDestroy() {
        if (trip != null) {
            if (source == fromWeb || source == fromRestDrive) {
                deleteLocalCache();
            }
            ((TripDiaryApplication) getApplication()).removeTrip(trip.tripName);
            trip = null;
        }
        ImageView imageView = (ImageView) findViewById(R.id.navigationHeaderBackground);
        if (imageView != null) {
            imageView.setImageBitmap(null);
        }
        System.gc();
        super.onDestroy();
    }
}
