package com.yupog2003.tripdiary.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.OpenFileActivityBuilder;
import com.yupog2003.tripdiary.MyActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.DrawableHelper;
import com.yupog2003.tripdiary.data.documentfile.DriveDocumentFile;
import com.yupog2003.tripdiary.services.DownloadFromDriveService;
import com.yupog2003.tripdiary.views.CheckableLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class DriveTripsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;
    GoogleApiClient googleApiClient;
    TripAdapter tripAdapter;
    public static final int REQUEST_PICK_DIR = 3;

    public DriveTripsFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_remotetrips, container, false);
        listView = (ListView) view.findViewById(R.id.listView);
        ViewCompat.setNestedScrollingEnabled(listView, true);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_container);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light, android.R.color.holo_orange_light, android.R.color.holo_red_light);
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_drive_trip, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.openfromdrive) {
            if (googleApiClient == null || !googleApiClient.isConnected()) {
                Toast.makeText(getActivity(), "GoogleApiClient hasn't been connected", Toast.LENGTH_SHORT).show();
                return true;
            }
            IntentSender intentSender = Drive.DriveApi
                    .newOpenFileActivityBuilder()
                    .setSelectionFilter(DriveDocumentFile.getTripFilters())
                    .build(googleApiClient);
            try {
                getActivity().startIntentSenderForResult(intentSender, REQUEST_PICK_DIR, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_PICK_DIR && resultCode == Activity.RESULT_OK) {
            final DriveId driveId = data.getParcelableExtra(OpenFileActivityBuilder.EXTRA_RESPONSE_DRIVE_ID);
            if (driveId == null) return;
            Intent intent = new Intent(getActivity(), ViewTripActivity.class);
            intent.putExtra(ViewTripActivity.tag_tripName, "");
            intent.putExtra(ViewTripActivity.tag_fromRestDrive, true);
            intent.putExtra(ViewTripActivity.tag_resourceId, driveId.getResourceId());
            startActivity(intent);
        }
    }

    public void loadData() {
        ((MyActivity) getActivity()).connectToDriveApi(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {
                googleApiClient = ((MyActivity) getActivity()).googleApiClient;
                new LoadTripsTask().execute();
            }

            @Override
            public void onConnectionSuspended(int i) {

            }
        });
    }

    class LoadTripsTask extends AsyncTask<Void, Void, DriveDocumentFile[]> {
        @Override
        protected void onPreExecute() {
            if (!swipeRefreshLayout.isRefreshing()) {
                swipeRefreshLayout.setRefreshing(true);
            }
        }

        @Override
        protected DriveDocumentFile[] doInBackground(Void... params) {
            if (googleApiClient == null || !googleApiClient.isConnected())
                return new DriveDocumentFile[0];
            return DriveDocumentFile.listTripFiles(googleApiClient);
        }

        @Override
        protected void onPostExecute(DriveDocumentFile[] driveDocumentFiles) {
            if (driveDocumentFiles != null) {
                tripAdapter = new TripAdapter(driveDocumentFiles);
                listView.setAdapter(tripAdapter);
                listView.setOnItemClickListener(tripAdapter);
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
                listView.setLongClickable(true);
                listView.setMultiChoiceModeListener(tripAdapter);
                if (swipeRefreshLayout.isRefreshing())
                    swipeRefreshLayout.setRefreshing(false);
            }
        }
    }

    class TripAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

        DriveDocumentFile[] tripFiles;
        int dip10;
        boolean onActionMode;
        boolean[] checks;
        boolean checkAll;

        public TripAdapter(DriveDocumentFile[] tripFiles) {
            this.tripFiles = tripFiles;
            this.dip10 = (int) DeviceHelper.pxFromDp(getActivity(), 10);
            this.onActionMode = false;
        }

        @Override
        public int getCount() {
            return tripFiles.length;
        }

        @Override
        public Object getItem(int position) {
            return tripFiles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(getActivity());
            textView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
            textView.setText(tripFiles[position].getName());
            textView.setPadding(dip10, dip10, dip10, dip10);
            CheckableLayout layout = new CheckableLayout(getActivity());
            layout.addView(textView);
            layout.setOnMultiChoiceMode(onActionMode);
            return layout;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            String[] selections = new String[]{getString(R.string.download), getString(R.string.open)};
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setSingleChoiceItems(selections, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (which == 0) {
                        if (TripDiaryApplication.rootDocumentFile.findFile(tripFiles[position].getName()) != null) {
                            Toast.makeText(getActivity(), R.string.same_trip, Toast.LENGTH_SHORT).show();
                            return;
                        }
                        ((MyActivity) getActivity()).getAccount(new MyActivity.OnAccountPickedListener() {
                            @Override
                            public void onAccountPicked(@NonNull String account) {
                                Intent intent = new Intent(getActivity(), DownloadFromDriveService.class);
                                intent.putExtra(DownloadFromDriveService.tag_account, account);
                                intent.putExtra(DownloadFromDriveService.tag_driveId, tripFiles[position].driveId);
                                getActivity().startService(intent);
                            }
                        }, false);
                    } else if (which == 1) {
                        Intent intent = new Intent(getActivity(), ViewTripActivity.class);
                        intent.putExtra(ViewTripActivity.tag_tripName, tripFiles[position].getName());
                        intent.putExtra(ViewTripActivity.tag_fromDrive, true);
                        intent.putExtra(ViewTripActivity.tag_driveId, tripFiles[position].driveId);
                        startActivity(intent);
                    }
                }
            });
            ab.show();

        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            checks[position] = checked;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.drive_trip_menu, menu);
            onActionMode = true;
            checks = new boolean[tripFiles.length];
            Arrays.fill(checks, false);
            checkAll = false;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            final ArrayList<DriveDocumentFile> checkedFiles = new ArrayList<>();
            for (int i = 0; i < checks.length; i++) {
                if (checks[i]) {
                    checkedFiles.add(tripFiles[i]);
                }
            }
            mode.finish();
            switch (item.getItemId()) {
                case R.id.selectall:
                    for (int i = 0; i < listView.getCount(); i++) {
                        listView.setItemChecked(i, !checkAll);
                    }
                    checkAll = !checkAll;
                    break;
                case R.id.delete:
                    AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity());
                    ab2.setTitle(getString(R.string.be_careful));
                    ab2.setMessage(getString(R.string.are_you_sure_to_delete));
                    ab2.setIcon(DrawableHelper.getAlertDrawable(getActivity()));
                    ab2.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            new UpdateDataTask(checkedFiles, UpdateDataTask.delete).execute();
                        }
                    });
                    ab2.setNegativeButton(getString(R.string.no), null);
                    ab2.show();
                    break;
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            onActionMode = false;
        }
    }

    @Override
    public void onRefresh() {
        loadData();
    }

    class UpdateDataTask extends AsyncTask<Void, Void, Void> {

        ArrayList<DriveDocumentFile> files;
        int option;
        public static final int delete = 1;

        public UpdateDataTask(ArrayList<DriveDocumentFile> files, int option) {
            this.files = files;
            this.option = option;
        }

        @Override
        protected Void doInBackground(Void... params) {
            switch (option) {
                case delete:
                    for (DriveDocumentFile file : files) {
                        file.delete();
                    }
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadData();
        }
    }
}
