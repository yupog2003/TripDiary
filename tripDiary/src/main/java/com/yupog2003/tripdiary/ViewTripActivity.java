package com.yupog2003.tripdiary;

import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.fragments.AllAudioFragment;
import com.yupog2003.tripdiary.fragments.AllPictureFragment;
import com.yupog2003.tripdiary.fragments.AllTextFragment;
import com.yupog2003.tripdiary.fragments.AllVideoFragment;
import com.yupog2003.tripdiary.fragments.ViewCostFragment;
import com.yupog2003.tripdiary.fragments.ViewMapFragment;

import java.io.File;

public class ViewTripActivity extends MyActivity implements OnClickListener {

    public static String path;
    public static String name;
    public static Trip trip;
    public static int rotation;
    public static boolean updatePOI;
    ViewMapFragment viewMapFragment;
    AllAudioFragment allAudioFragment;
    AllTextFragment allTextFragment;
    AllPictureFragment allPictureFragment;
    AllVideoFragment allVideoFragment;
    ViewCostFragment viewCostFragment;
    Button diary;
    Button photo;
    Button video;
    Button audio;
    Button map;
    Button money;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawerToggle;
    Mode mode = Mode.map_mode;

    enum Mode {
        map_mode, text_mode, photo_mode, video_mode, audio_mode, money_mode
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trip);
        if (getIntent().getBooleanExtra("stoptrip", false)) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            nm.cancel(0);
        }
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        int sWidth = DeviceHelper.getScreenWidth(this);
        int sHeight = DeviceHelper.getScreenHeight(this);
        rotation = sWidth > sHeight ? Surface.ROTATION_90 : Surface.ROTATION_0;
        path = getIntent().getStringExtra("path");
        name = getIntent().getStringExtra("name");
        trip = new Trip(ViewTripActivity.this, new File(path + "/" + name), false);
        updatePOI = false;
        allAudioFragment = new AllAudioFragment();
        allVideoFragment = new AllVideoFragment();
        allPictureFragment = new AllPictureFragment();
        allTextFragment = new AllTextFragment();
        viewMapFragment = new ViewMapFragment();
        viewCostFragment = new ViewCostFragment();
        Bundle args = new Bundle();
        args.putString("path", trip.dir.getPath());
        args.putInt("option", ViewCostFragment.optionTrip);
        args.putString("title", trip.dir.getName());
        viewCostFragment.setArguments(args);
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(R.id.fragment, viewMapFragment);
        ft.add(R.id.fragment, allAudioFragment);
        ft.add(R.id.fragment, allPictureFragment);
        ft.add(R.id.fragment, allTextFragment);
        ft.add(R.id.fragment, allVideoFragment);
        ft.add(R.id.fragment, viewCostFragment);
        ft.commit();
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(drawerToggle);
        map = (Button) findViewById(R.id.map);
        map.setOnClickListener(this);
        map.setSelected(true);
        diary = (Button) findViewById(R.id.diary);
        diary.setOnClickListener(this);
        photo = (Button) findViewById(R.id.photo);
        photo.setOnClickListener(this);
        video = (Button) findViewById(R.id.video);
        video.setOnClickListener(this);
        audio = (Button) findViewById(R.id.audio);
        audio.setOnClickListener(this);
        money = (Button) findViewById(R.id.money);
        money.setOnClickListener(this);
        setMode(Mode.map_mode);
    }

    private void setMode(Mode mode) {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.hide(viewMapFragment);
        ft.hide(allTextFragment);
        ft.hide(allAudioFragment);
        ft.hide(allPictureFragment);
        ft.hide(allVideoFragment);
        ft.hide(viewCostFragment);
        ft.commit();
        map.setSelected(false);
        diary.setSelected(false);
        photo.setSelected(false);
        video.setSelected(false);
        audio.setSelected(false);
        money.setSelected(false);
        ft = getFragmentManager().beginTransaction();
        this.mode = mode;
        switch (mode) {
            case map_mode:
                this.setTitle(trip.tripName);
                ft.show(viewMapFragment);
                map.setSelected(true);
                break;
            case text_mode:
                this.setTitle(trip.tripName + "-" + getString(R.string.diary));
                allTextFragment.refresh();
                ft.show(allTextFragment);
                diary.setSelected(true);
                break;
            case photo_mode:
                this.setTitle(trip.tripName + "-" + getString(R.string.photo));
                allPictureFragment.refresh();
                ft.show(allPictureFragment);
                photo.setSelected(true);
                break;
            case video_mode:
                this.setTitle(trip.tripName + "-" + getString(R.string.video));
                allVideoFragment.refresh();
                ft.show(allVideoFragment);
                video.setSelected(true);
                break;
            case audio_mode:
                this.setTitle(trip.tripName + "-" + getString(R.string.sound));
                allAudioFragment.refresh();
                ft.show(allAudioFragment);
                audio.setSelected(true);
                break;
            case money_mode:
                this.setTitle(trip.tripName + "-" + getString(R.string.cost));
                viewCostFragment.refresh();
                ft.show(viewCostFragment);
                money.setSelected(true);
                break;
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }

    @Override
    public void onBackPressed() {

        if (mode == Mode.map_mode) {
            finish();
        } else {
            setMode(Mode.map_mode);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return drawerToggle.onOptionsItemSelected(item);

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (updatePOI){
            onPOIUpdate();
        }
    }

    public void onClick(View v) {

        if (v.equals(map)) {
            setMode(Mode.map_mode);
            drawerLayout.closeDrawers();
        } else if (v.equals(diary)) {
            setMode(Mode.text_mode);
            drawerLayout.closeDrawers();
        } else if (v.equals(photo)) {
            setMode(Mode.photo_mode);
            drawerLayout.closeDrawers();
        } else if (v.equals(video)) {
            setMode(Mode.video_mode);
            drawerLayout.closeDrawers();
        } else if (v.equals(audio)) {
            setMode(Mode.audio_mode);
            drawerLayout.closeDrawers();
        } else if (v.equals(money)) {
            setMode(Mode.money_mode);
            drawerLayout.closeDrawers();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void startActivityForResult(Intent intent, int requestCode) {
        try {
            super.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    public void onPOIUpdate() {
        if (viewMapFragment != null) {
            viewMapFragment.setPOIs();
        }
        if (allTextFragment!=null){
            allTextFragment.refresh();
        }
        if (allPictureFragment!=null){
            allPictureFragment.refresh();
        }
        if (allVideoFragment!=null){
            allVideoFragment.refresh();
        }
        if (allAudioFragment!=null){
            allAudioFragment.refresh();
        }
        updatePOI = false;
    }

    public static boolean libraryLoadSuccess = false;

    static {
        try {
            System.loadLibrary("TripDiary");
            libraryLoadSuccess = true;
        } catch (Exception e) {
            e.printStackTrace();
            libraryLoadSuccess = false;
        } catch (UnsatisfiedLinkError e) {
            e.printStackTrace();
            libraryLoadSuccess = false;
        }

    }
}
