package com.yupog2003.tripdiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.format.Time;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.GpxAnalyzer2;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.services.RecordService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class RecordActivity extends MyActivity implements OnClickListener, OnInfoWindowClickListener, OnMarkerDragListener {

	Toolbar toolBar;
	String rootPath;
	String tripName;
	String tripPath;
	public static final String tag_rootpath = "rootpath";
	public static final String tag_tripname = "tripname";
	public static final String tag_addpoi_intent = "add_poi";
	public static final String tag_isgpsenabled = "isgpsenabled";
	public static final String pref_tag_onaddpoi = "on_add_poi";
	TextView distance;
	TextView totalTime;
	TextView velocity;
	TextView accuracy;
	TextView altitude;
	EditText poiName;
	Button addPOI;
	Button cancel;
	Button takePicture;
	Button takeVideo;
	Button takeAudio;
	Button takeText;
	Button takePaint;
	Button takeMoney;
	Button save;
	ImageButton refresh;
	ProgressBar refreshProgress;
	Handler handler;
	RelativeLayout maplayout;
	LinearLayout addMemoryLayout;
	LinearLayout informationLayout;
	MapFragment mapFragment;
	GoogleMap gmap;
	Polyline polyline;
	boolean addPOIMode;
	boolean isGPSEnabled;
	Menu menu;
	SharedPreferences preference;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_record);
		toolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		rootPath = getIntent().getStringExtra(tag_rootpath);
		tripName = getIntent().getStringExtra(tag_tripname);
		isGPSEnabled = getIntent().getBooleanExtra(tag_isgpsenabled, false);
		if (rootPath == null || tripName == null) {
			finish();
		}
		tripPath = rootPath + "/" + tripName;
		if (isGPSEnabled) {
			setTitle(tripName + " - " + getString(R.string.recording));
			mapFragment = MapFragment.newInstance();
			FragmentTransaction ft = getFragmentManager().beginTransaction();
			ft.replace(R.id.maplayout, mapFragment, "mapFragment");
			ft.commit();
		} else {
			setTitle(tripName);
		}
		preference = PreferenceManager.getDefaultSharedPreferences(this);
		handler = new Handler();
		maplayout = (RelativeLayout) findViewById(R.id.maplayout);
		addMemoryLayout = (LinearLayout) findViewById(R.id.addMemoryLayout);
		informationLayout = (LinearLayout) findViewById(R.id.informationLayout);
		distance = (TextView) findViewById(R.id.distance);
		totalTime = (TextView) findViewById(R.id.totaltime);
		velocity = (TextView) findViewById(R.id.velocity);
		accuracy = (TextView) findViewById(R.id.accuracy);
		altitude = (TextView) findViewById(R.id.altitude);
		poiName = (EditText) findViewById(R.id.poiName);
		addPOI = (Button) findViewById(R.id.addpoi);
		refresh = (ImageButton) findViewById(R.id.refresh);
		cancel = (Button) findViewById(R.id.cancel);
		cancel.setOnClickListener(this);
		addPOI.setOnClickListener(this);
		refresh.setOnClickListener(this);
		takePicture = (Button) findViewById(R.id.takepicture);
		takePicture.setOnClickListener(this);
		takeVideo = (Button) findViewById(R.id.takevideo);
		takeVideo.setOnClickListener(this);
		takeAudio = (Button) findViewById(R.id.takeaudio);
		takeAudio.setOnClickListener(this);
		takeText = (Button) findViewById(R.id.taketext);
		takeText.setOnClickListener(this);
		takePaint = (Button) findViewById(R.id.takepaint);
		takePaint.setOnClickListener(this);
		takeMoney = (Button) findViewById(R.id.takemoney);
		takeMoney.setOnClickListener(this);
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);
		refreshProgress = (ProgressBar) findViewById(R.id.refreshProgress);
		if (isGPSEnabled) {
			new Thread(new UpdateRunnable()).start();
		} else {
			refresh.setVisibility(View.GONE);
			informationLayout.setVisibility(View.GONE);
		}
		setAddPOIMode(false);
		if (getIntent().getBooleanExtra(tag_addpoi_intent, false)) {
			poiName.postDelayed(new Runnable() {

				@Override
				public void run() {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.showSoftInput(poiName, 0);
				}
			}, 200);
		}
	}

	@Override
	protected void onResume() {

		if (isGPSEnabled && RecordService.instance == null) {
			startActivity(new Intent(this, MainActivity.class));
			finish();
		}
		if (isGPSEnabled) {
			mapFragment.getMapAsync(new OnMapReadyCallback() {

				@Override
				public void onMapReady(GoogleMap googleMap) {

					gmap = googleMap;
					gmap.setMyLocationEnabled(true);
					gmap.setOnMarkerDragListener(RecordActivity.this);
					gmap.setOnInfoWindowClickListener(addPOIMode ? null : RecordActivity.this);
					gmap.getUiSettings().setZoomControlsEnabled(true);
					new RefreshTask(true, true).execute();
				}
			});
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_record, menu);
		this.menu = menu;
		if (!isGPSEnabled) {
			menu.findItem(R.id.pause).setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.pause) {
			if (RecordService.instance != null) {
				if (RecordService.instance.run) {
					item.setIcon(R.drawable.ic_resume);
					setTitle(tripName + " - " + getString(R.string.pausing));
				} else {
					item.setIcon(R.drawable.ic_pause);
				}
			}
			Intent i = new Intent(RecordService.actionPauseTrip);
			sendBroadcast(i);
		} else if (item.getItemId() == R.id.stop) {
			if (isGPSEnabled) {
				Intent i = new Intent(RecordService.actionStopTrip);
				sendBroadcast(i);
				finish();
			} else {
				Intent i = new Intent(RecordActivity.this, ViewTripActivity.class);
				i.putExtra("name", tripName);
				i.putExtra("path", rootPath);
				i.putExtra("stoptrip", true);
				startActivity(i);
				finish();
			}
		}
		return true;
	}

	private void setAddPOIMode(boolean addPOIMode) {
		this.addPOIMode = addPOIMode;
		if (addPOIMode) {
			addMemoryLayout.setVisibility(View.VISIBLE);
			save.setVisibility(View.VISIBLE);
			cancel.setVisibility(View.VISIBLE);
			addPOI.setVisibility(View.GONE);
			poiName.setEnabled(false);
			if (gmap != null) {
				gmap.setOnInfoWindowClickListener(null);
			}
		} else {
			addMemoryLayout.setVisibility(View.GONE);
			save.setVisibility(View.GONE);
			cancel.setVisibility(View.GONE);
			addPOI.setVisibility(View.VISIBLE);
			poiName.setEnabled(true);
			poiName.setText("");
			if (gmap != null) {
				gmap.setOnInfoWindowClickListener(this);
			}
			if (POIMarker != null) {
				POIMarker.remove();
			}
		}
	}

	String fileName;
	POI poi;
	Marker POIMarker;
	ArrayList<Marker> markers = new ArrayList<Marker>();
	private static final int REQUEST_PICTURE = 0;
	private static final int REQUEST_VIDEO = 1;

	@Override
	public void onClick(View v) {

		Time fileTime = new Time(Time.getCurrentTimezone());
		fileTime.setToNow();
		fileName = fileTime.format3339(false);
		fileName = fileName.substring(0, fileName.lastIndexOf("."));
		fileName = fileName.replace("-", "");
		fileName = fileName.replace("T", "");
		fileName = fileName.replace(":", "");
		if (v.equals(addPOI)) {
			if (poiName.getText().toString().equals("")) {
				Toast.makeText(RecordActivity.this, R.string.please_input_poi_name, Toast.LENGTH_SHORT).show();
			} else {
				LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
				Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (isGPSEnabled && location == null) {
					Toast.makeText(RecordActivity.this, getString(R.string.location_not_found), Toast.LENGTH_SHORT).show();
				} else {
					setAddPOIMode(true);
					String poiNameStr = poiName.getText().toString();
					String newPointPath = rootPath + "/" + tripName + "/" + poiNameStr;
					poi = new POI(new File(newPointPath));
					Time time = new Time();
					time.switchTimezone(Time.TIMEZONE_UTC);
					time.setToNow();
					if (isGPSEnabled) {
						if (poi.latitude == 0 && poi.longitude == 0) { // new_poi
							POIMarker = gmap.addMarker(new MarkerOptions().position(new LatLng(location.getLatitude(), location.getLongitude())).title(poi.title).draggable(true).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
							gmap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
							poi.updateBasicInformation(null, time, location.getLatitude(), location.getLongitude(), location.getAltitude());
						} else { // edit exist poi
							location.setAltitude(poi.altitude);
							cancel.setVisibility(View.GONE);
							if (markers != null) {
								for (int i = 0; i < markers.size(); i++) {
									if (markers.get(i).getTitle().equals(poi.title)) {
										POIMarker = markers.get(i);
										POIMarker.setDraggable(true);
										POIMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
										markers.remove(i);
										break;
									}
								}
							}
						}
					} else {
						poi.updateBasicInformation(null, time, null, null, null);
					}
					preference.edit().putString(pref_tag_onaddpoi, poi.title).commit();
					updatePOIStatus();
				}
			}
		} else if (v.equals(refresh)) {
			new RefreshTask(true, false).execute();
		} else if (v.equals(takePicture)) {
			Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			String filePath = poi.picDir.getPath() + "/" + fileName + ".jpg";
			File file = new File(filePath);
			Uri uri = Uri.fromFile(file);
			i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			if (i.resolveActivity(getPackageManager()) != null) {
				startActivityForResult(i, REQUEST_PICTURE);
			} else {
				Toast.makeText(this, getString(R.string.camera_is_not_available), Toast.LENGTH_SHORT).show();
			}
		} else if (v.equals(takeVideo)) {
			Intent i = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
			String filePath = poi.videoDir.getPath() + "/" + fileName + ".3gp";
			File file = new File(filePath);
			Uri uri = Uri.fromFile(file);
			i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
			i.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
			if (i.resolveActivity(getPackageManager()) != null) {
				startActivityForResult(i, REQUEST_VIDEO);
			} else {
				Toast.makeText(this, getString(R.string.camera_is_not_available), Toast.LENGTH_SHORT).show();
			}
		} else if (v.equals(takeAudio)) {
			new RecordAudioTask(poi.audioDir.getPath() + "/" + fileName + ".mp3").execute();
		} else if (v.equals(takeText)) {
			final EditText getText = new EditText(this);
			getText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			getText.setText(poi.diary);
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setTitle(getString(R.string.input_diary));
			ab.setView(getText);
			ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface arg0, int arg1) {

					if (poi != null)
						poi.updateDiary(getText.getText().toString());
					updatePOIStatus();
				}
			});
			ab.setNegativeButton(getString(R.string.cancel), null);
			ab.show();
		} else if (v.equals(takePaint)) {
			Intent intent = new Intent(this, PaintActivity.class);
			intent.putExtra("path", poi.picDir.getPath() + "/" + fileName + ".png");
			startActivityForResult(intent, REQUEST_PICTURE);
		} else if (v.equals(takeMoney)) {
			AlertDialog.Builder ab = new AlertDialog.Builder(this);
			ab.setTitle(getString(R.string.cost));
			final LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.take_money, null);
			ab.setView(layout);
			ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					EditText costName = (EditText) layout.findViewById(R.id.costname);
					RadioGroup costType = (RadioGroup) layout.findViewById(R.id.costtype);
					EditText costDollar = (EditText) layout.findViewById(R.id.costdollar);
					String name = costName.getText().toString();
					String dollar = costDollar.getText().toString();
					if (!name.equals("") && !dollar.equals("")) {
						int type = -1;
						if (costType.getCheckedRadioButtonId() == R.id.food) {
							type = 0;
						} else if (costType.getCheckedRadioButtonId() == R.id.lodging) {
							type = 1;
						} else if (costType.getCheckedRadioButtonId() == R.id.transportation) {
							type = 2;
						} else if (costType.getCheckedRadioButtonId() == R.id.other) {
							type = 3;
						} else {
							type = 0;
						}
						try {
							poi.addCost(type, name, Float.parseFloat(dollar));
							updatePOIStatus();
						} catch (NumberFormatException e) {
							Toast.makeText(RecordActivity.this, "Number format is not correct", Toast.LENGTH_SHORT).show();
							e.printStackTrace();
						}
					}
				}
			});
			ab.setNegativeButton(getString(R.string.cancel), null);
			ab.show();
		} else if (v.equals(cancel)) {
			if (poi != null) {
				poi.deleteSelf();
				poi = null;
			}
			preference.edit().remove(pref_tag_onaddpoi).commit();
			setAddPOIMode(false);
		} else if (v.equals(save)) {
			Toast.makeText(RecordActivity.this, R.string.poi_has_been_saved, Toast.LENGTH_SHORT).show();
			setAddPOIMode(false);
			preference.edit().remove(pref_tag_onaddpoi).commit();
			if (isGPSEnabled) {
				new RefreshTask(false, false).execute();
			}
		}
	}

	@Override
	public void onBackPressed() {

		if (addPOIMode) {
			if (cancel.getVisibility() == View.VISIBLE) {
				cancel.performClick();
			} else {
				save.performClick();
			}
		} else {
			super.onBackPressed();
		}
	}

	private void updatePOIStatus() {
		if (addPOIMode && poi != null) {
			try {
				takePicture.setText(getString(R.string.take_a_picture) + "(" + poi.picDir.listFiles(FileHelper.getPictureFileFilter()).length + ")");
				takeVideo.setText(getString(R.string.take_a_video) + "(" + poi.videoDir.listFiles(FileHelper.getVideoFileFilter()).length + ")");
				takeAudio.setText(getString(R.string.take_a_audio) + "(" + poi.audioDir.listFiles(FileHelper.getAudioFileFilter()).length + ")");
				takeText.setText(getString(R.string.write_text) + "(" + poi.diary.length() + ")");
				takeMoney.setText(getString(R.string.spend) + "(" + poi.costDir.list().length + ")");
			} catch (NullPointerException e) {
				e.printStackTrace();
				// cancel.performClick();
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == REQUEST_PICTURE || requestCode == REQUEST_VIDEO) {
				updatePOIStatus();
			}
		}
	}

	class UpdateRunnable implements Runnable {

		@Override
		public void run() {

			while (RecordService.instance != null) {
				final long totaltime = RecordService.instance.totalTime;
				final double totaldistance = RecordService.instance.totaldistance;
				final double nowSpeed = RecordService.instance.nowSpeed;
				final float faccuracy = RecordService.instance.accuracy;
				final double elevation = RecordService.instance.elevation;
				final String timeExpression = String.valueOf(totaltime / 3600) + ":" + String.valueOf(totaltime % 3600 / 60) + ":" + String.valueOf(totaltime % 3600 % 60);

				handler.post(new Runnable() {

					@Override
					public void run() {

						if (RecordService.instance != null && RecordService.instance.run) {
							if (menu != null) {
								menu.findItem(R.id.pause).setIcon(R.drawable.ic_pause);
							}
							setTitle(tripName + " - " + (faccuracy == -1 ? getString(R.string.searching_for_satellite) : getString(R.string.recording)));
							distance.setText(GpxAnalyzer2.getDistanceString((float) totaldistance / 1000, "km"));
							totalTime.setText(timeExpression);
							velocity.setText(GpxAnalyzer2.getDistanceString((float) nowSpeed * 18 / 5, "km/hr"));
							accuracy.setText((faccuracy == -1 ? "∞" : (GpxAnalyzer2.getAltitudeString(faccuracy, "m"))));
							altitude.setText(GpxAnalyzer2.getAltitudeString((float) elevation, "m"));
						} else {
							if (menu != null) {
								menu.findItem(R.id.pause).setIcon(R.drawable.ic_resume);
							}
							setTitle(tripName + " - " + getString(R.string.pausing));
						}
					}
				});
				try {
					Thread.sleep(RecordService.instance.updateDuration);
				} catch (InterruptedException e) {

					e.printStackTrace();
					break;
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			RecordActivity.this.finish();
		}

	}

	class RefreshTask extends AsyncTask<Boolean, String, String> {
		LatLng[] lat;
		LatLngBounds.Builder latlngBoundesBuilder;
		POI[] pois;
		boolean refreshTrack;
		boolean animateCamera;

		public RefreshTask(boolean refreshTrack, boolean animateCamera) {
			this.refreshTrack = refreshTrack;
			this.animateCamera = animateCamera;
		}

		@Override
		protected void onPreExecute() {

			refreshProgress.setVisibility(View.VISIBLE);
		}

		@Override
		protected String doInBackground(Boolean... params) {

			if (refreshTrack) {
				try {
					ArrayList<LatLng> latArray = new ArrayList<LatLng>();
					latlngBoundesBuilder = new LatLngBounds.Builder();
					BufferedReader br = new BufferedReader(new FileReader(new File(rootPath + "/" + tripName + "/" + tripName + ".gpx")));
					String s;
					while ((s = br.readLine()) != null) {
						if (s.contains("<trkpt")) {
							LatLng latlng;
							if (s.indexOf("lat") > s.indexOf("lon")) {
								latlng = new LatLng(Double.parseDouble(s.split("\"")[3]), Double.parseDouble(s.split("\"")[1]));
								latArray.add(latlng);
								latlngBoundesBuilder.include(latlng);
							} else {
								latlng = new LatLng(Double.parseDouble(s.split("\"")[1]), Double.parseDouble(s.split("\"")[3]));
								latArray.add(latlng);
								latlngBoundesBuilder.include(latlng);
							}
						}
					}
					br.close();
					lat = latArray.toArray(new LatLng[latArray.size()]);
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
			File[] poiFiles = new File(rootPath + "/" + tripName).listFiles(FileHelper.getDirFilter());
			if (poiFiles == null)
				return null;
			pois = new POI[poiFiles.length];
			for (int i = 0; i < pois.length; i++) {
				pois[i] = new POI(poiFiles[i]);
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(String... values) {


		}

		@Override
		protected void onPostExecute(String result) {

			if (refreshTrack && lat != null && lat.length > 0 && gmap != null) {
				int trackColor = PreferenceManager.getDefaultSharedPreferences(RecordActivity.this).getInt("trackcolor", 0xff6699cc);
				if (polyline != null) {
					polyline.remove();
				}
				polyline = gmap.addPolyline(new PolylineOptions().add(lat).width(5).color(trackColor));
			}
			if (animateCamera) {
				LatLng latlng = null;
				Location location = null;
				LocationManager lm = (LocationManager) getSystemService(LOCATION_SERVICE);
				location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if (location == null) {
					location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
				}
				if (location != null) {
					latlng = new LatLng(location.getLatitude(), location.getLongitude());
				} else if (lat != null && lat.length > 0) {
					latlng = lat[lat.length - 1];
				}
				if (latlng != null) {
					gmap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 16));
				}
			}
			if (markers != null) {
				for (int i = 0; i < markers.size(); i++) {
					markers.get(i).remove();
				}
                markers.clear();
			}else{
                markers=new ArrayList<Marker>();
            }
            if (pois != null) {
				for (int i = 0; i < pois.length; i++) {
					POI poi = pois[i];
					if (addPOIMode && POIMarker != null && poi.title.equals(POIMarker.getTitle()))
						continue;
					markers.add(gmap.addMarker(new MarkerOptions().position(new LatLng(poi.latitude, poi.longitude)).title(poi.title).draggable(false)));
				}
			}
			if (!addPOIMode && preference.getString(pref_tag_onaddpoi, null) != null) {
				poiName.setText(preference.getString(pref_tag_onaddpoi, ""));
				addPOI.performClick();
			}
			refreshProgress.setVisibility(View.GONE);
		}

	}

	class RecordAudioTask extends AsyncTask<Integer, Integer, Integer> {
		MediaRecorder mr;
		ProgressBar pb;
		TextView time;
		boolean run;
		long startTime;
		String filePath;
		static final int base = 40;
		boolean canPlay;

		public RecordAudioTask(String filePath) {
			this.filePath = filePath;
		}

		@Override
		protected void onPreExecute() {

			canPlay = false;
			run = false;
			mr = new MediaRecorder();
			mr.setAudioSource(MediaRecorder.AudioSource.MIC);
			mr.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB);
			mr.setOutputFile(filePath);
			mr.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			try {
				mr.prepare();
				LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.record_audio, null);
				pb = (ProgressBar) layout.findViewById(R.id.volum);
				pb.setMax((int) (20 * Math.log10(32767)) - base);
				pb.setProgress(0);
				time = (TextView) layout.findViewById(R.id.time);
				time.setText("00:00:00");
				AlertDialog.Builder ab = new AlertDialog.Builder(RecordActivity.this);
				ab.setTitle(getString(R.string.record_audio));
				ab.setView(layout);
				ab.setPositiveButton(getString(R.string.finish), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						run = false;
						dialog.dismiss();
					}
				});
				ab.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {

						run = false;
						new File(filePath).delete();
						dialog.dismiss();
					}
				});
				ab.setCancelable(false);
				ab.show();
				startTime = System.currentTimeMillis();
				mr.start();
				canPlay = true;
				run = true;
			} catch (IllegalStateException e) {

				e.printStackTrace();
				Toast.makeText(RecordActivity.this, getString(R.string.cannot_start_media_recorder_due_to_unknowm_error), Toast.LENGTH_SHORT).show();
			} catch (IOException e) {

				e.printStackTrace();
				Toast.makeText(RecordActivity.this, getString(R.string.cannot_start_media_recorder_due_to_unknowm_error), Toast.LENGTH_SHORT).show();
			}

		}

		@Override
		protected Integer doInBackground(Integer... params) {

			while (run) {
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {

					e.printStackTrace();
				}
				publishProgress(mr.getMaxAmplitude());
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {

			time.setText(convertSecondToTime(System.currentTimeMillis() - startTime));
			pb.setProgress((int) (20 * Math.log10(values[0])) - base);
		}

		private String convertSecondToTime(long mSecond) {
			int second = (int) (mSecond / 1000);
			StringBuffer sb = new StringBuffer();
			sb.append(String.valueOf(second / 3600) + ":");
			second = second % 3600;
			sb.append(String.valueOf(second / 60) + ":");
			second = second % 60;
			sb.append(String.valueOf(second));
			return sb.toString();
		}

		@Override
		protected void onPostExecute(Integer result) {

			if (canPlay) {
				mr.stop();
				mr.release();
				updatePOIStatus();
			}
		}
	}

	@Override
	public void onInfoWindowClick(Marker marker) {

		poiName.setText(marker.getTitle());
		addPOI.performClick();
	}

	@Override
	public void onMarkerDrag(Marker marker) {


	}

	@Override
	public void onMarkerDragEnd(Marker marker) {

		if (poi != null && marker != null && marker.getTitle() != null && marker.getTitle().equals(poi.title)) {
			poi.updateBasicInformation(null, null, marker.getPosition().latitude, marker.getPosition().longitude, null);
		}
	}

	@Override
	public void onMarkerDragStart(Marker marker) {


	}
}
