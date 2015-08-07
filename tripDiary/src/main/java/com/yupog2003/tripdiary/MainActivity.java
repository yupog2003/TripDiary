package com.yupog2003.tripdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.provider.DocumentFile;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
import com.yupog2003.tripdiary.data.ColorHelper;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.services.RecordService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

public class MainActivity extends MyActivity implements Button.OnClickListener {

    Button startTrip;
    Button resumeTrip;
    Button viewHistory;
    Button allRecord;

    public static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
        }
        getSharedPreferences("category", MODE_PRIVATE).edit().putString(getString(R.string.nocategory), String.valueOf(Color.WHITE)).apply();
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (DeviceHelper.isGpsEnabled(this)) {
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

    boolean tripNameClicked;

    private void startTripDialog() {
        tripNameClicked = false;
        final SharedPreferences categorysp = getSharedPreferences("category", MODE_PRIVATE);
        Map<String, ?> map = categorysp.getAll();
        Set<String> keyset = map.keySet();
        final String[] categories = keyset.toArray(new String[keyset.size()]);
        AlertDialog.Builder ab2 = new AlertDialog.Builder(MainActivity.this);
        View layout = getLayoutInflater().inflate(R.layout.edit_trip, (ViewGroup) findViewById(android.R.id.content), false);
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
                    temp = getString(R.string.nocategory);
                } else {
                    temp = categories[rg.getCheckedRadioButtonId()];
                }
                final String category = temp;
                if (name.length() > 0) {
                    if (FileHelper.findfile(TripDiaryApplication.rootDocumentFile, name) == null) {
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
        DocumentFile dir = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, name);
        if (dir == null) {
            dir = TripDiaryApplication.rootDocumentFile.createDirectory(name);
        }
        Trip trip = new Trip(getApplicationContext(), dir, false);
        if (category != null) {
            trip.setCategory(category);
        }
        if (note != null) {
            trip.updateNote(note);
        }
        if (DeviceHelper.isGpsEnabled(this)) {
            if (RecordService.instance == null) {
                Intent i = new Intent(MainActivity.this, RecordService.class);
                i.putExtra(RecordService.tag_tripName, name);
                startService(i);
                DeviceHelper.sendGATrack(MainActivity.this, "Trip", "start", name, null);
                MainActivity.this.finish();
            } else {
                Toast.makeText(MainActivity.this, R.string.please_stop_previous_trip_first, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getBaseContext(), getString(R.string.please_enable_the_gps_provider), Toast.LENGTH_SHORT).show();
        }

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.setting) {
            Intent intent = new Intent(MainActivity.this, PreferActivity.class);
            startActivity(intent);
        }
        return false;
    }

    private void resumeTripDialog() {
        AlertDialog.Builder ab = new AlertDialog.Builder(MainActivity.this);
        DocumentFile[] files = FileHelper.listFiles(TripDiaryApplication.rootDocumentFile, FileHelper.list_dirs);
        if (files == null)
            files = new DocumentFile[0];
        ArrayList<Trip> trips = new ArrayList<>();
        for (DocumentFile file : files) {
            if (file!=null){
                Trip trip = new Trip(getApplicationContext(), file, true);
                trips.add(trip);
            }
        }
        Collections.sort(trips, Collections.reverseOrder());
        final String[] strs = new String[trips.size()];
        for (int i = 0; i < strs.length; i++) {
            strs[i] = trips.get(i).tripName;
        }
        ab.setSingleChoiceItems(strs, -1, new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                StringBuilder sb = new StringBuilder();
                try {
                    DocumentFile noteFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, strs[which], "note");
                    BufferedReader br = new BufferedReader(new InputStreamReader(getContentResolver().openInputStream(noteFile.getUri())));
                    String s;
                    while ((s = br.readLine()) != null) {
                        sb.append(s).append("\n");
                    }
                    br.close();
                } catch (IOException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
                String noteStr = sb.toString();
                if (noteStr.endsWith("\n")) {
                    noteStr = noteStr.substring(0, noteStr.length() - 1);
                }
                DeviceHelper.sendGATrack(MainActivity.this, "Trip", "resume", strs[which], null);
                startTrip(strs[which], noteStr, null);
            }
        });
        ab.setTitle(getString(R.string.resume_trip));
        ab.setNegativeButton(getString(R.string.cancel), null);
        ab.show();
    }

    public void onClick(View v) {
        if (v.equals(startTrip)) {
            if (DeviceHelper.isGpsEnabled(this)) {
                startTripDialog();
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.please_enable_the_gps_provider), Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 0);
            }
        } else if (v.equals(viewHistory)) {
            startActivity(new Intent(MainActivity.this, ViewActivity.class));
        } else if (v.equals(resumeTrip)) {
            if (DeviceHelper.isGpsEnabled(this)) {
                resumeTripDialog();
            } else {
                Toast.makeText(getBaseContext(), getString(R.string.please_enable_the_gps_provider), Toast.LENGTH_SHORT).show();
                startActivityForResult(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
            }
        } else if (v.equals(allRecord)) {
            Intent i = new Intent(MainActivity.this, AllRecordActivity.class);
            String[] tripNames = FileHelper.listFileNames(TripDiaryApplication.rootDocumentFile, FileHelper.list_dirs);
            i.putExtra(AllRecordActivity.tag_trip_names, tripNames);
            startActivity(i);
        }
    }

}
