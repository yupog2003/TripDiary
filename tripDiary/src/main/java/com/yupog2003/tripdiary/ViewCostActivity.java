package com.yupog2003.tripdiary;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.yupog2003.tripdiary.fragments.ViewCostFragment;

public class ViewCostActivity extends MyActivity {
    ViewCostFragment viewCostFragment;
    public static final String tag_trip = "tag_trip";
    public static final String tag_poi = "tag_poi";
    public static final String tag_option = "tag_option";
    public static final String tag_totals="tah_totoals";
    public static final int optionPOI = 0;
    public static final int optionTrip = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cost);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        int option = getIntent().getIntExtra(tag_option, 0);
        String poiName = getIntent().getStringExtra(tag_poi);
        String tripName = getIntent().getStringExtra(tag_trip);
        setTitle(poiName + " " + getString(R.string.cost));
        viewCostFragment = new ViewCostFragment();
        Bundle args = new Bundle();
        args.putString(ViewCostActivity.tag_trip, tripName);
        args.putString(ViewCostActivity.tag_poi, poiName);
        args.putInt(ViewCostActivity.tag_option, option);
        viewCostFragment.setArguments(args);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.fragmentLayout, viewCostFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                ViewCostActivity.this.finish();
                return true;
        }
        return false;
    }
    public void requestUpdatePOI(){
        Intent intent=new Intent();
        Bundle bundle=new Bundle();
        bundle.putBoolean(ViewTripActivity.tag_request_updatePOI, true);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
    }
}
