package com.yupog2003.tripdiary.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
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
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.views.SquareImageView;
import com.yupog2003.tripdiary.views.UnScrollableGridView;

import java.io.File;

public class AllPictureFragment extends Fragment {
    POI[] pois;
    RecyclerView recyclerView;
    int width;
    int numColums;
    DisplayImageOptions options;

    public AllPictureFragment() {
        options = new DisplayImageOptions.Builder()
                .displayer(new FadeInBitmapDisplayer(500))
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
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
        recyclerView = new RecyclerView(getActivity());
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
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
        return super.onOptionsItemSelected(item);
    }

    class PictureAdapter extends BaseAdapter implements OnItemClickListener {
        File[] pictures;
        Bitmap[] bitmaps;

        public PictureAdapter(File[] pictures) {
            this.pictures = pictures;
            this.bitmaps=new Bitmap[pictures.length];
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
                imageView.setMaxWidth(width);
                imageView.setMaxHeight(width);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                convertView = imageView;
            }
            if (bitmaps[position]==null){
                ImageLoader.getInstance().displayImage("file://" + pictures[position].getPath(), (ImageView) convertView, options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        try{
                            bitmaps[position]=bitmap.copy(Bitmap.Config.RGB_565,true);
                        }catch (OutOfMemoryError e){
                            e.printStackTrace();
                        }
                    }

                });
            }else{
                ((ImageView)convertView).setImageBitmap(bitmaps[position]);
            }
            return convertView;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(pictures[position]), "image/*");
            getActivity().startActivity(intent);
        }
    }

    class POIAdapter extends RecyclerView.Adapter<POIAdapter.ViewHolder> {
        POI[] pois;
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
                this.gridView.setNumColumns(numColums);
                this.onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tripName=pois[index].dir.getParentFile().getName();
                        String poiName=pois[index].dir.getName();
                        Intent intent = new Intent(getActivity(), ViewPointActivity.class);
                        intent.putExtra(ViewPointActivity.tag_tripname, tripName);
                        intent.putExtra(ViewPointActivity.tag_poiname, poiName);
                        startActivity(intent);
                    }
                };
                this.cardView.setOnClickListener(onClickListener);
            }

        }

        public POIAdapter(POI[] pois) {
            this.pois = pois;
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
            holder.poiTime.setText(pois[position].time.formatInTimezone(ViewTripActivity.trip.timezone));
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
