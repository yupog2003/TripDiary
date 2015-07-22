package com.yupog2003.tripdiary.data;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupManager;
import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v4.provider.DocumentFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Map.Entry;

public class MyBackupAgent extends BackupAgentHelper {
    static final String defaultPreference = "com.yupog2003.tripdiary_preferences";
    static final String tripPreference = "trip";
    static final String categoryPreference = "category";
    static final String categoryExpandPreference = "categoryExpand";
    static final String tripTimeZonePreference = "tripTimezone";
    static final String backupKey = "com.yupog2003.tripdiary.backupkey";

    @Override
    public void onCreate() {
        SharedPreferencesBackupHelper helper = new SharedPreferencesBackupHelper(this, defaultPreference, tripPreference, categoryPreference, categoryExpandPreference, tripTimeZonePreference);
        addHelper(backupKey, helper);
    }

    public static void requestBackup(Context context) {
        BackupManager bm = new BackupManager(context);
        bm.dataChanged();
    }

    public static boolean saveSharedPreferencesToFile(Context context, String prefName, DocumentFile dst) {
        boolean res = false;
        ObjectOutputStream output = null;
        if (dst == null) return false;
        try {
            output = new ObjectOutputStream(context.getContentResolver().openOutputStream(dst.getUri()));
            SharedPreferences pref = context.getSharedPreferences(prefName, Context.MODE_PRIVATE);
            output.writeObject(pref.getAll());
            res = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (output != null) {
                    output.flush();
                    output.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }

    @SuppressWarnings({"unchecked"})
    public static boolean loadSharedPreferencesFromFile(Context context, String prefName, DocumentFile src) {
        boolean res = false;
        ObjectInputStream input = null;
        if (src == null)
            return false;
        try {
            input = new ObjectInputStream(context.getContentResolver().openInputStream(src.getUri()));
            Editor prefEdit = context.getSharedPreferences(prefName, Context.MODE_PRIVATE).edit();
            prefEdit.clear();
            Map<String, ?> entries = (Map<String, ?>) input.readObject();
            for (Entry<String, ?> entry : entries.entrySet()) {
                Object v = entry.getValue();
                String key = entry.getKey();
                if (v instanceof Boolean)
                    prefEdit.putBoolean(key, (Boolean) v);
                else if (v instanceof Float)
                    prefEdit.putFloat(key, (Float) v);
                else if (v instanceof Integer)
                    prefEdit.putInt(key, (Integer) v);
                else if (v instanceof Long)
                    prefEdit.putLong(key, (Long) v);
                else if (v instanceof String)
                    prefEdit.putString(key, (String) v);
            }
            prefEdit.commit();
            res = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return res;
    }
}
