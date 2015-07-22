package com.yupog2003.tripdiary;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.yupog2003.tripdiary.fragments.LocalTripsFragment;
import com.yupog2003.tripdiary.fragments.RemoteTripsFragment;

public class ViewActivity extends MyActivity {

    MyPagerAdapter pagerAdaper;
    ViewPager viewPager;
    TabLayout tabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        tabs = (TabLayout) findViewById(R.id.tabs);
        pagerAdaper = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdaper);
        viewPager.addOnPageChangeListener(pagerAdaper);
        tabs.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);
    }

    class MyPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {

        public Fragment[] fragments;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            fragments = new Fragment[3];
            fragments[0] = new RemoteTripsFragment();
            Bundle personalTripFragmentBundle = new Bundle();
            personalTripFragmentBundle.putInt(RemoteTripsFragment.tag_option, RemoteTripsFragment.option_personal);
            fragments[0].setArguments(personalTripFragmentBundle);
            fragments[1] = new LocalTripsFragment();
            Bundle localTripFragmentBundle = new Bundle();
            fragments[1].setArguments(localTripFragmentBundle);
            fragments[2] = new RemoteTripsFragment();
            Bundle publicTripFragmentBundle = new Bundle();
            publicTripFragmentBundle.putInt(RemoteTripsFragment.tag_option, RemoteTripsFragment.option_public);
            fragments[2].setArguments(publicTripFragmentBundle);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return getString(R.string.remote_personal_trips);
                case 1:
                    return getString(R.string.local_trips);
                case 2:
                    return getString(R.string.remote_public_trips);
            }
            return getTitle();
        }

        @Override
        public Fragment getItem(int position) {
            return fragments[position];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            Fragment f = getItem(position);
            if (f instanceof RemoteTripsFragment) {
                ((RemoteTripsFragment) f).loaddata();
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
