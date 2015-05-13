package com.yupog2003.tripdiary;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.yupog2003.tripdiary.data.ColorHelper;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyImageDecoder;
import com.yupog2003.tripdiary.data.TimeAnalyzer;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.services.RecordService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.Set;

public class MainActivity extends MyActivity implements Button.OnClickListener {

    Button startTrip;
    Button resumeTrip;
    Button viewHistory;
    Button allRecord;
    public static String rootPath;
    public static int distance_unit;
    public static final int unit_km = 0;
    public static final int unit_mile = 1;
    public static int altitude_unit;
    public static final int unit_m = 0;
    public static final int unit_ft = 1;
    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String serverURL = "http://219.85.61.62/TripDiary";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
        }
        rootPath = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("rootpath", Environment.getExternalStorageDirectory() + "/TripDiary");
        creatRootDir(rootPath);
        SharedPreferences.Editor editor = getSharedPreferences("category", MODE_PRIVATE).edit();
        editor.putString(getString(R.string.nocategory), String.valueOf(Color.WHITE));
        editor.commit();
        maintainNoCategory();
        distance_unit = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("distance_unit", "0"));
        altitude_unit = Integer.valueOf(PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("altitude_unit", "0"));
        startTrip = (Button) findViewById(R.id.starttrip);
        viewHistory = (Button) findViewById(R.id.viewhistory);
        resumeTrip = (Button) findViewById(R.id.resume_trip);
        allRecord = (Button) findViewById(R.id.all_record);
        if (checkPlayService()) {
            startTrip.setOnClickListener(this);
            viewHistory.setOnClickListener(this);
            resumeTrip.setOnClickListener(this);
            allRecord.setOnClickListener(this);
        }
        ImageDecoder myImageDecoder = new MyImageDecoder(getApplicationContext(), new BaseImageDecoder(false));
        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(MainActivity.this).imageDecoder(myImageDecoder).build();
        ImageLoader.getInstance().init(conf);

    }

    private void maintainNoCategory() {
        File[] trips = new File(rootPath).listFiles(FileHelper.getDirFilter());
        SharedPreferences tripsp = getSharedPreferences("trip", Context.MODE_PRIVATE);
        for (int i = 0; i < trips.length; i++) {
            if (tripsp.getString(trips[i].getName(), "nocategory").equals("nocategory")) {
                tripsp.edit().putString(trips[i].getName(), getString(R.string.nocategory)).commit();
            }
        }
        getSharedPreferences("category", Context.MODE_PRIVATE).edit().remove("nocategory").commit();
    }

    public static void creatRootDir(String rootPath) {
        File file = new File(rootPath);
        if (!file.exists()) {
            file.mkdirs();
        } else if (file.isFile()) {
            file.delete();
            file.mkdirs();
        }
        File nomedia = new File(rootPath + "/.nomedia");
        try {
            nomedia.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (isGpsEnabled()) {
            if (requestCode == 0)
                startTripDialog();
            else if (requestCode == 1)
                resumeTripDialog();
        }
    }

    private boolean checkPlayService() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(MainActivity.this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, MainActivity.this, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(MainActivity.this, "This device is not supported by Google Play Serivce", Toast.LENGTH_SHORT).show();
                MainActivity.this.finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {

        super.onResume();
        checkPlayService();
    }

    private boolean isGpsEnabled() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    boolean tripNameClicked;

    @SuppressWarnings("unchecked")
    private void startTripDialog() {
        tripNameClicked = false;
        final SharedPreferences categorysp = getSharedPreferences("category", MODE_PRIVATE);
        Map<String, String> map = (Map<String, String>) categorysp.getAll();
        Set<String> keyset = map.keySet();
        final String[] categories = keyset.toArray(new String[keyset.size()]);
        AlertDialog.Builder ab2 = new AlertDialog.Builder(MainActivity.this);
        View layout = getLayoutInflater().inflate(R.layout.edit_trip, null);
        final RadioGroup rg = (RadioGroup) layout.findViewById(R.id.categories);
        final TextView category = (TextView) layout.findViewById(R.id.category);
        for (int i = 0; i < categories.length; i++) {
            RadioButton rb = new RadioButton(MainActivity.this);
            rb.setText(categories[i]);
            rb.setId(i);
            rg.addView(rb);
            if (categories[i].equals(getString(R.string.nocategory))) {
                rg.check(i);
                String color = categorysp.getString(categories[i], String.valueOf(Color.WHITE));
                category.setCompoundDrawablesWithIntrinsicBounds(ColorHelper.getColorDrawable(MainActivity.this, 50, Integer.valueOf(color)), null, null, null);
            }
        }
        rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            public void onCheckedChanged(RadioGroup group, int checkedId) {

                String color = categorysp.getString(categories[checkedId], "Gray");
                category.setCompoundDrawablesWithIntrinsicBounds(ColorHelper.getColorDrawable(MainActivity.this, 50, Integer.valueOf(color)), null, null, null);
            }
        });
        ab2.setTitle(getString(R.string.Start_Trip));
        ab2.setView(layout);
        final EditText tripName = (EditText) layout.findViewById(R.id.tripname);
        final EditText tripNote = (EditText) layout.findViewById(R.id.tripnote);
        Calendar c = Calendar.getInstance();
        tripName.setText(String.valueOf(c.get(Calendar.YEAR)) + "-" + String.valueOf(c.get(Calendar.MONTH) + 1) + "-" + String.valueOf(c.get(Calendar.DAY_OF_MONTH)));
        tripName.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (!tripNameClicked) {
                    tripName.setText("");
                }
                tripNameClicked = true;
            }
        });
        ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                final String name = tripName.getText().toString();
                final String note = tripNote.getText().toString();
                int radioGroupIndex = rg.getCheckedRadioButtonId();
                String temp;
                if (radioGroupIndex == -1 || radioGroupIndex >= categories.length) {
                    temp = "nocategory";
                } else {
                    temp = categories[rg.getCheckedRadioButtonId()];
                }
                final String category = temp;
                if (name.length() > 0) {
                    if (!new File(rootPath + "/" + name).exists()) {
                        startTrip(name, note, category);
                    } else {
                        AlertDialog.Builder ab2 = new AlertDialog.Builder(MainActivity.this);
                        ab2.setTitle(getString(R.string.same_trip));
                        ab2.setMessage(getString(R.string.explain_same_trip));
                        ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {

                                startTrip(name, note, category);
                            }
                        });
                        ab2.setNegativeButton(getString(R.string.cancel), null);
                        ab2.show();
                    }
                } else {
                    Toast.makeText(MainActivity.this, getString(R.string.input_the_trip_name), Toast.LENGTH_SHORT).show();
                }
            }
        });
        ab2.setNegativeButton(getString(R.string.cancel), null);
        AlertDialog ad = ab2.create();
        ad.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        ad.show();

    }

    private void startTrip(String name, String note, String category) {
        Trip trip = new Trip(MainActivity.this, new File(rootPath + "/" + name));
        trip.setCategory(MainActivity.this, category);
        trip.updateNote(note);
        if (isGpsEnabled()) {
            Intent i = new Intent(MainActivity.this, RecordService.class);
            i.putExtra("name", name);
            i.putExtra("path", rootPath);
            i.putExtra("note", note);
            startService(i);
        } else {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            NotificationCompat.Builder nb = new NotificationCompat.Builder(MainActivity.this);
            nb.setSmallIcon(R.drawable.ic_launcher);
            nb.setContentText(getString(R.string.click_or_swipe_down_to_view_detail));
            nb.setContentTitle(name);
            nb.setTicker(getString(R.string.Start_Trip));
            Intent i2 = new Intent(MainActivity.this, RecordActivity.class);
            i2.putExtra(RecordActivity.tag_tripname, name);
            i2.putExtra(RecordActivity.tag_rootpath, rootPath);
            i2.putExtra(RecordActivity.tag_isgpsenabled, false);
            i2.putExtra(RecordActivity.tag_addpoi_intent, true);
            PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, i2, PendingIntent.FLAG_UPDATE_CURRENT);
            nb.addAction(R.drawable.poi, getString(R.string.add_poi), pi);
            nb.setContentIntent(pi);
            Intent i3 = new Intent(MainActivity.this, ViewTripActivity.class);
            i3.putExtra("name", name);
            i3.putExtra("path", rootPath);
            i3.putExtra("stoptrip", true);
            PendingIntent pi2 = PendingIntent.getActivity(MainActivity.this, 0, i3, PendingIntent.FLAG_UPDATE_CURRENT);
            nb.addAction(R.drawable.ic_stop, getString(R.string.stop), pi2);
            nb.setOngoing(true);
            nm.notify(0, nb.build());
        }
        DeviceHelper.sendGATrack(MainActivity.this, "Trip", "start", name, null);
        MainActivity.this.finish();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(MainActivity.this, PreferActivity.class);
            intent.putExtra("path", rootPath);
            startActivity(intent);
        }
        return false;
    }

    public void resumeTripDialog() {
        if (rootPath == null) {
            rootPath = PreferenceManager.getDefaultSharedPreferences(MainActivity.this).getString("rootpath", Environment.getExternalStorageDirectory() + "/TripDiary");
            creatRootDir(rootPath);
        }
        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
        File[] files = new File(rootPath).listFiles(FileHelper.getDirFilter());
        if (files == null)
            files = new File[0];
        ArrayList<TripInformation> trips = new ArrayList<MainActivity.TripInformation>();
        for (int i = 0; i < files.length; i++) {
            TripInformation trip = new TripInformation();
            trip.file = files[i];
            trip.name = files[i].getName();
            trip.time = TimeAnalyzer.getTripTime(rootPath, trip.name);
            trips.add(trip);
        }
        Collections.sort(trips, new Comparator<TripInformation>() {

            public int compare(TripInformation lhs, TripInformation rhs) {

                if (lhs.time == null || rhs.time == null)
                    return 0;
                else if (lhs.time.after(rhs.time))
                    return -1;
                else if (rhs.time.after(lhs.time))
                    return 1;
                else
                    return 0;
            }
        });
        final String[] strs = new String[trips.size()];
        for (int i = 0; i < strs.length; i++) {
            strs[i] = trips.get(i).name;
        }
        ab.setSingleChoiceItems(strs, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                Intent i = new Intent(MainActivity.this, RecordService.class);
                i.putExtra("name", strs[which]);
                i.putExtra("path", rootPath);
                StringBuffer sb = new StringBuffer();
                try {
                    BufferedReader br = new BufferedReader(new FileReader(rootPath + "/" + strs[which] + "/note"));
                    String s;
                    while ((s = br.readLine()) != null) {
                        sb.append(s + "\n");
                    }
                    br.close();
                } catch (FileNotFoundException e) {

                    e.printStackTrace();
                } catch (IOException e) {

                    e.printStackTrace();
                }
                String result = sb.toString();
                if (result.endsWith("\n")) {
                    result = result.substring(0, result.length() - 1);
                }
                i.putExtra("note", result);
                startService(i);
                DeviceHelper.sendGATrack(MainActivity.this,"Trip", "resume", strs[which], null);
                MainActivity.this.finish();
            }
        });
        ab.setTitle(getString(R.string.resume_trip));
        ab.setNegativeButton(getString(R.string.cancel), null);
        ab.show();
    }

    public void onClick(View v) {

        if (v.equals(startTrip)) {
            if (isGpsEnabled()) {
                startTripDialog();
            } else {
                AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
                ab.setTitle(getString(R.string.gps_is_disabled));
                ab.setMessage(getString(R.string.explain_gpx));
                ab.setPositiveButton(getString(R.string.start_with_gps), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        Toast.makeText(getBaseContext(), getString(R.string.please_enable_the_gps_provider), Toast.LENGTH_SHORT).show();
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
                    }
                });
                ab.setNegativeButton(getString(R.string.start_without_gps), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        startTripDialog();
                    }
                });
                ab.show();
            }
        } else if (v.equals(viewHistory)) {
            startActivity(new Intent(MainActivity.this, ViewActivity.class));
        } else if (v.equals(resumeTrip)) {
            if (isGpsEnabled()) {
                resumeTripDialog();
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.please_enable_the_gps_provider), Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
            }
        } else if (v.equals(allRecord)) {
            DeviceHelper.sendGATrack(MainActivity.this,"Trip", "view", "all_record", null);
            Intent i = new Intent(MainActivity.this, AllRecordActivity.class);
            String[] tripPaths = new File(rootPath).list(FileHelper.getDirFilter());
            i.putExtra(AllRecordActivity.tag_trip_paths, tripPaths);
            MainActivity.this.startActivity(i);
        }
    }

    class TripInformation {
        File file;
        String name;
        Time time;
    }

}
