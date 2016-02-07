package com.yupog2003.tripdiary;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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

public class ImageViewerActivity extends MyActivity {

    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private ViewPager viewPager;
    private Trip trip;
    public static final String tag_tripName = "tripName";
    private final Runnable mHidePart2Runnable = new Runnable() {
        @SuppressLint("InlinedApi")
        @Override
        public void run() {

            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        }
    };
    private final Runnable mShowPart2Runnable = new Runnable() {
        @Override
        public void run() {
            // Delayed display of UI elements
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.show();
            }
        }
    };
    private boolean mVisible;
    private boolean autoRotate;
    private final Runnable mHideRunnable = new Runnable() {
        @Override
        public void run() {
            hide();
        }
    };
    private final View.OnClickListener contentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            toggle();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        mVisible = true;
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        trip = ((TripDiaryApplication) getApplication()).getTrip(getIntent().getStringExtra(tag_tripName));
        ImageAdapter imageAdapter = new ImageAdapter();
        viewPager.setAdapter(imageAdapter);
        viewPager.addOnPageChangeListener(imageAdapter);
        findPicIndex(getIntent().getData());
        if (PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("autobrightness", false)) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.screenBrightness = 1.0f;
            getWindow().setAttributes(lp);
        }
        autoRotate = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("autorotate", false);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        delayedHide(100);
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
                        setTitle(poi.picFiles[j].getName());
                        return;
                    }
                }
                index += poi.picFiles.length;
            }
        }
    }

    private void toggle() {
        if (mVisible) {
            hide();
        } else {
            show();
        }
    }

    private void hide() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mVisible = false;

        // Schedule a runnable to remove the status and navigation bar after a delay
        mHideHandler.removeCallbacks(mShowPart2Runnable);
        mHideHandler.postDelayed(mHidePart2Runnable, UI_ANIMATION_DELAY);
    }

    @SuppressLint("InlinedApi")
    private void show() {
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        mVisible = true;

        // Schedule a runnable to display UI elements after a delay
        mHideHandler.removeCallbacks(mHidePart2Runnable);
        mHideHandler.postDelayed(mShowPart2Runnable, UI_ANIMATION_DELAY);
    }

    /**
     * Schedules a call to hide() in [delay] milliseconds, canceling any
     * previously scheduled calls.
     */
    private void delayedHide(int delayMillis) {
        mHideHandler.removeCallbacks(mHideRunnable);
        mHideHandler.postDelayed(mHideRunnable, delayMillis);
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

    class ImageAdapter extends PagerAdapter implements ViewPager.OnPageChangeListener {

        ArrayList<DocumentFile> images;

        public ImageAdapter() {
            images = new ArrayList<>();
            if (trip != null) {
                for (POI poi : trip.pois) {
                    images.addAll(Arrays.asList(poi.picFiles));
                }
            }
        }

        public DocumentFile getCurrentImage() {
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
            //Log.i("trip", "instantiate " + String.valueOf(position));
            SubsamplingScaleImageView imageView = new SubsamplingScaleImageView(container.getContext());
            imageView.setImage(ImageSource.uri(images.get(position).getUri()));
            imageView.setTag(position);
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
            imageView.setOnClickListener(contentOnClickListener);
            container.addView(imageView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            //Log.i("trip", "destroy " + String.valueOf(position));
            ((SubsamplingScaleImageView) object).recycle();
            container.removeView((View) object);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            /*View previousView = viewPager.findViewWithTag(viewPager.getCurrentItem() - 1);
            if (previousView != null && previousView instanceof SubsamplingScaleImageView) {
                previousView.setVisibility(View.INVISIBLE);
            }
            View nextView = viewPager.findViewWithTag(viewPager.getCurrentItem() + 1);
            if (nextView != null && nextView instanceof SubsamplingScaleImageView) {
                nextView.setVisibility(View.INVISIBLE);
            }*/
            //Log.i("trip", String.valueOf(viewPager.getCurrentItem()) + "," + String.valueOf(position) + "," + String.valueOf(positionOffset) + "," + String.valueOf(positionOffsetPixels));
        }

        @Override
        public void onPageSelected(int position) {
            //Log.i("trip", "onPageSelected " + String.valueOf(position));
            setTitle(images.get(position).getName());
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            //Log.i("trip", "ScrollState " + String.valueOf(state));
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                /*View currentView = viewPager.findViewWithTag(viewPager.getCurrentItem());
                if (currentView != null && currentView instanceof SubsamplingScaleImageView) {
                    currentView.setVisibility(View.VISIBLE);
                }*/
            }
        }
    }
}
