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
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
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

public class AllPictureFragment extends Fragment {
    POI[] pois;
    RecyclerView recyclerView;
    POIAdapter poiAdapter;
    int sideLength;
    int numColumns;
    String timezone;

    public AllPictureFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        int[] numColumnsAndWidth = new int[2];
        DeviceHelper.getNumColumnsAndWidth(getActivity(), numColumnsAndWidth);
        sideLength = numColumnsAndWidth[1];
        numColumns = numColumnsAndWidth[0];
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_all, container, false);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        setHasOptionsMenu(true);
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
        return super.onOptionsItemSelected(item);
    }

    class PictureAdapter extends BaseAdapter implements OnItemClickListener {
        DocumentFile[] pictures;
        DisplayImageOptions options;

        public PictureAdapter(DocumentFile[] pictures) {
            this.pictures = pictures;
            options = new DisplayImageOptions.Builder()
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                    .extraForDownloader(pictures)
                    .showImageOnLoading(new ColorDrawable(Color.LTGRAY))
                    .showImageOnFail(DrawableHelper.getAccentTintDrawable(getActivity(), R.drawable.ic_error))
                    .build();
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

        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                SquareImageView imageView = new SquareImageView(getActivity());
                imageView.setMaxWidth(sideLength);
                imageView.setMaxHeight(sideLength);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                convertView = imageView;
            }
            ImageLoader.getInstance().displayImage("trip://" + pictures[position].getUri().getPath(), new MyImageViewAware((ImageView) convertView), options);
            return convertView;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(pictures[position].getUri(), "image/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(intent);
        }
    }

    class POIAdapter extends RecyclerView.Adapter<POIAdapter.ViewHolder> {
        PictureAdapter[] pictureAdapters;

        public class ViewHolder extends RecyclerView.ViewHolder {
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
                this.gridView = (UnScrollableGridView) cardView.findViewById(R.id.pictures);
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
            this.pictureAdapters = new PictureAdapter[pois.length];
            for (int i = 0; i < pictureAdapters.length; i++) {
                pictureAdapters[i] = new PictureAdapter(pois[i].picFiles);
            }
        }

        @Override
        public POIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView v = (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.card_pictures, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(POIAdapter.ViewHolder holder, int position) {
            holder.poiName.setText(pois[position].title + "(" + String.valueOf(pois[position].picFiles.length) + ")");
            holder.poiTime.setText(pois[position].time.formatInTimezone(timezone));
            holder.gridView.setVisibility(pictureAdapters[position].getCount() == 0 ? View.GONE : View.VISIBLE);
            holder.gridView.setAdapter(pictureAdapters[position]);
            holder.gridView.setOnItemClickListener(pictureAdapters[position]);
            holder.index = position;
        }

        @Override
        public int getItemCount() {
            return pois.length;
        }
    }

}
