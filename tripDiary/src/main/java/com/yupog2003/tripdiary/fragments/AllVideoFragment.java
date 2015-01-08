package com.yupog2003.tripdiary.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

public class AllVideoFragment extends Fragment {
	POIAdapter adapter;
	POI[] pois;
	boolean[] expand;
	FloatingGroupExpandableListView listView;
	int width;

	public AllVideoFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		listView = new FloatingGroupExpandableListView(getActivity());
		listView.setGroupIndicator(null);
		// listView.setBackgroundColor(getResources().getColor(R.color.item_background));
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
		adapter = new POIAdapter(pois);
		WrapperExpandableListAdapter adapter2 = new WrapperExpandableListAdapter(adapter);
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

	class VideoAdapter extends BaseAdapter implements OnItemClickListener {

		File[] videos;

		MediaMetadataRetriever mmr;
		Canvas canvas;
		int left, top;
		final Bitmap playbitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_play);

		public VideoAdapter(File[] videos) {
			this.videos = videos;
			mmr = new MediaMetadataRetriever();
			left = playbitmap.getWidth() / 2;
			top = playbitmap.getHeight() / 2;
		}

		public int getCount() {

			if (videos == null)
				return 0;
			return videos.length;
		}

		public Object getItem(int position) {

			return videos[position];
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View view, ViewGroup viewGroup) {

			ImageView image = new ImageView(getActivity());
			mmr.setDataSource(videos[position].getPath());
			Bitmap b = mmr.getFrameAtTime();
			Bitmap bitmap = b == null ? Bitmap.createBitmap(width, width, Bitmap.Config.RGB_565) : Bitmap.createScaledBitmap(b, width, width, true);
			canvas = new Canvas(bitmap);
			canvas.drawBitmap(playbitmap, bitmap.getWidth() / 2 - left, bitmap.getHeight() / 2 - top, null);
			image.setImageBitmap(bitmap);
			return image;
		}

		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(videos[position]), "video/*");
			getActivity().startActivity(intent);
		}

	}

	class POIAdapter extends BaseExpandableListAdapter {

		POI[] pois;
		UnScrollableGridView[] videos;
		int numColums;

		public POIAdapter(POI[] pois) {
			this.pois = pois;
			videos = new UnScrollableGridView[pois.length];
			int screenWidth = DeviceHelper.getScreenWidth(getActivity());
			int screenHeight = DeviceHelper.getScreenHeight(getActivity());
			if (screenWidth > screenHeight) {
				width = screenWidth / 5;
				numColums = 5;
			} else {
				width = screenWidth / 3;
				numColums = 3;
			}
			for (int i = 0; i < videos.length; i++) {
				videos[i] = new UnScrollableGridView(getActivity());
				VideoAdapter adapter = new VideoAdapter(pois[i].videoFiles);
				videos[i].setAdapter(adapter);
				videos[i].setOnItemClickListener(adapter);
				videos[i].setNumColumns(numColums);
			}

		}

		public Object getChild(int groupPosition, int childPosition) {

			return videos[groupPosition];
		}

		public long getChildId(int groupPosition, int childPosition) {

			return groupPosition * 1000 + childPosition;
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
				convertView = getActivity().getLayoutInflater().inflate(R.layout.poi_group_item, parent, false);
				holder = new GroupViewHolder();
				holder.poiName = (TextView) convertView.findViewById(R.id.poiName);
				holder.poiExtra = (TextView) convertView.findViewById(R.id.poiExtra);
				convertView.setTag(holder);
			}
			holder = (GroupViewHolder) convertView.getTag();
			holder.poiName.setCompoundDrawablesWithIntrinsicBounds(isExpanded ? R.drawable.indicator_expand2 : R.drawable.indicator_collapse2, 0, 0, 0);
			holder.poiName.setText(pois[groupPosition].title + "(" + String.valueOf(pois[groupPosition].videoFiles.length) + ")");
			holder.poiExtra.setText("-" + TimeAnalyzer.formatInTimezone(pois[groupPosition].time, ViewTripActivity.trip.timezone));
			return convertView;
		}

		public boolean hasStableIds() {

			return false;
		}

		public boolean isChildSelectable(int groupPosition, int childPosition) {

			return false;
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
