package com.yupog2003.tripdiary;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yupog2003.tripdiary.data.DrawableHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.services.RecordService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.TimeZone;

public class RecordActivity extends MyActivity implements OnClickListener, OnInfoWindowClickListener, OnMarkerDragListener, Runnable, TextView.OnEditorActionListener {

    String tripName;
    public static final String tag_tripname = "tripname";
    public static final String pref_tag_onaddpoi = "on_add_poi";
    public static final int REQUEST_PICK_PLACE = 3;
    TextView distance;
    TextView totalTime;
    TextView velocity;
    TextView accuracy;
    TextView altitude;
    EditText poiName;
    ImageButton select;
    Button addPOI;
    Button delete;
    Button takePicture;
    Button takeVideo;
    Button takeAudio;
    Button takeText;
    Button takePaint;
    Button takeMoney;
    Button save;
    FloatingActionButton refresh;
    ProgressBar refreshProgress;
    Handler handler;
    LinearLayout addMemoryLayout;
    LinearLayout informationLayout;
    SupportMapFragment mapFragment;
    GoogleMap gmap;
    Polyline polyline;
    boolean addPOIMode;
    MenuItem pauseItem;
    LatLng userChosenLatLng;

    SharedPreferences preference;
    DocumentFile nowFileForCameraIntent;
    boolean activityVisible;
    int updateDuration;
    String searchForSatellite, recording, pausing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (RecordService.instance == null) {
            finishAndRemoveTask();
            return;
        }
        setContentView(R.layout.activity_record);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        tripName = getIntent().getStringExtra(tag_tripname);
        if (tripName == null) {
            finishAndRemoveTask();
        }
        updateDuration = RecordService.instance.updateDuration;
        searchForSatellite = getString(R.string.searching_for_satellite);
        recording = getString(R.string.recording);
        pausing = getString(R.string.pausing);
        setTitle(tripName + " - " + recording);
        mapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.maplayout, mapFragment, "mapFragment").commit();
        preference = PreferenceManager.getDefaultSharedPreferences(this);
        handler = new Handler();
        addMemoryLayout = (LinearLayout) findViewById(R.id.addMemoryLayout);
        informationLayout = (LinearLayout) findViewById(R.id.informationLayout);
        distance = (TextView) findViewById(R.id.distance);
        totalTime = (TextView) findViewById(R.id.totaltime);
        velocity = (TextView) findViewById(R.id.velocity);
        accuracy = (TextView) findViewById(R.id.accuracy);
        altitude = (TextView) findViewById(R.id.altitude);
        poiName = (EditText) findViewById(R.id.poiName);
        poiName.setOnEditorActionListener(this);
        addPOI = (Button) findViewById(R.id.addpoi);
        select = (ImageButton) findViewById(R.id.select);
        refresh = (FloatingActionButton) findViewById(R.id.refresh);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);
        addPOI.setOnClickListener(this);
        select.setOnClickListener(this);
        refresh.setOnClickListener(this);
        takePicture = (Button) findViewById(R.id.takepicture);
        takePicture.setOnClickListener(this);
        takeVideo = (Button) findViewById(R.id.takevideo);
        takeVideo.setOnClickListener(this);
        takeAudio = (Button) findViewById(R.id.takeaudio);
        takeAudio.setOnClickListener(this);
        takeText = (Button) findViewById(R.id.taketext);
        takeText.setOnClickListener(this);
        takePaint = (Button) findViewById(R.id.takepaint);
        takePaint.setOnClickListener(this);
        takeMoney = (Button) findViewById(R.id.takemoney);
        takeMoney.setOnClickListener(this);
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        refreshProgress = (ProgressBar) findViewById(R.id.refreshProgress);
        int accentColor = ContextCompat.getColor(this, R.color.accent);
        DrawableCompat.setTint(DrawableCompat.wrap(takePicture.getCompoundDrawables()[1].mutate()), accentColor);
        DrawableCompat.setTint(DrawableCompat.wrap(takeVideo.getCompoundDrawables()[1].mutate()), accentColor);
        DrawableCompat.setTint(DrawableCompat.wrap(takeAudio.getCompoundDrawables()[1].mutate()), accentColor);
        DrawableCompat.setTint(DrawableCompat.wrap(takeText.getCompoundDrawables()[1].mutate()), accentColor);
        DrawableCompat.setTint(DrawableCompat.wrap(takePaint.getCompoundDrawables()[1].mutate()), accentColor);
        DrawableCompat.setTint(DrawableCompat.wrap(takeMoney.getCompoundDrawables()[1].mutate()), accentColor);
        setAddPOIMode(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (RecordService.instance == null) {
            finishAndRemoveTask();
            return;
        }
        activityVisible = true;
        handler.postDelayed(this, updateDuration);
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;
                gmap.setMyLocationEnabled(true);
                gmap.setOnMarkerDragListener(RecordActivity.this);
                gmap.setOnInfoWindowClickListener(addPOIMode ? null : RecordActivity.this);
                gmap.getUiSettings().setZoomControlsEnabled(true);
                new RefreshTask(true, true, false).execute();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityVisible = false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_record, menu);
        this.pauseItem = menu.findItem(R.id.pause);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.pause) {
            if (RecordService.instance != null) {
                if (RecordService.instance.run) {
                    item.setIcon(R.drawable.ic_play);
                    setTitle(tripName + " - " + pausing);
                } else {
                    item.setIcon(R.drawable.ic_pause);
                }
            }
            Intent i = new Intent(RecordService.actionPauseTrip);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                i.addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
            }
            sendBroadcast(i);
        } else if (item.getItemId() == R.id.stop) {
            Intent i = new Intent(RecordService.actionStopTrip);
            sendBroadcast(i);
            finishAndRemoveTask();
        }
        return true;
    }

    private void setAddPOIMode(boolean addPOIMode) {
        this.addPOIMode = addPOIMode;
        if (addPOIMode) {
            informationLayout.setVisibility(View.GONE);
            addMemoryLayout.setVisibility(View.VISIBLE);
            save.setVisibility(View.VISIBLE);
            delete.setVisibility(View.VISIBLE);
            addPOI.setVisibility(View.GONE);
            select.setVisibility(View.GONE);
            refresh.setVisibility(View.GONE);
            hideKeyboard();
            if (gmap != null) {
                gmap.setOnInfoWindowClickListener(null);
            }
        } else {
            informationLayout.setVisibility(View.VISIBLE);
            addMemoryLayout.setVisibility(View.GONE);
            save.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            addPOI.setVisibility(View.VISIBLE);
            select.setVisibility(View.VISIBLE);
            refresh.setVisibility(View.VISIBLE);
            hideKeyboard();
            poiName.setText("");
            if (gmap != null) {
                gmap.setOnInfoWindowClickListener(this);
            }
            if (POIMarker != null) {
                POIMarker.remove();
                POIMarker = null;
            }
        }
    }

    private void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    String fileName;
    POI poi;
    Marker POIMarker;
    ArrayList<Marker> markers = new ArrayList<>();
    private static final int REQUEST_PICTURE = 0;
    private static final int REQUEST_VIDEO = 1;
    private static final int REQUEST_AUDIO = 2;

    @Override
    public void onClick(View v) {
        MyCalendar fileTime = MyCalendar.getInstance();
        fileName = fileTime.format3339();
        fileName = fileName.substring(0, fileName.lastIndexOf("."));
        fileName = fileName.replace("-", "");
        fileName = fileName.replace("T", "");
        fileName = fileName.replace(":", "");
        if (v.equals(addPOI)) {
            String poiNameStr = poiName.getText().toString();
            if (poiNameStr.equals("")) {
                Toast.makeText(RecordActivity.this, R.string.please_input_poi_name, Toast.LENGTH_SHORT).show();
                return;
            }
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if (ContextCompat.checkSelfPermission(RecordActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(RecordActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            if (RecordService.instance == null || RecordService.instance.trip == null) {
                return;
            }
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location == null) {
                Toast.makeText(RecordActivity.this, getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
                return;
            }
            MyCalendar time = MyCalendar.getInstance(TimeZone.getTimeZone("UTC"));
            poi = RecordService.instance.trip.createPOI(poiNameStr, time, 0, 0, 0);
            if (poi == null) {
                Toast.makeText(this, R.string.storage_error, Toast.LENGTH_SHORT).show();
                return;
            }
            setAddPOIMode(true);
            if (poi.latitude == 0 && poi.longitude == 0 && poi.altitude == 0) { // new_poi
                final double latitude = userChosenLatLng == null ? location.getLatitude() : userChosenLatLng.latitude;
                final double longitude = userChosenLatLng == null ? location.getLongitude() : userChosenLatLng.longitude;
                final double altitude = location.getAltitude();
                LatLng latLng = new LatLng(latitude, longitude);
                POIMarker = gmap.addMarker(new MarkerOptions().position(latLng).title(poi.title).snippet(time.formatInCurrentTimezone()).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                poi.updateBasicInformation(null, time, latitude, longitude, altitude);
            } else { // edit exist poi
                if (markers != null) {
                    for (int i = 0; i < markers.size(); i++) {
                        if (markers.get(i).getTitle().equals(poi.title)) {
                            POIMarker = markers.get(i);
                            POIMarker.setDraggable(true);
                            POIMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
                            markers.remove(i);
                            break;
                        }
                    }
                }
            }
            if (POIMarker != null) {
                gmap.animateCamera(CameraUpdateFactory.newLatLng(POIMarker.getPosition()));
                POIMarker.showInfoWindow();
            }
            userChosenLatLng = null;
            preference.edit().putString(pref_tag_onaddpoi, poi.title).apply();
            updatePOIStatus();
        } else if (v.equals(refresh)) {
            new RefreshTask(true, false, true).execute();
        } else if (v.equals(takePicture)) {
            if (poi == null || poi.picDir == null) return;
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (i.resolveActivity(getPackageManager()) != null) {
                nowFileForCameraIntent = poi.picDir.createFile("", fileName + ".jpg");
                if (nowFileForCameraIntent == null) {
                    Toast.makeText(this, R.string.storage_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                i.putExtra(MediaStore.EXTRA_OUTPUT, nowFileForCameraIntent.getUri());
                i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                i.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(i, REQUEST_PICTURE);
            } else {
                Toast.makeText(this, R.string.camera_is_not_available, Toast.LENGTH_SHORT).show();
            }
        } else if (v.equals(takeVideo)) {
            if (poi == null || poi.videoDir == null) return;
            Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            if (i.resolveActivity(getPackageManager()) != null) {
                nowFileForCameraIntent = poi.videoDir.createFile("", fileName + ".3gp");
                if (nowFileForCameraIntent == null) {
                    Toast.makeText(this, R.string.storage_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                i.putExtra(MediaStore.EXTRA_OUTPUT, nowFileForCameraIntent.getUri());
                i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                i.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(i, REQUEST_VIDEO);
            } else {
                Toast.makeText(this, R.string.camera_is_not_available, Toast.LENGTH_SHORT).show();
            }
        } else if (v.equals(takeAudio)) {
            if (poi == null || poi.audioDir == null) return;
            Intent i = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
            if (i.resolveActivity(getPackageManager()) != null) {
                nowFileForCameraIntent = poi.audioDir.createFile("", fileName + ".mp3");
                if (nowFileForCameraIntent == null) {
                    Toast.makeText(this, R.string.storage_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivityForResult(i, REQUEST_AUDIO);
            } else {
                Toast.makeText(this, R.string.audio_recorder_is_not_available, Toast.LENGTH_SHORT).show();
            }
        } else if (v.equals(takeText)) {
            if (poi == null || poi.diary == null) return;
            final EditText getText = new EditText(this);
            getText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            getText.setText(poi.diary);
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle(getString(R.string.input_diary));
            ab.setView(getText);
            ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface arg0, int arg1) {

                    if (poi != null)
                        poi.updateDiary(getText.getText().toString());
                    updatePOIStatus();
                }
            });
            ab.setNegativeButton(getString(R.string.cancel), null);
            ab.show();
        } else if (v.equals(takePaint)) {
            if (poi == null || poi.picDir == null) return;
            nowFileForCameraIntent = poi.picDir.createFile("", fileName + ".png");
            if (nowFileForCameraIntent == null) {
                Toast.makeText(this, R.string.storage_error, Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, PaintActivity.class);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, nowFileForCameraIntent.getUri());
            intent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivityForResult(intent, REQUEST_PICTURE);
        } else if (v.equals(takeMoney)) {
            if (poi == null) return;
            AlertDialog.Builder ab = new AlertDialog.Builder(this);
            ab.setTitle(getString(R.string.cost));
            final LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.take_money, (ViewGroup) findViewById(android.R.id.content), false);
            ab.setView(layout);
            ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    EditText costName = (EditText) layout.findViewById(R.id.costname);
                    RadioGroup costType = (RadioGroup) layout.findViewById(R.id.costtype);
                    EditText costDollar = (EditText) layout.findViewById(R.id.costdollar);
                    String name = costName.getText().toString();
                    String dollar = costDollar.getText().toString();
                    if (!name.equals("") && !dollar.equals("")) {
                        int type;
                        if (costType.getCheckedRadioButtonId() == R.id.food) {
                            type = 0;
                        } else if (costType.getCheckedRadioButtonId() == R.id.lodging) {
                            type = 1;
                        } else if (costType.getCheckedRadioButtonId() == R.id.transportation) {
                            type = 2;
                        } else if (costType.getCheckedRadioButtonId() == R.id.other) {
                            type = 3;
                        } else if (costType.getCheckedRadioButtonId() == R.id.ticket) {
                            type = 4;
                        } else {
                            type = 0;
                        }
                        try {
                            poi.addCost(type, name, Float.parseFloat(dollar));
                            updatePOIStatus();
                        } catch (NumberFormatException e) {
                            Toast.makeText(RecordActivity.this, "Number format is not correct", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            });
            ab.setNegativeButton(getString(R.string.cancel), null);
            ab.show();
        } else if (v.equals(delete)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(RecordActivity.this);
            ab.setMessage(getString(R.string.are_you_sure_to_delete_this_poi));
            ab.setTitle(getString(R.string.be_careful));
            ab.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    if (poi != null && RecordService.instance != null && RecordService.instance.trip != null) {
                        RecordService.instance.trip.deletePOI(poi.title);
                    }
                    preference.edit().remove(pref_tag_onaddpoi).apply();
                    setAddPOIMode(false);
                }

            });
            ab.setNegativeButton(getString(R.string.no), null);
            AlertDialog ad = ab.create();
            ad.setIcon(DrawableHelper.getAlertDrawable(getActivity()));
            ad.show();
        } else if (v.equals(save)) {
            String newPOIName = poiName.getText().toString();
            if (poi != null && !newPOIName.equals("") && !poi.title.equals(newPOIName)) {
                poi.renamePOI(newPOIName);
            }
            Toast.makeText(RecordActivity.this, R.string.poi_has_been_saved, Toast.LENGTH_SHORT).show();
            setAddPOIMode(false);
            preference.edit().remove(pref_tag_onaddpoi).apply();
            new RefreshTask(false, false, false).execute();
        } else if (v.equals(select)) {
            try {
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                startActivityForResult(builder.build(this), REQUEST_PICK_PLACE);
            } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (addPOIMode) {
            save.performClick();
        } else {
            super.onBackPressed();
        }
    }

    private void updatePOIStatus() {
        if (addPOIMode && poi != null) {
            try {
                takePicture.setText(getString(R.string.take_a_picture) + "(" + poi.picDir.listFiles(DocumentFile.list_pics).length + ")");
                takeVideo.setText(getString(R.string.take_a_video) + "(" + poi.videoDir.listFiles(DocumentFile.list_videos).length + ")");
                takeAudio.setText(getString(R.string.take_a_audio) + "(" + poi.audioDir.listFiles(DocumentFile.list_audios).length + ")");
                takeText.setText(getString(R.string.write_text) + "(" + poi.diary.length() + ")");
                takeMoney.setText(getString(R.string.spend) + "(" + poi.costDir.listFiles().length + ")");
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICTURE || requestCode == REQUEST_VIDEO || requestCode == REQUEST_AUDIO) {
            if (nowFileForCameraIntent == null) return;
            if (resultCode == Activity.RESULT_OK) {
                if (nowFileForCameraIntent.length() > 0) {
                    updatePOIStatus();
                } else if (data != null && data.getData() != null) {
                    try {
                        Uri uri = data.getData();
                        InputStream is = getContentResolver().openInputStream(uri);
                        OutputStream os = nowFileForCameraIntent.getOutputStream();
                        FileHelper.copyByStream(is, os);
                        updatePOIStatus();
                    } catch (Exception e) {
                        e.printStackTrace();
                        nowFileForCameraIntent.delete();
                    }
                } else {
                    nowFileForCameraIntent.delete();
                }
            } else {
                nowFileForCameraIntent.delete();
            }
            nowFileForCameraIntent = null;
        } else if (requestCode == REQUEST_PICK_PLACE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Place place = PlacePicker.getPlace(data, this);
                if (!addPOIMode && place != null) {
                    userChosenLatLng = place.getLatLng();
                    poiName.setText(place.getName().toString());
                    addPOI.performClick();
                }
            }
        }
    }

    @Override
    public void run() {
        if (RecordService.instance != null && activityVisible) {
            try {
                if (RecordService.instance.run) {
                    final long totalTimeMilliSeconds = RecordService.instance.totalTime;
                    final double totalDistance = RecordService.instance.totalDistance;
                    final double nowSpeed = RecordService.instance.nowSpeed;
                    final float faccuracy = RecordService.instance.accuracy;
                    final double elevation = RecordService.instance.elevation;
                    setTitle(tripName + " - " + (faccuracy == -1 ? searchForSatellite : recording));
                    distance.setText(GpxAnalyzer2.getDistanceString((float) totalDistance / 1000, "km"));
                    totalTime.setText(String.valueOf(totalTimeMilliSeconds / 3600) + ":" + String.valueOf(totalTimeMilliSeconds % 3600 / 60) + ":" + String.valueOf(totalTimeMilliSeconds % 3600 % 60));
                    velocity.setText(GpxAnalyzer2.getDistanceString((float) nowSpeed * 18 / 5, "km/hr"));
                    accuracy.setText((faccuracy == -1 ? "∞" : GpxAnalyzer2.getAltitudeString(faccuracy, "m")));
                    altitude.setText(GpxAnalyzer2.getAltitudeString((float) elevation, "m"));
                    if (pauseItem != null) {
                        pauseItem.setIcon(R.drawable.ic_pause);
                    }
                } else {
                    if (pauseItem != null) {
                        pauseItem.setIcon(R.drawable.ic_play);
                    }
                    setTitle(tripName + " - " + pausing);
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            handler.postDelayed(this, updateDuration);
        } else if (RecordService.instance == null) {
            finishAndRemoveTask();
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (v.equals(poiName)) {
            if (actionId == EditorInfo.IME_ACTION_DONE && !addPOIMode) {
                addPOI.performClick();
            }
        }
        return false;
    }

    class RefreshTask extends AsyncTask<Void, Void, String> {
        ArrayList<LatLng> lat;
        POI[] pois;
        boolean refreshTrack;
        boolean animateCamera;
        boolean refreshPOI;

        public RefreshTask(boolean refreshTrack, boolean animateCamera, boolean refreshPOI) {
            this.refreshTrack = refreshTrack;
            this.animateCamera = animateCamera;
            this.refreshPOI = refreshPOI;
        }

        @Override
        protected void onPreExecute() {
            refresh.setVisibility(View.INVISIBLE);
            refreshProgress.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(Void... params) {
            if (RecordService.instance == null) return null;
            if (refreshTrack) {
                try {
                    lat = new ArrayList<>();
                    DocumentFile gpxFile = RecordService.instance.trip.gpxFile;
                    BufferedReader br = new BufferedReader(new InputStreamReader(gpxFile.getInputStream()));
                    String s;
                    while ((s = br.readLine()) != null) {
                        if (s.contains("<trkpt") && s.contains("lat") && s.contains("lon")) {
                            LatLng latlng;
                            String[] tokes = s.split("\"");
                            if (s.indexOf("lat") > s.indexOf("lon")) {
                                latlng = new LatLng(Double.parseDouble(tokes[3]), Double.parseDouble(tokes[1]));
                            } else {
                                latlng = new LatLng(Double.parseDouble(tokes[1]), Double.parseDouble(tokes[3]));
                            }
                            lat.add(latlng);
                        }
                    }
                    br.close();
                } catch (IOException | IndexOutOfBoundsException | IllegalArgumentException | NullPointerException e) {
                    e.printStackTrace();
                }
            }
            if (RecordService.instance == null || RecordService.instance.trip == null)
                return null;
            if (refreshPOI) {
                RecordService.instance.trip.refreshPOIs();
            }
            pois = RecordService.instance.trip.pois;
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (refreshTrack && lat != null && lat.size() > 0 && gmap != null) {
                if (polyline == null) {
                    int trackColor = PreferenceManager.getDefaultSharedPreferences(RecordActivity.this).getInt("trackcolor", 0xff6699cc);
                    polyline = gmap.addPolyline(new PolylineOptions().width(5).color(trackColor));
                }
                polyline.setPoints(lat);
            }
            if (animateCamera) {
                if (ContextCompat.checkSelfPermission(RecordActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(RecordActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (addPOIMode && POIMarker != null) {
                        gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(POIMarker.getPosition(), 16));
                    } else {
                        LatLng latlng = null;
                        LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
                        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                        if (location == null) {
                            location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        }
                        if (location != null) {
                            latlng = new LatLng(location.getLatitude(), location.getLongitude());
                        } else if (lat != null && lat.size() > 0) {
                            latlng = lat.get(lat.size() - 1);
                        }
                        if (latlng != null) {
                            gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
                        }
                    }
                }
            }
            if (markers != null) {
                for (int i = 0; i < markers.size(); i++) {
                    markers.get(i).remove();
                }
                markers.clear();
            } else {
                markers = new ArrayList<>();
            }
            if (pois != null) {
                for (POI poi : pois) {
                    if (addPOIMode && POIMarker != null && poi.title.equals(POIMarker.getTitle()))
                        continue;
                    markers.add(gmap.addMarker(new MarkerOptions().position(new LatLng(poi.latitude, poi.longitude)).title(poi.title).snippet(poi.time.formatInCurrentTimezone()).draggable(false)));
                }
            }
            if (!addPOIMode && preference.getString(pref_tag_onaddpoi, null) != null) {
                poiName.setText(preference.getString(pref_tag_onaddpoi, ""));
                addPOI.performClick();
            }
            refresh.setVisibility(View.VISIBLE);
            refreshProgress.setVisibility(View.GONE);
        }

    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        if (!addPOIMode) {
            poiName.setText(marker.getTitle());
            addPOI.performClick();
        }
    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        if (poi != null && marker != null && marker.getTitle() != null && marker.getTitle().equals(poi.title)) {
            poi.updateBasicInformation(null, null, marker.getPosition().latitude, marker.getPosition().longitude, null);
        }
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    protected void onDestroy() {
        if (gmap != null) {
            gmap.setMyLocationEnabled(false);
            gmap.clear();
        }
        super.onDestroy();
    }
}
