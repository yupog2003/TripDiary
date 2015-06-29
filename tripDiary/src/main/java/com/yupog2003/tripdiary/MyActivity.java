package com.yupog2003.tripdiary;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.yupog2003.tripdiary.data.MyImageDecoder;


public class MyActivity extends AppCompatActivity {

	@Override
	public void onStart() {

		super.onStart();
		//EasyTracker.getInstance(this).activityStart(this);
	}

	@Override
	public void onStop() {

		super.onStop();
		//EasyTracker.getInstance(this).activityStop(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MyImageDecoder imageDecoder=new MyImageDecoder(this,new BaseImageDecoder(false));
		ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(this).imageDecoder(imageDecoder).build();
		ImageLoader.getInstance().init(conf);
	}

	public Activity getActivity(){
        return this;
    }
}
