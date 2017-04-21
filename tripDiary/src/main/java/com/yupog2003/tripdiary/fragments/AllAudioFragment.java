package com.yupog2003.tripdiary.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.DrawableHelper;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Weather;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.views.UnScrollableListView;

import java.util.Calendar;

public class AllAudioFragment extends Fragment {
    POI[] pois;
    RecyclerView recyclerView;
    POIAdapter poiAdapter;
    String timezone;
    Drawable audioDrawable;

    public AllAudioFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_all, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        audioDrawable = DrawableHelper.getAccentTintDrawable(getActivity(), R.drawable.ic_music);
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
            if (convertView == null) {
                TextView textView = new TextView(getActivity());
                textView.setTextAppearance(getActivity(), android.R.style.TextAppearance_DeviceDefault_Medium);
                textView.setCompoundDrawablesWithIntrinsicBounds(audioDrawable, null, null, null);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                convertView = textView;
            }
            ((TextView) convertView).setText(audios[position].getName());
            return convertView;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(audios[position].getUri(), "audio/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                getActivity().startActivity(intent);
            } catch (ActivityNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(getContext(), R.string.no_application_can_open_it, Toast.LENGTH_SHORT).show();
            }
        }

    }

    class POIAdapter extends RecyclerView.Adapter<POIAdapter.ViewHolder> {
        AudioAdapter[] audioAdapters;

        public class ViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case
            public CardView cardView;
            public TextView poiName;
            public TextView poiTime;
            public UnScrollableListView listView;
            public ImageView weather;
            public View.OnClickListener onClickListener;
            public int index;

            public ViewHolder(CardView v) {
                super(v);
                this.cardView = v;
                this.poiName = (TextView) cardView.findViewById(R.id.poiName);
                this.poiTime = (TextView) cardView.findViewById(R.id.poiTime);
                this.listView = (UnScrollableListView) cardView.findViewById(R.id.audios);
                this.weather = (ImageView) cardView.findViewById(R.id.weather);
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
            holder.poiTime.setText(pois[position].time.formatInTimezone(timezone));
            holder.listView.setVisibility(audioAdapters[position].getCount() == 0 ? View.GONE : View.VISIBLE);
            holder.listView.setAdapter(audioAdapters[position]);
            holder.listView.setOnItemClickListener(audioAdapters[position]);
            MyCalendar time = pois[position].time;
            time.setTimeZone(timezone);
            int weatherIcon = Weather.getIconForId(pois[position].weather, time.get(Calendar.HOUR_OF_DAY));
            holder.weather.setImageResource(weatherIcon);
            if (weatherIcon == R.drawable.ic_question_mark) {
                holder.weather.setVisibility(View.GONE);
            } else {
                holder.weather.setVisibility(View.VISIBLE);
            }
            holder.index = position;
        }

        @Override
        public int getItemCount() {
            return pois.length;
        }

    }

}
