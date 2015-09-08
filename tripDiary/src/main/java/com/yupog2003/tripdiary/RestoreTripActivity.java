package com.yupog2003.tripdiary;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Menu;

import com.yupog2003.tripdiary.fragments.PreferFragment;
import com.yupog2003.tripdiary.services.BackupRestoreTripService;

import java.util.ArrayList;
import java.util.Set;

public class RestoreTripActivity extends MyActivity {

    String category;
    ArrayList<Uri> uris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restore_trip);
        String action = getIntent().getAction();
        if (action.equals(Intent.ACTION_SEND)) {
            uris = new ArrayList<>();
            uris.add((Uri) getIntent().getParcelableExtra(Intent.EXTRA_STREAM));
        } else if (action.equals(Intent.ACTION_SEND_MULTIPLE)) {
            uris = getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
        }
        if (uris == null || uris.size() == 0) {
            finishAndRemoveTask();
            return;
        }
        checkHasPermission(new OnGrantPermissionCompletedListener() {
            @Override
            public void onGranted() {
                if (TripDiaryApplication.rootDocumentFile == null) {
                    TripDiaryApplication.updateRootPath();
                }
                Set<String> keySet = getSharedPreferences(PreferFragment.categorySettingName, MODE_PRIVATE).getAll().keySet();
                final String[] categories = keySet.toArray(new String[keySet.size()]);
                if (categories.length == 1) {
                    category = categories[0];
                    startRestoreService();
                    finish();
                } else {
                    AlertDialog.Builder ab = new AlertDialog.Builder(RestoreTripActivity.this);
                    ab.setTitle(R.string.choose_a_category);
                    ab.setSingleChoiceItems(categories, -1, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            category = categories[which];
                            startRestoreService();
                            finish();
                        }
                    });
                    ab.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    ab.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    ab.show();
                }
            }

            @Override
            public void onFailed() {
                finishAndRemoveTask();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    private void startRestoreService() {
        if (uris == null || category == null) return;
        Intent intent = new Intent(RestoreTripActivity.this, BackupRestoreTripService.class);
        intent.setAction(BackupRestoreTripService.ACTION_RESTORE);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.putExtra(BackupRestoreTripService.tag_category, category);
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_restore_trip, menu);
        return true;
    }

}
