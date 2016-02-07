package com.yupog2003.tripdiary.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.services.RecordService;

import java.util.TimeZone;


public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
            if (preferences.getBoolean("autoresume", false)) {
                if (preferences.getBoolean(RecordService.tag_onRecording, false)) {
                    String name = preferences.getString(RecordService.tag_lastRecordTrip, null);
                    if (name == null) return;
                    if (!DeviceHelper.isGpsEnabled(context)) {
                        Toast.makeText(context, R.string.please_enable_the_gps_provider, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    DocumentFile dir = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, name);
                    if (dir == null) { //new trip
                        dir = TripDiaryApplication.rootDocumentFile.createDirectory(name);
                        MyCalendar.updateTripTimeZone(context, name, TimeZone.getDefault().getID());
                    }
                    if (dir == null) {
                        Toast.makeText(context, R.string.storage_error, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent i = new Intent(context, RecordService.class);
                    i.putExtra(RecordService.tag_tripName, name);
                    context.startService(i);
                }
            }
        }
    }
}

