package com.yupog2003.tripdiary;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;

import com.google.analytics.tracking.android.EasyTracker;

public class MyActivity extends ActionBarActivity {

	@Override
	public void onStart() {

		super.onStart();
		EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {

		super.onStop();
		EasyTracker.getInstance(this).activityStop(this);
	}

    public Activity getActivity(){
        return this;
    }
}
