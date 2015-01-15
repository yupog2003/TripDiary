package com.yupog2003.tripdiary.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.TimeAnalyzer;
import com.yupog2003.tripdiary.views.FloatingGroupExpandableListView;
import com.yupog2003.tripdiary.views.UnScrollableGridView;
import com.yupog2003.tripdiary.views.WrapperExpandableListAdapter;

import java.io.File;
import java.util.Arrays;

public class AllPictureFragment extends Fragment {
	POI[] pois;
	boolean[] expand;
	FloatingGroupExpandableListView listView;
	int width;
	int numColums;
	DisplayImageOptions options;

	public AllPictureFragment() {
		options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).cacheInMemory(true).cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565).build();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		int screenWidth = DeviceHelper.getScreenWidth(getActivity());
		int screenHeight = DeviceHelper.getScreenHeight(getActivity());
		if (screenWidth > screenHeight) {
			width = screenWidth / 5;
			numColums = 5;
		} else {
			width = screenWidth / 3;
			numColums = 3;
		}
		listView = new FloatingGroupExpandableListView(getActivity());
		listView.setGroupIndicator(null);
		//listView.setBackgroundColor(getResources().getColor(R.color.item_background));
		setHasOptionsMenu(true);
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
		WrapperExpandableListAdapter adapter2=new WrapperExpandableListAdapter(adapter);
		listView.setAdapter(adapter2);
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

	class PictureAdapter extends BaseAdapter implements OnItemClickListener {
		File[] pictures;

		public PictureAdapter(File[] pictures) {
			this.pictures = pictures;
		}

		public int getCount() {

			if (pictures == null)
				return 0;
			return pictures.length;
		}

		public Object getItem(int position) {

			return pictures[position];
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			if (convertView == null) {
				ImageView imageView = new ImageView(getActivity());
				imageView.setLayoutParams(new AbsListView.LayoutParams(width, width));
				imageView.setMaxHeight(width);
				imageView.setMaxWidth(width);
				convertView = imageView;
			}
			ImageLoader.getInstance().displayImage("file://" + pictures[position].getPath(), (ImageView) convertView, options);
			return convertView;
		}

		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(pictures[position]), "image/*");
			getActivity().startActivity(intent);
		}

	}

	class POIAdapter extends BaseExpandableListAdapter {
		POI[] pois;
		UnScrollableGridView[] pictures;

		public POIAdapter(POI[] pois) {
			this.pois = pois;
			pictures = new UnScrollableGridView[pois.length];
			for (int i = 0; i < pictures.length; i++) {
				pictures[i] = new UnScrollableGridView(getActivity());
				PictureAdapter adapter = new PictureAdapter(pois[i].picFiles);
				pictures[i].setAdapter(adapter);
				pictures[i].setOnItemClickListener(adapter);
				pictures[i].setNumColumns(numColums);
			}
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

		public Object getChild(int groupPosition, int childPosition) {

			return pictures[groupPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {

			return groupPosition;
		}

		public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

			return (UnScrollableGridView) getChild(groupPosition, childPosition);
		}

		public int getChildrenCount(int groupPosition) {

			return 1;
		}

		public Object getGroup(int groupPosition) {

			return pois[groupPosition];
		}

		public int getGroupCount() {

			if (pois == null)
				return 0;
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
				convertView=getActivity().getLayoutInflater().inflate(R.layout.poi_group_item, parent,false);
				holder=new GroupViewHolder();
				holder.poiName=(TextView)convertView.findViewById(R.id.poiName);
				holder.poiExtra=(TextView)convertView.findViewById(R.id.poiExtra);
				convertView.setTag(holder);
			}
			holder=(GroupViewHolder)convertView.getTag();
			holder.poiName.setCompoundDrawablesWithIntrinsicBounds(isExpanded ? R.drawable.indicator_expand2 : R.drawable.indicator_collapse2, 0, 0, 0);
			holder.poiName.setText(pois[groupPosition].title+"(" + String.valueOf(pois[groupPosition].picFiles.length)+")");
			holder.poiExtra.setText("-"+TimeAnalyzer.formatInTimezone(pois[groupPosition].time, ViewTripActivity.trip.timezone));
			return convertView;
		}

		public boolean hasStableIds() {

			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {

			return false;
		}

	}
}
