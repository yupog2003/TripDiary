package com.yupog2003.tripdiary.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.provider.DocumentFile;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupCollapseListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.yupog2003.tripdiary.AllRecordActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.ColorHelper;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.services.SendTripService;
import com.yupog2003.tripdiary.views.CheckableLayout;
import com.yupog2003.tripdiary.views.FloatingGroupExpandableListView;
import com.yupog2003.tripdiary.views.WrapperExpandableListAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class LocalTripsFragment extends Fragment {
    String account;
    String token;
    ExpandableListView listView;
    public String newTripName;
    TripAdapter adapter;
    SearchView search;
    SharedPreferences categorysp;
    SharedPreferences.Editor categoryeditor;
    SharedPreferences tripsp;
    SharedPreferences.Editor tripeditor;
    SharedPreferences categoryExpandSp;
    SharedPreferences.Editor categoryExpandEditor;
    private static final int REQUEST_GET_TOKEN = 0;
    private static final int REQUEST_RESTORE_TRIP = 1;
    private static final int REQUEST_IMPORT_TRIP = 2;
    public static final String pref_timesort = "timesort";
    boolean uploadPublic;

    public LocalTripsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        listView = new FloatingGroupExpandableListView(getActivity());
        listView.setGroupIndicator(null);
        setHasOptionsMenu(true);
        categorysp = getActivity().getSharedPreferences("category", Context.MODE_PRIVATE);
        tripsp = getActivity().getSharedPreferences("trip", Context.MODE_PRIVATE);
        categoryExpandSp = getActivity().getSharedPreferences("categoryExpand", Context.MODE_PRIVATE);
        categoryeditor = categorysp.edit();
        tripeditor = tripsp.edit();
        categoryExpandEditor = categoryExpandSp.edit();
        return listView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        if (outState.isEmpty()) {
            outState.putBoolean("bug:fix", true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adapter == null) {
            loaddata();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_local_trip, menu);
        MenuItem searchItem = menu.findItem(R.id.searchview);
        search = (SearchView) MenuItemCompat.getActionView(searchItem);
        search.setQueryHint(getString(R.string.search_trip));
        search.setOnQueryTextListener(adapter);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.importtrip) {
            Map<String, String> map = (Map<String, String>) categorysp.getAll();
            Set<String> keyset = map.keySet();
            final String[] categories = keyset.toArray(new String[keyset.size()]);
            AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity());
            View layout = getActivity().getLayoutInflater().inflate(R.layout.edit_trip, (ViewGroup) getView(), false);
            final RadioGroup rg = (RadioGroup) layout.findViewById(R.id.categories);
            final TextView category = (TextView) layout.findViewById(R.id.category);
            for (int i = 0; i < categories.length; i++) {
                RadioButton rb = new RadioButton(getActivity());
                rb.setText(categories[i]);
                rb.setId(i);
                rg.addView(rb);
                if (categories[i].equals(getString(R.string.nocategory))) {
                    rg.check(i);
                    String color = categorysp.getString(categories[i], String.valueOf(Color.WHITE));
                    category.setCompoundDrawablesWithIntrinsicBounds(ColorHelper.getColorDrawable(getActivity(), 50, Integer.valueOf(color)), null, null, null);
                }
            }
            rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                public void onCheckedChanged(RadioGroup group, int checkedId) {

                    String color = categorysp.getString(categories[checkedId], String.valueOf(Color.WHITE));
                    category.setCompoundDrawablesWithIntrinsicBounds(ColorHelper.getColorDrawable(getActivity(), 50, Integer.valueOf(color)), null, null, null);
                }
            });
            ab2.setTitle(getString(R.string.import_trip_with_gpx));
            ab2.setView(layout);
            final EditText tripName = (EditText) layout.findViewById(R.id.tripname);
            final EditText tripNote = (EditText) layout.findViewById(R.id.tripnote);
            ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    String name = tripName.getText().toString();
                    String note = tripNote.getText().toString();
                    String category = getString(R.string.nocategory);
                    if (rg.getCheckedRadioButtonId() >= 0 && rg.getCheckedRadioButtonId() < categories.length) {
                        category = categories[rg.getCheckedRadioButtonId()];
                    }
                    if (name.length() > 0) {
                        if (FileHelper.findfile(TripDiaryApplication.rootDocumentFile, name) != null) {
                            Toast.makeText(getActivity(), getString(R.string.explain_same_trip_when_import), Toast.LENGTH_SHORT).show();
                        } else {
                            newTripName = name;
                            Trip trip = Trip.createTrip(getActivity(), newTripName);
                            trip.setCategory(getActivity(), category);
                            trip.updateNote(note);
                            loaddata();
                            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                            i.setType("application/gpx+xml");
                            startActivityForResult(Intent.createChooser(i, getString(R.string.select_the_gpx)), REQUEST_IMPORT_TRIP);
                        }
                    } else {
                        Toast.makeText(getActivity(), getString(R.string.input_the_trip_name), Toast.LENGTH_SHORT).show();
                    }
                }
            });
            ab2.setNegativeButton(getString(R.string.cancel), null);
            AlertDialog ad = ab2.create();
            ad.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            ad.show();
        } else if (item.getItemId() == R.id.restoretrip) {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.setType("application/zip");
            startActivityForResult(Intent.createChooser(i, getString(R.string.select_the_file_to_restore)), REQUEST_RESTORE_TRIP);
        } else if (item.getItemId() == R.id.timeSort) {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            int nowSelection = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(pref_timesort, 0);
            ab.setSingleChoiceItems(new String[]{getString(R.string.ascending), getString(R.string.descending)}, nowSelection, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    PreferenceManager.getDefaultSharedPreferences(getActivity()).edit().putInt(pref_timesort, which).commit();
                    loaddata();
                    dialog.dismiss();
                }
            });
            ab.show();
        }
        return false;
    }

    public void loaddata() {
        adapter = new TripAdapter();
        WrapperExpandableListAdapter adapter2 = new WrapperExpandableListAdapter(adapter);
        listView.setAdapter(adapter2);
        listView.setOnChildClickListener(adapter);
        listView.setOnGroupCollapseListener(adapter);
        listView.setOnGroupExpandListener(adapter);
        listView.setChoiceMode(ExpandableListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setLongClickable(true);
        listView.setMultiChoiceModeListener(adapter);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            if (categoryExpandSp.getBoolean((String) adapter.getGroup(i), true)) {
                listView.expandGroup(i);
            } else {
                listView.collapseGroup(i);
            }
        }
    }

    enum TimeSort {
        ascending,
        descending;

        public static TimeSort fromInteger(int i) {
            if (i == 0) {
                return ascending;
            } else {
                return descending;
            }
        }
    }

    class TripAdapter extends BaseExpandableListAdapter implements OnChildClickListener, ExpandableListView.MultiChoiceModeListener, OnQueryTextListener, OnGroupCollapseListener, OnGroupExpandListener {

        String[] categories;
        ArrayList<ArrayList<Trip>> trips;
        Trip[] tripsArray;
        HashMap<String, Drawable> categoryDrawables;
        boolean onActionMode = false;
        public ArrayList<String> checksName;

        public TripAdapter() {
            Map<String, ?> map = categorysp.getAll();
            Set<String> set = map.keySet();
            categories = set.toArray(new String[set.size()]);
            categoryDrawables = new HashMap<>();
            for (int i = 0; i < categories.length; i++) {
                try {
                    categoryDrawables.put(categories[i], ColorHelper.getColorDrawable(getActivity(), 40, Integer.valueOf(categorysp.getString(categories[i], String.valueOf(Color.WHITE)))));
                } catch (NumberFormatException e) {
                    categoryeditor.putString(categories[i], String.valueOf(Color.WHITE));
                    i--;
                    e.printStackTrace();
                }
            }
            trips = new ArrayList<>();
            DocumentFile[] files = FileHelper.listFiles(TripDiaryApplication.rootDocumentFile, FileHelper.list_dirs);
            ArrayList<Trip> list = new ArrayList<>();
            for (DocumentFile file : files) {
                Trip trip = new Trip(getActivity(), file, true);
                trip.setDrawable(categoryDrawables.get(trip.category));
                list.add(trip);
            }
            final TimeSort timeSort = TimeSort.fromInteger(PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt(pref_timesort, 0));
            if (timeSort == TimeSort.ascending){
                Collections.sort(list, new Comparator<Trip>() {
                    @Override
                    public int compare(Trip lhs, Trip rhs) {
                        if (lhs.time == null || rhs.time == null)
                            return 0;
                        if (lhs.time.after(rhs.time)) {
                            return 1;
                        } else if (rhs.time.after(lhs.time)) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
            }else if (timeSort == TimeSort.descending){
                Collections.sort(list, new Comparator<Trip>() {
                    @Override
                    public int compare(Trip lhs, Trip rhs) {
                        if (lhs.time == null || rhs.time == null)
                            return 0;
                        if (lhs.time.after(rhs.time)) {
                            return -1;
                        } else if (rhs.time.after(lhs.time)) {
                            return 1;
                        } else {
                            return 0;
                        }
                    }
                });
            }
            tripsArray = list.toArray(new Trip[list.size()]);
            list = new ArrayList<>();
            for (String categorySyr : categories) {
                ArrayList<Trip> category = new ArrayList<>();
                for (Trip trip : tripsArray) {
                    if (trip.category.equals(categorySyr)) {
                        category.add(trip);
                        list.add(trip);
                    }
                }
                trips.add(category);
            }
            tripsArray = list.toArray(new Trip[list.size()]);
        }

        public Object getChild(int groupPosition, int childPosition) {

            return trips.get(groupPosition).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {

            if (!listView.isGroupExpanded(groupPosition))
                return -1;
            int count = 0;
            for (int i = 0; i < groupPosition; i++) {
                if (listView.isGroupExpanded(i)) {
                    count += 1 + trips.get(i).size();
                } else {
                    count++;
                }
            }
            count += 1 + childPosition;
            return count;
        }

        class ViewHolder {
            TextView item;
            TextView itemextra;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                CheckableLayout layout = (CheckableLayout) getActivity().getLayoutInflater().inflate(R.layout.trip_list_item, parent, false);
                holder.item = (TextView) layout.findViewById(R.id.tripname);
                holder.itemextra = (TextView) layout.findViewById(R.id.tripextra);
                layout.setPadding(50, 0, 0, 0);
                convertView = layout;
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            Trip trip = trips.get(groupPosition).get(childPosition);
            String tripname = trip.tripName;
            holder.item.setText(tripname);
            holder.item.setCompoundDrawablesWithIntrinsicBounds(trip.drawable, null, null, null);
            holder.itemextra.setText("-" + trip.time.formatInTimezone(MyCalendar.getTripTimeZone(getActivity(), tripname)));
            ((CheckableLayout) convertView).setOnMultiChoiceMode(onActionMode);
            return convertView;
        }

        public int getChildrenCount(int groupPosition) {

            return trips.get(groupPosition).size();
        }

        public Object getGroup(int groupPosition) {

            return categories[groupPosition];
        }

        public int getGroupCount() {

            return categories.length;
        }

        public long getGroupId(int groupPosition) {

            return groupPosition;
        }

        class GroupViewHolder {
            TextView title;
            TextView count;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

            GroupViewHolder holder;
            if (convertView == null) {
                holder = new GroupViewHolder();
                View layout = getActivity().getLayoutInflater().inflate(R.layout.trip_group_item, parent, false);
                holder.title = (TextView) layout.findViewById(R.id.title);
                holder.count = (TextView) layout.findViewById(R.id.count);
                convertView = layout;
                convertView.setTag(holder);
            }
            holder = (GroupViewHolder) convertView.getTag();
            holder.title.setCompoundDrawablesWithIntrinsicBounds(isExpanded ? R.drawable.indicator_expand2 : R.drawable.indicator_collapse2, 0, 0, 0);
            holder.title.setText(categories[groupPosition]);
            holder.count.setText("(" + String.valueOf(getChildrenCount(groupPosition)) + ")");
            return convertView;
        }

        public boolean hasStableIds() {

            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {

            return true;
        }

        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

            if (onActionMode) {
                int position = (int) getChildId(groupPosition, childPosition);
                listView.setItemChecked(position, !listView.isItemChecked(position));
            } else {
                String name = trips.get(groupPosition).get(childPosition).tripName;
                Intent i = new Intent(getActivity(), ViewTripActivity.class);
                i.putExtra("name", name);
                DeviceHelper.sendGATrack(getActivity(), "Trip", "view", name, null);
                getActivity().startActivity(i);
            }
            return true;
        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            checksName = new ArrayList<>();
            for (int i = 0; i < checks.length; i++) {
                if (checks[i]) {
                    checksName.add(tripsArray[i].tripName);
                }
            }
            mode.finish();
            if (checksName.size() == 0)
                return false;
            if (item.getItemId() == R.id.delete) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle(getString(R.string.be_careful));
                ab.setMessage(getString(R.string.are_you_sure_to_delete));
                ab.setIcon(R.drawable.ic_alert);
                ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < checksName.size(); i++) {
                            FileHelper.findfile(TripDiaryApplication.rootDocumentFile, checksName.get(i)).delete();
                            tripeditor.remove(checksName.get(i));
                            tripeditor.commit();
                        }
                        loaddata();
                    }
                });
                ab.setNegativeButton(getString(R.string.cancel), null);
                ab.show();
            } else if (item.getItemId() == R.id.backup) {
                AlertDialog.Builder ab3 = new AlertDialog.Builder(getActivity());
                ab3.setTitle(getString(R.string.backup_path));
                final EditText editBackupPath = new EditText(getActivity());
                editBackupPath.setText(Environment.getExternalStorageDirectory().getPath());
                ab3.setView(editBackupPath);
                ab3.setPositiveButton(getString(R.string.enter), new OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        final String backupPath = editBackupPath.getText().toString();
                        File file = new File(backupPath);
                        if (file.exists() && file.isFile()) {
                            Toast.makeText(getActivity(), getString(R.string.error_please_use_another_path), Toast.LENGTH_LONG).show();
                        } else {
                            file.mkdirs();
                            ArrayList<DocumentFile> froms = new ArrayList<>();
                            ArrayList<DocumentFile> tos = new ArrayList<>();
                            for (int i = 0; i < checksName.size(); i++) {
                                froms.add(FileHelper.findfile(TripDiaryApplication.rootDocumentFile, checksName.get(i)));
                                tos.add(DocumentFile.fromFile(new File(backupPath + "/" + checksName.get(i) + ".zip")));
                            }
                            new CompressTask(froms, tos).execute("");
                        }

                    }
                });
                ab3.setNegativeButton(getString(R.string.cancel), null);
                ab3.show();
            } else if (item.getItemId() == R.id.edit) {
                String message = "";
                String s;
                try {
                    DocumentFile noteFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, checksName.get(0), "note");
                    BufferedReader br = new BufferedReader(new InputStreamReader(getActivity().getContentResolver().openInputStream(noteFile.getUri())));
                    while ((s = br.readLine()) != null) {
                        message += s + "\n";
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity());
                View layout = getActivity().getLayoutInflater().inflate(R.layout.edit_trip, (ViewGroup) getView(), false);
                final RadioGroup rg = (RadioGroup) layout.findViewById(R.id.categories);
                String originalcategory = tripsp.getString(checksName.get(0), getString(R.string.nocategory));
                Map<String, ?> map = categorysp.getAll();
                Set<String> keyset = map.keySet();
                final TextView category = (TextView) layout.findViewById(R.id.category);
                final String[] categories = keyset.toArray(new String[keyset.size()]);
                for (int i = 0; i < categories.length; i++) {
                    RadioButton rb = new RadioButton(getActivity());
                    rb.setText(categories[i]);
                    rb.setId(i);
                    rg.addView(rb);
                    if (originalcategory.equals(categories[i])) {
                        rg.check(i);
                        String color = categorysp.getString(categories[i], String.valueOf(Color.WHITE));
                        category.setCompoundDrawablesWithIntrinsicBounds(ColorHelper.getColorDrawable(getActivity(), 50, Integer.valueOf(color)), null, null, null);
                    }
                }
                rg.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        String color = categorysp.getString(categories[checkedId], String.valueOf(Color.WHITE));
                        category.setCompoundDrawablesWithIntrinsicBounds(ColorHelper.getColorDrawable(getActivity(), 50, Integer.valueOf(color)), null, null, null);
                    }
                });
                ab2.setTitle(getString(R.string.edit));
                ab2.setView(layout);
                final EditText name = (EditText) layout.findViewById(R.id.tripname);
                name.setText(checksName.get(0));
                final EditText note = (EditText) layout.findViewById(R.id.tripnote);
                note.setText(message);
                ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        String s = name.getText().toString();
                        if (FileHelper.findfile(TripDiaryApplication.rootDocumentFile, s) == null || s.equals(checksName.get(0))) {
                            Trip trip = new Trip(getActivity(), FileHelper.findfile(TripDiaryApplication.rootDocumentFile, checksName.get(0)), false);
                            trip.renameTrip(getActivity(), s);
                            trip.updateNote(note.getText().toString());
                            trip.setCategory(getActivity(), categories[rg.getCheckedRadioButtonId()]);
                            loaddata();
                        } else {
                            Toast.makeText(getActivity(), getString(R.string.explain_conflict_trip), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
                ab2.setNegativeButton(getString(R.string.cancel), null);
                AlertDialog ad = ab2.create();
                ad.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                ad.show();
            } else if (item.getItemId() == R.id.selectall) {
                for (int i = 0; i < listView.getCount(); i++) {
                    listView.setItemChecked(i, !checkAll);
                }
                checkAll = !checkAll;
            } else if (item.getItemId() == R.id.upload) {
                Account[] accounts = AccountManager.get(getActivity()).getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                if (accounts != null && accounts.length > 0) {
                    account = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("account", accounts[0].name);
                    AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                    ab.setTitle(getString(R.string.make_it_public));
                    ab.setMessage(getString(R.string.make_it_public_to_share));
                    ab.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            uploadPublic = true;
                            new GetAccessTokenTask().execute();
                        }
                    });
                    ab.setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            uploadPublic = false;
                            new GetAccessTokenTask().execute();
                        }
                    });
                    ab.show();
                }
            } else if (item.getItemId() == R.id.category) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle(getString(R.string.choose_a_category));
                ab.setSingleChoiceItems(categories, -1, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < checksName.size(); i++) {
                            tripeditor.putString(checksName.get(i), categories[which]);
                        }
                        tripeditor.commit();
                        dialog.dismiss();
                        loaddata();
                    }
                });
                ab.show();
            } else if (item.getItemId() == R.id.timezone) {
                new UpdateTripTimeZoneTask(checksName).execute();
            } else if (item.getItemId() == R.id.viewtogether) {
                Intent intent = new Intent(getActivity(), AllRecordActivity.class);
                String[] tripNames = checksName.toArray(new String[checksName.size()]);
                intent.putExtra(AllRecordActivity.tag_trip_names, tripNames);
                getActivity().startActivity(intent);
            }
            return true;
        }

        boolean[] checks;
        boolean checkAll;

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.local_trip_menu, menu);
            onActionMode = true;
            checks = new boolean[tripsArray.length];
            for (int i = 0; i < checks.length; i++)
                checks[i] = false;
            checkAll = false;
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
            onActionMode = false;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return false;
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            for (int i = 0; i < trips.size(); i++) {
                for (int j = 0; j < trips.get(i).size(); j++) {
                    if (getChildId(i, j) == position) {
                        int count = 0;
                        for (int k = 0; k < i; k++) {
                            count += trips.get(k).size();
                        }
                        count += j;
                        checks[count] = checked;
                    }
                }
            }
            int selects = 0;
            for (boolean check : checks) {
                if (check)
                    selects++;
            }
            mode.getMenu().findItem(R.id.edit).setVisible(!(selects > 1));

        }

        public boolean onQueryTextSubmit(String query) {

            final String searchname = search.getQuery().toString().toLowerCase(Locale.US);
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
            search.clearFocus();
            if (!searchname.equals("")) {
                final ArrayList<String> founds = new ArrayList<>();
                for (Trip trip : tripsArray) {
                    String itemname = trip.tripName;
                    if (itemname.toLowerCase(Locale.US).contains(searchname)) {
                        founds.add(itemname);
                    }
                }
                if (founds.size() == 0) {
                    Toast.makeText(getActivity(), getString(R.string.trip_not_found), Toast.LENGTH_SHORT).show();
                } else if (founds.size() == 1) {
                    String name = founds.get(0);
                    Intent intent = new Intent(getActivity(), ViewTripActivity.class);
                    intent.putExtra("name", name);
                    getActivity().startActivity(intent);
                } else {
                    AlertDialog.Builder choose = new AlertDialog.Builder(getActivity());
                    choose.setTitle(getString(R.string.choose_the_trip));
                    choose.setSingleChoiceItems(founds.toArray(new String[founds.size()]), -1, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            String name = founds.get(which);
                            Intent intent = new Intent(getActivity(), ViewTripActivity.class);
                            intent.putExtra("name", name);
                            dialog.dismiss();
                            getActivity().startActivity(intent);
                        }
                    });
                    choose.show();
                }
            }
            return true;
        }

        public boolean onQueryTextChange(String newText) {

            return false;
        }

        public void onGroupExpand(int groupPosition) {

            categoryExpandEditor.putBoolean(categories[groupPosition], true);
            categoryExpandEditor.commit();
        }

        public void onGroupCollapse(int groupPosition) {

            categoryExpandEditor.putBoolean(categories[groupPosition], false);
            categoryExpandEditor.commit();
        }
    }

    class CompressTask extends AsyncTask<String, String, ArrayList<DocumentFile>> {
        ProgressDialog pd;
        ArrayList<DocumentFile> froms, tos;

        public CompressTask(ArrayList<DocumentFile> froms, ArrayList<DocumentFile> tos) {
            if (froms.size() == tos.size()) {
                this.froms = froms;
                this.tos = tos;
            }
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getActivity());
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setTitle(getString(R.string.backup));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            pd.setMessage(progress[0]);
        }

        @Override
        protected ArrayList<DocumentFile> doInBackground(String... params) {

            if (froms != null && tos != null) {
                for (int i = 0; i < froms.size(); i++) {
                    DocumentFile from = froms.get(i);
                    publishProgress(getString(R.string.compressing) + ":" + FileHelper.getFileName(from) + "...");
                    DocumentFile to = tos.get(i);
                    if (to.exists())
                        to.delete();
                    FileHelper.zip(from, to);
                }
            }
            return tos;
        }

        @Override
        protected void onPostExecute(ArrayList<DocumentFile> result) {
            pd.dismiss();
            Toast.makeText(getActivity(), getString(R.string.backed_up_file), Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
            intent.setType("application/zip");
            ArrayList<Uri> uris = new ArrayList<>();
            for (int i = 0; i < tos.size(); i++)
                uris.add(tos.get(i).getUri());
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            startActivity(Intent.createChooser(intent, getString(R.string.backup_to)));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) {
            final Uri uri = data.getData();
            if (requestCode == REQUEST_IMPORT_TRIP) {
                if (uri == null || newTripName == null)
                    return;
                try {
                    InputStream is = getActivity().getContentResolver().openInputStream(uri);
                    DocumentFile outFile = TripDiaryApplication.rootDocumentFile.createDirectory(newTripName).createFile("", newTripName + ".gpx");
                    OutputStream os = getActivity().getContentResolver().openOutputStream(outFile.getUri());
                    FileHelper.copyByStream(is, os);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == REQUEST_RESTORE_TRIP) {
                if (uri == null)
                    return;
                new RestoreTripTask(uri).execute();
            } else if (requestCode == REQUEST_GET_TOKEN) {
                if (resultCode == Activity.RESULT_OK) {
                    new GetAccessTokenTask().execute();
                }
            }
        }
    }

    class RestoreTripTask extends AsyncTask<String, String, String> {

        Uri uri;
        ProgressDialog pd;
        String tripName;

        public RestoreTripTask(Uri uri) {
            this.uri = uri;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(getActivity());
            pd.setMessage(getString(R.string.unzipping));
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                final File tempZip = FileHelper.copyFromUriToFile(getActivity(), uri, getActivity().getCacheDir(), "tempZip.zip");
                if (tempZip != null) {
                    ZipInputStream zis = new ZipInputStream(new FileInputStream(tempZip));
                    ZipEntry entry;
                    String s = "";
                    if ((entry = zis.getNextEntry()) != null) {
                        s = entry.getName();
                    }
                    zis.close();
                    if (s.contains("/")){
                        s=s.replace("/", "");
                    }
                    tripName = s;
                    if (FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName) != null) {
                        finishAsk = false;
                        replace = false;
                        publishProgress("ask replace");
                        while (!finishAsk) {
                            Thread.sleep(200);
                        }
                        if (replace) {
                            FileHelper.unZip(tempZip, TripDiaryApplication.rootDocumentFile);
                            tempZip.delete();
                        }
                    } else {
                        FileHelper.unZip(tempZip, TripDiaryApplication.rootDocumentFile);
                        tempZip.delete();
                    }
                }
            } catch (IOException | NullPointerException | InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        boolean finishAsk;
        boolean replace;

        @Override
        protected void onProgressUpdate(String... values) {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.same_trip));
            ab.setMessage(getString(R.string.ask_for_replace_trip));
            ab.setCancelable(false);
            ab.setPositiveButton(getString(R.string.yes), new OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName).delete();
                    replace = true;
                    finishAsk = true;
                    dialog.dismiss();
                    pd.show();
                }
            });
            ab.setNegativeButton(getString(R.string.no), new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    replace = false;
                    finishAsk = true;
                    dialog.dismiss();
                    pd.show();
                }
            });
            pd.dismiss();
            ab.show();

        }

        @Override
        protected void onPostExecute(String s) {
            pd.dismiss();
            loaddata();
        }
    }

    class GetAccessTokenTask extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {

            try {
                token = GoogleAuthUtil.getToken(getActivity(), account, "oauth2:https://www.googleapis.com/auth/userinfo.email");
                if (adapter.checksName != null && getActivity() != null) {
                    for (int i = 0; i < adapter.checksName.size(); i++) {
                        Intent intent = new Intent(getActivity(), SendTripService.class);
                        intent.putExtra(SendTripService.tripNameTag, adapter.checksName.get(i));
                        intent.putExtra(SendTripService.accountTag, account);
                        intent.putExtra(SendTripService.tokenTag, token);
                        intent.putExtra(SendTripService.publicTag, uploadPublic);
                        getActivity().startService(intent);
                        DeviceHelper.sendGATrack(getActivity(), "Trip", "share_track_by_tripdiary", adapter.checksName.get(i), null);
                    }
                }
            } catch (UserRecoverableAuthException e) {

                e.printStackTrace();
                startActivityForResult(e.getIntent(), REQUEST_GET_TOKEN);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    class UpdateTripTimeZoneTask extends AsyncTask<String, String, String> {
        TextView message;
        ProgressBar progress;
        TextView progressMessage;
        AlertDialog dialog;
        boolean cancel = false;
        ArrayList<String> trips;

        public UpdateTripTimeZoneTask(ArrayList<String> trips) {
            this.trips = trips;
        }

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
        protected String doInBackground(String... params) {

            publishProgress("setMax", String.valueOf(trips.size()));
            for (int i = 0; i < trips.size(); i++) {
                if (cancel)
                    break;
                String tripName = trips.get(i);
                publishProgress(tripName, String.valueOf(i));
                try {
                    DocumentFile gpxFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, tripName + ".gpx");
                    BufferedReader br = new BufferedReader(new InputStreamReader(getActivity().getContentResolver().openInputStream(gpxFile.getUri())));
                    String s;
                    while ((s = br.readLine()) != null) {
                        if (s.contains("<trkpt ")) {
                            String[] toks = s.split("\"");
                            double lat, lng;
                            if (s.indexOf("lat") > s.indexOf("lon")) {
                                lat = Double.parseDouble(toks[3]);
                                lng = Double.parseDouble(toks[1]);
                            } else {
                                lat = Double.parseDouble(toks[1]);
                                lng = Double.parseDouble(toks[3]);
                            }
                            MyCalendar.updateTripTimeZoneFromLatLng(getActivity(), tripName, lat, lng);
                            DocumentFile cacheFile = FileHelper.findfile(gpxFile.getParentFile(), tripName + ".gpx.cache");
                            if (cacheFile != null) {
                                cacheFile.delete();
                            }
                            break;
                        }
                    }
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
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
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            super.onPostExecute(result);
        }
    }
}
