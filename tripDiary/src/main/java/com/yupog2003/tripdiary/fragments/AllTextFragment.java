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
import android.widget.TextView;
import android.widget.Toast;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.POI;

import java.io.File;

public class AllTextFragment extends Fragment {
    POI[] pois;
    RecyclerView recyclerView;
    POIAdapter poiAdapter;

    public AllTextFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        recyclerView = new RecyclerView(getActivity());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        setHasOptionsMenu(true);
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
        File fontFile = new File(getActivity().getFilesDir(), PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("diaryfont", ""));
        if (fontFile.exists() && fontFile.isFile()) {
            try {
                poiAdapter.setTypeFace(Typeface.createFromFile(fontFile));
            } catch (RuntimeException e) {
                Toast.makeText(getActivity(), getString(R.string.invalid_font), Toast.LENGTH_SHORT).show();
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
        POI[] pois;
        String[] diarys;
        Typeface typeFace;

        public class ViewHolder extends RecyclerView.ViewHolder {
            CardView cardView;
            public TextView poiName;
            public TextView poiTime;
            public TextView text;
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
                this.onClickListener = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String tripName = FileHelper.getFileName(pois[index].dir.getParentFile());
                        String poiName = FileHelper.getFileName(pois[index].dir);
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

        public POIAdapter(POI[] pois) {
            this.pois = pois;
            diarys = new String[pois.length];
            for (int i = 0; i < diarys.length; i++) {
                diarys[i] = pois[i].diary;
                if (diarys[i] == null) {
                    diarys[i] = "";
                }
                if (diarys[i].length() > 0) {
                    diarys[i] = diarys[i].substring(0, diarys[i].length() - 1);
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
            holder.poiTime.setText(pois[position].time.formatInTimezone(ViewTripActivity.trip.timezone));
            holder.index = position;
            holder.text.setVisibility(diarys[position].length() == 0 ? View.GONE : View.VISIBLE);
            holder.text.setText(diarys[position]);
        }

        @Override
        public int getItemCount() {
            return pois.length;
        }


    }

}
