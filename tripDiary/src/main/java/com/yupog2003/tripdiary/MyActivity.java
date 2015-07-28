package com.yupog2003.tripdiary;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.yupog2003.tripdiary.data.MyImageDecoder;
import com.yupog2003.tripdiary.data.MyImageDownloader;


public class MyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyImageDecoder imageDecoder = new MyImageDecoder(this, new BaseImageDecoder(false));
        MyImageDownloader imageDownloader=new MyImageDownloader(this);
        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(this).imageDownloader(imageDownloader).imageDecoder(imageDecoder).build();
        ImageLoader.getInstance().init(conf);
    }

    public Activity getActivity() {
        return this;
    }
}
