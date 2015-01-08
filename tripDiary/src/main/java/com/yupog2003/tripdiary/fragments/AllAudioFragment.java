package com.yupog2003.tripdiary.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.TextView;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.TimeAnalyzer;
import com.yupog2003.tripdiary.views.FloatingGroupExpandableListView;
import com.yupog2003.tripdiary.views.WrapperExpandableListAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class AllAudioFragment extends Fragment {
	POI[] pois;
	boolean[] expand;
	FloatingGroupExpandableListView listView;

	public AllAudioFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		setHasOptionsMenu(true);
		listView = new FloatingGroupExpandableListView(getActivity());
		listView.setGroupIndicator(null);
		// listView.setBackgroundColor(getResources().getColor(R.color.item_background));
		refresh();
		return listView;
	}

	@Override
	public void onResume() {

		super.onResume();
	}

	public void refresh() {
		this.pois = ViewTripActivity.trip.pois;
		if (expand == null) {
			expand = new boolean[pois.length];
			Arrays.fill(expand, false);
		}
		POIAdapter adapter = new POIAdapter(pois);
		WrapperExpandableListAdapter adapter2 = new WrapperExpandableListAdapter(adapter);
		listView.setAdapter(adapter2);
		listView.setOnChildClickListener(adapter);
		for (int i = 0; i < adapter.getGroupCount(); i++) {
			if (i >= expand.length)
				continue;
			if (expand[i]) {
				listView.expandGroup(i);
			} else {
				listView.collapseGroup(i);
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.fragment_all, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.expandall:
			expandAll();
			break;
		case R.id.collapseall:
			collapseAll();
			break;
		}
		return true;
	}

	public void expandAll() {
		int groupCount = listView.getExpandableListAdapter().getGroupCount();
		for (int i = 0; i < groupCount; i++) {
			listView.expandGroup(i);
		}
	}

	public void collapseAll() {
		int groupCount = listView.getExpandableListAdapter().getGroupCount();
		for (int i = 0; i < groupCount; i++) {
			listView.collapseGroup(i);
		}
	}

	class POIAdapter extends BaseExpandableListAdapter implements OnChildClickListener {
		POI[] pois;
		ArrayList<File[]> audios;

		public POIAdapter(POI[] pois) {
			this.pois = pois;
			audios = new ArrayList<File[]>();
			for (int i = 0; i < pois.length; i++) {
				audios.add(pois[i].audioFiles);
			}
		}

		public Object getChild(int groupPosition, int childPosition) {

			return audios.get(groupPosition)[childPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {

			return groupPosition * 1000 + childPosition;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

			TextView textView = new TextView(getActivity());
			textView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
			textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_music, 0, 0, 0);
			textView.setText(((File) getChild(groupPosition, childPosition)).getName());
			textView.setGravity(Gravity.CENTER_VERTICAL);
			textView.setPadding(50, 0, 0, 0);
			return textView;
		}

		public int getChildrenCount(int groupPosition) {

			return audios.get(groupPosition).length;
		}

		public Object getGroup(int groupPosition) {

			return pois[groupPosition];
		}

		public int getGroupCount() {

			return pois.length;
		}

		public long getGroupId(int groupPosition) {

			return groupPosition;
		}

		class GroupViewHolder {
			TextView poiName;
			TextView poiExtra;
		}

		public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

			GroupViewHolder holder;
			if (convertView == null) {
				convertView = getActivity().getLayoutInflater().inflate(R.layout.poi_group_item, parent, false);
				holder = new GroupViewHolder();
				holder.poiName = (TextView) convertView.findViewById(R.id.poiName);
				holder.poiExtra = (TextView) convertView.findViewById(R.id.poiExtra);
				convertView.setTag(holder);
			}
			holder = (GroupViewHolder) convertView.getTag();
			holder.poiName.setCompoundDrawablesWithIntrinsicBounds(isExpanded ? R.drawable.indicator_expand2 : R.drawable.indicator_collapse2, 0, 0, 0);
			holder.poiName.setText(pois[groupPosition].title + "(" + String.valueOf(audios.get(groupPosition).length) + ")");
			holder.poiExtra.setText("-" + TimeAnalyzer.formatInTimezone(pois[groupPosition].time, ViewTripActivity.trip.timezone));
			return convertView;
		}

		public boolean hasStableIds() {

			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {

			return true;
		}

		public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile((File) getChild(groupPosition, childPosition)), "audio/*");
			getActivity().startActivity(intent);
			return true;
		}

		@Override
		public void onGroupExpanded(int groupPosition) {

			if (expand != null && groupPosition > -1 && groupPosition < expand.length) {
				expand[groupPosition] = true;
			}
			super.onGroupExpanded(groupPosition);
		}

		@Override
		public void onGroupCollapsed(int groupPosition) {

			if (expand != null && groupPosition > -1 && groupPosition < expand.length) {
				expand[groupPosition] = false;
			}
			super.onGroupCollapsed(groupPosition);
		}
	}
}
