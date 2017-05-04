package com.yupog2003.tripdiary.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.ScatterData;
import com.github.mikephil.charting.data.ScatterDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.ViewGraphActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Trip;

import java.util.ArrayList;
import java.util.List;

public class ViewGraphFragment extends Fragment {
    CombinedChart lineChart;
    ProgressBar pb;
    ArrayList<Entry> altitudeEntries;
    ArrayList<Entry> speedEntries;
    ArrayList<String> xVals;
    ArrayList<Float> distances;
    Trip trip;
    OnGraphClickListener onGraphClickListener;
    public static final String tag_tripname = "tag_tripname";
    public static final String tag_showOpenButton = "showOpenButton";

    public ViewGraphFragment() {

    }

    public interface OnGraphClickListener {
        void onGraphClicked(int xIndex);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_graph, container, false);
        lineChart = (CombinedChart) view.findViewById(R.id.linechart);
        pb = (ProgressBar) view.findViewById(R.id.progressBar);
        String tripName = getArguments().getString(tag_tripname);
        trip = ((TripDiaryApplication) getActivity().getApplication()).getTrip(tripName);
        if (trip != null && trip.cache != null) {
            DeviceHelper.sendGATrack(getActivity(), "Trip", "view_graph", trip.tripName, null);
            new LoadChartTask().execute();
        } else {
            Toast.makeText(getActivity(), "Can not get track", Toast.LENGTH_SHORT).show();
        }
        boolean showOpenButton = getArguments().getBoolean(tag_showOpenButton, false);
        ImageButton openButton = (ImageButton) view.findViewById(R.id.openInNew);
        if (showOpenButton) {
            openButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(), ViewGraphActivity.class);
                    intent.putExtra(ViewGraphActivity.tag_tripname, trip.tripName);
                    startActivity(intent);
                }
            });
            openButton.setVisibility(View.VISIBLE);
        } else {
            openButton.setVisibility(View.GONE);
        }
        return view;
    }

    class AltitudeFormatter implements IAxisValueFormatter, IValueFormatter {


        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return GpxAnalyzer2.getAltitudeString(value, "m");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return GpxAnalyzer2.getAltitudeString(value, "m");
        }
    }

    class SpeedFormatter implements IAxisValueFormatter, IValueFormatter {

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return GpxAnalyzer2.getDistanceString(value, "km/hr");
        }

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return GpxAnalyzer2.getDistanceString(value, "km/hr");
        }
    }

    class MyXAxisValueFormatter implements IAxisValueFormatter {

        private String[] mValues;

        public MyXAxisValueFormatter(String[] values) {
            this.mValues = values;
        }

        public MyXAxisValueFormatter(ArrayList<String> values) {
            this.mValues = values.toArray(new String[values.size()]);
        }

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            return mValues[(int) value];
        }

    }

    class POIValueFormatter implements IValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            Object data = entry.getData();
            if (data != null && data instanceof POI) {
                return ((POI) data).title;
            }
            return null;
        }
    }

    class MyMarkerView extends MarkerView {

        TextView altitude;
        TextView speed;
        TextView distance;
        TextView time;

        public MyMarkerView(Context context, int layoutResource) {
            super(context, layoutResource);
            altitude = (TextView) findViewById(R.id.altitude);
            speed = (TextView) findViewById(R.id.speed);
            distance = (TextView) findViewById(R.id.distance);
            time = (TextView) findViewById(R.id.time);
        }

        @Override
        public void refreshContent(Entry entry, Highlight highlight) {
            int index = (int) entry.getX();
            altitude.setText(getString(R.string.Altitude) + ":" + GpxAnalyzer2.getAltitudeString(altitudeEntries.get(index).getY(), "m"));
            speed.setText(getString(R.string.velocity) + ":" + GpxAnalyzer2.getDistanceString(speedEntries.get(index).getY(), "km/hr"));
            distance.setText(getString(R.string.distance) + ":" + GpxAnalyzer2.getDistanceString(distances.get(index) / 1000, "km"));
            String timeStr = xVals.get(index);
            time.setText(timeStr.substring(timeStr.indexOf("-") + 1));
            if (onGraphClickListener != null) {
                onGraphClickListener.onGraphClicked(index);
            }
            super.refreshContent(entry, highlight);
        }
    }

    class LoadChartTask extends AsyncTask<Void, Void, CombinedData> {


        public LoadChartTask() {

        }

        @Override
        protected void onPreExecute() {
            lineChart.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected CombinedData doInBackground(Void... params) {
            if (trip.cache == null) {
                return null;
            }
            List<ILineDataSet> lineDataSets = new ArrayList<>();
            int trackLength = trip.cache.getTrackLength();
            altitudeEntries = new ArrayList<>();
            speedEntries = new ArrayList<>();
            xVals = new ArrayList<>();
            distances = new ArrayList<>();
            float totalDistance = 0;
            for (int i = 0; i < trackLength; i++) {
                altitudeEntries.add(new Entry(i, trip.cache.altitudes[i]));
                int startIndex = i - 10;
                int endIndex = i + 10;
                if (startIndex < 0) startIndex = 0;
                if (endIndex >= trackLength) endIndex = trackLength - 1;
                float dist = GpxAnalyzerJava.distFrom(trip.cache.latitudes[startIndex], trip.cache.longitudes[startIndex], trip.cache.latitudes[endIndex], trip.cache.longitudes[endIndex]);
                MyCalendar startTime = MyCalendar.getTime(trip.cache.times[startIndex], MyCalendar.type_self);
                MyCalendar endTime = MyCalendar.getTime(trip.cache.times[endIndex], MyCalendar.type_self);
                float seconds = MyCalendar.getMinusTimeInSecond(startTime, endTime);
                float speed = dist / seconds * 18 / 5;
                speedEntries.add(new Entry(i, speed));
                xVals.add(trip.cache.times[i]);
                if (i > 0)
                    totalDistance += GpxAnalyzerJava.distFrom(trip.cache.latitudes[i - 1], trip.cache.longitudes[i - 1], trip.cache.latitudes[i], trip.cache.longitudes[i]);
                distances.add(totalDistance);
            }
            LineDataSet altitudeDataSet = new LineDataSet(altitudeEntries, getString(R.string.Altitude));
            altitudeDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            altitudeDataSet.setDrawCircles(false);
            altitudeDataSet.setColor(Color.GREEN);
            altitudeDataSet.setValueFormatter(new AltitudeFormatter());
            lineDataSets.add(altitudeDataSet);

            LineDataSet speedDataSet = new LineDataSet(speedEntries, getString(R.string.velocity));
            speedDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
            speedDataSet.setDrawCircles(false);
            speedDataSet.setColor(Color.BLUE);
            speedDataSet.setValueFormatter(new SpeedFormatter());
            lineDataSets.add(speedDataSet);
            LineData lineData = new LineData(lineDataSets);

            CombinedData combinedData = new CombinedData();
            combinedData.setData(lineData);

            SparseArray<POI> poiSparseArray = trip.getPOIsInTrackMap();
            if (poiSparseArray.size() > 0) { //add scatter data only if there are pois to show
                List<IScatterDataSet> scatterDataSets = new ArrayList<>();
                ArrayList<Entry> poiAltitudes = new ArrayList<>();
                ArrayList<Entry> poiSpeeds = new ArrayList<>();

                for (int i = 0; i < poiSparseArray.size(); i++) {
                    int xIndex = poiSparseArray.keyAt(i);
                    POI poi = poiSparseArray.valueAt(i);
                    poiAltitudes.add(new Entry(xIndex, altitudeEntries.get(xIndex).getY(), poi));
                    poiSpeeds.add(new Entry(xIndex, speedEntries.get(xIndex).getY(), poi));
                }
                int markerColor = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("markercolor", 0xffff0000);
                ScatterDataSet poiAltitudesDataSet = new ScatterDataSet(poiAltitudes, "");
                poiAltitudesDataSet.setColor(markerColor);
                poiAltitudesDataSet.setValueFormatter(new POIValueFormatter());
                poiAltitudesDataSet.setHighlightEnabled(false);
                poiAltitudesDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                ScatterDataSet poiSpeedsDataSet = new ScatterDataSet(poiSpeeds, "");
                poiSpeedsDataSet.setColor(markerColor);
                poiSpeedsDataSet.setValueFormatter(new POIValueFormatter());
                poiSpeedsDataSet.setHighlightEnabled(false);
                poiSpeedsDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);

                scatterDataSets.add(poiAltitudesDataSet);
                scatterDataSets.add(poiSpeedsDataSet);
                ScatterData scatterData = new ScatterData(scatterDataSets);
                combinedData.setData(scatterData);
            }
            return combinedData;
        }

        @Override
        protected void onPostExecute(CombinedData combinedData) {
            if (combinedData != null) {
                lineChart.setData(combinedData);
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.getXAxis().setValueFormatter(new MyXAxisValueFormatter(xVals));
                lineChart.getAxisLeft().setValueFormatter(new AltitudeFormatter());
                lineChart.getAxisRight().setValueFormatter(new SpeedFormatter());
                Description description = new Description();
                description.setText(trip.tripName);
                lineChart.setDescription(description);
                lineChart.setMarker(new MyMarkerView(getContext(), R.layout.graph_markerview));
                lineChart.setHardwareAccelerationEnabled(true);
                lineChart.invalidate();
                lineChart.setVisibility(View.VISIBLE);
            }
            pb.setVisibility(View.GONE);
        }
    }

    public void setOnGraphClickListener(OnGraphClickListener listener) {
        this.onGraphClickListener = listener;
    }

    public void highlightIndex(int index) {
        if (index == -1) {
            lineChart.highlightValues(null);
        } else {
            Highlight altitudeHighlight = new Highlight(index, 0, 0);
            Highlight speedHighlight = new Highlight(index, 1, 0);
            lineChart.highlightValues(new Highlight[]{altitudeHighlight, speedHighlight});
        }
    }

    public void setMarkerEnabled(boolean enabled) {
        lineChart.setDrawMarkers(enabled);
    }
}
