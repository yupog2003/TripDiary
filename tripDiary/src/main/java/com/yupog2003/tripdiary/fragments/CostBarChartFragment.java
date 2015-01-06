package com.yupog2003.tripdiary.fragments;

import android.app.Fragment;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.data.FileHelper;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CostBarChartFragment extends Fragment {
    String path;
    String title;
    int option;
    int[] colors;
    float[] totals;
    boolean hasData = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.option = getArguments().getInt("option");
        this.path = getArguments().getString("path");
        this.title = getArguments().getString("title");
        return inflater.inflate(R.layout.fragment_cost_barchart, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        TypedArray array = getActivity().getResources().obtainTypedArray(R.array.cost_type_colors);
        colors = new int[array.length()];
        for (int i = 0; i < colors.length; i++)
            colors[i] = array.getColor(i, 0);
        totals = new float[array.length()];
        array.recycle();
        switch (option) {
            case ViewCostFragment.optionPOI:
                File file = new File(path + "/costs");
                File[] files = file.listFiles();
                if (files == null)
                    files = new File[0];
                for (int i = 0; i < files.length; i++) {
                    readData(files[i]);
                    hasData = true;
                }
                break;
            case ViewCostFragment.optionTrip:
                File file2 = new File(path);
                File[] files2 = file2.listFiles();
                if (files2 == null)
                    files2 = new File[0];
                for (int i = 0; i < files2.length; i++) {
                    File file3 = new File(files2[i].getPath() + "/costs");
                    FileHelper.maintenDir(file3);
                    File[] files3 = file3.listFiles();
                    if (files3 != null) {
                        for (int j = 0; j < files3.length; j++) {
                            readData(files3[j]);
                            hasData = true;
                        }
                    }
                }
                break;
        }
        XYMultipleSeriesRenderer render = new XYMultipleSeriesRenderer();
        render.setXAxisMax(4.5);
        render.setXAxisMin(0.5);
        render.setLegendTextSize(30);
        render.setApplyBackgroundColor(true);
        render.setBackgroundColor(Color.WHITE);
        render.setMarginsColor(Color.argb(0x00, 0x01, 0x01, 0x01));
        String[] titles = getActivity().getResources().getStringArray(R.array.cost_types);
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        for (int i = 0; i < totals.length; i++) {
            CategorySeries series = new CategorySeries(titles[i]);
            for (int j = 0; j < totals.length; j++) {
                if (i == j) {
                    series.add(titles[i], totals[j]);
                } else {
                    series.add(0);
                }
            }
            dataset.addSeries(series.toXYSeries());
            SimpleSeriesRenderer r = new SimpleSeriesRenderer();
            r.setColor(colors[i]);
            render.addSeriesRenderer(r);
        }
        FrameLayout layout = (FrameLayout) getView();
        View barchartView = ChartFactory.getBarChartView(getActivity(), dataset, render, Type.DEFAULT);
        if (barchartView != null)
            layout.addView(barchartView);
    }

    private void readData(File file) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String s;
            int type = -1;
            while ((s = br.readLine()) != null) {
                if (s.startsWith("type=")) {
                    type = Integer.valueOf(s.substring(s.indexOf("=") + 1));
                } else if (s.startsWith("dollar=") && type != -1) {
                    totals[type] += Float.valueOf(s.substring(s.indexOf("=") + 1));
                    type = -1;
                }
            }
            br.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
