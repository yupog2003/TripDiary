package com.yupog2003.tripdiary.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Weather;

import java.io.File;
import java.util.Calendar;

public class AllTextFragment extends Fragment {
    POI[] pois;
    RecyclerView recyclerView;
    POIAdapter poiAdapter;
    String timezone;

    public AllTextFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_all, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
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
            File fontFile = new File(getActivity().getFilesDir(), PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("diaryfont", ""));
            if (fontFile.exists() && fontFile.isFile()) {
                try {
                    poiAdapter.setTypeFace(Typeface.createFromFile(fontFile));
                } catch (RuntimeException e) {
                    Toast.makeText(getActivity(), getString(R.string.invalid_font), Toast.LENGTH_SHORT).show();
                }
            }
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

    class POIAdapter extends RecyclerView.Adapter<POIAdapter.ViewHolder> {
        String[] diarys;
        Typeface typeFace;

        public class ViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;
            public TextView poiName;
            public TextView poiTime;
            public TextView text;
            public ImageView weather;
            public View.OnClickListener onClickListener;
            public int index;

            public ViewHolder(CardView v) {
                super(v);
                this.cardView = v;
                this.poiName = (TextView) cardView.findViewById(R.id.poiName);
                this.poiTime = (TextView) cardView.findViewById(R.id.poiTime);
                this.text = (TextView) cardView.findViewById(R.id.text);
                if (typeFace != null) {
                    text.setTypeface(typeFace);
                }
                this.text.setTextSize(PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("diaryfontsize", 20));
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
            diarys = new String[pois.length];
            for (int i = 0; i < diarys.length; i++) {
                diarys[i] = pois[i].diary;
                if (diarys[i] == null) {
                    diarys[i] = "";
                }
            }
        }

        public void setTypeFace(Typeface typeFace) {
            this.typeFace = typeFace;
        }

        @Override
        public POIAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            CardView v = (CardView) LayoutInflater.from(getActivity()).inflate(R.layout.card_texts, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(POIAdapter.ViewHolder holder, int position) {
            holder.poiName.setText(pois[position].title + "(" + String.valueOf(pois[position].diary.length()) + ")");
            holder.poiTime.setText(pois[position].time.formatInTimezone(timezone));
            holder.index = position;
            holder.text.setVisibility(diarys[position].length() == 0 ? View.GONE : View.VISIBLE);
            holder.text.setText(diarys[position]);
            MyCalendar time = pois[position].time;
            time.setTimeZone(timezone);
            int weatherIcon = Weather.getIconForId(pois[position].weather, time.get(Calendar.HOUR_OF_DAY));
            holder.weather.setImageResource(weatherIcon);
            if (weatherIcon == R.drawable.ic_question_mark) {
                holder.weather.setVisibility(View.GONE);
            } else {
                holder.weather.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public int getItemCount() {
            return pois.length;
        }
    }

}
