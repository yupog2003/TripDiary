package com.yupog2003.tripdiary.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.provider.DocumentFile;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
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

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
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
import com.yupog2003.tripdiary.PlayPointActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewGraphAcivity;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.ColorHelper;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.FileHelper.DirAdapter;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.MyLatLng2;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.PackageHelper;
import com.yupog2003.tripdiary.data.TrackCache;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.services.GenerateVideoService;
import com.yupog2003.tripdiary.services.SendTripService;
import com.yupog2003.tripdiary.views.POIInfoWindowAdapter;
import com.yupog2003.tripdiary.views.SpinnerActionProvider;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Set;
import java.util.TimeZone;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ViewMapFragment extends Fragment implements OnInfoWindowClickListener, OnMapLongClickListener, OnMarkerDragListener, OnClickListener {
    SupportMapFragment mapFragment;
    GoogleMap gmap;
    ArrayList<Marker> markers;
    LatLng[] lat;
    TrackCache cache;
    ImageButton viewInformation;
    ImageButton switchMapMode;
    ImageButton playTrip;
    ImageButton stopTrip;
    ImageButton fastforward;
    ImageButton slowforward;
    ImageButton viewGraph;
    ImageButton streetView;
    ImageButton viewNote;
    SeekBar processSeekBar;
    LinearLayout playPanel;
    TextView altitude;
    TextView distance;
    TextView speed;
    TextView time;
    boolean uploadPublic;
    SearchView search;
    SpinnerActionProvider spinnerActionProvider;
    TripPlayer tripPlayer;
    Handler handler;
    int trackColor;
    LinearLayout buttonBar;
    String token;
    String account;
    POIInfoWindowAdapter poiInfoWindowAdapter;
    int rotation;
    Trip trip;

    private static final int REQUEST_GET_TOKEN = 2;
    private static final int REQUEST_BACKGROUND_MUSIC = 3;
    // private static final int playtriptype_normal=0;
    private static final int playtriptype_skyview = 1;

    ViewGroup rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_view_map, container, false);
        rotation = DeviceHelper.getRotation(getActivity());
        buttonBar = (LinearLayout) rootView.findViewById(R.id.buttonbar);
        trackColor = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("trackcolor", 0xff6699cc);
        markers = new ArrayList<>();
        handler = new Handler();
        viewInformation = (ImageButton) rootView.findViewById(R.id.viewinformation);
        switchMapMode = (ImageButton) rootView.findViewById(R.id.switchmapmode);
        playTrip = (ImageButton) rootView.findViewById(R.id.playtrip);
        stopTrip = (ImageButton) rootView.findViewById(R.id.stoptrip);
        fastforward = (ImageButton) rootView.findViewById(R.id.fastforward);
        slowforward = (ImageButton) rootView.findViewById(R.id.slowforward);
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
        viewGraph.setOnClickListener(this);
        streetView.setOnClickListener(this);
        viewNote.setOnClickListener(this);
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

    public void refresh(final LatLng[] lats, final LatLngBounds bounds, final ProgressDialog pd) {
        //pd.dismiss();
        mapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(GoogleMap googleMap) {
                trip = ((ViewTripActivity) getActivity()).trip;
                gmap = googleMap;
                gmap.setMyLocationEnabled(true);
                gmap.getUiSettings().setZoomControlsEnabled(true);
                gmap.setOnInfoWindowClickListener(ViewMapFragment.this);
                gmap.setOnMapLongClickListener(ViewMapFragment.this);
                gmap.setOnMarkerDragListener(ViewMapFragment.this);
                poiInfoWindowAdapter = new POIInfoWindowAdapter(getActivity(), trip.pois, null);
                gmap.setInfoWindowAdapter(poiInfoWindowAdapter);
                lat = lats;
                cache = trip.cache;
                if (lat != null && bounds != null) {
                    gmap.addPolyline(new PolylineOptions().add(lat).width(5).color(trackColor));
                    gmap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, (int) DeviceHelper.pxFromDp(getActivity(), 10)));
                }
                viewInformation.setOnClickListener(ViewMapFragment.this);
                playTrip.setOnClickListener(ViewMapFragment.this);
                setPOIs();
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
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
                    final ArrayList<Marker> founds = new ArrayList<>();
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
            if (trip != null) {
                trip.deleteCache();
                Toast.makeText(getActivity(), getString(R.string.cache_has_been_cleared), Toast.LENGTH_SHORT).show();
                DeviceHelper.sendGATrack(getActivity(), "Trip", "clear_cache", trip.tripName, null);
            }
        } else if (item.getItemId() == R.id.sharetrackby) {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.share_track_by___));
            String[] bys = new String[]{getString(R.string.gpx), getString(R.string.kml) + "(Google Earth)", getString(R.string.kmz_with_explanation), getString(R.string.app_name)};
            ab.setSingleChoiceItems(bys, -1, new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    switch (which) {
                        case 0:
                            AlertDialog.Builder ab0 = new AlertDialog.Builder(getActivity());
                            ab0.setTitle(getString(R.string.share_track_gpx));
                            final EditText filepath0 = new EditText(getActivity());
                            filepath0.setText(Environment.getExternalStorageDirectory() + "/" + trip.tripName + ".gpx");
                            ab0.setView(filepath0);
                            ab0.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    try {
                                        File outputFile = new File(filepath0.getText().toString());
                                        BufferedReader br = new BufferedReader(new InputStreamReader(getActivity().getContentResolver().openInputStream(trip.gpxFile.getUri())));
                                        String s;
                                        BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile));
                                        while ((s = br.readLine()) != null) {
                                            bw.write(s);
                                            bw.write("\n");
                                            if (s.contains("<gpx")) {
                                                POI[] pois = trip.pois;
                                                for (POI poi : pois) {
                                                    bw.write(" <wpt lat=\"" + String.valueOf(poi.latitude) + "\" lon=\"" + String.valueOf(poi.longitude) + "\">\n");
                                                    bw.write("  <ele>" + String.valueOf(poi.altitude) + "</ele>\n");
                                                    bw.write("  <name>" + poi.title + "</name>\n");
                                                    bw.write("  <time>" + poi.time.format3339() + "</time>\n");
                                                    bw.write(" </wpt>\n");
                                                }
                                            }
                                        }
                                        br.close();
                                        bw.flush();
                                        bw.close();
                                        Intent intent = new Intent(Intent.ACTION_SEND);
                                        intent.setType("application/gpx+xml");
                                        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(outputFile));
                                        getActivity().startActivity(intent);
                                        DeviceHelper.sendGATrack(getActivity(), "Trip", "share_track_by_gpx", trip.tripName, null);
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                            ab0.setNegativeButton(getString(R.string.cancel), null);
                            ab0.show();
                            break;
                        case 1:
                            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                            ab.setTitle(getString(R.string.share_track_kml));
                            final EditText filepath = new EditText(getActivity());
                            filepath.setText(Environment.getExternalStorageDirectory() + "/" + trip.tripName + ".kml");
                            ab.setView(filepath);
                            ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    File kmlFile = new File(filepath.getText().toString());
                                    FileHelper.convertToKml(markers, cache, DocumentFile.fromFile(kmlFile), trip.note);
                                    Intent intent = new Intent(Intent.ACTION_SEND);
                                    intent.setType("application/vnd.google-earth.kml+xml");
                                    intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(kmlFile));
                                    getActivity().startActivity(intent);
                                    DeviceHelper.sendGATrack(getActivity(), "Trip", "share_track_by_kml", trip.tripName, null);
                                }
                            });
                            ab.setNegativeButton(getString(R.string.cancel), null);
                            ab.show();
                            break;
                        case 2:
                            AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity());
                            ab2.setTitle(getString(R.string.share_track_kmz));
                            final EditText filepath2 = new EditText(getActivity());
                            filepath2.setText(Environment.getExternalStorageDirectory() + "/" + trip.tripName + ".kmz");
                            ab2.setView(filepath2);
                            ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {

                                    File kmzFile = new File(filepath2.getText().toString());
                                    new GenerateKMZTask(kmzFile).execute();
                                }
                            });
                            ab2.setNegativeButton(getString(R.string.cancel), null);
                            ab2.show();
                            break;
                        case 3:
                            Account[] accounts = AccountManager.get(getActivity()).getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                            if (accounts != null && accounts.length > 0) {
                                account = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("account", accounts[0].name);
                                AlertDialog.Builder ab3 = new AlertDialog.Builder(getActivity());
                                ab3.setTitle(getString(R.string.make_it_public));
                                ab3.setMessage(getString(R.string.make_it_public_to_share));
                                ab3.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        uploadPublic = true;
                                        new GetAccessTokenTask().execute();
                                    }
                                });
                                ab3.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

                                    public void onClick(DialogInterface dialog, int which) {

                                        uploadPublic = false;
                                        new GetAccessTokenTask().execute();
                                    }
                                });
                                ab3.show();
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

                            DeviceHelper.sendGATrack(getActivity(), "Trip", "import_memory", trip.tripName, null);
                            new ImportMemoryTask(adapter.getRoot()).execute();
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

        File memory;
        TextView message;
        ProgressBar progress;
        TextView progressMessage;
        AlertDialog dialog;
        boolean cancel = false;

        public ImportMemoryTask(File memory) {
            this.memory = memory;
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
            if (trip.pois.length < 1 || cache.times == null)
                return null;
            HashMap<Long, POI> pois = new HashMap<>();
            for (POI poi : trip.pois) {
                MyCalendar time = poi.time;
                time.setTimeZone(trip.timezone);
                pois.put(time.getTimeInMillis(), poi);
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
                start = MyCalendar.getTime(trip.timezone, cache.times[0], MyCalendar.type_self).getTimeInMillis();
                end = MyCalendar.getTime(trip.timezone, cache.times[cache.times.length - 1], MyCalendar.type_self).getTimeInMillis();
            }
            HashMap<Long, File> mems = new HashMap<>();
            for (File memory : memories) {
                long timeMills = getFileTime(memory);
                if (timeMills >= start && timeMills <= end) {
                    mems.put(timeMills, memory);
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
                if (!FileHelper.isMemory(infile))
                    continue;
                for (int j = key; j < poisMiddleTime.length; j++) {
                    if (memTimes[i] <= poisMiddleTime[j]) {
                        pois.get(poiTimes[j]).importMemories(null, infile);
                        key = j;
                        coppied = true;
                        break;
                    }
                }
                if (!coppied) {
                    pois.get(poiTimes[poiTimes.length - 1]).importMemories(null, infile);
                }
            }
            return null;
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
        }
    }

    public void setPOIs() {
        if (markers == null || gmap == null || trip == null || trip.pois == null || !isAdded())
            return;
        for (int i = 0; i < markers.size(); i++) {
            markers.get(i).remove();
        }
        markers.clear();
        float hue = ColorHelper.getMarkerColorHue(getActivity());
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
        ArrayList<String> markerNames = new ArrayList<>();
        markerNames.add(getString(R.string.select_poi));
        for (int i = 0; i < markers.size(); i++) {
            markerNames.add(markers.get(i).getTitle());
        }
        if (poiInfoWindowAdapter != null) {
            poiInfoWindowAdapter.setPOIs(trip.pois);
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
        setHasOptionsMenu(true);
    }

    public void onInfoWindowClick(Marker marker) {
        String pointtitle = marker.getTitle();
        viewPOI(pointtitle);
    }

    private void viewPOI(String poiName) {
        if (FileHelper.findfile(trip.dir, poiName) != null) {
            Intent intent = new Intent(getActivity(), ViewPointActivity.class);
            intent.putExtra(ViewPointActivity.tag_tripname, trip.tripName);
            intent.putExtra(ViewPointActivity.tag_poiname, poiName);
            intent.putExtra(ViewPointActivity.tag_fromActivity, ViewTripActivity.class.getSimpleName());
            getActivity().startActivityForResult(intent, ViewTripActivity.REQUEST_VIEW_POI);
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
                time.set(editdate.getYear(), editdate.getMonth(), editdate.getDayOfMonth(), edittime.getCurrentHour(), edittime.getCurrentMinute(), 0);
                time.format3339();
                time.setTimeZone("UTC");
                if (title.equals("")) {
                    Toast.makeText(getActivity(), getString(R.string.input_the_poi_title), Toast.LENGTH_LONG).show();
                } else {
                    if (FileHelper.findfile(trip.dir, title) != null) {
                        Toast.makeText(getActivity(), getString(R.string.there_is_a_same_poi), Toast.LENGTH_LONG).show();
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
        POI poi = new POI(getActivity(), trip.dir.createDirectory(poiName));
        poi.updateBasicInformation(null, time, latlng.latitude, latlng.longitude, altitude);
        if (getActivity() != null && getActivity() instanceof ViewTripActivity) {
            ((ViewTripActivity) getActivity()).onPOIUpdate(null);
        }
    }

    private MyLatLng2 getLatLngInTrack(LatLng latlng) {
        MyLatLng2 result = new MyLatLng2(latlng.latitude, latlng.longitude, 0, MyCalendar.getInstance().formatInCurrentTimezone());
        if (cache == null || lat == null)
            return result;
        if (cache.latitudes == null || cache.longitudes == null)
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
        if (resultIndex < cache.latitudes.length && resultIndex < cache.longitudes.length && resultIndex < cache.altitudes.length && resultIndex < cache.times.length)
            result = new MyLatLng2(cache.latitudes[resultIndex], cache.longitudes[resultIndex], cache.altitudes[resultIndex], cache.times[resultIndex]);
        return result;
    }

    public void onMarkerDrag(Marker marker) {


    }

    public void onMarkerDragEnd(Marker marker) {
        POI poi = trip.getPOI(marker.getTitle());
        poi.updateBasicInformation(null, null, marker.getPosition().latitude, marker.getPosition().longitude, null);
        if (getActivity() != null && getActivity() instanceof ViewTripActivity) {
            ((ViewTripActivity) getActivity()).onPOIUpdate(poi.title);
        }
    }

    public void onMarkerDragStart(Marker marker) {


    }

    @TargetApi(19)
    public void onClick(View v) {

        if (v == null)
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
                DeviceHelper.sendGATrack(getActivity(), "Trip", "play", trip.tripName, null);
            }
            tripPlayer.play();
        } else if (v.equals(stopTrip)) {
            if (tripPlayer != null) {
                tripPlayer.stop();
            }
        } else if (v.equals(fastforward)) {
            if (tripPlayer != null) {
                tripPlayer.changeForward(TripPlayer.fast);
            }
        } else if (v.equals(slowforward)) {
            if (tripPlayer != null) {
                tripPlayer.changeForward(TripPlayer.slow);
            }
        } else if (v.equals(viewGraph)) {
            Intent intent = new Intent(getActivity(), ViewGraphAcivity.class);
            startActivity(intent);
            DeviceHelper.sendGATrack(getActivity(), "Trip", "view_graph", trip.tripName, null);
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
                DeviceHelper.sendGATrack(getActivity(), "Trip", "streetview", trip.tripName, null);
            } else {
                PackageHelper.askForInstallApp(getActivity(), PackageHelper.StreetViewPackageNmae, getString(R.string.street_view));
            }
        } else if (v.equals(viewNote)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.Note));
            final String noteStr = trip.note;
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
                if (!"".equals(musicpath)) {
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
                        intent.putExtra(PlayPointActivity.tag_trip, trip.tripName);
                        intent.putExtra(PlayPointActivity.tag_poi, markersMap.get(i));
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
        SparseArray<String> markersMap = new SparseArray<>();
        if (trip == null || trip.pois == null || cache == null || cache.times == null || trip.timezone == null) {
            return markersMap;
        }
        POI[] pois = trip.pois.clone();
        String timezone = trip.timezone;
        for (POI poi : pois) {
            int start = 0, end = cache.times.length - 1, middle;
            MyCalendar poiTime = poi.time;
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
            markersMap.put(start, poi.title);
        }
        return markersMap;
    }

    class GetAccessTokenTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                token = GoogleAuthUtil.getToken(getActivity(), account, "oauth2:https://www.googleapis.com/auth/userinfo.email");
                Intent intent = new Intent(getActivity(), SendTripService.class);
                intent.putExtra(SendTripService.tripNameTag, trip.tripName);
                intent.putExtra(SendTripService.accountTag, account);
                intent.putExtra(SendTripService.tokenTag, token);
                intent.putExtra(SendTripService.publicTag, uploadPublic);
                getActivity().startService(intent);
                DeviceHelper.sendGATrack(getActivity(), "Trip", "share_track_by_tripdiary", trip.tripName, null);
            } catch (UserRecoverableAuthException e) {
                e.printStackTrace();
                startActivityForResult(e.getIntent(), REQUEST_GET_TOKEN);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    class GenerateKMZTask extends AsyncTask<String, Object, String> {

        File kmzFile;
        ProgressDialog pd;

        public GenerateKMZTask(File kmzFile) {
            this.kmzFile = kmzFile;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getActivity());
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(kmzFile));
                int lineColor = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("trackcolor", 0xff6699cc);
                String lineColorInKML = String.format("%02x%02x%02x%02x", Color.alpha(lineColor), Color.blue(lineColor), Color.green(lineColor), Color.red(lineColor));
                int iconColor = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("markercolor", 0xffff0000);
                String iconColorInKML = String.format("%02x%02x%02x%02x", Color.alpha(iconColor), Color.blue(iconColor), Color.green(iconColor), Color.red(iconColor));
                File tempDir = new File(getActivity().getCacheDir(), "kmzcache");
                tempDir.mkdirs();
                File tempKML = new File(tempDir, "doc.kml");
                BufferedWriter bw = new BufferedWriter(new FileWriter(tempKML));
                bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
                bw.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
                bw.write("<Document>\n");
                bw.write("<name>" + trip.tripName + "</name>\n");
                bw.write("<description>" + trip.note + "</description>\n");
                bw.write("<Style id=\"lineColor\">\n");
                bw.write("<LineStyle>\n");
                bw.write("<color>" + lineColorInKML + "</color>\n");
                bw.write("<width>4</width>\n");
                bw.write("</LineStyle>\n");
                bw.write("</Style>\n");
                bw.write("<Style id=\"iconColor\">\n");
                bw.write("<IconStyle>\n");
                bw.write("<color>" + iconColorInKML + "</color>\n");
                bw.write("</IconStyle>\n");
                bw.write("</Style>\n");
                bw.write("<Folder>\n");
                bw.write("<Placemark>\n");
                bw.write("<name>" + trip.tripName + "</name>\n");
                bw.write("<description>Generated by TripDiary</description>\n");
                bw.write("<styleUrl>#lineColor</styleUrl>\n");
                bw.write("<LineString>\n");
                bw.write("<coordinates>");
                int length = Math.min(cache.latitudes.length, cache.longitudes.length);
                length = Math.min(length, cache.altitudes.length);
                publishProgress(0, length);
                double maxLatitude = -Double.MAX_VALUE;
                double minLatitude = Double.MAX_VALUE;
                double maxLongitude = -Double.MAX_VALUE;
                double minLongitude = Double.MAX_VALUE;
                for (int i = 0; i < length; i++) {
                    double longitude = cache.longitudes[i];
                    double latitude = cache.latitudes[i];
                    if (maxLatitude < latitude) {
                        maxLatitude = latitude;
                    }
                    if (minLatitude > latitude) {
                        minLatitude = latitude;
                    }
                    if (maxLongitude < longitude) {
                        maxLongitude = longitude;
                    }
                    if (minLongitude > longitude) {
                        minLongitude = longitude;
                    }
                    bw.write(String.format(" %f,%f,%f\n", longitude, latitude, cache.altitudes[i]));
                    publishProgress(i, length);
                }
                bw.write("</coordinates>\n");
                bw.write("</LineString>\n");
                bw.write("</Placemark>\n");
                final int POIsSize = trip.pois.length;
                final String timeZone = trip.timezone;
                final int targetPicWidth = 640;
                for (int i = 0; i < POIsSize; i++) {
                    POI poi = trip.pois[i];
                    int picSize = poi.picFiles.length;
                    StringBuilder descriptionBuilder = new StringBuilder();
                    descriptionBuilder.append(poi.time.formatInTimezone(timeZone)).append("<br/>").append(poi.diary).append("<br/>");
                    for (int j = 0; j < picSize; j++) {
                        InputStream is = getActivity().getContentResolver().openInputStream(poi.picFiles[j].getUri());
                        BitmapFactory.Options op = new BitmapFactory.Options();
                        op.inJustDecodeBounds = true;
                        BitmapFactory.decodeStream(is, new Rect(0, 0, 0, 0), op);
                        op.inSampleSize = (int) Math.ceil(op.outWidth / targetPicWidth);
                        op.inJustDecodeBounds = false;
                        is = getActivity().getContentResolver().openInputStream(poi.picFiles[j].getUri());
                        Bitmap bitmap = BitmapFactory.decodeStream(is, new Rect(0, 0, 0, 0), op);
                        String fileName = FileHelper.getFileName(poi.picFiles[j]);
                        zos.putNextEntry(new ZipEntry(fileName));
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, zos);
                        bitmap.recycle();
                        zos.closeEntry();
                        descriptionBuilder.append("<img src=\"").append(fileName).append("\" width=\"").append(String.valueOf(targetPicWidth)).append("\" /><br/>");
                        publishProgress(i * 100 + 100 * j / picSize, POIsSize * 100);
                    }
                    bw.write("<Placemark>\n");
                    bw.write("<name>" + poi.title + "</name>\n");
                    bw.write("<styleUrl>#iconColor</styleUrl>\n");
                    bw.write("<description>" + descriptionBuilder.toString() + "</description>\n");
                    bw.write("<Point>\n");
                    bw.write("<coordinates>" + String.valueOf(poi.longitude) + "," + String.valueOf(poi.latitude) + ",0</coordinates>\n");
                    bw.write("</Point>\n");
                    bw.write("</Placemark>\n");
                }
                bw.write("</Folder>\n");
                bw.write("<LookAt>\n");
                bw.write("<longitude>" + String.valueOf((maxLongitude + minLongitude) / 2) + "</longitude>\n");
                bw.write("<latitude>" + String.valueOf((maxLatitude + minLatitude) / 2) + "</latitude>\n");
                bw.write("<altitude>0</altitude>\n");
                bw.write("<heading>0</heading>\n");
                bw.write("<tilt>45</tilt>\n");
                bw.write("<range>20000</range>\n");
                bw.write("<altitudeMode>clampToGround</altitudeMode>\n");
                bw.write("</LookAt>\n");
                bw.write("</Document>\n");
                bw.write("</kml>");
                bw.flush();
                bw.close();
                writeEntry(tempKML.getName(), tempKML, zos);
                zos.close();
                FileHelper.deletedir(tempDir.getPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        private void writeEntry(String entryStr, File file, ZipOutputStream zos) {
            try {
                zos.putNextEntry(new ZipEntry(entryStr));
                byte[] buffer = new byte[4096];
                int count;
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
                while ((count = bis.read(buffer, 0, 4096)) != -1) {
                    zos.write(buffer, 0, count);
                }
                zos.flush();
                bis.close();
                zos.closeEntry();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            pd.setProgress((int) values[0]);
            pd.setMax((int) values[1]);
        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/vnd.google-earth.kmz");
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(kmzFile));
            startActivity(intent);
            DeviceHelper.sendGATrack(getActivity(), "Trip", "share_track_by_kmz", trip.tripName, null);
        }

    }

    public static Bitmap gmapBitmap;
    public static Point[] trackPoints;
    public String videoBackgroundMusic;
    public Button backgroundMusicButton;

    private void generateVideo() {
        DeviceHelper.sendGATrack(getActivity(), "Trip", "generate_video", trip.tripName, null);
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
        videoName.setText(trip.tripName);
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
                        float hue = ColorHelper.getMarkerColorHue(getActivity());
                        BitmapDescriptor bd = BitmapDescriptorFactory.defaultMarker(hue);
                        for (int i = 0; i < trip.pois.length; i++) {
                            POI poi = trip.pois[i];
                            LatLng position = new LatLng(poi.latitude, poi.longitude);
                            googleMap.addMarker(new MarkerOptions().position(position).icon(bd));
                        }
                        LatLngBounds.Builder latlngBoundesBuilder = new LatLngBounds.Builder();
                        for (LatLng latlng : lat) {
                            latlngBoundesBuilder.include(latlng);
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
        lat = null;
        cache = null;
        trip = null;
        if (markers != null) {
            for (Marker marker : markers) {
                marker.remove();
            }
            markers.clear();
        }
        if (gmap != null) {
            gmap.clear();
            gmap.setInfoWindowAdapter(null);
            gmap.setOnInfoWindowClickListener(null);
            gmap.setOnMapLongClickListener(null);
            gmap.setOnMarkerDragListener(null);
            gmap = null;
        }
        if (poiInfoWindowAdapter != null) {
            poiInfoWindowAdapter.destroy();
        }
        System.gc();
        super.onDestroy();
    }

}
