package com.yupog2003.tripdiary.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.CardView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yupog2003.tripdiary.MyActivity;
import com.yupog2003.tripdiary.PlayPointActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewGraphActivity;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.DrawableHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.MyLatLng2;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.PackageHelper;
import com.yupog2003.tripdiary.data.TrackCache;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.services.BackupRestoreTripService;
import com.yupog2003.tripdiary.services.GenerateVideoService;
import com.yupog2003.tripdiary.services.SendTripService;
import com.yupog2003.tripdiary.services.UploadToDriveService;
import com.yupog2003.tripdiary.views.POIInfoWindowAdapter;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;

public class ViewMapFragment extends Fragment implements OnInfoWindowClickListener, OnMapLongClickListener, OnMarkerDragListener, OnClickListener {
    SupportMapFragment mapFragment;
    GoogleMap gmap;
    ArrayList<Marker> markers;
    TrackCache cache;
    ImageButton viewInformation;
    ImageButton switchMapMode;
    ImageButton playTrip;
    ImageButton stopTrip;
    ImageButton fastForward;
    ImageButton slowForward;
    ImageButton viewGraph;
    ImageButton streetView;
    ImageButton viewNote;
    TripPlayer tripPlayer;
    Handler handler;
    POIInfoWindowAdapter poiInfoWindowAdapter;
    Trip trip;

    private static final int REQUEST_BACKGROUND_MUSIC = 3;
    // private static final int playtriptype_normal=0;
    private static final int playtriptype_skyview = 1;

    ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_view_map, container, false);
        markers = new ArrayList<>();
        handler = new Handler();
        viewInformation = (ImageButton) rootView.findViewById(R.id.viewinformation);
        switchMapMode = (ImageButton) rootView.findViewById(R.id.switchmapmode);
        playTrip = (ImageButton) rootView.findViewById(R.id.playtrip);
        stopTrip = (ImageButton) rootView.findViewById(R.id.stoptrip);
        fastForward = (ImageButton) rootView.findViewById(R.id.fastforward);
        slowForward = (ImageButton) rootView.findViewById(R.id.slowforward);
        viewGraph = (ImageButton) rootView.findViewById(R.id.viewgraph);
        streetView = (ImageButton) rootView.findViewById(R.id.streetview);
        viewNote = (ImageButton) rootView.findViewById(R.id.viewnote);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapFragment = SupportMapFragment.newInstance();
        getChildFragmentManager().beginTransaction().add(R.id.maplayout, mapFragment).commit();

    }

    @Override
    public void onResume() {
        super.onResume();
        if (tripPlayer != null) {
            tripPlayer.resume();
        }
    }

    public void initialMap(final Trip trip, final LatLng[] lats, final LatLngBounds bounds, final ProgressBar progressBar) {
        if (trip == null) return;
        this.trip = trip;
        viewNote.setOnClickListener(ViewMapFragment.this);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                gmap = googleMap;
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    gmap.setMyLocationEnabled(true);
                }
                gmap.getUiSettings().setZoomControlsEnabled(true);
                switchMapMode.setOnClickListener(ViewMapFragment.this);
                streetView.setOnClickListener(ViewMapFragment.this);
                cache = trip.cache;
                if (lats != null && cache != null && bounds != null) {
                    int trackColor = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("trackcolor", 0xff6699cc);
                    gmap.addPolyline(new PolylineOptions().add(lats).width(5).color(trackColor));
                    gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, (int) DeviceHelper.pxFromDp(getActivity(), 10)));
                    viewInformation.setOnClickListener(ViewMapFragment.this);
                    playTrip.setOnClickListener(ViewMapFragment.this);
                    stopTrip.setOnClickListener(ViewMapFragment.this);
                    fastForward.setOnClickListener(ViewMapFragment.this);
                    slowForward.setOnClickListener(ViewMapFragment.this);
                    viewGraph.setOnClickListener(ViewMapFragment.this);
                } else {
                    Toast.makeText(getActivity(), getString(R.string.invalid_gpx_file), Toast.LENGTH_SHORT).show();
                }
                if (trip.pois != null) {
                    setPOIs();
                    gmap.setOnInfoWindowClickListener(ViewMapFragment.this);
                    gmap.setOnMapLongClickListener(ViewMapFragment.this);
                    gmap.setOnMarkerDragListener(ViewMapFragment.this);
                    poiInfoWindowAdapter = new POIInfoWindowAdapter(getActivity(), trip.pois, null);
                    gmap.setInfoWindowAdapter(poiInfoWindowAdapter);
                }
                if (progressBar != null) {
                    progressBar.setProgress(0); //prevent progress drawable become 100% next time
                    progressBar.setVisibility(View.GONE);
                }
            }
        });


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_view_map, menu);
        MenuItem searchItem = menu.findItem(R.id.searchviewmap);
        final SearchView search = (SearchView) MenuItemCompat.getActionView(searchItem);
        search.setQueryHint(getString(R.string.search_poi));
        search.setOnQueryTextListener(new OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                search.clearFocus();
                final String searchName = search.getQuery().toString();
                if (!searchName.equals("")) {
                    final ArrayList<Marker> founds = new ArrayList<>();
                    final int markersSize = markers.size();
                    for (int i = 0; i < markersSize; i++) {
                        if (markers.get(i).getTitle().contains(searchName)) {
                            founds.add(markers.get(i));
                        }
                    }
                    if (founds.size() == 0) {
                        Toast.makeText(getActivity(), getString(R.string.poi_not_found), Toast.LENGTH_SHORT).show();
                    } else if (founds.size() == 1) {
                        gmap.animateCamera(CameraUpdateFactory.newLatLng(founds.get(0).getPosition()));
                        founds.get(0).showInfoWindow();
                    } else {
                        AlertDialog.Builder choose = new AlertDialog.Builder(getActivity());
                        choose.setTitle(getString(R.string.choose_the_poi));
                        String[] foundsName = new String[founds.size()];
                        for (int j = 0; j < founds.size(); j++) {
                            foundsName[j] = founds.get(j).getTitle();
                        }
                        choose.setSingleChoiceItems(foundsName, -1, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                gmap.animateCamera(CameraUpdateFactory.newLatLng(founds.get(which).getPosition()));
                                founds.get(which).showInfoWindow();
                                dialog.dismiss();
                            }
                        });
                        choose.show();
                    }
                }
                return false;
            }

            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        }
        if (trip == null) return true;
        if (item.getItemId() == R.id.clearcache) {
            trip.deleteCache();
            Toast.makeText(getActivity(), getString(R.string.cache_has_been_cleared), Toast.LENGTH_SHORT).show();
            DeviceHelper.sendGATrack(getActivity(), "Trip", "clear_cache", trip.tripName, null);
        } else if (item.getItemId() == R.id.sharetrackby) {
            if (trip.cache == null) return true;
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.share_track_by___));
            String[] bys = new String[]{getString(R.string.gpx), getString(R.string.kml) + "(Google Earth)", getString(R.string.kmz_with_explanation)};
            ab.setSingleChoiceItems(bys, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, final int which) {
                    ((MyActivity) getActivity()).pickDir(getString(R.string.select_output_directory), new MyActivity.OnDirPickedListener() {
                        @Override
                        public void onDirPicked(File dir) {
                            switch (which) {
                                case 0:
                                    DocumentFile gpxFile = DocumentFile.fromFile(new File(dir, trip.tripName + ".gpx"));
                                    new GenerateKMxTask(gpxFile, GenerateKMxTask.gpx).execute();
                                    break;
                                case 1:
                                    DocumentFile kmlFile = DocumentFile.fromFile(new File(dir, trip.tripName + ".kml"));
                                    new GenerateKMxTask(kmlFile, GenerateKMxTask.kml).execute();
                                    break;
                                case 2:
                                    DocumentFile kmzFile = DocumentFile.fromFile(new File(dir, trip.tripName + ".kmz"));
                                    new GenerateKMxTask(kmzFile, GenerateKMxTask.kmz).execute();
                                    break;
                            }
                        }
                    });
                    dialog.dismiss();
                }
            });
            ab.show();
        } else if (item.getItemId() == R.id.importmemory) {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.import_memory));
            ab.setMessage(getString(R.string.import_memory_explanation));
            ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    ((MyActivity) getActivity()).pickDir(getString(R.string.select_a_directory), new MyActivity.OnDirPickedListener() {
                        @Override
                        public void onDirPicked(File dir) {
                            DeviceHelper.sendGATrack(getActivity(), "Trip", "import_memory", trip.tripName, null);
                            importMemoryTask = new ImportMemoryTask(dir);
                            importMemoryTask.execute();
                        }
                    });
                }
            });
            ab.setNegativeButton(getString(R.string.cancel), null);
            ab.show();
        } else if (item.getItemId() == R.id.generateVideo) {
            if (cache == null) return true;
            if (DeviceHelper.isMobileNetworkAvailable(getActivity())) {
                generateVideo();
            } else {
                Toast.makeText(getActivity(), R.string.please_connect_to_internet_for_downloading_google_map_background, Toast.LENGTH_SHORT).show();
            }
        } else if (item.getItemId() == R.id.backup) {
            if (trip.tripName == null) return true;
            ((MyActivity) getActivity()).pickDir(getString(R.string.select_output_directory), new MyActivity.OnDirPickedListener() {
                @Override
                public void onDirPicked(File dir) {
                    ArrayList<String> tripNames = new ArrayList<>();
                    tripNames.add(trip.tripName);
                    Intent intent = new Intent(getActivity(), BackupRestoreTripService.class);
                    intent.setAction(BackupRestoreTripService.ACTION_BACKUP);
                    intent.putStringArrayListExtra(BackupRestoreTripService.tag_tripnames, tripNames);
                    intent.putExtra(BackupRestoreTripService.tag_directory, dir);
                    getActivity().startService(intent);
                }
            });
        } else if (item.getItemId() == R.id.upload) {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            String[] options = new String[]{getString(R.string.upload_to_tripdiary), getString(R.string.upload_to_google_drive)};
            ab.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, final int which) {
                    ((MyActivity) getActivity()).getAccount(new MyActivity.OnAccountPickedListener() {
                        @Override
                        public void onAccountPicked(@NonNull final String account) {
                            switch (which) {
                                case 0:
                                    askUploadPublic(account);
                                    break;
                                case 1:
                                    ((MyActivity) getActivity()).connectToDriveApi(new GoogleApiClient.ConnectionCallbacks() {
                                        @Override
                                        public void onConnected(Bundle bundle) {
                                            ArrayList<String> tripNames = new ArrayList<>();
                                            tripNames.add(trip.tripName);
                                            Intent intent = new Intent(getActivity(), UploadToDriveService.class);
                                            intent.putExtra(UploadToDriveService.tag_tripNames, tripNames);
                                            intent.putExtra(UploadToDriveService.tag_account, account);
                                            getActivity().startService(intent);
                                        }

                                        @Override
                                        public void onConnectionSuspended(int i) {

                                        }
                                    });
                                    break;
                            }
                        }
                    }, false);
                    dialog.dismiss();
                }
            });
            ab.show();
        }
        return true;
    }

    private void askUploadPublic(final String account) {
        AlertDialog.Builder ab3 = new AlertDialog.Builder(getActivity());
        ab3.setTitle(getString(R.string.make_it_public));
        ab3.setMessage(getString(R.string.make_it_public_to_share));
        ab3.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                shareTrackByTripDiary(account, true);
            }
        });
        ab3.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                shareTrackByTripDiary(account, false);
            }
        });
        ab3.show();
    }

    private void shareTrackByTripDiary(final String account, final boolean uploadPublic) {
        if (getActivity() == null || account == null) return;
        ((MyActivity) getActivity()).getAccessToken(account, new MyActivity.OnAccessTokenGotListener() {
            @Override
            public void onAccessTokenGot(@NonNull String token) {
                Intent intent = new Intent(getActivity(), SendTripService.class);
                intent.putExtra(SendTripService.tripNameTag, trip.tripName);
                intent.putExtra(SendTripService.accountTag, account);
                intent.putExtra(SendTripService.tokenTag, token);
                intent.putExtra(SendTripService.publicTag, uploadPublic);
                getActivity().startService(intent);
                DeviceHelper.sendGATrack(getActivity(), "Trip", "share_track_by_tripdiary", trip.tripName, null);
            }
        });
    }

    ImportMemoryTask importMemoryTask;

    class ImportMemoryTask extends AsyncTask<Void, String, String> {

        File memoryDir;
        TextView message;
        ProgressBar progress;
        TextView progressMessage;
        AlertDialog dialog;
        boolean cancel = false;
        HashMap<Long, File> memories;
        long start, end;

        public ImportMemoryTask(File memoryDir) {
            this.memoryDir = memoryDir;
        }

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.importing));
            LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.progressdialog_import_memory, rootView, false);
            message = (TextView) layout.findViewById(R.id.message);
            progress = (ProgressBar) layout.findViewById(R.id.progressBar);
            progressMessage = (TextView) layout.findViewById(R.id.progress);
            ab.setView(layout);
            ab.setCancelable(false);
            ab.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    cancel = true;
                }
            });
            dialog = ab.create();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            publishProgress("Sorting...", "0");
            if (trip.pois.length < 1 || cache.times == null || memoryDir == null)
                return null;
            //prepare memories
            start = 0;
            end = Long.MAX_VALUE;
            if (cache.times.length > 0) {
                start = MyCalendar.getTime(trip.timezone, cache.times[0], MyCalendar.type_self).getTimeInMillis();
                end = MyCalendar.getTime(trip.timezone, cache.times[cache.times.length - 1], MyCalendar.type_self).getTimeInMillis();
            }
            memories = new HashMap<>();
            getMemories(memoryDir);
            if (cancel) return null;
            Set<Long> set = memories.keySet();
            Long[] memTimes = set.toArray(new Long[set.size()]);
            publishProgress("setMax", String.valueOf(memTimes.length));
            Arrays.sort(memTimes);
            //prepare pois
            Arrays.sort(trip.pois);
            Long[] poiTimes = new Long[trip.pois.length];
            for (int i = 0; i < poiTimes.length; i++) {
                if (cancel) return null;
                MyCalendar time = trip.pois[i].time;
                time.setTimeZone(trip.timezone);
                poiTimes[i] = time.getTimeInMillis();
            }
            Arrays.sort(poiTimes);
            int habitOfAddingPOI = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("habitofaddingpoi", "1"));
            Long[] poiTimeSection = new Long[poiTimes.length + 1];
            switch (habitOfAddingPOI) {
                case 0: //before
                    System.arraycopy(poiTimes, 0, poiTimeSection, 0, poiTimes.length);
                    break;
                case 1: //while
                    for (int i = 1; i < poiTimeSection.length - 1; i++) {
                        poiTimeSection[i] = (poiTimes[i - 1] + poiTimes[i]) / 2;
                    }
                    break;
                case 2: //after
                    System.arraycopy(poiTimes, 0, poiTimeSection, 1, poiTimes.length);
                    break;
            }
            poiTimeSection[0] = start;
            poiTimeSection[poiTimeSection.length - 1] = end;
            int nowPOITimeSection = 0;
            for (int memIndex = 0; memIndex < memTimes.length; memIndex++) {
                if (cancel) return null;
                long memoryTime = memTimes[memIndex];
                File inFile = memories.get(memoryTime);
                publishProgress(inFile.getName(), String.valueOf(memIndex));
                if (!FileHelper.isMemory(inFile)) continue;
                boolean coppied = false;
                for (; nowPOITimeSection < poiTimeSection.length - 1; nowPOITimeSection++) {
                    if (cancel) return null;
                    if (memoryTime <= poiTimeSection[nowPOITimeSection + 1]) {
                        trip.pois[nowPOITimeSection].importMemories(null, inFile);
                        coppied = true;
                        break;
                    }
                }
                if (!coppied) {
                    trip.pois[nowPOITimeSection].importMemories(null, inFile);
                }
            }
            return null;
        }

        private void getMemories(File file) {
            if (cancel) return;
            if (file == null) return;
            if (file.isDirectory()) {
                File[] children = file.listFiles();
                if (children == null) return;
                for (File child : children) {
                    getMemories(child);
                }
            } else if (FileHelper.isMemory(file)) {
                long timeMills = getFileTime(file);
                if (timeMills >= start && timeMills <= end) {
                    memories.put(timeMills, file);
                }
            }
        }

        private long getFileTime(File file) {
            String mime = FileHelper.getMimeFromFile(file);
            long timeMills;
            if (mime.equals("image/jpeg")) {
                try {
                    ExifInterface exif = new ExifInterface(file.getPath());
                    String datetime = exif.getAttribute(ExifInterface.TAG_DATETIME);
                    if (datetime != null) {
                        MyCalendar time1 = MyCalendar.getTime(trip.timezone, datetime, MyCalendar.type_exif);
                        return time1.getTimeInMillis();
                    } else {
                        timeMills = file.lastModified();
                    }
                } catch (IOException e) {
                    timeMills = file.lastModified();
                    e.printStackTrace();
                }
            } else {
                timeMills = file.lastModified();
            }
            return timeMills;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            if (values[0].equals("setMax")) {
                progress.setMax(Integer.valueOf(values[1]));
                progressMessage.setText("0/" + values[1]);
            } else {
                message.setText(values[0]);
                progress.setProgress(Integer.valueOf(values[1]));
                progressMessage.setText(values[1] + "/" + String.valueOf(progress.getMax()));
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (!isAdded() || getActivity() == null)
                return;
            dialog.dismiss();
            if (getActivity() != null && getActivity() instanceof ViewTripActivity) {
                ((ViewTripActivity) getActivity()).onPOIUpdate(null);
            }
            importMemoryTask = null;
        }
    }

    public void setPOIs() {
        if (markers == null || gmap == null || trip == null || trip.pois == null || !isAdded())
            return;
        for (int i = 0; i < markers.size(); i++) {
            markers.get(i).remove();
        }
        markers.clear();
        float hue = DrawableHelper.getMarkerColorHue(getActivity());
        BitmapDescriptor bd = BitmapDescriptorFactory.defaultMarker(hue);
        for (POI poi : trip.pois) {
            String poiTime = poi.time.formatInTimezone(trip.timezone);
            markers.add(gmap.addMarker(new MarkerOptions()
                            .position(new LatLng(poi.latitude, poi.longitude))
                            .title(poi.title)
                            .snippet(" " + poiTime + "\n " + poi.diary)
                            .draggable(true)
                            .icon(bd)
            ));
        }
        if (poiInfoWindowAdapter != null) {
            poiInfoWindowAdapter.setPOIs(trip.pois);
        }
        if (getActivity() == null) return;
        ArrayList<String> markerNames = new ArrayList<>();
        if (markers.size() == 0) {
            markerNames.add(getString(R.string.no_poi));
        } else {
            markerNames.add(getString(R.string.select_poi));
        }
        for (int i = 0; i < markers.size(); i++) {
            markerNames.add(markers.get(i).getTitle());
        }
        ViewTripActivity viewTripActivity = (ViewTripActivity) getActivity();
        AppCompatSpinner spinner = viewTripActivity.spinner;
        spinner.setEnabled(true);
        ActionBar actionBar = viewTripActivity.getSupportActionBar();
        if (actionBar == null) return;
        ArrayAdapter adapter = new ArrayAdapter<>(actionBar.getThemedContext(), R.layout.support_simple_spinner_dropdown_item, markerNames);
        adapter.setDropDownViewResource(R.layout.viewmap_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        if (adapter.getCount() > 1) {
            spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if (position > 0 && position - 1 < markers.size()) {
                        Marker marker = markers.get(position - 1);
                        gmap.animateCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
                        marker.showInfoWindow();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        } else {
            spinner.setEnabled(false);
        }
        setHasOptionsMenu(true);
    }

    public void onInfoWindowClick(Marker marker) {
        viewPOI(marker.getTitle());
    }

    private void viewPOI(String poiName) {
        Intent intent = new Intent(getActivity(), ViewPointActivity.class);
        intent.putExtra(ViewPointActivity.tag_tripname, trip.tripName);
        intent.putExtra(ViewPointActivity.tag_poiname, poiName);
        intent.putExtra(ViewPointActivity.tag_fromActivity, ViewTripActivity.class.getSimpleName());
        getActivity().startActivityForResult(intent, ViewTripActivity.REQUEST_VIEW_POI);
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            switch (requestCode) {
                case REQUEST_BACKGROUND_MUSIC:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri uri1 = data.getData();
                        if (uri1 != null) {
                            File resultFile = FileHelper.copyFromUriToFile(getActivity(), uri1, getActivity().getCacheDir(), null);
                            if (resultFile != null) {
                                this.videoBackgroundMusic = resultFile.getPath();
                                if (backgroundMusicButton != null) {
                                    this.backgroundMusicButton.setText(resultFile.getName());
                                }
                            }
                        }
                    }
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void onMapLongClick(final LatLng latlng) {
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle(getString(R.string.add_poi));
        View layout = getActivity().getLayoutInflater().inflate(R.layout.edit_poi, rootView, false);
        ab.setView(layout);
        final MyLatLng2 position = getLatLngInTrack(latlng);
        final EditText edittitle = (EditText) layout.findViewById(R.id.edit_poi_title);
        final EditText editlatitude = (EditText) layout.findViewById(R.id.edit_poi_latitude);
        editlatitude.setText(String.valueOf(position.latitude));
        final EditText editlongitude = (EditText) layout.findViewById(R.id.edit_poi_longitude);
        editlongitude.setText(String.valueOf(position.longitude));
        final EditText editaltitude = (EditText) layout.findViewById(R.id.edit_poi_altitude);
        editaltitude.setText(String.valueOf(position.altitude));
        final DatePicker editdate = (DatePicker) layout.findViewById(R.id.edit_poi_date);
        MyCalendar time = MyCalendar.getTime(position.time, MyCalendar.type_self);
        editdate.updateDate(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH));
        final TimePicker edittime = (TimePicker) layout.findViewById(R.id.edit_poi_time);
        edittime.setIs24HourView(true);
        edittime.setCurrentHour(time.get(Calendar.HOUR_OF_DAY));
        edittime.setCurrentMinute(time.get(Calendar.MINUTE));
        final int defaultSeconds = time.get(Calendar.SECOND);
        ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                String title = edittitle.getText().toString();
                String latStr = editlatitude.getText().toString();
                String lngStr = editlongitude.getText().toString();
                String altStr = editaltitude.getText().toString();
                double lat = latStr.equals("") ? position.latitude : Double.parseDouble(latStr);
                double lng = lngStr.equals("") ? position.longitude : Double.parseDouble(lngStr);
                double altitude = altStr.equals("") ? position.altitude : Double.parseDouble(altStr);
                LatLng location = new LatLng(lat, lng);
                MyCalendar time = MyCalendar.getInstance(TimeZone.getTimeZone(trip.timezone));
                time.set(editdate.getYear(), editdate.getMonth(), editdate.getDayOfMonth(), edittime.getCurrentHour(), edittime.getCurrentMinute(), defaultSeconds);
                time.format3339();
                time.setTimeZone("UTC");
                if (title.equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.input_the_poi_title), Toast.LENGTH_SHORT).show();
                } else {
                    if (FileHelper.findfile(trip.dir, title) != null) {
                        Toast.makeText(getActivity(), getString(R.string.there_is_a_same_poi), Toast.LENGTH_SHORT).show();
                    } else {
                        addPoint(title, location, altitude, time);
                    }
                }
            }
        });
        ab.setNegativeButton(getString(R.string.cancel), null);
        ab.show();
    }

    private void addPoint(String poiName, LatLng latlng, double altitude, MyCalendar time) {
        if (getActivity() == null || trip == null) return;
        POI poi = trip.createPOI(poiName, time, latlng.latitude, latlng.longitude, altitude);
        if (poi != null) {
            ((ViewTripActivity) getActivity()).onPOIUpdate(null);
        }
    }

    private MyLatLng2 getLatLngInTrack(LatLng latlng) {
        MyLatLng2 result = new MyLatLng2(latlng.latitude, latlng.longitude, 0, MyCalendar.getInstance().formatInTimezone(trip.timezone));
        if (cache == null)
            return result;
        if (cache.latitudes == null || cache.longitudes == null)
            return result;
        double minDistance = Double.MAX_VALUE;
        int resultIndex = 0;
        int latLength = Math.min(cache.latitudes.length, cache.longitudes.length);
        double distance;
        for (int i = 0; i < latLength; i++) {
            distance = Math.pow((latlng.latitude - cache.latitudes[i]), 2) + Math.pow(latlng.longitude - cache.longitudes[i], 2);
            if (distance < minDistance) {
                minDistance = distance;
                resultIndex = i;
            }
        }
        if (resultIndex < cache.latitudes.length && resultIndex < cache.longitudes.length && resultIndex < cache.altitudes.length && resultIndex < cache.times.length)
            result = new MyLatLng2(cache.latitudes[resultIndex], cache.longitudes[resultIndex], cache.altitudes[resultIndex], cache.times[resultIndex]);
        return result;
    }

    public void onMarkerDrag(Marker marker) {

    }

    public void onMarkerDragEnd(Marker marker) {
        if (trip == null) return;
        POI poi = trip.getPOI(marker.getTitle());
        if (poi == null) return;
        poi.updateBasicInformation(null, null, marker.getPosition().latitude, marker.getPosition().longitude, null, null);
        if (getActivity() != null && getActivity() instanceof ViewTripActivity) {
            ((ViewTripActivity) getActivity()).onPOIUpdate(poi.title);
        }
    }

    public void onMarkerDragStart(Marker marker) {


    }

    public void onClick(View v) {
        if (v == null || trip == null)
            return;
        if (v.equals(viewInformation)) {
            if (cache == null) {
                Toast.makeText(getActivity(), getString(R.string.no_data_can_be_displayed), Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.statistics));
            View layout = getActivity().getLayoutInflater().inflate(R.layout.map_view_basicinformation, rootView, false);
            TextView starttime = (TextView) layout.findViewById(R.id.starttime);
            starttime.setText(cache.startTime);
            TextView stoptime = (TextView) layout.findViewById(R.id.stoptime);
            stoptime.setText(cache.endTime);
            TextView totaltime = (TextView) layout.findViewById(R.id.totaltime);
            totaltime.setText(":" + cache.totalTime);
            TextView distance = (TextView) layout.findViewById(R.id.distance);
            distance.setText(":" + GpxAnalyzer2.getDistanceString(cache.distance / 1000, "km"));
            TextView avgspeed = (TextView) layout.findViewById(R.id.avgspeed);
            avgspeed.setText(":" + GpxAnalyzer2.getDistanceString(cache.avgSpeed, "km/hr"));
            TextView maxspeed = (TextView) layout.findViewById(R.id.maxspeed);
            maxspeed.setText(":" + GpxAnalyzer2.getDistanceString(cache.maxSpeed, "km/hr"));
            TextView totalclimb = (TextView) layout.findViewById(R.id.totalclimb);
            totalclimb.setText(":" + GpxAnalyzer2.getAltitudeString(cache.climb, "m"));
            TextView maxaltitude = (TextView) layout.findViewById(R.id.maxaltitude);
            maxaltitude.setText(":" + GpxAnalyzer2.getAltitudeString(cache.maxAltitude, "m"));
            TextView minaltitude = (TextView) layout.findViewById(R.id.minaltitude);
            minaltitude.setText(":" + GpxAnalyzer2.getAltitudeString(cache.minAltitude, "m"));
            ab.setView(layout);
            ab.setPositiveButton(getString(R.string.enter), null);
            ab.show();
            DeviceHelper.sendGATrack(getActivity(), "Trip", "view_basicinformation", trip.tripName, null);
        } else if (v.equals(switchMapMode)) {
            if (gmap == null)
                return;
            PopupMenu popupMenu = new PopupMenu(getActivity(), switchMapMode);
            popupMenu.inflate(R.menu.map_type);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.normal:
                            gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            break;
                        case R.id.satellite:
                            gmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                            break;
                        case R.id.hybrid:
                            gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;
                        case R.id.terrain:
                            gmap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            break;
                    }
                    return true;
                }
            });
            popupMenu.show();
        } else if (v.equals(playTrip)) {
            if (tripPlayer == null) {
                tripPlayer = new TripPlayer();
                DeviceHelper.sendGATrack(getActivity(), "Trip", "play", trip.tripName, null);
            }
            tripPlayer.play();
        } else if (v.equals(stopTrip)) {
            if (tripPlayer != null) {
                tripPlayer.stop();
            }
        } else if (v.equals(fastForward)) {
            if (tripPlayer != null) {
                tripPlayer.changeForward(TripPlayer.fast);
            }
        } else if (v.equals(slowForward)) {
            if (tripPlayer != null) {
                tripPlayer.changeForward(TripPlayer.slow);
            }
        } else if (v.equals(viewGraph)) {
            Intent intent = new Intent(getActivity(), ViewGraphActivity.class);
            intent.putExtra(ViewGraphActivity.tag_tripname, trip.tripName);
            startActivity(intent);
        } else if (v.equals(streetView)) {
            if (PackageHelper.isAppInstalled(getActivity(), PackageHelper.StreetViewPackageNmae)) {
                final RelativeLayout mapLayout = (RelativeLayout) rootView.findViewById(R.id.maplayout);
                final ImageButton streetMan = new ImageButton(getActivity());
                streetMan.setImageResource(R.drawable.ic_streetman);
                streetMan.setBackgroundColor(Color.TRANSPARENT);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                mapLayout.addView(streetMan, params);
                streetMan.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {

                        mapLayout.removeView(streetMan);
                        mapFragment.getMapAsync(new OnMapReadyCallback() {
                            @Override
                            public void onMapReady(GoogleMap googleMap) {
                                LatLng latlng = googleMap.getCameraPosition().target;
                                Uri uri = Uri.parse("google.streetview:cbll=" + String.valueOf(latlng.latitude) + "," + String.valueOf(latlng.longitude));
                                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                                if (intent2.resolveActivity(getActivity().getPackageManager()) != null) {
                                    getActivity().startActivity(intent2);
                                } else {
                                    Toast.makeText(getActivity(), getString(R.string.street_view_is_not_available), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    }
                });
                Toast.makeText(getActivity(), getString(R.string.explain_how_to_use_street_view), Toast.LENGTH_LONG).show();
            } else {
                PackageHelper.askForInstallApp(getActivity(), PackageHelper.StreetViewPackageNmae, getString(R.string.street_view));
            }
        } else if (v.equals(viewNote)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.Note));
            final String noteStr = trip.note;
            TextView textView = new TextView(getActivity());
            textView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Medium);
            textView.setText(noteStr);
            int dip10 = (int) DeviceHelper.pxFromDp(getActivity(), 10);
            textView.setPadding(dip10, dip10, dip10, dip10);
            ab.setView(textView);
            ab.setPositiveButton(getString(R.string.enter), null);
            ab.setNegativeButton(getString(R.string.edit), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    dialog.dismiss();
                    AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity());
                    ab2.setTitle(getString(R.string.edit) + " " + getString(R.string.Note));
                    final EditText editText = new EditText(getActivity());
                    editText.setText(noteStr);
                    ab2.setView(editText);
                    ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            trip.updateNote(editText.getText().toString());
                        }
                    });
                    ab2.setNegativeButton(getString(R.string.cancel), null);
                    ab2.show();
                }
            });
            ab.show();
            DeviceHelper.sendGATrack(getActivity(), "Trip", "view_note", trip.tripName, null);
        }
    }

    class TripPlayer implements Runnable, OnSeekBarChangeListener {
        int index;
        int playMode;
        boolean run;
        MediaPlayer mp;
        Marker playPoint;
        ImageView runPointImage;
        float bearing;
        RelativeLayout mapLayout;
        int latlength;
        int i, add = 1;
        int interval;
        public static final int fast = 0;
        public static final int slow = 1;
        SparseArray<POI> markersMap;
        float[] distances;
        LatLng[] lat;
        CardView playPanel;
        TextView altitude;
        TextView distance;
        TextView speed;
        TextView time;
        SeekBar processSeekBar;

        public TripPlayer() {
            index = 0;
            playMode = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("playingtripmode", "1"));
            interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("playtripspeed", "10"));
            latlength = 0;
            processSeekBar = (SeekBar) rootView.findViewById(R.id.playProcess);
            processSeekBar.setOnSeekBarChangeListener(this);
            playPanel = (CardView) rootView.findViewById(R.id.playPanel);
            altitude = (TextView) rootView.findViewById(R.id.altitude);
            distance = (TextView) rootView.findViewById(R.id.distance);
            speed = (TextView) rootView.findViewById(R.id.speed);
            time = (TextView) rootView.findViewById(R.id.elapsedTime);
            mapLayout = (RelativeLayout) rootView.findViewById(R.id.maplayout);
            if (cache != null) {
                latlength = cache.getTrackLength();
                lat = cache.getLats();
            }
            if (latlength > 0)
                processSeekBar.setMax(latlength - 1);
            markersMap = getPOIsMap();
            if (playMode == playtriptype_skyview) {
                runPointImage = new ImageView(getActivity());
                runPointImage.setImageResource(R.drawable.runpoint);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                mapLayout.addView(runPointImage, params);
            } else if (latlength > 0 && gmap != null) {
                playPoint = gmap.addMarker(new MarkerOptions().position(lat[0]).icon(BitmapDescriptorFactory.fromResource(R.drawable.runpoint)).anchor((float) 0.5, (float) 0.5));
            }
            mp = null;
            if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("playmusic", false)) {
                String musicPath = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("musicpath", "");
                if (!"".equals(musicPath)) {
                    File file = new File(getActivity().getFilesDir(), musicPath);
                    if (file.exists() && file.isFile() && FileHelper.isAudio(file)) {
                        mp = MediaPlayer.create(getActivity(), Uri.fromFile(file));
                        mp.setLooping(true);
                        mp.start();
                    }
                }
            }
            distances = new float[latlength];
            if (distances.length > 0) {
                distances[0] = 0;
                for (int i = 1; i < distances.length; i++) {
                    distances[i] = distances[i - 1] + GpxAnalyzerJava.distFrom(cache.latitudes[i - 1], cache.longitudes[i - 1], cache.latitudes[i], cache.longitudes[i]);
                }
            }
            playTrip.setImageResource(R.drawable.ic_pause);
            stopTrip.setVisibility(View.VISIBLE);
            fastForward.setVisibility(View.VISIBLE);
            slowForward.setVisibility(View.VISIBLE);
            viewInformation.setVisibility(View.GONE);
            viewGraph.setVisibility(View.GONE);
            streetView.setVisibility(View.GONE);
            viewNote.setVisibility(View.GONE);
            playPanel.setVisibility(View.VISIBLE);
            processSeekBar.setVisibility(View.VISIBLE);
            run = false;
        }

        public void play() {
            run = !run;
            if (run) { // resume or start
                handler.post(this);
                playTrip.setImageResource(R.drawable.ic_pause);
            } else { // pause
                playTrip.setImageResource(R.drawable.ic_play);
            }
        }

        public void pause() {
            run = true;
            play();
        }

        public void resume() {
            run = false;
            play();
        }

        public void stop() {
            index = 0;
            run = false;
            stopTrip.setVisibility(View.GONE);
            fastForward.setVisibility(View.GONE);
            slowForward.setVisibility(View.GONE);
            viewInformation.setVisibility(View.VISIBLE);
            viewGraph.setVisibility(View.VISIBLE);
            streetView.setVisibility(View.VISIBLE);
            viewNote.setVisibility(View.VISIBLE);
            playPanel.setVisibility(View.GONE);
            processSeekBar.setVisibility(View.GONE);
            playTrip.setImageResource(R.drawable.ic_play);
            if (playPoint != null)
                playPoint.remove();
            if (playMode == playtriptype_skyview) {
                mapLayout.removeView(runPointImage);
            }
            if (mp != null) {
                mp.stop();
                mp.release();
                mp = null;
            }
            tripPlayer = null;
        }

        public void changeForward(int option) {
            switch (option) {
                case fast:
                    if (interval == 1)
                        add *= 2;
                    else
                        interval /= 2;
                    break;
                case slow:
                    if (add == 1)
                        interval *= 2;
                    else
                        add /= 2;
                    break;
                default:
                    break;
            }
        }

        public void run() {
            for (int i = index; i < index + add; i++) {
                if (run && markersMap.get(i) != null) {
                    setPosition(i);
                    index = i + 1;
                    Activity activity = getActivity();
                    if (activity != null) {
                        pause();
                        Intent intent = new Intent(activity, PlayPointActivity.class);
                        intent.putExtra(PlayPointActivity.tag_trip, trip.tripName);
                        intent.putExtra(PlayPointActivity.tag_poi, markersMap.get(i).title);
                        activity.startActivity(intent);
                    }
                    break;
                }
            }
            if (run && index < latlength) {
                setPosition(index);
                index += add;
                handler.postDelayed(this, interval);
            } else if (index >= latlength) {
                stop();
            }
        }

        private void setPosition(int index) {
            if (cache == null || lat == null)
                return;
            if (cache.times == null || cache.altitudes == null || distances == null)
                return;
            if (index >= latlength || cache.times[index] == null)
                return;
            time.setText(cache.times[index].substring(5));
            altitude.setText(getString(R.string.Altitude) + ":" + GpxAnalyzer2.getAltitudeString(cache.altitudes[index], "m"));
            distance.setText(getString(R.string.distance) + ":" + GpxAnalyzer2.getDistanceString(distances[index] / 1000, "km"));
            int start = index - 10, end = index + 10;
            if (start < 0)
                start = 0;
            if (end >= latlength)
                end = latlength - 1;
            float distance = GpxAnalyzerJava.distFrom(lat[start].latitude, lat[start].longitude, lat[end].latitude, lat[end].longitude);
            float seconds = (MyCalendar.getTime(cache.times[end], MyCalendar.type_self).getTimeInMillis() - MyCalendar.getTime(cache.times[start], MyCalendar.type_self).getTimeInMillis()) / 1000;
            float speedVal = distance / seconds * 18 / 5;
            String speedStr = GpxAnalyzer2.getDistanceString(speedVal, "km/hr");
            speed.setText(getString(R.string.velocity) + ":" + speedStr);
            processSeekBar.setProgress(index);
            if (playMode == playtriptype_skyview) {
                bearing += 0.05;
                if (bearing > 360)
                    bearing = 0;
                if (gmap != null && lat[index] != null)
                    gmap.animateCamera(CameraUpdateFactory.newCameraPosition(new CameraPosition(lat[index], gmap.getCameraPosition().zoom, 90, bearing)), interval, null);
            } else {
                playPoint.setPosition(lat[index]);
            }
        }

        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser) {
                index = progress;
                setPosition(index);
            }
        }

        public void onStartTrackingTouch(SeekBar seekBar) {
            pause();
        }

        public void onStopTrackingTouch(SeekBar seekBar) {
            resume();
        }

    }

    private SparseArray<POI> getPOIsMap() {
        if (trip != null) {
            return trip.getPOIsInTrackMap();
        } else {
            return new SparseArray<>();
        }
    }

    class GenerateKMxTask extends AsyncTask<Void, Object, DocumentFile> {

        DocumentFile outputFile;
        ProgressDialog pd;
        int option;
        public static final int kmz = 0;
        public static final int kml = 1;
        public static final int gpx = 2;

        public GenerateKMxTask(DocumentFile outputFile, int option) {
            this.outputFile = outputFile;
            this.option = option;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getActivity());
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected DocumentFile doInBackground(Void... params) {
            Trip.GenerateKMxListener listener = new Trip.GenerateKMxListener() {
                @Override
                public void onProgressChanged(int progress, int total) {
                    publishProgress(progress, total);
                }
            };
            if (option == kmz) {
                return trip.generateKMZ(outputFile, listener);
            } else if (option == kml) {
                return trip.generateKML(outputFile, listener);
            } else if (option == gpx) {
                return trip.generateGPX(outputFile, listener);
            } else {
                return null;
            }

        }

        @Override
        protected void onProgressUpdate(Object... values) {
            pd.setProgress((int) values[0]);
            pd.setMax((int) values[1]);
        }

        @Override
        protected void onPostExecute(DocumentFile file) {
            pd.dismiss();
            if (file != null) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, file.getUri());
                if (option == kml) {
                    intent.setType("application/vnd.google-earth.kml");
                    DeviceHelper.sendGATrack(getActivity(), "Trip", "share_track_by_kml", trip.tripName, null);
                } else if (option == kmz) {
                    intent.setType("application/vnd.google-earth.kmz");
                    DeviceHelper.sendGATrack(getActivity(), "Trip", "share_track_by_kmz", trip.tripName, null);
                } else if (option == gpx) {
                    intent.setType("application/gpx+xml");
                    DeviceHelper.sendGATrack(getActivity(), "Trip", "share_track_by_gpx", trip.tripName, null);
                }
                startActivity(intent);
            }
        }
    }

    public static Bitmap gmapBitmap;
    public static Point[] trackPoints;
    public static SparseArray<POI> poisMap;
    public String videoBackgroundMusic;
    public Button backgroundMusicButton;

    private void generateVideo() {
        videoBackgroundMusic = null;
        trackPoints = null;
        gmapBitmap = null;
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle(R.string.generate_video);
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_generate_video, rootView, false);
        ab.setView(layout);
        final EditText videoName = (EditText) layout.findViewById(R.id.videoName);
        videoName.setText(trip.tripName);
        final Spinner resolution = (Spinner) layout.findViewById(R.id.resolution);
        resolution.setSelection(0);
        final Spinner fps = (Spinner) layout.findViewById(R.id.fps);
        fps.setSelection(2);
        this.backgroundMusicButton = (Button) layout.findViewById(R.id.backgroundMusic);
        this.backgroundMusicButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i3 = new Intent(Intent.ACTION_GET_CONTENT);
                i3.setType("audio/*");
                startActivityForResult(Intent.createChooser(i3, getString(R.string.select_the_audio_by)), REQUEST_BACKGROUND_MUSIC);
            }
        });
        ab.setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String resolutionStr = resolution.getSelectedItem().toString();
                final int videoWidth = Integer.valueOf(resolutionStr.split("x")[0]);
                final int videoHeight = Integer.valueOf(resolutionStr.split("x")[1]);
                final int fpsValue = Integer.valueOf(fps.getSelectedItem().toString());
                final String videoNameStr = videoName.getText().toString() + ".mp4";
                dialog.dismiss();
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                MapView mapView = new MapView(getActivity());
                ab.setView(mapView);
                final AlertDialog alertDialog = ab.create();
                alertDialog.show();
                int dp32 = (int) DeviceHelper.pxFromDp(getActivity(), 32);
                alertDialog.getWindow().setLayout(videoWidth + dp32, videoHeight + dp32);
                MapsInitializer.initialize(getActivity());
                mapView.onCreate(new Bundle());
                mapView.onResume();
                mapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(final GoogleMap googleMap) {
                        if (googleMap == null) {
                            Toast.makeText(getActivity(), R.string.unknown_error, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        final LatLng[] lat = cache.getLats();
                        int trackColor = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("trackcolor", 0xff6699cc);
                        googleMap.addPolyline(new PolylineOptions().add(lat).color(trackColor).width(5));
                        float hue = DrawableHelper.getMarkerColorHue(getActivity());
                        BitmapDescriptor bd = BitmapDescriptorFactory.defaultMarker(hue);
                        poisMap = getPOIsMap();
                        if (poisMap == null) return;
                        for (int i = 0; i < poisMap.size(); i++) {
                            POI poi = poisMap.valueAt(i);
                            LatLng position = new LatLng(poi.latitude, poi.longitude);
                            googleMap.addMarker(new MarkerOptions().position(position).icon(bd));
                        }
                        LatLngBounds.Builder latLngBoundsBuilder = new LatLngBounds.Builder();
                        for (LatLng latlng : lat) {
                            latLngBoundsBuilder.include(latlng);
                        }
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latLngBoundsBuilder.build(), 0), new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                googleMap.animateCamera(CameraUpdateFactory.zoomOut(), new GoogleMap.CancelableCallback() {
                                    @Override
                                    public void onFinish() {
                                        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                                            @Override
                                            public void onMapLoaded() {
                                                googleMap.setOnMapLoadedCallback(null);
                                                googleMap.snapshot(new GoogleMap.SnapshotReadyCallback() {
                                                    @Override
                                                    public void onSnapshotReady(Bitmap bitmap) {
                                                        Projection projection = googleMap.getProjection();
                                                        int[] markersIndexes = new int[poisMap.size()]; //indexes in track
                                                        for (int i = 0; i < markersIndexes.length; i++) {
                                                            markersIndexes[i] = poisMap.keyAt(i);
                                                        }
                                                        Arrays.sort(markersIndexes);
                                                        int num_tracks = poisMap.size() + 1;
                                                        int frames_per_track = GenerateVideoService.secondsPerTrack * fpsValue;
                                                        Point[] trackPoints = new Point[num_tracks * frames_per_track];
                                                        for (int i = 0; i < num_tracks; i++) {
                                                            int start, end, length;
                                                            if (i == 0) {
                                                                start = 0;
                                                            } else {
                                                                start = markersIndexes[i - 1];
                                                            }
                                                            if (i == num_tracks - 1) {
                                                                end = lat.length - 1;
                                                            } else {
                                                                end = markersIndexes[i];
                                                            }
                                                            length = end - start;
                                                            for (int j = 0; j < frames_per_track; j++) {
                                                                int indexInLat = start + length * j / frames_per_track;
                                                                trackPoints[i * frames_per_track + j] = projection.toScreenLocation(lat[indexInLat]);
                                                            }
                                                        }
                                                        ViewMapFragment.gmapBitmap = bitmap;
                                                        ViewMapFragment.trackPoints = trackPoints;
                                                        googleMap.clear();
                                                        alertDialog.dismiss();
                                                        Intent intent = new Intent(getActivity(), GenerateVideoService.class);
                                                        intent.putExtra(GenerateVideoService.tag_background_music_path, videoBackgroundMusic);
                                                        intent.putExtra(GenerateVideoService.tag_video_width, videoWidth);
                                                        intent.putExtra(GenerateVideoService.tag_video_height, videoHeight);
                                                        intent.putExtra(GenerateVideoService.tag_videoname, videoNameStr);
                                                        intent.putExtra(GenerateVideoService.tag_fps, fpsValue);
                                                        intent.putExtra(GenerateVideoService.tag_tripName, trip.tripName);
                                                        intent.putExtra(GenerateVideoService.tag_timeZone, trip.timezone);
                                                        getActivity().startService(intent);
                                                    }
                                                });
                                            }
                                        });
                                    }

                                    @Override
                                    public void onCancel() {

                                    }
                                });
                            }

                            @Override
                            public void onCancel() {

                            }
                        });
                    }
                });
            }
        });
        ab.setNegativeButton(R.string.cancel, null);
        ab.show();
    }

    @Override
    public void onDestroy() {
        if (tripPlayer != null) {
            tripPlayer.stop();
        }
        cache = null;
        trip = null;
        if (markers != null) {
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
        }
        if (gmap != null) {
            if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                gmap.setMyLocationEnabled(false);
            }
            gmap.clear();
        }
        if (importMemoryTask != null) {
            importMemoryTask.cancel = true;
            importMemoryTask = null;
        }
        super.onDestroy();
    }

}
