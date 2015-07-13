package com.yupog2003.tripdiary.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v13.app.FragmentStatePagerAdapter;
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
import com.yupog2003.tripdiary.views.SlidingTabLayout;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ViewCostFragment extends Fragment implements View.OnClickListener {

    public int option;
    public static final int optionPOI = 0;
    public static final int optionTrip = 1;
    public String path;
    public String title;
    View rootView;
    SlidingTabLayout tabLayout;
    FloatingActionButton add;
    ViewPager viewPager;
    TabsAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        this.option = getArguments().getInt("option");
        this.path = getArguments().getString("path");
        this.title = getArguments().getString("title");
        rootView = inflater.inflate(R.layout.fragment_view_cost, container, false);
        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        tabLayout = (SlidingTabLayout) rootView.findViewById(R.id.tabs);
        add = (FloatingActionButton) rootView.findViewById(R.id.add);
        add.setOnClickListener(this);
        if (option == optionTrip) {
            add.setVisibility(View.GONE);
        }
        refresh();
        return rootView;
    }

    @Override
    public void onResume() {

        super.onResume();
        setHasOptionsMenu(true);
    }

    public void refresh() {
        adapter = new TabsAdapter(getFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(adapter);
        tabLayout.setSelectedIndicatorColors(Color.WHITE);
        tabLayout.setDistributeEvenly(true);
        tabLayout.setViewPager(viewPager);

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
            final LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.take_money, null);
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
                            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(path + "/costs/" + name), false));
                            bw.write("type=" + String.valueOf(type) + "\n");
                            bw.write("dollar=" + dollar);
                            bw.flush();
                            bw.close();
                            refresh();
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

    class TabsAdapter extends FragmentStatePagerAdapter implements OnPageChangeListener {

        CostListFragment costListFragment;
        CostPieChartFragment costPieChartFragment;
        CostBarChartFragment costBarChartFragment;

        public TabsAdapter(FragmentManager fm) {
            super(fm);

            costListFragment = new CostListFragment();
            costPieChartFragment = new CostPieChartFragment();
            costBarChartFragment = new CostBarChartFragment();
            Bundle args = new Bundle();
            args.putString("path", path);
            args.putString("title", title);
            args.putInt("option", option);
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
            add.setVisibility(option == optionPOI && position == 0 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

}
