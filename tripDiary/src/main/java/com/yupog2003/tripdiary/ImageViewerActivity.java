package com.yupog2003.tripdiary;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageViewerActivity extends MyActivity implements View.OnClickListener {

    private ViewPager viewPager;
    private View contentView;
    private Trip trip;
    public static final String tag_tripName = "tripName";
    private boolean autoRotate;
    private boolean visible;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        contentView = findViewById(R.id.content);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        trip = ((TripDiaryApplication) getApplication()).getTrip(getIntent().getStringExtra(tag_tripName));
        ImageAdapter imageAdapter = new ImageAdapter();
        viewPager.setAdapter(imageAdapter);
        viewPager.addOnPageChangeListener(imageAdapter);
        findPicIndex(getIntent().getData());
        visible = true;
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("autobrightness", false)) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.screenBrightness = 1.0f;
            getWindow().setAttributes(lp);
        }
        autoRotate = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("autorotate", false);
        hide();
    }

    private void toggle() {
        if (visible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        contentView.setFitsSystemWindows(false);
        contentView.setPadding(0, 0, 0, 0);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LOW_PROFILE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.hide();
        visible = false;
    }

    private void show() {
        contentView.setFitsSystemWindows(true);
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
        );
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.show();
        visible = true;
    }

    private void findPicIndex(Uri uri) {
        int index = 0;
        if (trip != null && uri != null) {
            for (int i = 0; i < trip.pois.length; i++) {
                POI poi = trip.pois[i];
                for (int j = 0; j < poi.picFiles.length; j++) {
                    if (uri.equals(poi.picFiles[j].getUri())) {
                        index += j;
                        viewPager.setCurrentItem(index);
                        ((ImageAdapter)viewPager.getAdapter()).onPageSelected(index);
                        return;
                    }
                }
                index += poi.picFiles.length;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_image_viewer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Uri uri = ((ImageAdapter) viewPager.getAdapter()).getCurrentImage().getUri();
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(intent);
                break;
            case R.id.exif:
                try {
                    File tempImage = new File(getCacheDir(), "ImageForEXIF");
                    FileHelper.copyFile(((ImageAdapter) viewPager.getAdapter()).getCurrentImage(), tempImage);
                    ExifInterface exifInterface = new ExifInterface(tempImage.getPath());
                    View view = getLayoutInflater().inflate(R.layout.exif, (ViewGroup) findViewById(R.id.content), false);
                    TextView time = (TextView) view.findViewById(R.id.time);
                    time.setText(exifInterface.getAttribute(ExifInterface.TAG_DATETIME));
                    TextView aperture = (TextView) view.findViewById(R.id.aperture);
                    aperture.setText("F" + exifInterface.getAttribute(ExifInterface.TAG_APERTURE));
                    TextView exposureTime = (TextView) view.findViewById(R.id.exposureTime);
                    exposureTime.setText(exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME) + "s");
                    TextView flash = (TextView) view.findViewById(R.id.flash);
                    flash.setText(exifInterface.getAttribute(ExifInterface.TAG_FLASH));
                    TextView focalLength = (TextView) view.findViewById(R.id.focalLength);
                    focalLength.setText(exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH) + "mm");
                    TextView length = (TextView) view.findViewById(R.id.length);
                    length.setText(exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH) + "px");
                    TextView width = (TextView) view.findViewById(R.id.width);
                    width.setText(exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH) + "px");
                    TextView iso = (TextView) view.findViewById(R.id.iso);
                    iso.setText(exifInterface.getAttribute(ExifInterface.TAG_ISO));
                    TextView model = (TextView) view.findViewById(R.id.model);
                    model.setText(exifInterface.getAttribute(ExifInterface.TAG_MODEL));
                    AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                    ab.setView(view);
                    ab.setTitle("EXIF");
                    ab.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        toggle();
    }


    private class ImageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        ArrayList<DocumentFile> images;
        ArrayList<POI> poiArrayList;

        private ImageAdapter() {
            images = new ArrayList<>();
            poiArrayList = new ArrayList<>();
            if (trip != null) {
                for (POI poi : trip.pois) {
                    images.addAll(Arrays.asList(poi.picFiles));
                    int length = poi.picFiles.length;
                    for (int i = 0; i < length; i++) {
                        poiArrayList.add(poi);
                    }
                }
            }
        }

        private DocumentFile getCurrentImage() {
            return images.get(viewPager.getCurrentItem());
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public View instantiateItem(ViewGroup container, int position) {
            SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(container.getContext());
            imageView.setImage(ImageSource.uri(images.get(position).getUri()));
            if (autoRotate) {
                BitmapFactory.Options o = new BitmapFactory.Options();
                o.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(images.get(position).getInputStream(), new Rect(0, 0, 0, 0), o);
                boolean b1 = DeviceHelper.getScreenWidth(getActivity()) > DeviceHelper.getScreenHeight(getActivity());
                boolean b2 = o.outWidth > o.outHeight;
                if (b1 != b2) {
                    imageView.setOrientation(SubsamplingScaleImageView.ORIENTATION_270);
                }
            }
            imageView.setOnClickListener(ImageViewerActivity.this);
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            String poiName = poiArrayList.get(position).title;
            String imageName = images.get(position).getName();
            setTitle(poiName + "/" + imageName);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }
}

