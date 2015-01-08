package com.yupog2003.tripdiary.fragments;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
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
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.SearchView.OnQueryTextListener;
import android.text.format.Time;
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

import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.yupog2003.tripdiary.AllRecordActivity;
import com.yupog2003.tripdiary.MainActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.ColorHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.TimeAnalyzer;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.services.SendTripService;
import com.yupog2003.tripdiary.views.CheckableLayout;
import com.yupog2003.tripdiary.views.FloatingGroupExpandableListView;
import com.yupog2003.tripdiary.views.WrapperExpandableListAdapter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class LocalTripsFragment extends Fragment {
	String path;
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
	public static final String tag_path = "tagPath";
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
		this.path = MainActivity.rootPath;
		loaddata();
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
			View layout = getActivity().getLayoutInflater().inflate(R.layout.edit_trip, null);
			final RadioGroup rg = (RadioGroup) layout.findViewById(R.id.categories);
			final TextView category = (TextView) layout.findViewById(R.id.category);
			for (int i = 0; i < categories.length; i++) {
				RadioButton rb = new RadioButton(getActivity());
				rb.setText(categories[i]);
				rb.setId(i);
				rg.addView(rb);
				if (categories[i].equals("nocategory")) {
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
					String category = "nocategory";
					if (rg.getCheckedRadioButtonId() >= 0 && rg.getCheckedRadioButtonId() < categories.length) {
						category = categories[rg.getCheckedRadioButtonId()];
					}
					if (name.length() > 0) {
						if (new File(path + "/" + name).exists()) {
							Toast.makeText(getActivity(), getString(R.string.explain_same_trip_when_import), Toast.LENGTH_SHORT).show();
						} else {
							newTripName = name;
							Trip trip = new Trip(getActivity(), new File(path + "/" + name));
							trip.setCategory(getActivity(), category);
							trip.updateNote(note);
							loaddata();
							Intent i = new Intent(Intent.ACTION_GET_CONTENT);
							i.setType("application/gpx+xml");
							startActivityForResult(Intent.createChooser(i, getString(R.string.select_the_gpx)), R.id.importtrip);
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
			startActivityForResult(Intent.createChooser(i, getString(R.string.select_the_file_to_restore)), R.id.restoretrip);
		}
		return false;
	}

	public void loaddata() {
		if (path == null) {
			if (getActivity() != null) {
				path = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("rootpath", Environment.getExternalStorageDirectory() + "/TripDiary");
				FileHelper.maintenDir(new File(path));
			} else {
				return;
			}
		}
		adapter = new TripAdapter(path);
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

	class TripAdapter extends BaseExpandableListAdapter implements OnChildClickListener, ExpandableListView.MultiChoiceModeListener, OnQueryTextListener, OnGroupCollapseListener, OnGroupExpandListener {

		String[] categories;
		ArrayList<ArrayList<TripInformation>> trips;
		TripInformation[] tripsArray;
		HashMap<String, Drawable> categoryDrawables;
		boolean onActionMode = false;
		int count = 0;
		public ArrayList<String> checksName;

		public TripAdapter(String path) {
			Map<String, ?> map = categorysp.getAll();
			Set<String> set = map.keySet();
			categories = set.toArray(new String[set.size()]);
			categoryDrawables = new HashMap<String, Drawable>();
			for (int i = 0; i < categories.length; i++) {
				try {
					categoryDrawables.put(categories[i], ColorHelper.getColorDrawable(getActivity(), 40, Integer.valueOf(categorysp.getString(categories[i], String.valueOf(Color.WHITE)))));
				} catch (NumberFormatException e) {
					categoryeditor.putString(categories[i], String.valueOf(Color.WHITE));
					i--;
					e.printStackTrace();
				}
			}
			trips = new ArrayList<ArrayList<TripInformation>>();
			File file = new File(path);
			File[] files = file.listFiles(FileHelper.getDirFilter());
			if (files == null)
				files = new File[0];
			ArrayList<TripInformation> list = new ArrayList<TripInformation>();
			for (int i = 0; i < files.length; i++) {
				list.add(new TripInformation(files[i]));
			}
			Collections.sort(list, new Comparator<TripInformation>() {

				public int compare(TripInformation lhs, TripInformation rhs) {

					if (lhs.time == null || rhs.time == null)
						return 0;
					else if (lhs.time.after(rhs.time))
						return 1;
					else if (rhs.time.after(lhs.time))
						return -1;
					else
						return 0;
				}
			});
			tripsArray = list.toArray(new TripInformation[list.size()]);
			list = new ArrayList<TripInformation>();
			for (int i = 0; i < categories.length; i++) {
				ArrayList<TripInformation> category = new ArrayList<TripInformation>();
				for (int j = 0; j < tripsArray.length; j++) {
					if (tripsArray[j].category.equals(categories[i])) {
						category.add(tripsArray[j]);
						list.add(tripsArray[j]);
					}
				}
				trips.add(category);
			}
			tripsArray = list.toArray(new TripInformation[list.size()]);
		}

		class TripInformation {
			public File file;
			public Time time;
			public String category;
			public Drawable drawable;

			public TripInformation(File file) {
				this.file = file;
				this.time = TimeAnalyzer.getTripTime(path, file.getName());
				this.category = tripsp.getString(file.getName(), "nocategory");
				this.drawable = categoryDrawables.get(category);
			}
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
				CheckableLayout layout = (CheckableLayout) getActivity().getLayoutInflater().inflate(R.layout.trip_list_item, null);
				holder.item = (TextView) layout.findViewById(R.id.tripname);
				holder.itemextra = (TextView) layout.findViewById(R.id.tripextra);
				layout.setPadding(50, 0, 0, 0);
				convertView = layout;
				convertView.setTag(holder);
			}
			holder = (ViewHolder) convertView.getTag();
			TripInformation trip = trips.get(groupPosition).get(childPosition);
			String tripname = trip.file.getName();
			holder.item.setText(tripname);
			holder.item.setCompoundDrawablesWithIntrinsicBounds(trip.drawable, null, null, null);
			holder.itemextra.setText("-" + TimeAnalyzer.formatInTimezone(trip.time, TimeAnalyzer.getTripTimeZone(getActivity(), tripname)));
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
				View layout = getActivity().getLayoutInflater().inflate(R.layout.trip_group_item, null);
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
				String name = trips.get(groupPosition).get(childPosition).file.getName();
				Intent i = new Intent(getActivity(), ViewTripActivity.class);
				i.putExtra("path", path);
				i.putExtra("name", name);
				EasyTracker.getInstance(getActivity()).send(MapBuilder.createEvent("Trip", "view", name, null).build());
				getActivity().startActivity(i);
			}
			return true;
		}

		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

			checksName = new ArrayList<String>();
			for (int i = 0; i < checks.length; i++) {
				if (checks[i]) {
					checksName.add(tripsArray[i].file.getName());
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
							FileHelper.deletedir(path + "/" + checksName.get(i));
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
				editBackupPath.setText(path);
				ab3.setView(editBackupPath);
				ab3.setPositiveButton(getString(R.string.enter), new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						final String backupPath = editBackupPath.getText().toString();
						File file = new File(backupPath);
						if (file.exists() && file.isFile()) {
							Toast.makeText(getActivity(), getString(R.string.error_please_use_another_path), Toast.LENGTH_LONG).show();
						} else {
							file.mkdirs();
							ArrayList<File> froms = new ArrayList<File>();
							ArrayList<File> tos = new ArrayList<File>();
							for (int i = 0; i < checksName.size(); i++) {
								froms.add(new File(path + "/" + checksName.get(i)));
								tos.add(new File(backupPath + "/" + checksName.get(i) + ".zip"));
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
					BufferedReader br = new BufferedReader(new FileReader(path + "/" + checksName.get(0) + "/note"));
					while ((s = br.readLine()) != null) {
						message += s + "\n";
					}
					br.close();
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
				AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity());
				View layout = getActivity().getLayoutInflater().inflate(R.layout.edit_trip, null);
				final RadioGroup rg = (RadioGroup) layout.findViewById(R.id.categories);
				String originalcategory = tripsp.getString(checksName.get(0), "nocategory");
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
						if (!new File(path + "/" + s).exists() || s.equals(checksName.get(0))) {
							Trip trip = new Trip(getActivity(), new File(path + "/" + checksName.get(0)));
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
				String[] tripPaths = new String[checksName.size()];
				for (int i = 0; i < tripPaths.length; i++) {
					tripPaths[i] = checksName.get(i);
				}
				intent.putExtra(AllRecordActivity.tag_trip_paths, tripPaths);
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

			mode = null;
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
			for (int i = 0; i < checks.length; i++) {
				if (checks[i])
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
				final ArrayList<String> founds = new ArrayList<String>();
				int adaptercount = tripsArray.length;
				for (int i = 0; i < adaptercount; i++) {
					String itemname = tripsArray[i].file.getName();
					if (itemname.toLowerCase(Locale.US).contains(searchname)) {
						founds.add(itemname);
					}
				}
				if (founds.size() == 0) {
					Toast.makeText(getActivity(), getString(R.string.trip_not_found), Toast.LENGTH_SHORT).show();
				} else if (founds.size() == 1) {
					String name = founds.get(0);
					Intent intent = new Intent(getActivity(), ViewTripActivity.class);
					intent.putExtra("path", path);
					intent.putExtra("name", name);
					getActivity().startActivity(intent);
				} else {
					AlertDialog.Builder choose = new AlertDialog.Builder(getActivity());
					choose.setTitle(getString(R.string.choose_the_trip));
					choose.setSingleChoiceItems(founds.toArray(new String[founds.size()]), -1, new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							String name = founds.get(which);
							Intent intent = new Intent(getActivity(), ViewTripActivity.class);
							intent.putExtra("path", path);
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

	class CompressTask extends AsyncTask<String, String, ArrayList<File>> {
		ProgressDialog pd;
		ArrayList<File> froms, tos;

		public CompressTask(ArrayList<File> froms, ArrayList<File> tos) {
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
		protected ArrayList<File> doInBackground(String... params) {

			if (froms != null && tos != null) {
				for (int i = 0; i < froms.size(); i++) {
					File from = froms.get(i);
					publishProgress(getString(R.string.compressing) + ":" + from.getName() + "...");
					File to = tos.get(i);
					if (to.exists())
						to.delete();
					FileHelper.zip(from, to);
				}
			}
			return tos;
		}

		@Override
		protected void onPostExecute(ArrayList<File> result) {
			pd.dismiss();
			Toast.makeText(getActivity(), getString(R.string.backed_up_file) + ":" + path, Toast.LENGTH_LONG).show();
			Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
			intent.setType("application/zip");
			ArrayList<Uri> uris = new ArrayList<Uri>();
			for (int i = 0; i < tos.size(); i++)
				uris.add(Uri.fromFile(tos.get(i)));
			intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
			startActivity(Intent.createChooser(intent, getString(R.string.backup_to)));
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			Uri uri = data.getData();
			if (requestCode == R.id.importtrip) {
				if (uri == null)
					return;
				String importPath = uri.getPath();
				if (importPath == null || newTripName == null || path == null)
					return;
				FileHelper.copyFile(new File(importPath), new File(path + "/" + newTripName + "/" + newTripName + ".gpx"));
			} else if (requestCode == R.id.restoretrip) {
				if (uri == null)
					return;
				final String zipPath = uri.getPath();
				if (zipPath == null)
					return;
				final String tripName = zipPath.substring(zipPath.lastIndexOf("/") + 1, zipPath.contains(".") ? zipPath.lastIndexOf(".") : zipPath.length());
				if (new File(path + "/" + tripName).exists()) {
					AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
					ab.setTitle(getString(R.string.same_trip));
					ab.setMessage(getString(R.string.ask_for_replace_trip));
					ab.setPositiveButton(getString(R.string.enter), new OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {

							FileHelper.deletedir(path + "/" + tripName);
							FileHelper.unZip(zipPath, path + "/");
							loaddata();
						}
					});
					ab.setNegativeButton(getString(R.string.cancel), null);
					ab.show();
				} else {
					FileHelper.unZip(zipPath, path + "/");
					loaddata();
				}
			} else if (requestCode == REQUEST_GET_TOKEN) {
				if (resultCode == Activity.RESULT_OK) {
					new GetAccessTokenTask().execute();
				}
			}
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
						intent.putExtra(SendTripService.filePathTag, path + "/" + adapter.checksName.get(i));
						intent.putExtra(SendTripService.accountTag, account);
						intent.putExtra(SendTripService.tokenTag, token);
						intent.putExtra(SendTripService.publicTag, uploadPublic);
						getActivity().startService(intent);
						EasyTracker.getInstance(getActivity()).send(MapBuilder.createEvent("Trip", "share_track_by_tripdiary", adapter.checksName.get(i), null).build());
					}
				}
			} catch (UserRecoverableAuthException e) {

				e.printStackTrace();
				startActivityForResult(e.getIntent(), REQUEST_GET_TOKEN);
			} catch (IOException e) {

				e.printStackTrace();
			} catch (GoogleAuthException e) {

				e.printStackTrace();
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
			LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.progressdialog_import_memory, null);
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
					BufferedReader br = new BufferedReader(new FileReader(new File(path + "/" + tripName + "/" + tripName + ".gpx")));
					String s;
					while ((s = br.readLine()) != null) {
						if (s.startsWith("<trkpt ")) {
							String[] toks = s.split("\"");
							double lat, lng;
							if (s.indexOf("lat") > s.indexOf("lon")) {
								lat = Double.parseDouble(toks[3]);
								lng = Double.parseDouble(toks[1]);
							} else {
								lat = Double.parseDouble(toks[1]);
								lng = Double.parseDouble(toks[3]);
							}
							TimeAnalyzer.updateTripTimeZoneFromLatLng(getActivity(), tripName, lat, lng);
							new File(path + "/" + tripName + "/" + tripName + ".gpx.cache").delete();
							break;
						}
					}
					br.close();
				} catch (FileNotFoundException e) {

					e.printStackTrace();
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
