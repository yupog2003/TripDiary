package com.yupog2003.tripdiary;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.yupog2003.tripdiary.fragments.CalendarTripsFragment;
import com.yupog2003.tripdiary.fragments.DriveTripsFragment;
import com.yupog2003.tripdiary.fragments.LocalTripsFragment;
import com.yupog2003.tripdiary.fragments.RemoteTripsFragment;

public class ViewActivity extends MyActivity implements NavigationView.OnNavigationItemSelectedListener {

    LocalTripsFragment localTripsFragment;
    RemoteTripsFragment personalFragment;
    RemoteTripsFragment publicFragment;
    DriveTripsFragment driveTripsFragment;
    CalendarTripsFragment calendarTripsFragment;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        setTitle(R.string.local_trips);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerlayout);
        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolBar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);
        personalFragment = new RemoteTripsFragment();
        Bundle personalTripFragmentBundle = new Bundle();
        personalTripFragmentBundle.putInt(RemoteTripsFragment.tag_option, RemoteTripsFragment.option_personal);
        personalFragment.setArguments(personalTripFragmentBundle);
        localTripsFragment = new LocalTripsFragment();
        Bundle localTripFragmentBundle = new Bundle();
        localTripsFragment.setArguments(localTripFragmentBundle);
        publicFragment = new RemoteTripsFragment();
        Bundle publicTripFragmentBundle = new Bundle();
        publicTripFragmentBundle.putInt(RemoteTripsFragment.tag_option, RemoteTripsFragment.option_public);
        publicFragment.setArguments(publicTripFragmentBundle);
        driveTripsFragment = new DriveTripsFragment();
        calendarTripsFragment=new CalendarTripsFragment();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.fragment, localTripsFragment);
        ft.add(R.id.fragment, calendarTripsFragment);
        ft.add(R.id.fragment, personalFragment);
        ft.add(R.id.fragment, publicFragment);
        ft.add(R.id.fragment, driveTripsFragment);
        ft.hide(calendarTripsFragment);
        ft.hide(personalFragment);
        ft.hide(publicFragment);
        ft.hide(driveTripsFragment);
        ft.commit();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DriveTripsFragment.REQUEST_PICK_DIR){
            driveTripsFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        drawerLayout.closeDrawers();
        resetAppBar();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.hide(localTripsFragment);
        ft.hide(calendarTripsFragment);
        ft.hide(personalFragment);
        ft.hide(publicFragment);
        ft.hide(driveTripsFragment);
        ft.commit();
        ft = getSupportFragmentManager().beginTransaction();
        switch (menuItem.getItemId()) {
            case R.id.local:
                ft.show(localTripsFragment);
                setTitle(R.string.local_trips);
                localTripsFragment.loadData();
                break;
            case R.id.calendar:
                ft.show(calendarTripsFragment);
                setTitle(R.string.calendar);
                calendarTripsFragment.loadData();
                break;
            case R.id.cloudPersonal:
                ft.show(personalFragment);
                setTitle(R.string.remote_personal_trips);
                personalFragment.loadData();
                break;
            case R.id.cloudPublic:
                ft.show(publicFragment);
                setTitle(R.string.remote_public_trips);
                publicFragment.loadData();
                break;
            case R.id.drive:
                ft.show(driveTripsFragment);
                setTitle(R.string.google_drive);
                driveTripsFragment.loadData();
                break;
        }
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.commit();
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return actionBarDrawerToggle.onOptionsItemSelected(item);
    }

    private void resetAppBar() {
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.onNestedFling(coordinatorLayout, appBarLayout, null, 0, -1000, true);
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }
}
