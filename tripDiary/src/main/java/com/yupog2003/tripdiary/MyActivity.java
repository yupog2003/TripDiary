package com.yupog2003.tripdiary;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.documentfile.DriveDocumentFile;
import com.yupog2003.tripdiary.services.RecordService;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MyActivity extends AppCompatActivity {

    public static final int REQUEST_PERMISSION = 80;
    public static final int REQUEST_ACCOUNT = 81;
    public static final int REQUEST_GET_TOKEN = 82;
    public static final int REQUEST_CONNECT_TO_DRIVE = 83;
    public static final int REQUEST_CONNECT_TO_REST_API = 84;

    public MyActivity getActivity() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (RecordService.instance != null) {
            RecordService.instance.hasTask = true;
        }
    }

    @NonNull
    public static List<ActivityManager.AppTask> getMyTasks(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            return activityManager.getAppTasks();
        } else {
            return new ArrayList<>();
        }
    }

    @Nullable
    public static ActivityManager.AppTask findViewTripActivityTask(Context context, String tripName) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            for (ActivityManager.AppTask task : getMyTasks(context)) {
                try {
                    String s = task.getTaskInfo().baseIntent.getStringExtra(ViewTripActivity.tag_tripName);
                    if (s != null && s.equals(tripName)) {
                        return task;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public boolean isServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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

    private OnGrantPermissionCompletedListener onGrantPermissionCompletedListener;

    public void checkHasPermission(OnGrantPermissionCompletedListener listener, String... permissions) {
        if (listener == null) return;
        this.onGrantPermissionCompletedListener = listener;
        ArrayList<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
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
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), REQUEST_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_PERMISSION) {
            boolean granted = true;
            for (int grantResult : grantResults) {
                granted &= grantResult == PackageManager.PERMISSION_GRANTED;
            }
            if (onGrantPermissionCompletedListener != null) {
                if (granted) {
                    onGrantPermissionCompletedListener.onGranted();
                } else {
                    onGrantPermissionCompletedListener.onFailed();
                }
                onGrantPermissionCompletedListener = null;
            }
        }
    }

    public interface OnAccountPickedListener {
        void onAccountPicked(@NonNull String account);
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
        } else if (requestCode == REQUEST_GET_TOKEN && resultCode == Activity.RESULT_OK) {
            new GetAccessTokenTask().execute();
        } else if (requestCode == REQUEST_CONNECT_TO_DRIVE && resultCode == Activity.RESULT_OK) {
            if (googleApiClient != null) {
                googleApiClient.connect();
            }
        } else if (requestCode == REQUEST_CONNECT_TO_REST_API && resultCode == Activity.RESULT_OK) {
            connectToRestDriveApi(onConnectedToRestDriveApiListener);
        }
    }

    public void getAccessToken(String account, OnAccessTokenGotListener listener) {
        if (account == null) return;
        this.account = account;
        this.onAccessTokenGotListener = listener;
        new GetAccessTokenTask().execute();
    }

    public interface OnAccessTokenGotListener {
        void onAccessTokenGot(@NonNull String token);
    }

    private OnAccessTokenGotListener onAccessTokenGotListener;
    private String account;

    private class GetAccessTokenTask extends AsyncTask<Void, Void, String> {

        Intent loginIntent;
        String token;

        @Override
        protected String doInBackground(Void... params) {
            try {
                token = GoogleAuthUtil.getToken(getActivity(), account, "oauth2:https://www.googleapis.com/auth/userinfo.email");
            } catch (UserRecoverableAuthException e) {
                e.printStackTrace();
                loginIntent = e.getIntent();
                token = null;
            } catch (Exception e) {
                e.printStackTrace();
                token = null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (token != null && onAccessTokenGotListener != null) {
                onAccessTokenGotListener.onAccessTokenGot(token);
            } else if (loginIntent != null) {
                startActivityForResult(loginIntent, REQUEST_GET_TOKEN);
            }
        }
    }

    public GoogleApiClient googleApiClient;

    public void connectToDriveApi(final GoogleApiClient.ConnectionCallbacks callbacks) {
        getAccount(new OnAccountPickedListener() {
            @Override
            public void onAccountPicked(@NonNull final String account) {
                googleApiClient = DriveDocumentFile.getDefaultGoogleApiClientBuilder(MyActivity.this, account)
                        .addConnectionCallbacks(callbacks)
                        .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(ConnectionResult connectionResult) {
                                if (connectionResult.hasResolution()) {
                                    try {
                                        connectionResult.startResolutionForResult(MyActivity.this, REQUEST_CONNECT_TO_DRIVE);
                                    } catch (IntentSender.SendIntentException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), MyActivity.this, 0).show();
                                }
                            }
                        }).build();
                googleApiClient.connect();
                TripDiaryApplication.googleApiClient = googleApiClient;
            }
        }, false);

    }

    public interface OnDirPickedListener {
        void onDirPicked(File dir);
    }

    public void pickDir(String dialogTitle, final OnDirPickedListener listener) {
        AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
        ListView listView = new ListView(getActivity());
        final FileHelper.DirAdapter adapter = new FileHelper.DirAdapter(getActivity(), false, Environment.getExternalStorageDirectory());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);
        ab.setTitle(dialogTitle);
        ab.setView(listView);
        ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) {
                    listener.onDirPicked(adapter.getRoot());
                }
            }
        });
        ab.setNegativeButton(getString(R.string.cancel), null);
        ab.show();
    }

    public interface OnConnectedToRestDriveApiListener {
        void onConnected(Drive service, String account);
    }

    OnConnectedToRestDriveApiListener onConnectedToRestDriveApiListener;

    public void connectToRestDriveApi(final OnConnectedToRestDriveApiListener listener) {
        this.onConnectedToRestDriveApiListener = listener;
        getAccount(new OnAccountPickedListener() {
            @Override
            public void onAccountPicked(@NonNull final String account) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> scopes = new ArrayList<>();
                        scopes.add(DriveScopes.DRIVE);
                        GoogleAccountCredential credential = GoogleAccountCredential.usingOAuth2(MyActivity.this, scopes);
                        credential.setSelectedAccountName(account);
                        Drive service = new Drive.Builder(AndroidHttp.newCompatibleTransport(), new GsonFactory(), credential).build();
                        try {
                            service.about().get().execute();
                            TripDiaryApplication.service = service;
                            if (listener != null) {
                                listener.onConnected(service, account);
                            }
                        } catch (UserRecoverableAuthIOException e) {
                            e.printStackTrace();
                            startActivityForResult(e.getIntent(), REQUEST_CONNECT_TO_REST_API);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        }, false);

    }
}
