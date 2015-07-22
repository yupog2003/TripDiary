package com.yupog2003.tripdiary.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewCostActivity;

import java.util.ArrayList;

public class CostPieChartFragment extends Fragment {
    int option;
    int[] colors;
    float[] totals;
    PieChart pieChart;
    public String tripName;
    public String poiName;

    public CostPieChartFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.option = getArguments().getInt(ViewCostActivity.tag_option);
        this.tripName = getArguments().getString(ViewCostActivity.tag_trip);
        this.poiName = getArguments().getString(ViewCostActivity.tag_poi);
        this.pieChart = new PieChart(getActivity());
        refresh();
        return pieChart;
    }

    public void refresh(){
        TypedArray array = getActivity().getResources().obtainTypedArray(R.array.cost_type_colors);
        colors = new int[array.length()];
        for (int i = 0; i < colors.length; i++)
            colors[i] = array.getColor(i, 0);
        array.recycle();
        totals = getArguments().getFloatArray(ViewCostActivity.tag_totals);
        ArrayList<Entry> values = new ArrayList<>();
        String[] titles = getActivity().getResources().getStringArray(R.array.cost_types);
        for (int i = 0; i < totals.length; i++) {
            values.add(new Entry(totals[i], i));
        }
        PieDataSet pieDataSet = new PieDataSet(values, "");
        pieDataSet.setColors(colors);
        PieData pieData = new PieData(titles, pieDataSet);
        pieChart.setData(pieData);
        pieChart.setDescription("");
        pieChart.invalidate();
    }

}
