package com.yupog2003.tripdiary;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION = 8000;
    public static final int REQUEST_ACCOUNT = 8001;

    public Activity getActivity() {
        return this;
    }

    public static List<ActivityManager.AppTask> getMyTasks(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            return activityManager.getAppTasks();
        } else {
            return new ArrayList<>();
        }
    }

    public static ActivityManager.AppTask findViewTripActivityTask(Context context, String tripName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (ActivityManager.AppTask task : getMyTasks(context)) {
                String s = task.getTaskInfo().baseIntent.getStringExtra(ViewTripActivity.tag_tripName);
                if (s != null && s.equals(tripName)) {
                    return task;
                }
            }
        }
        return null;
    }

    @Override
    public void finishAndRemoveTask() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            super.finishAndRemoveTask();
        } else {
            finish();
        }
    }

    public interface OnGrantPermissionCompletedListener {
        void onGranted();

        void onFailed();
    }

    private OnGrantPermissionCompletedListener listener;

    public void checkHasPermission(OnGrantPermissionCompletedListener listener, String... permissions) {
        if (listener == null) return;
        this.listener = listener;
        ArrayList<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
                if (shouldShowRequestPermissionRationale(permission)) {
                    switch (permission) {
                        case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                            Toast.makeText(this, R.string.permission_explanation_write_external_storage, Toast.LENGTH_LONG).show();
                            break;
                        case Manifest.permission.ACCESS_FINE_LOCATION:
                            Toast.makeText(this, R.string.permission_explanation_location, Toast.LENGTH_LONG).show();
                            break;
                    }
                }
            }
        }
        if (permissionList.isEmpty()) {
            listener.onGranted();
        } else {
            requestPermissions(permissionList.toArray(new String[permissionList.size()]), REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            boolean granted = true;
            for (int grantResult : grantResults) {
                granted &= grantResult == PackageManager.PERMISSION_GRANTED;
            }
            if (listener != null) {
                if (granted) {
                    listener.onGranted();
                } else {
                    listener.onFailed();
                }
                listener = null;
            }
        }
    }

    public interface OnAccountPickedListener {
        void onAccountPicked(String account);
    }

    private OnAccountPickedListener onAccountPickedListener;

    public void getAccount(OnAccountPickedListener listener, boolean alwaysShow) {
        if (listener == null) return;
        this.onAccountPickedListener = listener;
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, alwaysShow, null, null, null, null);
        if (alwaysShow) {
            startActivityForResult(intent, REQUEST_ACCOUNT);
        } else {
            String account = PreferenceManager.getDefaultSharedPreferences(this).getString("account", null);
            if (account == null) {
                startActivityForResult(intent, REQUEST_ACCOUNT);
            } else {
                onAccountPickedListener.onAccountPicked(account);
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ACCOUNT && resultCode == Activity.RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            if (accountName != null) {
                PreferenceManager.getDefaultSharedPreferences(this).edit().putString("account", accountName).apply();
                if (onAccountPickedListener != null) {
                    onAccountPickedListener.onAccountPicked(accountName);
                }
            }
            onAccountPickedListener = null;
        }
    }
}
