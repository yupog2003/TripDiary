package com.yupog2003.tripdiary.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.database.DataSetObserver;
import android.os.Bundle;
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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;

import com.yupog2003.tripdiary.R;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ViewCostFragment extends Fragment {

	public int option;
	public static final int optionPOI = 0;
	public static final int optionTrip = 1;
	public String path;
	public String title;
	View rootView;
	TabHost tabHost;
	ViewPager viewPager;
	TabsAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		this.option = getArguments().getInt("option");
		this.path = getArguments().getString("path");
		this.title = getArguments().getString("title");
		rootView = inflater.inflate(R.layout.fragment_view_cost, null);
		viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
		tabHost = (TabHost) rootView.findViewById(android.R.id.tabhost);
		tabHost.setup();
		ImageButton btn_list = new ImageButton(getActivity());
		btn_list.setImageResource(R.drawable.ic_money);
		btn_list.setBackgroundResource(R.drawable.btn_cost_selector);
		ImageButton btn_pie = new ImageButton(getActivity());
		btn_pie.setImageResource(R.drawable.ic_piechart);
		btn_pie.setBackgroundResource(R.drawable.btn_cost_selector);
		ImageButton btn_line = new ImageButton(getActivity());
		btn_line.setImageResource(R.drawable.ic_line_chart);
		btn_line.setBackgroundResource(R.drawable.btn_cost_selector);
		tabHost.addTab(tabHost.newTabSpec("list").setIndicator(btn_list).setContent(R.id.viewpager));
		tabHost.addTab(tabHost.newTabSpec("pie").setIndicator(btn_pie).setContent(R.id.viewpager));
		tabHost.addTab(tabHost.newTabSpec("line").setIndicator(btn_line).setContent(R.id.viewpager));
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
		if (tabHost != null) {
			tabHost.setOnTabChangedListener(adapter);
		}
		if (viewPager != null) {
			viewPager.setAdapter(adapter);
			viewPager.setOnPageChangeListener(adapter);
			viewPager.setCurrentItem(1);
			viewPager.setCurrentItem(0);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

		inflater.inflate(R.menu.activity_view_cost, menu);
		if (option == optionTrip) {
			menu.findItem(R.id.addcost).setVisible(false);
		}

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.addcost) {
			if (option == optionPOI) {
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
							int type = -1;
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
		return true;
	}

	class TabsAdapter extends FragmentStatePagerAdapter implements OnTabChangeListener, OnPageChangeListener {

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
		public void unregisterDataSetObserver(DataSetObserver observer) {

			if (observer != null) {
				super.unregisterDataSetObserver(observer);
			}

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
		public void onTabChanged(String tabId) {

			if (viewPager!=null){
				if (tabId.equals("list")) {
					viewPager.setCurrentItem(0);
				} else if (tabId.equals("pie")) {
					viewPager.setCurrentItem(1);
				} else if (tabId.equals("line")) {
					viewPager.setCurrentItem(2);
				}
			}
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {


		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {


		}

		@Override
		public void onPageSelected(int arg0) {

			tabHost.setCurrentTab(arg0);
		}

	}

}
