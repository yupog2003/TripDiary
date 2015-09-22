package com.yupog2003.tripdiary.fragments;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewCostActivity;

import java.util.ArrayList;

public class CostBarChartFragment extends Fragment {
    int[] colors;
    float[] totals;
    BarChart barChart;

    public CostBarChartFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.barChart = new BarChart(getActivity());
        refresh();
        return barChart;
    }

    public void refresh() {
        TypedArray array = getActivity().getResources().obtainTypedArray(R.array.cost_type_colors);
        colors = new int[array.length()];
        for (int i = 0; i < colors.length; i++)
            colors[i] = array.getColor(i, 0);
        array.recycle();
        totals = getArguments().getFloatArray(ViewCostActivity.tag_totals);
        String[] titles = getActivity().getResources().getStringArray(R.array.cost_types);
        ArrayList<BarEntry> values = new ArrayList<>();
        for (int i = 0; i < totals.length; i++) {
            values.add(new BarEntry(totals[i], i));
        }
        BarDataSet barDataSet = new BarDataSet(values, "");
        barDataSet.setColors(colors);
        BarData barData = new BarData(titles, barDataSet);
        barChart.setData(barData);
        barChart.setDescription("");
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getLegend().setEnabled(false);
        barChart.invalidate();
    }

}
