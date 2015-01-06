package com.yupog2003.tripdiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v13.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.TimeAnalyzer;
import com.yupog2003.tripdiary.fragments.AudioFragment;
import com.yupog2003.tripdiary.fragments.PictureFragment;
import com.yupog2003.tripdiary.fragments.TextFragment;
import com.yupog2003.tripdiary.fragments.VideoFragment;
import com.yupog2003.tripdiary.fragments.ViewCostFragment;
import com.yupog2003.tripdiary.views.PagerSlidingTabStrip;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class ViewPointActivity extends MyActivity {
	String path;
	String name;
	public static POI poi;
	public static final DecimalFormat latlngFormat = new DecimalFormat("#.######");
	MyPagerAdapter adapter;
	PagerSlidingTabStrip pagerTab;
	static final int pick_multi_file = 0;
	static final int import_picture = 1;
	static final int import_video = 2;
	static final int import_audio = 3;
	String timezone;
	ViewPager viewPager;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_point);
		Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
		if (toolBar != null) {
			setSupportActionBar(toolBar);
		}
		path = getIntent().getStringExtra("path");
		name = path.substring(path.lastIndexOf("/") + 1);
		timezone = TimeAnalyzer.getPOITimeZone(ViewPointActivity.this, path);
		poi = new POI(new File(path));
		ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(ViewPointActivity.this).build();
		ImageLoader.getInstance().init(conf);
		setTitle(name);
		initialtab(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void initialtab(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		viewPager = (ViewPager) findViewById(R.id.viewpager);
		adapter = new MyPagerAdapter(getFragmentManager());
		viewPager.setAdapter(adapter);
		pagerTab = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pagerTab.setIndicatorColorResource(android.R.color.holo_blue_light);
		pagerTab.setShouldExpand(true);
		pagerTab.setViewPager(viewPager);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_view_point, menu);
		return true;
	}

	class MyPagerAdapter extends FragmentPagerAdapter {
		TextFragment textFragment;
		PictureFragment pictureFragment;
		VideoFragment videoFragment;
		AudioFragment audioFragment;

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
			// TODO Auto-generated constructor stub
			textFragment = new TextFragment();
			pictureFragment = new PictureFragment();
			videoFragment = new VideoFragment();
			audioFragment = new AudioFragment();
		}

		@Override
		public Fragment getItem(int arg0) {
			// TODO Auto-generated method stub
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
			// TODO Auto-generated method stub
			return 4;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			// TODO Auto-generated method stub
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
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.importpicture) {
			Intent i = new Intent(Intent.ACTION_GET_CONTENT);
			i.setType("image/*");
			startActivityForResult(Intent.createChooser(i, getString(R.string.select_the_picture_by)), import_picture);
		} else if (item.getItemId() == R.id.importvideo) {
			Intent i2 = new Intent(Intent.ACTION_GET_CONTENT);
			i2.setType("video/*");
			startActivityForResult(Intent.createChooser(i2, getString(R.string.select_the_video_by)), import_video);
		} else if (item.getItemId() == R.id.importaudio) {
			Intent i3 = new Intent(Intent.ACTION_GET_CONTENT);
			i3.setType("audio/*");
			startActivityForResult(Intent.createChooser(i3, getString(R.string.select_the_audio_by)), import_audio);
		} else if (item.getItemId() == R.id.importfiles) {
			Intent intent = new Intent(ViewPointActivity.this, MultiFileChooseActivity.class);
			intent.putExtra("root", Environment.getExternalStorageDirectory().getPath());
			startActivityForResult(intent, pick_multi_file);
		} else if (item.getItemId() == R.id.playpoint) {
			Intent intent = new Intent(ViewPointActivity.this, PlayPointActivity.class);
			intent.putExtra("path", path);
			ViewPointActivity.this.startActivity(intent);
		} else if (item.getItemId() == android.R.id.home) {
			ViewPointActivity.this.finish();
		} else if (item.getItemId() == R.id.viewbasicinformation) {
			AlertDialog.Builder ab = new AlertDialog.Builder(ViewPointActivity.this);
			ab.setTitle(poi.title);
			LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
			View layout = inflater.inflate(R.layout.view_basicinformation, null);
			TextView location = (TextView) layout.findViewById(R.id.location);
			location.setText("(" + latlngFormat.format(poi.latitude) + "," + latlngFormat.format(poi.longitude) + ")");
			TextView altitude = (TextView) layout.findViewById(R.id.altitude);
			altitude.setText(GpxAnalyzer2.getAltitudeString((float) poi.altitude, "m"));
			TextView time = (TextView) layout.findViewById(R.id.time);
			time.setText(TimeAnalyzer.formatInTimezone(poi.time, timezone));
			ab.setView(layout);
			ab.setPositiveButton(getString(R.string.edit), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					AlertDialog.Builder ab2 = new AlertDialog.Builder(ViewPointActivity.this);
					ab2.setTitle(getString(R.string.edit));
					View layout = getLayoutInflater().inflate(R.layout.edit_poi, null);
					final EditText edittitle = (EditText) layout.findViewById(R.id.edit_poi_title);
					edittitle.setText(poi.title);
					final EditText editlatitude = (EditText) layout.findViewById(R.id.edit_poi_latitude);
					editlatitude.setText(String.valueOf(poi.latitude));
					final EditText editlongitude = (EditText) layout.findViewById(R.id.edit_poi_longitude);
					editlongitude.setText(String.valueOf(poi.longitude));
					final EditText editaltitude = (EditText) layout.findViewById(R.id.edit_poi_altitude);
					double altitude = poi.altitude;
					if (MainActivity.altitude_unit == MainActivity.unit_ft) {
						altitude /= 0.3048;
					}
					editaltitude.setText(String.valueOf(altitude));
					final DatePicker editdate = (DatePicker) layout.findViewById(R.id.edit_poi_date);
					final TimePicker edittime = (TimePicker) layout.findViewById(R.id.edit_poi_time);
					Time time = poi.time;
					time.switchTimezone(timezone);
					editdate.updateDate(time.year, time.month, time.monthDay);
					edittime.setIs24HourView(true);
					edittime.setCurrentHour(time.hour);
					edittime.setCurrentMinute(time.minute);
					ab2.setView(layout);
					ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String editAltitudeStr = editaltitude.getText().toString();
							String editLatitudeStr = editlatitude.getText().toString();
							String editLongitudeStr = editlongitude.getText().toString();
							if (editAltitudeStr.isEmpty() || editLatitudeStr.isEmpty() || editLongitudeStr.isEmpty()) {
								Toast.makeText(ViewPointActivity.this, R.string.some_fields_are_not_filled, Toast.LENGTH_SHORT).show();
								return;
							}
							Time time = new Time(timezone);
							time.set(0, edittime.getCurrentMinute(), edittime.getCurrentHour(), editdate.getDayOfMonth(), editdate.getMonth(), editdate.getYear());
							time.switchTimezone(Time.TIMEZONE_UTC);
							double altitude = Double.parseDouble(editAltitudeStr);
							if (MainActivity.altitude_unit == MainActivity.unit_ft) {
								altitude *= 0.3048;
							}
							poi.renamePOI(edittitle.getText().toString());
							poi.updateBasicInformation(edittitle.getText().toString(), time, Double.parseDouble(editLatitudeStr), Double.parseDouble(editLongitudeStr), altitude);
							setTitle(edittitle.getText().toString());
							Intent data = new Intent();
							data.putExtra("update", true);
							setResult(getIntent().getIntExtra("request_code", 1), data);
						}
					});
					ab2.setNegativeButton(getString(R.string.cancel), null);
					ab2.show();
				}
			});
			ab.show();
		} else if (item.getItemId() == R.id.viewcost) {
			Intent intent2 = new Intent(ViewPointActivity.this, ViewCostActivity.class);
			intent2.putExtra("path", path);
			intent2.putExtra("title", name);
			intent2.putExtra("option", ViewCostFragment.optionPOI);
			startActivity(intent2);
		} else if (item.getItemId() == R.id.delete) {
			AlertDialog.Builder ab2 = new AlertDialog.Builder(ViewPointActivity.this);
			ab2.setMessage(getString(R.string.are_you_sure_to_delete_this_poi));
			ab2.setTitle(getString(R.string.be_careful));
			ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					poi.deleteSelf();
					Intent data = new Intent();
					data.putExtra("update", true);
					setResult(getIntent().getIntExtra("request_code", 2), data);
					ViewPointActivity.this.finish();
				}

			});
			ab2.setNegativeButton(getString(R.string.cancel), null);
			AlertDialog ad = ab2.create();
			ad.setIcon(R.drawable.ic_alert);
			ad.show();
		}
		return false;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		boolean updateRequest = false;
		if (data != null) {
			Uri uri = data.getData();
			Cursor cursor;
			String filepath, filename;
			if (requestCode == import_picture) {
				cursor = getContentResolver().query(uri, new String[] { android.provider.MediaStore.Images.Media.DATA }, null, null, null);
				if (cursor != null) {
					cursor.moveToFirst();
					filepath = cursor.getString(0);
					if (filepath != null) {
						filename = filepath.substring(filepath.lastIndexOf("/") + 1);
						FileHelper.copyFile(new File(filepath), new File(path + "/pictures/" + filename));
						updateRequest = true;
					}
				} else {
					Toast.makeText(ViewPointActivity.this, getString(R.string.not_a_picture), Toast.LENGTH_SHORT).show();
				}
			} else if (requestCode == import_video) {
				cursor = getContentResolver().query(uri, new String[] { android.provider.MediaStore.Video.Media.DATA }, null, null, null);
				if (cursor != null) {
					cursor.moveToFirst();
					filepath = cursor.getString(0);
					if (filepath != null) {
						filename = filepath.substring(filepath.lastIndexOf("/") + 1);
						FileHelper.copyFile(new File(filepath), new File(path + "/videos/" + filename));
						updateRequest = true;
					}
				} else {
					Toast.makeText(ViewPointActivity.this, getString(R.string.not_a_video), Toast.LENGTH_SHORT).show();
				}
			} else if (requestCode == import_audio) {
				cursor = getContentResolver().query(uri, new String[] { android.provider.MediaStore.Audio.Media.DATA }, null, null, null);
				if (cursor != null) {
					cursor.moveToFirst();
					filepath = cursor.getString(0);
					if (filepath != null) {
						filename = filepath.substring(filepath.lastIndexOf("/") + 1);
						FileHelper.copyFile(new File(filepath), new File(path + "/audios/" + filename));
						updateRequest = true;
					}
				} else {
					Toast.makeText(ViewPointActivity.this, getString(R.string.not_a_audio), Toast.LENGTH_SHORT).show();
				}
			} else if (requestCode == pick_multi_file) {
				if (resultCode == Activity.RESULT_OK) {
					new ImportFilesTask(data.getExtras().getStringArrayList("files")).execute();
				}
			}
		}
		if (updateRequest) {
			Intent data1 = new Intent();
			data1.putExtra("update", true);
			setResult(getIntent().getIntExtra("request_code", 1), data1);
		}
	}

	class ImportFilesTask extends AsyncTask<String, Integer, String> {

		ProgressDialog pd;
		ArrayList<String> files;

		public ImportFilesTask(ArrayList<String> files) {
			this.files = files;
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd = new ProgressDialog(ViewPointActivity.this);
			pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			if (files != null)
				pd.setMax(files.size());
			pd.setTitle(R.string.importing);
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			if (files == null)
				return null;
			for (int i = 0; i < files.size(); i++) {
				publishProgress(i);
				File file = new File(files.get(i));
				if (file.isDirectory()) {
					continue;
				} else if (FileHelper.isPicture(file)) {
					FileHelper.copyFile(file, new File(poi.picDir, file.getName()));
				} else if (FileHelper.isVideo(file)) {
					FileHelper.copyFile(file, new File(poi.videoDir, file.getName()));
				} else if (FileHelper.isAudio(file)) {
					FileHelper.copyFile(file, new File(poi.audioDir, file.getName()));
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			pd.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pd.dismiss();
			adapter.pictureFragment.onResume();
			adapter.videoFragment.onResume();
			adapter.audioFragment.onResume();
		}
	}

}
