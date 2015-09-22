package com.yupog2003.tripdiary.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yupog2003.tripdiary.CategoryActivity;
import com.yupog2003.tripdiary.MyActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyBackupAgent;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;

import java.io.File;

public class PreferFragment extends PreferenceFragment implements OnPreferenceChangeListener, OnPreferenceClickListener {
    Preference musicpath;
    Preference backupsetting;
    Preference restoresetting;
    Preference tripcategory;
    Preference diaryfont;
    Preference tripTimeZone;
    Preference account;
    Preference rootpath;
    ListPreference playingtripmode;
    ListPreference playtripspeed;
    ListPreference playpoispeed;
    ListPreference distanceUnit;
    ListPreference altitudeUnit;
    SwitchPreference usesaf;

    private static final int selectmusicpath = 0;
    private static final int selectdiaryfont = 1;
    private static final int selectrootpath = 2;
    private static DocumentFile backupPreferenceFile;
    public static final String categorySettingName = "category";
    public static final String tripSettingName = "trip";
    public static final String tripTimeSettingName = "tripTime";
    public static final String tripExpandSettingName = "categoryExpand";
    public static final String tripTimezoneSettingName = "tripTimezone";
    public static final String defaultSettingName = "com.yupog2003.tripdiary_preferences";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);
        musicpath = findPreference("musicpath");
        musicpath.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("musicpath", getString(R.string.select_music_path)));
        musicpath.setOnPreferenceClickListener(this);
        tripcategory = findPreference("tripcategory");
        tripcategory.setOnPreferenceClickListener(this);
        diaryfont = findPreference("diaryfont");
        diaryfont.setOnPreferenceClickListener(this);
        diaryfont.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("diaryfont", getString(R.string.select_diary_font)));
        playingtripmode = (ListPreference) findPreference("playingtripmode");
        playingtripmode.setSummary(playingtripmode.getEntry());
        playingtripmode.setOnPreferenceChangeListener(this);
        playtripspeed = (ListPreference) findPreference("playtripspeed");
        playtripspeed.setSummary(playtripspeed.getEntry());
        playtripspeed.setOnPreferenceChangeListener(this);
        playpoispeed = (ListPreference) findPreference("playpoispeed");
        playpoispeed.setSummary(playpoispeed.getEntry());
        playpoispeed.setOnPreferenceChangeListener(this);
        backupsetting = findPreference("backupsetting");
        backupsetting.setOnPreferenceClickListener(this);
        restoresetting = findPreference("restoresetting");
        restoresetting.setOnPreferenceClickListener(this);
        rootpath = findPreference("rootpath");
        rootpath.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("rootpath", Environment.getExternalStorageDirectory() + "/TripDiary"));
        rootpath.setOnPreferenceChangeListener(this);
        rootpath.setOnPreferenceClickListener(this);
        tripTimeZone = findPreference("triptimezone");
        tripTimeZone.setOnPreferenceClickListener(this);
        account = findPreference("account");
        account.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("account", ""));
        account.setOnPreferenceClickListener(this);
        distanceUnit = (ListPreference) findPreference("distance_unit");
        distanceUnit.setOnPreferenceChangeListener(this);
        altitudeUnit = (ListPreference) findPreference("altitude_unit");
        altitudeUnit.setOnPreferenceChangeListener(this);
        usesaf = (SwitchPreference) findPreference("usesaf");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            usesaf.setEnabled(false);
        }
        backupPreferenceFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, ".settings");
        if (backupPreferenceFile == null) {
            backupPreferenceFile = TripDiaryApplication.rootDocumentFile.createDirectory(".settings");
        }
    }

    @SuppressLint("DefaultLocale")
    @TargetApi(21)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            SharedPreferences setting = getPreferenceManager().getSharedPreferences();
            SharedPreferences.Editor editor = setting.edit();
            switch (requestCode) {
                case selectmusicpath:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri uri1 = data.getData();
                        if (uri1 != null) {
                            File resultFile = FileHelper.copyFromUriToFile(getActivity(), uri1, getActivity().getFilesDir(), null);
                            if (resultFile != null) {
                                editor.putString("musicpath", resultFile.getName());
                                editor.apply();
                                musicpath.setSummary(resultFile.getName());
                            }
                        }
                    }
                    break;
                case selectdiaryfont:
                    if (resultCode == Activity.RESULT_OK) {
                        Uri uri1 = data.getData();
                        if (uri1 != null) {
                            File resultFile = FileHelper.copyFromUriToFile(getActivity(), uri1, getActivity().getFilesDir(), null);
                            if (resultFile != null) {
                                editor.putString("diaryfont", resultFile.getName());
                                editor.apply();
                                diaryfont.setSummary(resultFile.getName());
                            }
                        }
                    }
                    break;
                case selectrootpath:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && resultCode == Activity.RESULT_OK && data.getData() != null) {
                        Uri uri = data.getData();
                        String authority = uri.getAuthority();
                        if (authority.equals("com.android.externalstorage.documents")) {
                            final int flags = (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                            getActivity().getContentResolver().takePersistableUriPermission(uri, flags);
                            setNewRootPath(uri.toString());
                        } else {
                            Toast.makeText(getActivity(), "Please select another path. Either internal or external storage.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
            }
        }
    }

    public boolean onPreferenceClick(Preference preference) {

        if (preference.equals(musicpath)) {
            Intent i3 = new Intent(Intent.ACTION_GET_CONTENT);
            i3.setType("audio/*");
            startActivityForResult(Intent.createChooser(i3, getString(R.string.select_music_path)), selectmusicpath);
        } else if (preference.equals(tripcategory)) {
            getActivity().startActivity(new Intent(getActivity(), CategoryActivity.class));
        } else if (preference.equals(diaryfont)) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("file/*");
            startActivityForResult(Intent.createChooser(i, getString(R.string.diary_font)), selectdiaryfont);
        } else if (preference.equals(backupsetting)) {
            if (backupPreferenceFile == null) return true;
            DocumentFile[] settingFiles = backupPreferenceFile.listFiles();
            DocumentFile file = FileHelper.findfile(settingFiles, defaultSettingName);
            if (file == null) {
                file = backupPreferenceFile.createFile("", defaultSettingName);
            }
            boolean b1 = MyBackupAgent.saveSharedPreferencesToFile(getActivity(), defaultSettingName, file);

            file = FileHelper.findfile(settingFiles, categorySettingName);
            if (file == null) {
                file = backupPreferenceFile.createFile("", categorySettingName);
            }
            boolean b2 = MyBackupAgent.saveSharedPreferencesToFile(getActivity(), categorySettingName, file);

            file = FileHelper.findfile(settingFiles, tripSettingName);
            if (file == null) {
                file = backupPreferenceFile.createFile("", tripSettingName);
            }
            boolean b3 = MyBackupAgent.saveSharedPreferencesToFile(getActivity(), tripSettingName, file);

            file = FileHelper.findfile(settingFiles, tripExpandSettingName);
            if (file == null) {
                file = backupPreferenceFile.createFile("", tripExpandSettingName);
            }
            boolean b4 = MyBackupAgent.saveSharedPreferencesToFile(getActivity(), tripExpandSettingName, file);

            file = FileHelper.findfile(settingFiles, tripTimezoneSettingName);
            if (file == null) {
                file = backupPreferenceFile.createFile("", tripTimezoneSettingName);
            }
            boolean b5 = MyBackupAgent.saveSharedPreferencesToFile(getActivity(), tripTimezoneSettingName, file);

            file = FileHelper.findfile(settingFiles, tripTimeSettingName);
            if (file == null) {
                file = backupPreferenceFile.createFile("", tripTimeSettingName);
            }
            boolean b6 = MyBackupAgent.saveSharedPreferencesToFile(getActivity(), tripTimeSettingName, file);
            if (b1 && b2 && b3 & b4 && b5 && b6)
                Toast.makeText(getActivity(), getString(R.string.setting_has_been_backed_up), Toast.LENGTH_SHORT).show();
        } else if (preference.equals(restoresetting)) {
            boolean b1 = MyBackupAgent.loadSharedPreferencesFromFile(getActivity(), defaultSettingName, FileHelper.findfile(backupPreferenceFile, defaultSettingName));
            boolean b2 = MyBackupAgent.loadSharedPreferencesFromFile(getActivity(), categorySettingName, FileHelper.findfile(backupPreferenceFile, categorySettingName));
            boolean b3 = MyBackupAgent.loadSharedPreferencesFromFile(getActivity(), tripSettingName, FileHelper.findfile(backupPreferenceFile, tripSettingName));
            boolean b4 = MyBackupAgent.loadSharedPreferencesFromFile(getActivity(), tripExpandSettingName, FileHelper.findfile(backupPreferenceFile, tripExpandSettingName));
            boolean b5 = MyBackupAgent.loadSharedPreferencesFromFile(getActivity(), tripTimezoneSettingName, FileHelper.findfile(backupPreferenceFile, tripTimezoneSettingName));
            boolean b6 = MyBackupAgent.loadSharedPreferencesFromFile(getActivity(), tripTimeSettingName, FileHelper.findfile(backupPreferenceFile, tripTimeSettingName));
            if (b1 && b2 && b3 && b4 && b5 && b6)
                Toast.makeText(getActivity(), getString(R.string.setting_has_been_restored), Toast.LENGTH_SHORT).show();
        } else if (preference.equals(rootpath)) {
            boolean isUseSAF = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("usesaf", false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isUseSAF) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                startActivityForResult(intent, selectrootpath);
            } else {
                ((MyActivity) getActivity()).pickDir(getString(R.string.select_a_directory), new MyActivity.OnDirPickedListener() {
                    @Override
                    public void onDirPicked(File dir) {
                        String newRootPath = dir.getPath();
                        if (FileHelper.checkHasWritePermission(getActivity(), newRootPath)) {
                            setNewRootPath(newRootPath);
                        }
                    }
                });
            }

        } else if (preference.equals(tripTimeZone)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.update_trip_timezone));
            ab.setMessage(getString(R.string.explain_update_trip_timezone));
            ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    if (DeviceHelper.isMobileNetworkAvailable(getActivity())) {
                        updateTripTimeZoneTask = new UpdateTripTimeZoneTask();
                        updateTripTimeZoneTask.execute();
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.no_internet_connection), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ab.setNegativeButton(getString(R.string.cancel), null);
            ab.show();
        } else if (preference.equals(account)) {
            ((MyActivity) getActivity()).getAccount(new MyActivity.OnAccountPickedListener() {
                @Override
                public void onAccountPicked(@NonNull String accountName) {
                    account.setSummary(accountName);
                }
            }, true);
        }
        return false;
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {

        if (preference instanceof ListPreference) {
            ((ListPreference) preference).setValue((String) newValue);
            preference.setSummary(((ListPreference) preference).getEntry());
        }
        if (preference.equals(rootpath)) {
            String newPath = String.valueOf(newValue);
            if (new File(newPath).isDirectory()) {
                rootpath.setSummary(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("rootpath", Environment.getExternalStorageDirectory().getPath()));
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getActivity()).edit();
                editor.putString("rootpath", newPath);
                editor.apply();
                TripDiaryApplication.updateRootPath();
            } else {
                Toast.makeText(getActivity(), newPath + " " + getString(R.string.is_not_a_valid_directory), Toast.LENGTH_SHORT).show();
            }
        }
        if (preference.equals(distanceUnit)) {
            distanceUnit.setValue(String.valueOf(newValue));
            TripDiaryApplication.distance_unit = Integer.valueOf(String.valueOf(newValue));
        }
        if (preference.equals(altitudeUnit)) {
            altitudeUnit.setValue(String.valueOf(newValue));
            TripDiaryApplication.altitude_unit = Integer.valueOf(String.valueOf(newValue));
        }
        return false;
    }

    private void setNewRootPath(String newRootPath) {
        PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putString("rootpath", newRootPath).apply();
        rootpath.setSummary(newRootPath);
        TripDiaryApplication.updateRootPath();
        backupPreferenceFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, ".settings");
        if (backupPreferenceFile == null) {
            backupPreferenceFile = TripDiaryApplication.rootDocumentFile.createDirectory(".settings");
        }
    }

    UpdateTripTimeZoneTask updateTripTimeZoneTask;

    class UpdateTripTimeZoneTask extends AsyncTask<Void, String, String> {
        TextView message;
        ProgressBar progress;
        TextView progressMessage;
        AlertDialog dialog;
        boolean cancel = false;

        @Override
        protected void onPreExecute() {

            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.updating));
            LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.progressdialog_import_memory, (ViewGroup) getView(), false);
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
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {

            DocumentFile[] trips = TripDiaryApplication.rootDocumentFile.listFiles(DocumentFile.list_dirs);
            publishProgress("setMax", String.valueOf(trips.length));
            for (int i = 0; i < trips.length; i++) {
                if (cancel)
                    break;
                String tripName = trips[i].getName();
                publishProgress(tripName, String.valueOf(i));
                DocumentFile gpxFile = FileHelper.findfile(trips[i], tripName + ".gpx");
                if (gpxFile != null) {
                    MyCalendar.updateTripTimeZoneFromGpxFile(getActivity(), tripName, gpxFile);
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();
            updateTripTimeZoneTask = null;
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
            updateTripTimeZoneTask = null;
        }

    }

    @Override
    public void onDestroy() {
        if (updateTripTimeZoneTask != null) {
            updateTripTimeZoneTask.cancel = true;
            updateTripTimeZoneTask = null;
        }
        super.onDestroy();
    }
}
