package com.yupog2003.tripdiary.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.DrawableHelper;
import com.yupog2003.tripdiary.data.MyImageViewAware;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.views.SquareImageView;
import com.yupog2003.tripdiary.views.UnScrollableGridView;

public class AllVideoFragment extends Fragment {
    POIAdapter adapter;
    POI[] pois;
    RecyclerView recyclerView;
    int sideLength;
    int numColumns;
    POIAdapter poiAdapter;
    String timezone;

    public AllVideoFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_all, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        int[] numColumnsAndWidth = new int[2];
        DeviceHelper.getNumColumnsAndWidth(getActivity(), numColumnsAndWidth);
        sideLength = numColumnsAndWidth[1];
        numColumns = numColumnsAndWidth[0];
        setHasOptionsMenu(true);
        if (poiAdapter != null) {
            recyclerView.setAdapter(poiAdapter);
        }
        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void refresh() {
        if (getActivity() != null && getActivity() instanceof ViewTripActivity) {
            this.pois = ((ViewTripActivity) getActivity()).trip.pois;
            this.timezone = ((ViewTripActivity) getActivity()).trip.timezone;
            poiAdapter = new POIAdapter();
            recyclerView.setAdapter(poiAdapter);
        }

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

        DocumentFile[] videos;
        DisplayImageOptions options;

        public VideoAdapter(DocumentFile[] videos) {
            this.videos = videos;
            if (videos.length > 0) {
                options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .bitmapConfig(Bitmap.Config.RGB_565)
                        .extraForDownloader(videos)
                        .showImageOnLoading(new ColorDrawable(Color.LTGRAY))
                        .showImageOnFail(DrawableHelper.getAccentTintDrawable(getActivity(), R.drawable.ic_play))
                        .build();
            }
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

        public View getView(int position, View convertView, ViewGroup viewGroup) {
            if (convertView == null) {
                SquareImageView imageView = new SquareImageView(getActivity());
                imageView.setMaxWidth(sideLength);
                imageView.setMaxHeight(sideLength);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                convertView = imageView;
            }
            ImageLoader.getInstance().displayImage("trip://" + videos[position].getUri().getPath(), new MyImageViewAware((ImageView) convertView), options);
            return convertView;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(videos[position].getUri(), "video/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getActivity().startActivity(intent);
        }
    }

    class POIAdapter extends RecyclerView.Adapter<POIAdapter.ViewHolder> {

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
                this.gridView.setNumColumns(numColumns);
                this.onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tripName = pois[index].dir.getParentFile().getName();
                        String poiName = pois[index].dir.getName();
                        Intent intent = new Intent(getActivity(), ViewPointActivity.class);
                        intent.putExtra(ViewPointActivity.tag_tripname, tripName);
                        intent.putExtra(ViewPointActivity.tag_poiname, poiName);
                        intent.putExtra(ViewPointActivity.tag_fromActivity, ViewTripActivity.class.getSimpleName());
                        getActivity().startActivityForResult(intent, ViewTripActivity.REQUEST_VIEW_POI);
                    }
                };
                this.cardView.setOnClickListener(onClickListener);
            }
        }

        public POIAdapter() {
            videoAdapters = new VideoAdapter[pois.length];
            for (int i = 0; i < videoAdapters.length; i++) {
                videoAdapters[i] = new VideoAdapter(pois[i].videoFiles);
            }

        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView v = (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.card_videos, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.poiName.setText(pois[position].title + "(" + String.valueOf(pois[position].videoFiles.length) + ")");
            holder.poiTime.setText(pois[position].time.formatInTimezone(timezone));
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
