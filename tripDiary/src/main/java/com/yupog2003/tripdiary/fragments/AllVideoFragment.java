package com.yupog2003.tripdiary.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.TimeAnalyzer;
import com.yupog2003.tripdiary.views.SquareImageView;
import com.yupog2003.tripdiary.views.UnScrollableGridView;

import java.io.File;

public class AllVideoFragment extends Fragment {
	POIAdapter adapter;
	POI[] pois;
	RecyclerView recyclerView;
	int width;
	int numColums;
	public AllVideoFragment() {

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		recyclerView = new RecyclerView(getActivity());
		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		int screenWidth = DeviceHelper.getScreenWidth(getActivity());
		int screenHeight = DeviceHelper.getScreenHeight(getActivity());
		if (screenWidth > screenHeight) {
			width = screenWidth / 5;
			numColums = 5;
		} else {
			width = screenWidth / 3;
			numColums = 3;
		}
		setHasOptionsMenu(true);
		refresh();
		return recyclerView;
	}

	@Override
	public void onResume() {
		//refresh();
		super.onResume();

	}

	public void refresh() {
		this.pois = ViewTripActivity.trip.pois;
		recyclerView.setAdapter(new POIAdapter(pois));
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.fragment_all, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return true;
	}

	class VideoAdapter extends BaseAdapter implements OnItemClickListener {

		File[] videos;
        DisplayImageOptions options;
		MediaMetadataRetriever mmr;

		public VideoAdapter(File[] videos) {
			this.videos = videos;
			mmr = new MediaMetadataRetriever();
			options = new DisplayImageOptions.Builder()
					.displayer(new FadeInBitmapDisplayer(500))
					.showImageOnFail(R.drawable.ic_play)
					.cacheInMemory(true)
					.cacheOnDisk(false)
					.bitmapConfig(Bitmap.Config.RGB_565)
					.build();
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
			SquareImageView image = new SquareImageView(getActivity());
            image.setMaxWidth(width);
            image.setMaxHeight(width);
			image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage("file://" + videos[position].getPath(), image, options);
			return image;
		}

		public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(videos[position]), "video/*");
			getActivity().startActivity(intent);
		}

	}

	class POIAdapter extends RecyclerView.Adapter<POIAdapter.ViewHolder> {

		POI[] pois;
		VideoAdapter[] videoAdapters;

		public class ViewHolder extends RecyclerView.ViewHolder {
			// each data item is just a string in this case
			public CardView cardView;
			public TextView poiName;
			public TextView poiTime;
			public UnScrollableGridView gridView;
			public View.OnClickListener onClickListener;
			public int index;

			public ViewHolder(CardView v) {
				super(v);
				this.cardView = v;
				this.poiName = (TextView) cardView.findViewById(R.id.poiName);
				this.poiTime = (TextView) cardView.findViewById(R.id.poiTime);
				this.gridView = (UnScrollableGridView) cardView.findViewById(R.id.videos);
				this.gridView.setNumColumns(numColums);
				this.onClickListener = new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(getActivity(), ViewPointActivity.class);
						intent.putExtra("path", pois[index].dir.getPath());
						startActivity(intent);
					}
				};
				this.cardView.setOnClickListener(onClickListener);
			}
		}
		public POIAdapter(POI[] pois) {
			this.pois = pois;
			videoAdapters=new VideoAdapter[pois.length];
			for (int i = 0; i < videoAdapters.length; i++) {
				videoAdapters[i]=new VideoAdapter(pois[i].videoFiles);
			}

		}

		@Override
		public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
			CardView v = (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.card_videos, parent, false);
			ViewHolder viewHolder = new ViewHolder(v);
			return viewHolder;
		}

		@Override
		public void onBindViewHolder(ViewHolder holder, int position) {
			holder.poiName.setText(pois[position].title + "(" + String.valueOf(pois[position].videoFiles.length) + ")");
			holder.poiTime.setText(TimeAnalyzer.formatInTimezone(pois[position].time, ViewTripActivity.trip.timezone));
			holder.index = position;
			holder.gridView.setVisibility(videoAdapters[position].getCount() == 0 ? View.GONE : View.VISIBLE);
			holder.gridView.setAdapter(videoAdapters[position]);
			holder.gridView.setOnItemClickListener(videoAdapters[position]);
		}

		@Override
		public int getItemCount() {
			return pois.length;
		}


	}
}
