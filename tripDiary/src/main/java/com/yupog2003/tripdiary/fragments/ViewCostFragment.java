package com.yupog2003.tripdiary.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.provider.DocumentFile;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.ViewCostActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Trip;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class ViewCostFragment extends Fragment implements View.OnClickListener {

    public int option;
    public String tripName;
    public String poiName;
    ViewGroup rootView;
    TabLayout tabLayout;
    FloatingActionButton add;
    ViewPager viewPager;
    TabsAdapter adapter;
    public DocumentFile[] costFiles;
    float[] totals;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.option = getArguments().getInt(ViewCostActivity.tag_option);
        this.tripName = getArguments().getString(ViewCostActivity.tag_trip);
        this.poiName = getArguments().getString(ViewCostActivity.tag_poi);
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_view_cost, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
        add = (FloatingActionButton) rootView.findViewById(R.id.add);
        add.setOnClickListener(this);
        if (option == ViewCostActivity.optionTrip) {
            add.setVisibility(View.GONE);
        }
        if (option == ViewCostActivity.optionPOI) {
            refreshData(false);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setHasOptionsMenu(true);
    }

    public void refreshData(boolean updatePOI) {
        if (option == ViewCostActivity.optionTrip) {
            Trip trip = ViewTripActivity.trip;
            ArrayList<DocumentFile> costList = new ArrayList<>();
            for (POI poi : trip.pois) {
                costList.addAll(Arrays.asList(poi.costFiles));
            }
            costFiles = costList.toArray(new DocumentFile[costList.size()]);
        } else if (option == ViewCostActivity.optionPOI) {
            if (updatePOI) {
                ViewTripActivity.onPOIUpdate(poiName);
            }
            DocumentFile poiFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, poiName, "costs");
            costFiles = poiFile.listFiles();
        }
        TypedArray array = getActivity().getResources().obtainTypedArray(R.array.cost_type_colors);
        totals = new float[array.length()];
        array.recycle();
        for (DocumentFile file : costFiles) {
            readData(file);
        }
        refresh();
    }

    public void refresh() {
        adapter = new TabsAdapter(getChildFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.activity_view_cost, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return true;
    }

    @Override
    public void onClick(View v) {
        if (v.equals(add)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
            ab.setTitle(getString(R.string.cost));
            final LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.take_money, rootView, false);
            ab.setView(layout);
            ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    EditText costName = (EditText) layout.findViewById(R.id.costname);
                    RadioGroup costType = (RadioGroup) layout.findViewById(R.id.costtype);
                    EditText costDollar = (EditText) layout.findViewById(R.id.costdollar);
                    String name = costName.getText().toString();
                    String dollar = costDollar.getText().toString();
                    if (!name.equals("") && !dollar.equals("")) {
                        int type;
                        if (costType.getCheckedRadioButtonId() == R.id.food) {
                            type = 0;
                        } else if (costType.getCheckedRadioButtonId() == R.id.lodging) {
                            type = 1;
                        } else if (costType.getCheckedRadioButtonId() == R.id.transportation) {
                            type = 2;
                        } else if (costType.getCheckedRadioButtonId() == R.id.other) {
                            type = 3;
                        } else {
                            type = 0;
                        }
                        try {
                            DocumentFile outputFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, poiName, "costs").createFile("", name);
                            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getActivity().getContentResolver().openOutputStream(outputFile.getUri())));
                            bw.write("type=" + String.valueOf(type) + "\n");
                            bw.write("dollar=" + dollar);
                            bw.flush();
                            bw.close();
                            refreshData(true);
                        } catch (IOException e) {

                            e.printStackTrace();
                        }
                    }
                }
            });
            ab.setNegativeButton(getString(R.string.cancel), null);
            ab.show();
        }
    }

    class TabsAdapter extends FragmentPagerAdapter implements OnPageChangeListener {

        CostListFragment costListFragment;
        CostPieChartFragment costPieChartFragment;
        CostBarChartFragment costBarChartFragment;

        public TabsAdapter(FragmentManager fm) {
            super(fm);
            costListFragment = new CostListFragment();
            costPieChartFragment = new CostPieChartFragment();
            costBarChartFragment = new CostBarChartFragment();
            Bundle args = new Bundle();
            args.putString(ViewCostActivity.tag_trip, tripName);
            args.putString(ViewCostActivity.tag_poi, poiName);
            args.putInt(ViewCostActivity.tag_option, option);
            args.putFloatArray(ViewCostActivity.tag_totals, totals);
            costListFragment.setArguments(args);
            costPieChartFragment.setArguments(args);
            costBarChartFragment.setArguments(args);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return costListFragment;
                case 1:
                    return costPieChartFragment;
                case 2:
                    return costBarChartFragment;
                default:
                    return costListFragment;
            }
        }

        @Override
        public int getCount() {

            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return getString(R.string.list);
                case 1:
                    return getString(R.string.piechart);
                case 2:
                    return getString(R.string.barchart);
            }
            return getActivity().getTitle();
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            add.setVisibility(option == ViewCostActivity.optionPOI && position == 0 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void readData(DocumentFile file) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getActivity().getContentResolver().openInputStream(file.getUri())));
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
        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
