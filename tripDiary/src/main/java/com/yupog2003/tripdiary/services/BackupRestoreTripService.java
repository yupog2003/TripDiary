package com.yupog2003.tripdiary.services;

import android.app.ActivityManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.yupog2003.tripdiary.MyActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.ViewActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.fragments.PreferFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class BackupRestoreTripService extends IntentService {

    public static final String ACTION_BACKUP = "com.yupog2003.tripdiary.services.action.BACKUP";
    public static final String ACTION_RESTORE = "com.yupog2003.tripdiary.services.action.RESTORE";
    public static final String tag_tripnames = "com.yupog2003.tripdiary.services.tag_tripnames";
    public static final String tag_category = "com.yupog2003.tripdiary.services.tag_category";
    public static final String tag_directory = "com.yupog2003.tripdiary.services.tag_directory";

    public BackupRestoreTripService() {
        super("BackupRestoreTripService");
        nb = new NotificationCompat.Builder(this);
    }

    ArrayList<Uri> uris;         //for restore
    ArrayList<String> tripNames; //for backup
    NotificationCompat.Builder nb;

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null && intent.getAction() != null) {
            final String action = intent.getAction();
            if (action.equals(ACTION_BACKUP)) {
                backup(intent);
            } else if (action.equals(ACTION_RESTORE)) {
                restore(intent);
            }
        }
    }

    private void backup(Intent intent) {
        if ((tripNames = intent.getStringArrayListExtra(tag_tripnames)) != null) {
            publishProgress(getString(R.string.zipping), getString(R.string.zipping), null, true);
            DocumentFile[] tripFiles = TripDiaryApplication.rootDocumentFile.listFiles(DocumentFile.list_dirs);
            ArrayList<Uri> resultUris = new ArrayList<>();
            StringBuilder sb = new StringBuilder();
            File dir = (File) intent.getSerializableExtra(tag_directory);
            if (dir == null) dir = Environment.getExternalStorageDirectory();
            String dirPath = dir.getPath();
            for (int i = 0; i < tripNames.size(); i++) {
                String tripName = tripNames.get(i);
                if (tripName == null) continue;
                sb.append(dirPath).append("/").append(tripName).append(".zip").append(", ");
                publishProgress(getString(R.string.zipping), getString(R.string.zipping), tripName, true);
                DocumentFile from = FileHelper.findfile(tripFiles, tripName);
                DocumentFile to = DocumentFile.fromFile(new File(dir, tripName + ".zip"));
                if (from == null) continue;
                if (to.exists())
                    to.delete();
                FileHelper.zip(from, to);
                resultUris.add(to.getUri());
            }
            Intent sendIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            sendIntent.setType("application/zip");
            sendIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, resultUris);
            sendIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            sendIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            PendingIntent pi = PendingIntent.getActivity(this, 0, sendIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            nb.setContentIntent(pi);
            nb.setProgress(0, 0, false);
            nb.setTicker(getString(R.string.backup_complete));
            nb.setContentTitle(getString(R.string.backup_complete));
            String trips = sb.toString();
            if (trips.length() > 2) {
                trips = trips.substring(0, trips.length() - 2);
            }
            nb.setContentText(trips);
            nb.setSmallIcon(R.drawable.ic_backup);
            NotificationManagerCompat nm = NotificationManagerCompat.from(this);
            nm.notify(0, nb.build());
            stopForeground(true);
        }
    }

    private void restore(Intent intent) {
        if ((uris = intent.getParcelableArrayListExtra(Intent.EXTRA_STREAM)) != null) {
            publishProgress(getString(R.string.unzipping), getString(R.string.unzipping), null, true);
            String category = intent.getStringExtra(tag_category);
            String tripName = null;
            boolean success;
            StringBuilder sb = new StringBuilder();
            DocumentFile[] files = TripDiaryApplication.rootDocumentFile.listFiles();
            for (Uri uri : uris) {
                success = false;
                tripName = null;
                try {
                    tripName = tryGetTripName(uri);
                    if (tripName == null) {
                        continue;
                    }
                    sb.append(tripName).append(", ");
                    publishProgress(getString(R.string.unzipping), getString(R.string.unzipping), tripName, true);
                    if (FileHelper.findfile(files, tripName) != null) {
                        success = true;
                        continue;
                        /*TODO handle replace or not*/
                    } else {
                        FileHelper.unZip(getContentResolver().openInputStream(uri), TripDiaryApplication.rootDocumentFile);
                        DocumentFile gpxFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, tripName + ".gpx");
                        if (gpxFile != null) {
                            MyCalendar.updateTripTimeZoneFromGpxFile(BackupRestoreTripService.this, tripName, gpxFile);
                        }
                        success = true;
                    }
                } catch (IOException | NullPointerException | IllegalArgumentException e) {
                    e.printStackTrace();
                }
                if (success) {
                    getSharedPreferences(PreferFragment.tripSettingName, MODE_PRIVATE).edit().putString(tripName, category != null ? category : getString(R.string.nocategory)).apply();
                }
            }
            Intent i;
            if (uris.size() == 1 && tripName != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityManager.AppTask task = MyActivity.findViewTripActivityTask(BackupRestoreTripService.this, tripName);
                    if (task != null) {
                        task.finishAndRemoveTask();
                    }
                }
                i = new Intent(BackupRestoreTripService.this, ViewTripActivity.class);
                i.putExtra(ViewTripActivity.tag_tripName, tripName);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            } else {
                i = new Intent(BackupRestoreTripService.this, ViewActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            PendingIntent pi = PendingIntent.getActivity(this, 0, i, PendingIntent.FLAG_UPDATE_CURRENT);
            nb.setContentIntent(pi);
            nb.setProgress(0, 0, false);
            nb.setTicker(getString(R.string.restore_complete));
            nb.setContentTitle(getString(R.string.restore_complete));
            String trips = sb.toString();
            if (trips.length() > 2) {
                trips = trips.substring(0, trips.length() - 2);
            }
            nb.setContentText(trips);
            nb.setSmallIcon(R.drawable.ic_launcher);
            NotificationManagerCompat nm = NotificationManagerCompat.from(this);
            nm.notify(0, nb.build());
            stopForeground(true);
        }
    }

    private String tryGetTripName(Uri uri) {
        try {
            ZipInputStream zis = new ZipInputStream(getContentResolver().openInputStream(uri));
            ZipEntry entry;
            String s = "";
            if ((entry = zis.getNextEntry()) != null) {
                s = entry.getName();
            }
            zis.close();
            if (s.contains("/")) {
                s = s.replace("/", "");
            }
            return s;
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void publishProgress(String ticker, String contentTitle, String contentText, boolean progressIndeterminate) {
        if (contentTitle != null) {
            nb.setContentTitle(contentTitle);
        }
        if (contentText != null) {
            nb.setContentText(contentText);
        }
        if (ticker != null) {
            nb.setTicker(ticker);
        }
        nb.setProgress(0, 0, progressIndeterminate);
        nb.setSmallIcon(R.drawable.ic_backup);
        startForeground(1, nb.build());
    }
}
