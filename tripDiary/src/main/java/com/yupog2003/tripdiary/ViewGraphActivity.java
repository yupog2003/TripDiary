package com.yupog2003.tripdiary;

import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.ValueFormatter;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.TrackCache;
import com.yupog2003.tripdiary.data.Trip;

import java.util.ArrayList;

public class ViewGraphActivity extends MyActivity {
    LineChart lineChart;
    ProgressBar pb;
    TrackCache trackCache;
    ArrayList<Entry> altitudeEntries;
    ArrayList<Entry> speedEntries;
    ArrayList<String> xVals;
    ArrayList<Float> distances;
    String tripName;
    public static final String tag_tripname = "tag_tripname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_graph);
        lineChart = (LineChart) findViewById(R.id.linechart);
        pb = (ProgressBar) findViewById(R.id.progressBar);
        tripName = getIntent().getStringExtra(tag_tripname);
        Trip trip = ((TripDiaryApplication) getApplication()).getTrip(tripName);
        if (trip != null && trip.cache != null) {
            DeviceHelper.sendGATrack(getActivity(), "Trip", "view_graph", trip.tripName, null);
            trackCache = trip.cache;
            tripName = trip.tripName;
            new LoadChartTask().execute();
        } else {
            Toast.makeText(getActivity(), "Can not get track", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_graph_acivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    class AltitudeFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float v) {
            return GpxAnalyzer2.getAltitudeString(v, "m");
        }
    }

    class SpeedFormatter implements ValueFormatter {

        @Override
        public String getFormattedValue(float v) {
            return GpxAnalyzer2.getDistanceString(v, "km/hr");
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
            int index = entry.getXIndex();
            altitude.setText(getString(R.string.Altitude) + ":" + GpxAnalyzer2.getAltitudeString(altitudeEntries.get(index).getVal(), "m"));
            speed.setText(getString(R.string.velocity) + ":" + GpxAnalyzer2.getDistanceString(speedEntries.get(index).getVal(), "km/hr"));
            distance.setText(getString(R.string.distance) + ":" + GpxAnalyzer2.getDistanceString(distances.get(index) / 1000, "km"));
            String timeStr = xVals.get(index);
            time.setText(timeStr.substring(timeStr.indexOf("-") + 1));
        }

        @Override
        public int getXOffset() {
            return 0;
        }

        @Override
        public int getYOffset() {
            return 0;
        }
    }

    class LoadChartTask extends AsyncTask<Void, Void, ArrayList<LineDataSet>> {
        @Override
        protected void onPreExecute() {
            lineChart.setVisibility(View.INVISIBLE);
            pb.setVisibility(View.VISIBLE);
        }

        @Override
        protected ArrayList<LineDataSet> doInBackground(Void... params) {
            if (trackCache == null) {
                return null;
            }
            int trackLength = trackCache.getTrackLength();
            ArrayList<LineDataSet> dataSets = new ArrayList<>();
            altitudeEntries = new ArrayList<>();
            speedEntries = new ArrayList<>();
            xVals = new ArrayList<>();
            distances = new ArrayList<>();
            float totalDistance = 0;
            for (int i = 0; i + 20 < trackLength; i += 20) {
                altitudeEntries.add(new Entry(trackCache.altitudes[i], i / 20));
                float dist = GpxAnalyzerJava.distFrom(trackCache.latitudes[i], trackCache.longitudes[i], trackCache.latitudes[i + 20], trackCache.longitudes[i + 20]);
                MyCalendar startTime = MyCalendar.getTime(trackCache.times[i], MyCalendar.type_self);
                MyCalendar endTime = MyCalendar.getTime(trackCache.times[i + 20], MyCalendar.type_self);
                float seconds = MyCalendar.getMinusTimeInSecond(startTime, endTime);
                float speed = dist / seconds * 18 / 5;
                speedEntries.add(new Entry(speed, i / 20));
                xVals.add(trackCache.times[i]);
                if (i >= 20) {
                    for (int j = i - 20; j < i; j++) {
                        totalDistance += GpxAnalyzerJava.distFrom(trackCache.latitudes[j], trackCache.longitudes[j], trackCache.latitudes[j + 1], trackCache.longitudes[j + 1]);
                    }
                }
                distances.add(totalDistance);
            }
            LineDataSet altitudeDataSet = new LineDataSet(altitudeEntries, getString(R.string.Altitude));
            altitudeDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
            altitudeDataSet.setDrawCircles(false);
            altitudeDataSet.setColor(Color.GREEN);
            altitudeDataSet.setValueFormatter(new AltitudeFormatter());
            dataSets.add(altitudeDataSet);

            LineDataSet speedDataSet = new LineDataSet(speedEntries, getString(R.string.velocity));
            speedDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
            speedDataSet.setDrawCircles(false);
            speedDataSet.setColor(Color.BLUE);
            speedDataSet.setValueFormatter(new SpeedFormatter());
            dataSets.add(speedDataSet);
            return dataSets;
        }

        @Override
        protected void onPostExecute(ArrayList<LineDataSet> dataSets) {
            if (dataSets != null) {
                lineChart.setData(new LineData(xVals, dataSets));
                lineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
                lineChart.getAxisLeft().setValueFormatter(new AltitudeFormatter());
                lineChart.getAxisLeft().setStartAtZero(false);
                lineChart.getAxisRight().setValueFormatter(new SpeedFormatter());
                lineChart.getAxisRight().setStartAtZero(false);
                lineChart.setDescription(tripName);
                lineChart.setMarkerView(new MyMarkerView(ViewGraphActivity.this, R.layout.graph_markerview));
                lineChart.setHardwareAccelerationEnabled(true);
                lineChart.invalidate();
                lineChart.setVisibility(View.VISIBLE);
            }
            pb.setVisibility(View.GONE);
        }
    }
}
