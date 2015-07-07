package com.yupog2003.tripdiary.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.format.Time;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
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
import com.yupog2003.tripdiary.ViewGraphAcivity;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.FileHelper.DirAdapter;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava.ProgressChangedListener;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.MyLatLng2;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.PackageHelper;
import com.yupog2003.tripdiary.data.TrackCache;
import com.yupog2003.tripdiary.services.GenerateVideoService;
import com.yupog2003.tripdiary.services.SendTripService;
import com.yupog2003.tripdiary.views.POIInfoWindowAdapter;
import com.yupog2003.tripdiary.views.SpinnerActionProvider;

import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;

public class ViewMapFragment extends Fragment implements OnInfoWindowClickListener, OnMapLongClickListener, OnMarkerDragListener, OnClickListener {
    MapFragment mapFragment;
    GoogleMap gmap;
    String path;
    String name;
    ArrayList<Marker> markers;
    LatLng[] lat;
    TrackCache cache;
    ImageButton viewInformation;
    ImageButton switchMapMode;
    ImageButton playTrip;
    ImageButton stopTrip;
    ImageButton fastforward;
    ImageButton slowforward;
    ImageButton showBar;
    ImageButton viewGraph;
    ImageButton streetView;
    ImageButton viewNote;
    SeekBar processSeekBar;
    LinearLayout playPanel;
    TextView altitude;
    TextView distance;
    TextView speed;
    TextView time;
    boolean showAll = true;
    boolean uploadPublic;
    SearchView search;
    SpinnerActionProvider spinnerActionProvider;
    TripPlayer tripPlayer;
    SetLocus setLocusTask;
    Handler handler;
    int trackColor;
    LinearLayout buttonBar;
    String token;
    String account;
    RelativeLayout mapLayout;

    private static final int import_track = 0;
    private static final int update_request = 1;
    private static final int REQUEST_GET_TOKEN = 2;
    private static final int REQUEST_BACKGROUND_MUSIC = 3;
    // private static final int playtriptype_normal=0;
    private static final int playtriptype_skyview = 1;
    private static final int request_write_location_to_POI = 1;
    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_view_map, null);
        mapFragment = MapFragment.newInstance();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.maplayout, mapFragment, "mapFragment");
        ft.commit();
        mapLayout = (RelativeLayout) rootView.findViewById(R.id.maplayout);
        buttonBar = (LinearLayout) rootView.findViewById(R.id.buttonbar);
        path = ViewTripActivity.path;
        name = ViewTripActivity.name;
        trackColor = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("trackcolor", 0xff6699cc);
        markers = new ArrayList<Marker>();
        handler = new Handler();
        viewInformation = (ImageButton) rootView.findViewById(R.id.viewinformation);
        switchMapMode = (ImageButton) rootView.findViewById(R.id.switchmapmode);
        playTrip = (ImageButton) rootView.findViewById(R.id.playtrip);
        stopTrip = (ImageButton) rootView.findViewById(R.id.stoptrip);
        fastforward = (ImageButton) rootView.findViewById(R.id.fastforward);
        slowforward = (ImageButton) rootView.findViewById(R.id.slowforward);
        showBar = (ImageButton) rootView.findViewById(R.id.showbar);
        viewGraph = (ImageButton) rootView.findViewById(R.id.viewgraph);
        streetView = (ImageButton) rootView.findViewById(R.id.streetview);
        viewNote = (ImageButton) rootView.findViewById(R.id.viewnote);
        playPanel = (LinearLayout) rootView.findViewById(R.id.playPanel);
        altitude = (TextView) rootView.findViewById(R.id.altitude);
        distance = (TextView) rootView.findViewById(R.id.distance);
        speed = (TextView) rootView.findViewById(R.id.speed);
        time = (TextView) rootView.findViewById(R.id.time);
        processSeekBar = (SeekBar) rootView.findViewById(R.id.playProcess);
        stopTrip.setOnClickListener(this);
        fastforward.setOnClickListener(this);
        slowforward.setOnClickListener(this);
        switchMapMode.setOnClickListener(this);
        showBar.setOnClickListener(this);
        viewGraph.setOnClickListener(this);
        streetView.setOnClickListener(this);
        viewNote.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onResume() {

        super.onResume();
        setHasOptionsMenu(true);
        if (gmap == null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {

                @Override
                public void onMapReady(GoogleMap googleMap) {

                    gmap = googleMap;
                    gmap.setMyLocationEnabled(true);
                    gmap.getUiSettings().setZoomControlsEnabled(true);
                    gmap.setOnInfoWindowClickListener(ViewMapFragment.this);
                    gmap.setOnMapLongClickListener(ViewMapFragment.this);
                    gmap.setOnMarkerDragListener(ViewMapFragment.this);
                    gmap.setInfoWindowAdapter(new POIInfoWindowAdapter(getActivity(), path + "/" + name));
                    if (new File(path + "/" + name + "/" + name + ".gpx").length() > 0) {
                        setLocusTask = new SetLocus();
                        setLocusTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, 0);
                    } else {
                        handleCannotFindGpx();
                    }
                    setPOIs();
                }
            });
        }
        if (tripPlayer != null) {
            tripPlayer.resume();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.fragment_view_map, menu);
        MenuItem searchItem = menu.findItem(R.id.searchviewmap);
        search = (SearchView) MenuItemCompat.getActionView(searchItem);
        search.setQueryHint(getString(R.string.search_poi));
        search.setOnQueryTextListener(new OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
                search.clearFocus();
                final String searchname = search.getQuery().toString();
                if (!searchname.equals("")) {
                    final ArrayList<Marker> founds = new ArrayList<Marker>();
                    final int markersSize = markers.size();
                    for (int i = 0; i < markersSize; i++) {
                        if (markers.get(i).getTitle().contains(searchname)) {
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
                        String[] foundsname = new String[founds.size()];
                        for (int j = 0; j < founds.size(); j++) {
                            foundsname[j] = founds.get(j).getTitle();
                        }
                        choose.setSingleChoiceItems(foundsname, -1, new DialogInterface.OnClickListener() {

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
        if (spinnerActionProvider != null) {
            MenuItem spinnerItem = menu.findItem(R.id.spinner);
            MenuItemCompat.setActionProvider(spinnerItem, spinnerActionProvider);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getActivity().finish();
        } else if (item.getItemId() == R.id.clearcache) {
            if (ViewTripActivity.trip != null) {
                ViewTripActivity.trip.deleteCache();
                Toast.makeText(getActivity(), getString(R.string.cache_has_been_cleared), Toast.LENGTH_SHORT).show();
                DeviceHelper.sendGATrack(getActivity(),"Trip", "clear_cache", ViewTripActivity.trip.dir.getName(), null);
            }
        } else if (item.getItemId() == R.id.sharetrackby) {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.share_track_by___));
            String[] bys = new String[]{getString(R.string.gpx), getString(R.string.kml), getString(R.string.app_name)};
            ab.setSingleChoiceItems(bys, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    switch (which) {
                        case 0:
                            Intent intent = new Intent(Intent.ACTION_SEND);
                            intent.setType("application/gpx+xml");
                            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(ViewTripActivity.trip.gpxFile));
                            getActivity().startActivity(intent);
                            DeviceHelper.sendGATrack(getActivity(),"Trip", "share_track_by_gpx", ViewTripActivity.trip.dir.getName(), null);
                            break;
                        case 1:
                            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                            ab.setTitle(getString(R.string.share_track_kml));
                            final EditText filepath = new EditText(getActivity());
                            filepath.setText(path + "/" + name + "/" + name + ".kml");
                            ab.setView(filepath);
                            ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    File kmlFile = new File(filepath.getText().toString());
                                    FileHelper.convertToKml(markers, lat, kmlFile, ViewTripActivity.trip.note);
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("application/vnd.google-earth.kml+xml");
                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(kmlFile));
                                    getActivity().startActivity(intent);
                                    DeviceHelper.sendGATrack(getActivity(),"Trip", "share_track_by_kml", ViewTripActivity.trip.dir.getName(), null);
                                }
                            });
                            ab.setNegativeButton(getString(R.string.cancel), null);
                            ab.show();
                            break;
                        case 2:
                            Account[] accounts = AccountManager.get(getActivity()).getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                            if (accounts != null && accounts.length > 0) {
                                account = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("account", accounts[0].name);
                                AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity());
                                ab2.setTitle(getString(R.string.make_it_public));
                                ab2.setMessage(getString(R.string.make_it_public_to_share));
                                ab2.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        uploadPublic = true;
                                        new GetAccessTokenTask().execute();
                                    }
                                });
                                ab2.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        uploadPublic = false;
                                        new GetAccessTokenTask().execute();
                                    }
                                });
                                ab2.show();
                            }
                            break;
                    }
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

                    AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                    ListView listView = new ListView(getActivity());
                    final DirAdapter adapter = new DirAdapter(getActivity(), false, Environment.getExternalStorageDirectory());
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(adapter);
                    ab.setTitle(getString(R.string.select_a_directory));
                    ab.setView(listView);
                    ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            DeviceHelper.sendGATrack(getActivity(),"Trip", "import_memory", ViewTripActivity.trip.dir.getName(), null);
                            new ImportMemoryTask(ViewTripActivity.trip.dir, adapter.getRoot()).execute();
                        }
                    });
                    ab.setNegativeButton(getString(R.string.cancel), null);
                    ab.show();
                }
            });
            ab.setNegativeButton(getString(R.string.cancel), null);
            ab.show();
        } else if (item.getItemId() == R.id.generateVideo) {
            if (DeviceHelper.isMobileNetworkAvailable(getActivity())) {
                generateVideo();
            } else {
                Toast.makeText(getActivity(), R.string.please_connect_to_internet_for_downloading_google_map_background, Toast.LENGTH_SHORT).show();
            }
        }
        return true;
    }

    class ImportMemoryTask extends AsyncTask<File, String, String> {

        File tripFile;
        File memory;
        TextView message;
        ProgressBar progress;
        TextView progressMessage;
        AlertDialog dialog;
        boolean cancel = false;

        public ImportMemoryTask(File trip, File memory) {
            this.tripFile = trip;
            this.memory = memory;
        }

        @Override
        protected void onPreExecute() {

            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.importing));
            LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.progressdialog_import_memory, null);
            message = (TextView) layout.findViewById(R.id.message);
            progress = (ProgressBar) layout.findViewById(R.id.progressBar);
            progressMessage = (TextView) layout.findViewById(R.id.progress);
            ab.setView(layout);
            ab.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    cancel = true;
                }
            });
            dialog = ab.create();
            dialog.show();
        }

        @Override
        protected String doInBackground(File... params) {

            publishProgress("Sorting...", "0");
            if (ViewTripActivity.trip.pois.length < 1 || cache.times == null)
                return null;
            HashMap<Long, POI> pois = new HashMap<Long, POI>();
            for (int i = 0; i < ViewTripActivity.trip.pois.length; i++) {
                MyCalendar time = ViewTripActivity.trip.pois[i].time;
                time.setTimeZone(ViewTripActivity.trip.timezone);
                pois.put(time.getTimeInMillis(), ViewTripActivity.trip.pois[i]);
            }
            Set<Long> set = pois.keySet();
            Long[] poiTimes = set.toArray(new Long[set.size()]);
            Arrays.sort(poiTimes);
            File[] memories = memory.listFiles(FileHelper.getMemoryFilter());
            if (memories == null)
                memories = new File[0];
            long start = 0;
            long end = Long.MAX_VALUE;
            if (cache.times.length > 0) {
                start = MyCalendar.getTime(ViewTripActivity.trip.timezone, cache.times[0], MyCalendar.type_self).getTimeInMillis();
                end = MyCalendar.getTime(ViewTripActivity.trip.timezone, cache.times[cache.times.length - 1], MyCalendar.type_self).getTimeInMillis();
            }
            HashMap<Long, File> mems = new HashMap<Long, File>();
            for (int i = 0; i < memories.length; i++) {
                long timeMills = getFileTime(memories[i]);
                if (timeMills >= start && timeMills <= end) {
                    mems.put(timeMills, memories[i]);
                }
            }
            set = mems.keySet();
            Long[] memTimes = set.toArray(new Long[set.size()]);
            Arrays.sort(memTimes);
            publishProgress("setMax", String.valueOf(memTimes.length));
            int key = 0;
            long[] poisMiddleTime = new long[poiTimes.length - 1];
            for (int i = 0; i < poisMiddleTime.length; i++) {
                poisMiddleTime[i] = (poiTimes[i] + poiTimes[i + 1]) / 2;
            }
            for (int i = 0; i < memTimes.length; i++) {
                if (cancel)
                    break;
                boolean coppied = false;
                File infile = mems.get(memTimes[i]);
                publishProgress(infile.getName(), String.valueOf(i));
                String path = getDestDirFromFile(infile); // pictures,videos,audios
                if (path == null)
                    continue;
                for (int j = key; j < poisMiddleTime.length; j++) {
                    if (memTimes[i] <= poisMiddleTime[j]) {
                        FileHelper.copyFile(infile, new File(pois.get(poiTimes[j]).dir.getPath() + "/" + path + "/" + infile.getName()));
                        key = j;
                        coppied = true;
                        break;
                    }
                }
                if (!coppied) {
                    FileHelper.copyFile(infile, new File(pois.get(poiTimes[poiTimes.length - 1]).dir.getPath() + "/" + path + "/" + infile.getName()));
                }
            }
            return null;
        }

        private String getDestDirFromFile(File file) {
            String path = null;
            if (FileHelper.isPicture(file))
                path = "pictures";
            else if (FileHelper.isVideo(file))
                path = "videos";
            else if (FileHelper.isAudio(file))
                path = "audios";
            return path;
        }

        private long getFileTime(File file) {
            String mime = FileHelper.getMimeFromFile(file);
            long timeMills = 0;
            if (mime.equals("image/jpeg")) {
                try {
                    ExifInterface exif = new ExifInterface(file.getPath());
                    String datetime = exif.getAttribute(ExifInterface.TAG_DATETIME);
                    if (datetime != null) {
                        String date = datetime.split(" ")[0];
                        String time = datetime.split(" ")[1];
                        String[] dates = date.split(":");
                        String[] times = time.split(":");
                        MyCalendar time1 = MyCalendar.getInstance(TimeZone.getTimeZone(ViewTripActivity.trip.timezone));
                        time1.set(Integer.parseInt(dates[0]), Integer.parseInt(dates[1])-1, Integer.parseInt(dates[0]), Integer.parseInt(times[0]), Integer.parseInt(times[1]), Integer.parseInt(times[2]));
                        timeMills = time1.getTimeInMillis();
                    }
                } catch (IOException e) {

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
            ViewTripActivity.trip.refreshPOIs();
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.finish));
            ab.setMessage(getString(R.string.finish_import_memory));
            ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    FileHelper.deletedir(memory.getPath());
                }
            });
            ab.setNegativeButton(getString(R.string.cancel), null);
            ab.show();
        }
    }

    private void handleCannotFindGpx() {
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle(getString(R.string.cannot_find_gpx_data));
        ab.setMessage(getString(R.string.ask_import_gpx_file));
        ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                importGpx();
            }
        });
        ab.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                Toast.makeText(getActivity(), getString(R.string.there_is_no_data_to_display), Toast.LENGTH_LONG).show();
                getActivity().finish();
            }
        });
        ab.show();
    }

    private void importGpx() {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("file/*");
        Intent i2 = Intent.createChooser(i, getString(R.string.select_the_gpx));
        startActivityForResult(i2, import_track);
    }

    private void updateAll() {
        if (gmap != null && lat != null) {
            gmap.clear();
            gmap.addPolyline(new PolylineOptions().add(lat).width(5).color(trackColor));
            setPOIs();
        }
    }

    public void setPOIs() {
        if (markers == null || gmap == null || ViewTripActivity.trip == null || ViewTripActivity.trip.pois == null || !isAdded())
            return;
        for (int i = 0; i < markers.size(); i++) {
            markers.get(i).remove();
        }
        markers.clear();
        ViewTripActivity.trip.refreshPOIs();
        for (int i = 0; i < ViewTripActivity.trip.pois.length; i++) {
            POI poi = ViewTripActivity.trip.pois[i];
            String poiTime = poi.time.formatInTimezone(ViewTripActivity.trip.timezone);
            markers.add(gmap.addMarker(new MarkerOptions().position(new LatLng(poi.latitude, poi.longitude)).title(poi.title).snippet(" " + poiTime + "\n " + poi.diary).draggable(true)));
        }
        ArrayList<String> markerNames = new ArrayList<String>();
        markerNames.add(getString(R.string.select_poi));
        for (int i = 0; i < markers.size(); i++) {
            markerNames.add(markers.get(i).getTitle());
        }
        if (spinnerActionProvider == null) {
            spinnerActionProvider = new SpinnerActionProvider(getActivity());
        }
        spinnerActionProvider.setItems(markerNames, new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (view instanceof TextView) {
                    ((TextView) view).setTextColor(getResources().getColor(R.color.textColorPrimary));
                }
                if (position > 0 && position - 1 < markers.size()) {
                    position--;
                    gmap.animateCamera(CameraUpdateFactory.newLatLng(markers.get(position).getPosition()));
                    markers.get(position).showInfoWindow();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
    }

    private void writeLocationToPoint(TrackCache cache) {
        MyCalendar[] times = new MyCalendar[cache.times.length];
        for (int i = 0; i < times.length; i++) {
            MyCalendar time = MyCalendar.getTime(ViewTripActivity.trip.timezone, cache.times[i], MyCalendar.type_self);
            times[i] = time;
        }
        for (int i = 0; i < ViewTripActivity.trip.pois.length; i++) {
            MyCalendar nowTime = MyCalendar.getInstance(TimeZone.getTimeZone("UTC"));
            nowTime.setTimeInMillis(ViewTripActivity.trip.pois[i].time.getTimeInMillis());
            nowTime.setTimeZone(ViewTripActivity.trip.timezone);
            MyCalendar previousTime = times[0];
            MyCalendar nextTime = times[times.length - 1];
            if (MyCalendar.isTimeMatched(previousTime, nowTime, nextTime)) {
                for (int j = 0; j < times.length; j++) {
                    nextTime = times[j];
                    if (MyCalendar.isTimeMatched(previousTime, nowTime, nextTime)) {
                        ViewTripActivity.trip.pois[i].updateBasicInformation(null, null, cache.latitudes[j], cache.longitudes[j], (double) cache.altitudes[j]);
                        break;
                    }
                    previousTime = nextTime;
                }
            }
        }

    }

    class SetLocus extends AsyncTask<Integer, Integer, LatLng[]> {
        int option;
        long fileSize;
        ProgressBar progressBar;
        ProgressChangedListener listener;
        LatLngBounds.Builder latlngBoundesBuilder;

        public void stop() {
            if (ViewTripActivity.trip != null) {
                ViewTripActivity.trip.stopGetCache();
            }
        }

        @Override
        protected void onPreExecute() {
            latlngBoundesBuilder = new LatLngBounds.Builder();
            fileSize = ViewTripActivity.trip.cacheFile.exists() ? ViewTripActivity.trip.cacheFile.length() : ViewTripActivity.trip.gpxFile.length();
            progressBar = (ProgressBar) rootView.findViewById(R.id.analysis_progress);
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setMax(100);
            progressBar.setProgress(0);
            listener = new ProgressChangedListener() {

                public void onProgressChanged(long progress) {

                    if (fileSize != 0)
                        publishProgress(0, (int) (progress * 100 / fileSize));
                }
            };
            getActivity().setProgressBarIndeterminateVisibility(true);
            if (!ViewTripActivity.libraryLoadSuccess) {
                Toast.makeText(getActivity(), getString(R.string.failed_to_loadlibrary), Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected LatLng[] doInBackground(Integer... arg0) {

            option = arg0[0];
            if (isAdded()) {
                publishProgress(ViewTripActivity.trip.cacheFile.exists() ? 1 : 2);
                if (ViewTripActivity.libraryLoadSuccess) {
                    ViewTripActivity.trip.getCacheJNI(getActivity(), handler, listener);
                } else {
                    ViewTripActivity.trip.getCacheJava(getActivity(), handler, listener);
                }
                if (option == request_write_location_to_POI) {
                    if (ViewTripActivity.trip.cache != null && isAdded()) {
                        publishProgress(3);
                        writeLocationToPoint(ViewTripActivity.trip.cache);
                    } else if (isAdded()) {
                        Toast.makeText(getActivity(), getString(R.string.gpx_doesnt_contain_time_information), Toast.LENGTH_LONG).show();
                    }
                    System.gc();
                }
            }
            try {
                if (ViewTripActivity.trip.cache != null) {
                    cache = ViewTripActivity.trip.cache;
                    final int latsSize = Math.min(cache.latitudes.length, cache.longitudes.length);
                    lat = new LatLng[latsSize];
                    for (int i = 0; i < latsSize; i++) {
                        lat[i] = new LatLng(ViewTripActivity.trip.cache.latitudes[i], ViewTripActivity.trip.cache.longitudes[i]);
                        latlngBoundesBuilder.include(lat[i]);
                    }
                }
            } catch (Exception e) {
                ViewTripActivity.trip.cacheFile.delete();
                e.printStackTrace();
            }
            return lat;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if (isAdded()) {
                switch (progress[0]) {
                    case 0:
                        progressBar.setProgress(progress[1]);
                        break;
                    case 1:
                        Toast.makeText(getActivity(), getString(R.string.analysis_gpx), Toast.LENGTH_LONG).show();
                        break;
                    case 2:
                        Toast.makeText(getActivity(), getString(R.string.first_analysis_gpx), Toast.LENGTH_LONG).show();
                        break;
                    case 3:
                        Toast.makeText(getActivity(), getString(R.string.setup_pois), Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        }

        @Override
        protected void onPostExecute(LatLng[] result) {
            if (isAdded()) {
                if (mapFragment.getMap() != null && gmap != null && result != null && result.length > 0) {
                    gmap.addPolyline(new PolylineOptions().add(result).width(5).color(trackColor));
                    gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBoundesBuilder.build(), DeviceHelper.getScreenWidth(getActivity()), DeviceHelper.getScreenHeight(getActivity()), (int) DeviceHelper.pxFromDp(getActivity(), 10)));
                } else {
                    Toast.makeText(getActivity(), getString(R.string.invalid_gpx_file), Toast.LENGTH_LONG).show();
                    ViewTripActivity.trip.deleteCache();
                }
                viewInformation.setOnClickListener(ViewMapFragment.this);
                playTrip.setOnClickListener(ViewMapFragment.this);
                progressBar.setVisibility(View.GONE);
                getActivity().setProgressBarIndeterminateVisibility(false);
            }
        }
    }

    public void onInfoWindowClick(Marker marker) {

        String pointtitle = marker.getTitle();
        viewPOI(pointtitle);
    }

    private void viewPOI(String poiName) {
        if (new File(path + "/" + name + "/" + poiName).exists()) {
            Intent intent = new Intent(getActivity(), ViewPointActivity.class);
            intent.putExtra("path", path + "/" + name + "/" + poiName);
            intent.putExtra("request_code", update_request);
            startActivityForResult(intent, update_request);
        }
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
                case update_request:
                    if (data.getBooleanExtra("update", false)) {
                        setPOIs();
                    }
                    break;
                case import_track:
                    Uri uri = data.getData();
                    if (uri != null) {
                        File file = FileHelper.copyFromUriToFile(getActivity(), uri, new File(path + "/" + name), name + ".gpx");
                        if (file != null) {
                            setLocusTask = new SetLocus();
                            setLocusTask.execute(0);
                        }
                    }
                    break;
                case REQUEST_GET_TOKEN:
                    if (resultCode == Activity.RESULT_OK) {
                        new GetAccessTokenTask().execute();
                    }
                    break;
                case REQUEST_BACKGROUND_MUSIC:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri uri1 = data.getData();
                        if (uri1 != null) {
                            File resultFile = FileHelper.copyFromUriToFile(getActivity(), uri1, getActivity().getCacheDir(), null);
                            if (resultFile != null) {
                                this.videoBackgroundMusic = resultFile.getPath();
                                this.backgroundMusicButton.setText(resultFile.getName());
                            }
                        }
                    }
                    break;
            }
        }
    }

    public void onMapLongClick(final LatLng latlng) {

        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle(getString(R.string.add_poi));
        View layout = getActivity().getLayoutInflater().inflate(R.layout.edit_poi, null);
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
                MyCalendar time = MyCalendar.getInstance(TimeZone.getTimeZone(ViewTripActivity.trip.timezone));
                time.set(editdate.getYear(), editdate.getMonth(), editdate.getDayOfMonth(), edittime.getCurrentHour(), edittime.getCurrentMinute(), 0);
                time.format3339();
                time.setTimeZone("UTC");
                if (title.equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.input_the_poi_title), Toast.LENGTH_LONG).show();
                } else {
                    final String newPointPath = path + "/" + name + "/" + title;
                    File file = new File(newPointPath);
                    if (file.exists()) {
                        Toast.makeText(getActivity(), getString(R.string.there_is_a_same_poi), Toast.LENGTH_LONG).show();
                    } else {
                        addPoint(newPointPath, location, altitude, time);
                    }
                }
            }
        });
        ab.setNegativeButton(getString(R.string.cancel), null);
        ab.show();
    }

    private void addPoint(String newPointPath, LatLng latlng, double altitude, MyCalendar time) {
        POI poi = new POI(new File(newPointPath));
        poi.updateBasicInformation(null, time, latlng.latitude, latlng.longitude, altitude);
        updateAll();
    }

    private MyLatLng2 getLatLngInTrack(LatLng latlng) {
        MyLatLng2 result = new MyLatLng2(latlng.latitude, latlng.longitude, 0, MyCalendar.getInstance().formatInCurrentTimezone());
        if (ViewTripActivity.trip == null)
            return result;
        if (ViewTripActivity.trip.cache == null || lat == null)
            return result;
        if (ViewTripActivity.trip.cache.latitudes == null || ViewTripActivity.trip.cache.longitudes == null)
            return result;
        double minDistance = Double.MAX_VALUE;
        int resultIndex = 0;
        int latLength = lat.length;
        double distance;
        for (int i = 0; i < latLength; i++) {
            distance = Math.pow((latlng.latitude - lat[i].latitude), 2) + Math.pow(latlng.longitude - lat[i].longitude, 2);
            if (distance < minDistance) {
                minDistance = distance;
                resultIndex = i;
            }
        }
        if (resultIndex < ViewTripActivity.trip.cache.latitudes.length && resultIndex < ViewTripActivity.trip.cache.longitudes.length && resultIndex < ViewTripActivity.trip.cache.altitudes.length && resultIndex < ViewTripActivity.trip.cache.times.length)
            result = new MyLatLng2(ViewTripActivity.trip.cache.latitudes[resultIndex], ViewTripActivity.trip.cache.longitudes[resultIndex], ViewTripActivity.trip.cache.altitudes[resultIndex], ViewTripActivity.trip.cache.times[resultIndex]);
        return result;
    }

    public void onMarkerDrag(Marker marker) {


    }

    public void onMarkerDragEnd(Marker marker) {

        POI poi = new POI(new File(path + "/" + name + "/" + marker.getTitle()));
        poi.updateBasicInformation(null, null, marker.getPosition().latitude, marker.getPosition().longitude, null);
        updateAll();
    }

    public void onMarkerDragStart(Marker marker) {


    }

    @TargetApi(19)
    public void onClick(View v) {

        if (v == null)
            return;
        if (v.equals(viewInformation)) {
            if (ViewTripActivity.trip.cache == null) {
                Toast.makeText(getActivity(), getString(R.string.no_data_can_be_displayed), Toast.LENGTH_SHORT).show();
                return;
            }
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.statistics));
            View layout = getActivity().getLayoutInflater().inflate(R.layout.map_view_basicinformation, null);
            TextView starttime = (TextView) layout.findViewById(R.id.starttime);
            starttime.setText(ViewTripActivity.trip.cache.startTime);
            TextView stoptime = (TextView) layout.findViewById(R.id.stoptime);
            stoptime.setText(ViewTripActivity.trip.cache.endTime);
            TextView totaltime = (TextView) layout.findViewById(R.id.totaltime);
            totaltime.setText(":" + ViewTripActivity.trip.cache.totalTime);
            TextView distance = (TextView) layout.findViewById(R.id.distance);
            distance.setText(":" + GpxAnalyzer2.getDistanceString(ViewTripActivity.trip.cache.distance / 1000, "km"));
            TextView avgspeed = (TextView) layout.findViewById(R.id.avgspeed);
            avgspeed.setText(":" + GpxAnalyzer2.getDistanceString(ViewTripActivity.trip.cache.avgSpeed, "km/hr"));
            TextView maxspeed = (TextView) layout.findViewById(R.id.maxspeed);
            maxspeed.setText(":" + GpxAnalyzer2.getDistanceString(ViewTripActivity.trip.cache.maxSpeed, "km/hr"));
            TextView totalclimb = (TextView) layout.findViewById(R.id.totalclimb);
            totalclimb.setText(":" + GpxAnalyzer2.getAltitudeString(ViewTripActivity.trip.cache.climb, "m"));
            TextView maxaltitude = (TextView) layout.findViewById(R.id.maxaltitude);
            maxaltitude.setText(":" + GpxAnalyzer2.getAltitudeString(ViewTripActivity.trip.cache.maxAltitude, "m"));
            TextView minaltitude = (TextView) layout.findViewById(R.id.minaltitude);
            minaltitude.setText(":" + GpxAnalyzer2.getAltitudeString(ViewTripActivity.trip.cache.minAltitude, "m"));
            ab.setView(layout);
            ab.setPositiveButton(getString(R.string.enter), null);
            ab.show();
            DeviceHelper.sendGATrack(getActivity(),"Trip", "view_basicinformation", ViewTripActivity.trip.dir.getName(), null);
        } else if (v.equals(switchMapMode)) {
            if (gmap == null)
                return;
            switch (gmap.getMapType()) {
                case GoogleMap.MAP_TYPE_NORMAL:
                    gmap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                    break;
                case GoogleMap.MAP_TYPE_SATELLITE:
                    gmap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    break;
                case GoogleMap.MAP_TYPE_HYBRID:
                    gmap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                    break;
                case GoogleMap.MAP_TYPE_TERRAIN:
                    gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
                default:
                    gmap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    break;
            }
        } else if (v.equals(playTrip)) {
            if (tripPlayer == null) {
                tripPlayer = new TripPlayer();
                DeviceHelper.sendGATrack(getActivity(),"Trip", "play", ViewTripActivity.trip.dir.getName(), null);
            }
            tripPlayer.play();
        } else if (v.equals(stopTrip)) {
            if (tripPlayer != null) {
                tripPlayer.stop();
            }
        } else if (v.equals(showBar)) {
            showAll = !showAll;
            if (showAll) {
                buttonBar.setVisibility(View.VISIBLE);
                ((MyActivity) getActivity()).getSupportActionBar().show();
                if (ViewTripActivity.rotation == Surface.ROTATION_0 || ViewTripActivity.rotation == Surface.ROTATION_180) {
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
                    params.addRule(RelativeLayout.ABOVE, R.id.buttonbar);
                    processSeekBar.setLayoutParams(params);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    View decorView = getActivity().getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                } else {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    getActivity().getWindow().setAttributes(attrs);
                }
            } else {
                buttonBar.setVisibility(View.GONE);
                ((MyActivity) getActivity()).getSupportActionBar().hide();
                if (ViewTripActivity.rotation == Surface.ROTATION_0 || ViewTripActivity.rotation == Surface.ROTATION_180) {
                    RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) processSeekBar.getLayoutParams();
                    params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    processSeekBar.setLayoutParams(params);
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    View decorView = getActivity().getWindow().getDecorView();
                    decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                } else {
                    WindowManager.LayoutParams attrs = getActivity().getWindow().getAttributes();
                    attrs.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    getActivity().getWindow().setAttributes(attrs);
                }
            }
            DeviceHelper.sendGATrack(getActivity(),"Trip", "toggle_bar", ViewTripActivity.trip.dir.getName(), null);
        } else if (v.equals(fastforward)) {
            if (tripPlayer != null) {
                tripPlayer.changeForward(TripPlayer.fast);
            }
        } else if (v.equals(slowforward)) {
            if (tripPlayer != null) {
                tripPlayer.changeForward(TripPlayer.slow);
            }
        } else if (v.equals(viewGraph)) {
            /*Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(ViewTripActivity.trip.graphicFile), "image/*");
            getActivity().startActivity(intent);*/
            Intent intent=new Intent(getActivity(), ViewGraphAcivity.class);
            startActivity(intent);
            DeviceHelper.sendGATrack(getActivity(),"Trip", "view_graph", ViewTripActivity.trip.dir.getName(), null);
        } else if (v.equals(streetView)) {
            if (PackageHelper.isAppInstalled(getActivity(), PackageHelper.StreetViewPackageNmae)) {
                final RelativeLayout mapLayout = (RelativeLayout) rootView.findViewById(R.id.maplayout);
                final ImageButton streetman = new ImageButton(getActivity());
                streetman.setImageResource(R.drawable.ic_streetman);
                streetman.setBackgroundColor(Color.TRANSPARENT);
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
                mapLayout.addView(streetman, params);
                streetman.setOnClickListener(new OnClickListener() {

                    public void onClick(View v) {

                        mapLayout.removeView(streetman);
                        LatLng latlng = gmap.getCameraPosition().target;
                        Uri uri = Uri.parse("google.streetview:cbll=" + String.valueOf(latlng.latitude) + "," + String.valueOf(latlng.longitude));
                        Intent intent2 = new Intent(Intent.ACTION_VIEW, uri);
                        if (intent2.resolveActivity(getActivity().getPackageManager()) != null) {
                            getActivity().startActivity(intent2);
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.street_view_is_not_available), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Toast.makeText(getActivity(), getString(R.string.explain_how_to_use_street_view), Toast.LENGTH_LONG).show();
                DeviceHelper.sendGATrack(getActivity(),"Trip", "streetview", ViewTripActivity.trip.dir.getName(), null);
            } else {
                PackageHelper.askForInstallApp(getActivity(), PackageHelper.StreetViewPackageNmae, getString(R.string.street_view));
            }
        } else if (v.equals(viewNote)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.Note));
            final String noteStr = ViewTripActivity.trip.note;
            TextView textView = new TextView(getActivity());
            textView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
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

                            ViewTripActivity.trip.updateNote(editText.getText().toString());
                        }
                    });
                    ab2.setNegativeButton(getString(R.string.cancel), null);
                    ab2.show();
                }
            });
            ab.show();
            DeviceHelper.sendGATrack(getActivity(),"Trip", "view_note", ViewTripActivity.trip.dir.getName(), null);
        }
    }

    class TripPlayer implements Runnable, OnSeekBarChangeListener {
        int index;
        int playMode;
        boolean run;
        int pointSize;
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
        SparseArray<String> markersMap;
        DecimalFormat speedFormat;
        float[] distances;

        public TripPlayer() {
            index = 0;
            pointSize = getPointSize();
            playMode = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("playingtripmode", "0"));
            interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("playtripspeed", "10"));
            if (cache != null && lat != null && cache.times != null) {
                latlength = Math.min(lat.length, Math.min(cache.times.length, cache.altitudes.length));
            }
            if (latlength > 0)
                processSeekBar.setMax(latlength - 1);
            markersMap = getMarkersMap();
            speedFormat = new DecimalFormat("#.##");
            processSeekBar.setOnSeekBarChangeListener(this);
            mapLayout = (RelativeLayout) rootView.findViewById(R.id.maplayout);
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
                String musicpath = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("musicpath", "");
                if (!musicpath.equals("")) {
                    File file = new File(getActivity().getFilesDir(), musicpath);
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
            fastforward.setVisibility(View.VISIBLE);
            slowforward.setVisibility(View.VISIBLE);
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
            fastforward.setVisibility(View.GONE);
            slowforward.setVisibility(View.GONE);
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
                        intent.putExtra("path", path + "/" + name + "/" + markersMap.get(i));
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
                if (gmap != null && mapFragment.getMap() != null && lat[index] != null)
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

        private int getPointSize() {
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = true;
            BitmapFactory.decodeResource(getResources(), R.drawable.runpoint, option);
            return option.outWidth / 2;
        }


    }

    private SparseArray<String> getMarkersMap() {
        SparseArray<String> markersMap = new SparseArray<String>();
        POI[] pois = ViewTripActivity.trip.pois.clone();
        String timezone = ViewTripActivity.trip.timezone;
        if (cache == null)
            return markersMap;
        if (cache.times == null || timezone == null)
            return markersMap;
        for (int i = 0; i < pois.length; i++) {
            int start = 0, end = cache.times.length - 1, middle;
            MyCalendar poiTime = pois[i].time;
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
            markersMap.put(start, pois[i].title);
        }
        return markersMap;
    }

    class GetAccessTokenTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                token = GoogleAuthUtil.getToken(getActivity(), account, "oauth2:https://www.googleapis.com/auth/userinfo.email");
                Intent intent = new Intent(getActivity(), SendTripService.class);
                intent.putExtra(SendTripService.filePathTag, ViewTripActivity.trip.dir.getPath());
                intent.putExtra(SendTripService.accountTag, account);
                intent.putExtra(SendTripService.tokenTag, token);
                intent.putExtra(SendTripService.publicTag, uploadPublic);
                getActivity().startService(intent);
                DeviceHelper.sendGATrack(getActivity(),"Trip", "share_track_by_tripdiary", ViewTripActivity.trip.dir.getName(), null);
            } catch (UserRecoverableAuthException e) {
                e.printStackTrace();
                startActivityForResult(e.getIntent(), REQUEST_GET_TOKEN);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (GoogleAuthException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    public static Bitmap gmapBitmap;
    public static Point[] trackPoints;
    public String videoBackgroundMusic;
    public Button backgroundMusicButton;

    private void generateVideo() {
        DeviceHelper.sendGATrack(getActivity(), "Trip", "generate_video", ViewTripActivity.trip.dir.getName(), null);
        File tempDir = new File(getActivity().getCacheDir(), GenerateVideoService.cacheDirName);
        FileHelper.deletedir(tempDir.getPath());
        tempDir.mkdirs();
        videoBackgroundMusic = null;
        trackPoints = null;
        gmapBitmap = null;
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ab.setTitle(R.string.generate_video);
        View layout = getActivity().getLayoutInflater().inflate(R.layout.dialog_generate_video, null);
        ab.setView(layout);
        final EditText videoName = (EditText) layout.findViewById(R.id.videoName);
        videoName.setText(ViewTripActivity.trip.tripName);
        final Spinner resolution = (Spinner) layout.findViewById(R.id.resolution);
        resolution.setSelection(0);
        final Spinner fps = (Spinner) layout.findViewById(R.id.fps);
        fps.setSelection(2);
        final Button backgroundMusic = (Button) layout.findViewById(R.id.backgroundMusic);
        this.backgroundMusicButton = backgroundMusic;
        backgroundMusic.setOnClickListener(new OnClickListener() {
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
                final int secondsPerTrack = GenerateVideoService.secondsPerTrack;
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
                        googleMap.addPolyline(new PolylineOptions().add(lat).color(trackColor).width(5));
                        for (int i = 0; i < ViewTripActivity.trip.pois.length; i++) {
                            POI poi = ViewTripActivity.trip.pois[i];
                            LatLng position = new LatLng(poi.latitude, poi.longitude);
                            googleMap.addMarker(new MarkerOptions().position(position));
                        }
                        LatLngBounds.Builder latlngBoundesBuilder = new LatLngBounds.Builder();
                        for (int i = 0; i < lat.length; i++) {
                            latlngBoundesBuilder.include(lat[i]);
                        }
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(latlngBoundesBuilder.build(), 0), new GoogleMap.CancelableCallback() {
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
                                                        SparseArray<String> markersMap = getMarkersMap();
                                                        Projection projection = googleMap.getProjection();
                                                        int[] markersIndexes = new int[markersMap.size()];
                                                        for (int i = 0; i < markersIndexes.length; i++) {
                                                            markersIndexes[i] = markersMap.keyAt(i);
                                                        }
                                                        Arrays.sort(markersIndexes);
                                                        int num_tracks = markersMap.size() + 1;
                                                        int frames_per_track = secondsPerTrack * fpsValue;
                                                        Point[] trackPoints = new Point[num_tracks * frames_per_track];
                                                        for (int i = 0; i < num_tracks; i++) {
                                                            int start, end, length;
                                                            if (i == 0) {
                                                                start = 0;
                                                            } else {
                                                                start = markersIndexes[i - 1] + 1;
                                                            }
                                                            if (i == num_tracks - 1) {
                                                                end = lat.length - 1;
                                                            } else {
                                                                end = markersIndexes[i] - 1;
                                                            }
                                                            length = end - start;
                                                            for (int j = 0; j < frames_per_track; j++) {
                                                                int indexInLat = start + length * j / frames_per_track;
                                                                trackPoints[i * frames_per_track + j] = projection.toScreenLocation(lat[indexInLat]);
                                                            }
                                                        }
                                                        ViewMapFragment.this.gmapBitmap = bitmap;
                                                        ViewMapFragment.this.trackPoints = trackPoints;
                                                        alertDialog.cancel();
                                                        Intent intent = new Intent(getActivity(), GenerateVideoService.class);
                                                        intent.putExtra(GenerateVideoService.tag_background_music_path, videoBackgroundMusic);
                                                        intent.putExtra(GenerateVideoService.tag_video_width, videoWidth);
                                                        intent.putExtra(GenerateVideoService.tag_video_height, videoHeight);
                                                        intent.putExtra(GenerateVideoService.tag_videoname, videoNameStr);
                                                        intent.putExtra(GenerateVideoService.tag_fps, fpsValue);
                                                        intent.putExtra(GenerateVideoService.tag_tripName, ViewTripActivity.trip.tripName);
                                                        intent.putExtra(GenerateVideoService.tag_timeZone, ViewTripActivity.trip.timezone);
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

        if (setLocusTask != null && !setLocusTask.isCancelled()) {
            setLocusTask.stop();
        }
        if (tripPlayer != null) {
            tripPlayer.stop();
        }
        super.onDestroy();
    }

}
