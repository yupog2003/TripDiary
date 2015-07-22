package com.yupog2003.tripdiary.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.provider.DocumentFile;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.views.UnScrollableListView;

public class AllAudioFragment extends Fragment {
    POI[] pois;
    RecyclerView recyclerView;
    int width;
    int numColums;
    POIAdapter poiAdapter;

    public AllAudioFragment() {

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
        setHasOptionsMenu(true);
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        return recyclerView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void refresh() {
        this.pois = ViewTripActivity.trip.pois;
        poiAdapter = new POIAdapter(pois);
        recyclerView.setAdapter(poiAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.fragment_all, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    class AudioAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {
        DocumentFile[] audios;

        public AudioAdapter(DocumentFile[] audios) {
            this.audios = audios;
        }

        public int getCount() {

            if (audios == null)
                return 0;
            return audios.length;
        }

        public Object getItem(int position) {

            return audios[position];
        }

        public long getItemId(int position) {

            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            TextView textView = new TextView(getActivity());
            textView.setText(FileHelper.getFileName(audios[position]));
            textView.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Medium);
            textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_music, 0, 0, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            return textView;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(audios[position].getUri(), "audio/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getActivity().startActivity(intent);
        }

    }

    class POIAdapter extends RecyclerView.Adapter<POIAdapter.ViewHolder> {
        POI[] pois;
        AudioAdapter[] audioAdapters;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public CardView cardView;
            public TextView poiName;
            public TextView poiTime;
            public UnScrollableListView gridView;
            public View.OnClickListener onClickListener;
            public int index;

            public ViewHolder(CardView v) {
                super(v);
                this.cardView = v;
                this.poiName = (TextView) cardView.findViewById(R.id.poiName);
                this.poiTime = (TextView) cardView.findViewById(R.id.poiTime);
                this.gridView = (UnScrollableListView) cardView.findViewById(R.id.audios);
                this.onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tripName = FileHelper.getFileName(pois[index].dir.getParentFile());
                        String poiName = FileHelper.getFileName(pois[index].dir);
                        Intent intent = new Intent(getActivity(), ViewPointActivity.class);
                        intent.putExtra(ViewPointActivity.tag_tripname, tripName);
                        intent.putExtra(ViewPointActivity.tag_poiname, poiName);
                        intent.putExtra(ViewPointActivity.tag_fromActivity, ViewTripActivity.class.getSimpleName());
                        startActivity(intent);
                    }
                };
                this.cardView.setOnClickListener(onClickListener);
            }
        }

        public POIAdapter(POI[] pois) {
            this.pois = pois;
            this.audioAdapters = new AudioAdapter[pois.length];
            for (int i = 0; i < audioAdapters.length; i++) {
                audioAdapters[i] = new AudioAdapter(pois[i].audioFiles);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView v = (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.card_audios, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            holder.poiName.setText(pois[position].title + "(" + String.valueOf(pois[position].audioFiles.length) + ")");
            holder.poiTime.setText(pois[position].time.formatInTimezone(ViewTripActivity.trip.timezone));
            holder.gridView.setVisibility(audioAdapters[position].getCount() == 0 ? View.GONE : View.VISIBLE);
            holder.gridView.setAdapter(audioAdapters[position]);
            holder.gridView.setOnItemClickListener(audioAdapters[position]);
            holder.index = position;
        }

        @Override
        public int getItemCount() {
            return pois.length;
        }

    }
}
