package com.yupog2003.tripdiary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.DrawableHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.data.Weather;
import com.yupog2003.tripdiary.fragments.AudioFragment;
import com.yupog2003.tripdiary.fragments.PictureFragment;
import com.yupog2003.tripdiary.fragments.TextFragment;
import com.yupog2003.tripdiary.fragments.VideoFragment;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class ViewPointActivity extends MyActivity {
    public POI poi;
    public static final DecimalFormat latlngFormat = new DecimalFormat("#.######");
    MyPagerAdapter adapter;
    TabLayout pagerTab;
    static final int import_picture = 1;
    static final int import_video = 2;
    static final int import_audio = 3;
    static final int pick_multi_file = 4;
    String timezone;
    ViewPager viewPager;
    AppBarLayout appBarLayout;
    CoordinatorLayout coordinatorLayout;
    int appBarLayoutDefaultHeight;
    public static final String tag_tripname = "tag_tripname";
    public static final String tag_poiname = "tag_poiname";
    public static final String tag_fromActivity = "tag_activity";
    public static final int REQUEST_VIEW_COST = 6;
    static final boolean preLollipop = Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_point);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
            assert getSupportActionBar() != null;
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        String tripName = getIntent().getStringExtra(tag_tripname);
        String poiName = getIntent().getStringExtra(tag_poiname);
        String activity = getIntent().getStringExtra(tag_fromActivity);
        appBarLayout = (AppBarLayout) findViewById(R.id.appbar);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        pagerTab = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        if (activity != null && activity.equals(ViewTripActivity.class.getSimpleName())) {
            Trip trip = ((TripDiaryApplication) getApplication()).getTrip(tripName);
            if (trip == null) {
                finish();
                return;
            }
            poi = trip.getPOI(poiName);
            timezone = trip.timezone;
        } else if (activity != null && activity.equals(AllRecordActivity.class.getSimpleName())) {
            poi = AllRecordActivity.getPOI(tripName, poiName);
            timezone = MyCalendar.getTripTimeZone(this, tripName);
        }
        if (poi == null) {
            finish();
            return;
        }
        setTitle(poiName);
        getSupportActionBar().setSubtitle(poi.time.formatInTimezone(timezone));
        DeviceHelper.sendGATrack(getActivity(), "Trip", "view_poi", poi.parentTripFile.getName() + "-" + poi.title, null);
        appBarLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    appBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    appBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                appBarLayoutDefaultHeight = appBarLayout.getHeight();
                initialtab();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void initialtab() {
        adapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(adapter);
        viewPager.setOffscreenPageLimit(2);
        pagerTab.setupWithViewPager(viewPager);
        if (poi.diary.length() > 0) {
            adapter.onPageSelected(0);
        } else if (poi.picFiles.length > 0) {
            viewPager.setCurrentItem(1);
        } else if (poi.videoFiles.length > 0) {
            viewPager.setCurrentItem(2);
        } else if (poi.audioFiles.length > 0) {
            viewPager.setCurrentItem(3);
        } else {
            adapter.onPageSelected(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_view_point, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (poi == null || !poi.dir.exists()) {
            requestUpdatePOIs(true);
            finish();
        }
    }

    private void resetAppBar() {
        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appBarLayout.getLayoutParams();
        AppBarLayout.Behavior behavior = (AppBarLayout.Behavior) params.getBehavior();
        if (behavior != null) {
            behavior.onNestedFling(coordinatorLayout, appBarLayout, null, 0, -1000, true);
        }
    }

    class MyPagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener {
        TextFragment textFragment;
        PictureFragment pictureFragment;
        VideoFragment videoFragment;
        AudioFragment audioFragment;

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
            textFragment = new TextFragment();
            pictureFragment = new PictureFragment();
            videoFragment = new VideoFragment();
            audioFragment = new AudioFragment();
        }

        @Override
        public Fragment getItem(int arg0) {
            switch (arg0) {
                case 0:
                    return textFragment;
                case 1:
                    return pictureFragment;
                case 2:
                    return videoFragment;
                case 3:
                    return audioFragment;
            }
            return new Fragment();
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            String title = "";
            switch (position) {
                case 0:
                    title = getString(R.string.diary);
                    break;
                case 1:
                    title = getString(R.string.photo);
                    break;
                case 2:
                    title = getString(R.string.video);
                    break;
                case 3:
                    title = getString(R.string.sound);
                    break;
            }
            return title;
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            resetAppBar();
            switch (position) {
                case 0:
                    viewPager.setPadding(0, 0, 0, appBarLayoutDefaultHeight);
                    break;
                case 1:
                    viewPager.setPadding(0, 0, 0, preLollipop ? appBarLayoutDefaultHeight : 0);
                    break;
                case 2:
                    viewPager.setPadding(0, 0, 0, preLollipop ? appBarLayoutDefaultHeight : 0);
                    break;
                case 3:
                    viewPager.setPadding(0, 0, 0, preLollipop ? appBarLayoutDefaultHeight : 0);
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.importpicture) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            if (Build.VERSION.SDK_INT >= 18) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            intent.setType("image/*");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_the_picture_by)), import_picture);
        } else if (item.getItemId() == R.id.importvideo) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            if (Build.VERSION.SDK_INT >= 18) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            intent.setType("video/*");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_the_video_by)), import_video);
        } else if (item.getItemId() == R.id.importaudio) {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            if (Build.VERSION.SDK_INT >= 18) {
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
            intent.setType("audio/*");
            startActivityForResult(Intent.createChooser(intent, getString(R.string.select_the_audio_by)), import_audio);
        } else if (item.getItemId() == R.id.importfiles) {
            Intent intent = new Intent(ViewPointActivity.this, MultiFileChooseActivity.class);
            startActivityForResult(intent, pick_multi_file);
        } else if (item.getItemId() == R.id.playpoint) {
            Intent intent = new Intent(ViewPointActivity.this, PlayPointActivity.class);
            intent.putExtra(PlayPointActivity.tag_trip, poi.dir.getParentFile().getName());
            intent.putExtra(PlayPointActivity.tag_poi, poi.title);
            ViewPointActivity.this.startActivity(intent);
        } else if (item.getItemId() == android.R.id.home) {
            ViewPointActivity.this.finish();
        } else if (item.getItemId() == R.id.viewbasicinformation) {
            AlertDialog.Builder ab = new AlertDialog.Builder(ViewPointActivity.this);
            ab.setTitle(poi.title);
            View layout = getLayoutInflater().inflate(R.layout.view_basicinformation, (ViewGroup) findViewById(android.R.id.content), false);
            TextView location = (TextView) layout.findViewById(R.id.location);
            location.setText("(" + latlngFormat.format(poi.latitude) + "," + latlngFormat.format(poi.longitude) + ")");
            TextView altitude = (TextView) layout.findViewById(R.id.altitude);
            altitude.setText(GpxAnalyzer2.getAltitudeString((float) poi.altitude, "m"));
            TextView time = (TextView) layout.findViewById(R.id.time);
            String timeStr = poi.time.formatInTimezone(timezone);
            time.setText(timeStr);
            poi.time.setTimeZone(timezone);
            ImageView weather = (ImageView) layout.findViewById(R.id.weather);
            weather.setImageResource(Weather.getIconForId(poi.weather, poi.time.get(MyCalendar.HOUR_OF_DAY)));
            ab.setView(layout);
            ab.setPositiveButton(getString(R.string.edit), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    AlertDialog.Builder ab2 = new AlertDialog.Builder(ViewPointActivity.this);
                    ab2.setTitle(getString(R.string.edit));
                    View layout = getLayoutInflater().inflate(R.layout.edit_poi, (ViewGroup) findViewById(android.R.id.content), false);
                    final EditText edittitle = (EditText) layout.findViewById(R.id.edit_poi_title);
                    edittitle.setText(poi.title);
                    final EditText editlatitude = (EditText) layout.findViewById(R.id.edit_poi_latitude);
                    editlatitude.setText(String.valueOf(poi.latitude));
                    final EditText editlongitude = (EditText) layout.findViewById(R.id.edit_poi_longitude);
                    editlongitude.setText(String.valueOf(poi.longitude));
                    final EditText editaltitude = (EditText) layout.findViewById(R.id.edit_poi_altitude);
                    double altitude = poi.altitude;
                    if (TripDiaryApplication.altitude_unit == TripDiaryApplication.unit_ft) {
                        altitude /= 0.3048;
                    }
                    editaltitude.setText(String.valueOf(altitude));
                    final DatePicker editdate = (DatePicker) layout.findViewById(R.id.edit_poi_date);
                    final TimePicker edittime = (TimePicker) layout.findViewById(R.id.edit_poi_time);
                    MyCalendar time = poi.time;
                    time.setTimeZone(timezone);
                    final AppCompatSpinner weather = (AppCompatSpinner) layout.findViewById(R.id.weather);
                    Weather.WeatherAdapter weatherAdapter = new Weather.WeatherAdapter(getActivity(), 0, time.get(MyCalendar.HOUR_OF_DAY), poi);
                    weather.setAdapter(weatherAdapter);
                    weather.setSelection(Weather.WeatherAdapter.getPositionFromId(poi.weather));
                    editdate.updateDate(time.get(Calendar.YEAR), time.get(Calendar.MONTH), time.get(Calendar.DAY_OF_MONTH));
                    edittime.setIs24HourView(true);
                    edittime.setCurrentHour(time.get(Calendar.HOUR_OF_DAY));
                    edittime.setCurrentMinute(time.get(Calendar.MINUTE));
                    ab2.setView(layout);
                    final String oldName = poi.title;
                    final int oldSec = time.get(Calendar.SECOND);
                    ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            String editAltitudeStr = editaltitude.getText().toString();
                            String editLatitudeStr = editlatitude.getText().toString();
                            String editLongitudeStr = editlongitude.getText().toString();
                            if (editAltitudeStr.isEmpty() || editLatitudeStr.isEmpty() || editLongitudeStr.isEmpty()) {
                                Toast.makeText(ViewPointActivity.this, R.string.some_fields_are_not_filled, Toast.LENGTH_SHORT).show();
                                return;
                            }
                            MyCalendar time = MyCalendar.getInstance(TimeZone.getTimeZone(timezone));
                            time.set(editdate.getYear(), editdate.getMonth(), editdate.getDayOfMonth(), edittime.getCurrentHour(), edittime.getCurrentMinute(), oldSec);
                            time.format3339();
                            time.setTimeZone("UTC");
                            double altitude = Double.parseDouble(editAltitudeStr);
                            if (TripDiaryApplication.altitude_unit == TripDiaryApplication.unit_ft) {
                                altitude *= 0.3048;
                            }
                            String weatherId = Weather.WeatherAdapter.getIdFromPosition(weather.getSelectedItemPosition());
                            poi.updateBasicInformation(null, time, Double.parseDouble(editLatitudeStr), Double.parseDouble(editLongitudeStr), altitude, weatherId);
                            String newName = edittitle.getText().toString();
                            if (!newName.equals(oldName)) {
                                if (FileHelper.findfile(poi.dir.getParentFile(), newName) == null) {
                                    poi.renamePOI(newName);
                                    setTitle(newName);
                                    if (getSupportActionBar() != null) {
                                        getSupportActionBar().setSubtitle(poi.time.formatInTimezone(timezone));
                                    }
                                    requestUpdatePOIs(false);
                                } else {
                                    Toast.makeText(getActivity(), R.string.there_is_a_same_poi, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                requestUpdatePOI();
                                if (getSupportActionBar() != null) {
                                    getSupportActionBar().setSubtitle(poi.time.formatInTimezone(timezone));
                                }
                            }
                        }
                    });
                    ab2.setNegativeButton(getString(R.string.cancel), null);
                    ab2.show();
                }
            });
            ab.show();
        } else if (item.getItemId() == R.id.viewcost) {
            Intent intent2 = new Intent(ViewPointActivity.this, ViewCostActivity.class);
            intent2.putExtra(ViewCostActivity.tag_trip, poi.dir.getParentFile().getName());
            intent2.putExtra(ViewCostActivity.tag_poi, poi.dir.getName());
            intent2.putExtra(ViewCostActivity.tag_option, ViewCostActivity.optionPOI);
            startActivityForResult(intent2, REQUEST_VIEW_COST);
        } else if (item.getItemId() == R.id.delete) {
            AlertDialog.Builder ab2 = new AlertDialog.Builder(ViewPointActivity.this);
            ab2.setMessage(getString(R.string.are_you_sure_to_delete_this_poi));
            ab2.setTitle(getString(R.string.be_careful));
            ab2.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    poi.deleteSelf();
                    requestUpdatePOIs(true);
                    ViewPointActivity.this.finish();
                }

            });
            ab2.setNegativeButton(getString(R.string.no), null);
            AlertDialog ad = ab2.create();
            ad.setIcon(DrawableHelper.getAlertDrawable(getActivity()));
            ad.show();
        } else if (item.getItemId() == R.id.sharelocation) {
            String text = "http://maps.google.com/maps?q=loc:" + String.valueOf(poi.latitude) + "," + String.valueOf(poi.longitude);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, text);
            startActivity(intent);
        }
        return false;
    }

    public void requestUpdatePOI() {
        if (poi != null) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putBoolean(ViewTripActivity.tag_request_updatePOI, true);
            bundle.putString(ViewTripActivity.tag_update_poiNames, poi.title);
            intent.putExtras(bundle);
            setResult(Activity.RESULT_OK, intent);
            poi.updateAllFields();
        }
        updateFragments();
    }

    public void requestUpdatePOIs(boolean poiDeleted) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putBoolean(ViewTripActivity.tag_request_updatePOI, true);
        bundle.putString(ViewTripActivity.tag_update_poiNames, null);
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        if (poi != null && !poiDeleted) {
            poi.updateAllFields();
            updateFragments();
        }
    }

    private void updateFragments() {
        if (adapter != null) {
            adapter.textFragment.refresh();
            adapter.pictureFragment.refresh();
            adapter.videoFragment.refresh();
            adapter.audioFragment.refresh();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == pick_multi_file) {
                ArrayList<Uri> uris = data.getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                new ImportFilesTask(uris).execute();
            } else if (requestCode == import_picture || requestCode == import_video || requestCode == import_audio) {
                ClipData clipData;
                ArrayList<Uri> uris = new ArrayList<>();
                if (Build.VERSION.SDK_INT >= 18 && (clipData = data.getClipData()) != null) {
                    for (int i = 0; i < clipData.getItemCount(); i++) {
                        uris.add(clipData.getItemAt(i).getUri());
                    }
                } else {
                    Uri uri = data.getData();
                    if (uri != null) {
                        uris.add(uri);
                    }
                }
                new ImportFilesTask(uris).execute();
            } else if (requestCode == REQUEST_VIEW_COST) {
                Bundle bundle = data.getExtras();
                if (bundle == null) return;
                if (bundle.getBoolean(ViewTripActivity.tag_request_updatePOI, false)) {
                    requestUpdatePOI();
                }
            }
        }
    }

    class ImportFilesTask extends AsyncTask<Void, Integer, String> {

        ProgressDialog pd;
        ArrayList<Uri> uris;

        public ImportFilesTask(ArrayList<Uri> uris) {
            this.uris = uris;
        }

        @Override
        protected void onPreExecute() {
            pd = new ProgressDialog(ViewPointActivity.this);
            pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            if (uris != null)
                pd.setMax(uris.size());
            pd.setTitle(R.string.importing);
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            if (uris == null)
                return null;
            POI.ImportMemoriesListener listener = new POI.ImportMemoriesListener() {
                @Override
                public void onProgressUpdate(int progess) {
                    publishProgress(progess);
                }
            };
            poi.importMemories(listener, uris.toArray(new Uri[uris.size()]));
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            pd.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
            requestUpdatePOI();
        }
    }

}
