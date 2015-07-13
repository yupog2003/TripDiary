package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.os.Handler;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.TimeZone;

public class GpxAnalyzerJava {
	TrackCache cache;
	String gpxPath;
	Context context;
	Handler contextHandler;
	ProgressChangedListener listener;
	public static final double earthRadius = 6378.1 * 1000;
	public static final int altitudeDifferThreshold = 20;

	public GpxAnalyzerJava(String gpxPath, Context context, Handler contextHandler) {
		this.gpxPath = gpxPath;
		this.context = context;
		this.contextHandler = contextHandler;
		stop = false;
	}

	public boolean analyze() {
		String tripPath = gpxPath.substring(0, gpxPath.lastIndexOf("/"));
		String tripName = gpxPath.substring(gpxPath.lastIndexOf("/") + 1, gpxPath.lastIndexOf("."));
		int timeZoneOffset = 0;
		cache = new TrackCache();
		boolean cacheExsit = new File(gpxPath + ".cache").exists();
		if (cacheExsit) {
			int numOfLines = FileHelper.getNumOfLinesInFile(gpxPath + ".cache");
			int size = (numOfLines - 9) / 4;
			cache.latitudes = new double[size];
			cache.longitudes = new double[size];
			cache.altitudes = new float[size];
			cache.times = new String[size];
		} else {
			String timezone = MyCalendar.getTripTimeZone(context, tripName);
			timeZoneOffset = TimeZone.getTimeZone(timezone).getRawOffset()/1000;
		}
		//ArrayList<Float> speeds = cacheExsit ? null : new ArrayList<Float>();
		boolean success = cacheExsit ? getCache(gpxPath + ".cache", cache) : parse(gpxPath, cache, timeZoneOffset);
		if (!cacheExsit && success) {
			//saveGraph(context, gpxPath + ".graph", cache.altitudes, speeds, contextHandler);
		}
		return success;
	}

	public TrackCache getCache() {
		return this.cache;
	}

	public boolean getCache(String cachePath, TrackCache cache) {
		try {
			File file = new File(cachePath);
			FileInputStream fis = new FileInputStream(file);
			InputStreamReader is = new InputStreamReader(fis);
			BufferedReader br = new BufferedReader(is);
			String s;
			cache.startTime = br.readLine();
			cache.endTime = br.readLine();
			cache.totalTime = br.readLine();
			cache.distance = Float.parseFloat(br.readLine());
			cache.avgSpeed = Float.parseFloat(br.readLine());
			cache.maxSpeed = Float.parseFloat(br.readLine());
			cache.climb = Float.parseFloat(br.readLine());
			cache.maxAltitude = Float.parseFloat(br.readLine());
			cache.maxAltitude = Float.parseFloat(br.readLine());
			int index = 0, count = 0;
			while ((s = br.readLine()) != null) {
				if (stop) {
					br.close();
					return false;
				}
				count++;
				if (count == 1250) {
					onProgressChanged(file.length() * index / cache.latitudes.length);
					count = 0;
				}
				cache.latitudes[index] = Double.parseDouble(s);
				s = br.readLine();
				cache.longitudes[index] = Double.parseDouble(s);
				s = br.readLine();
				cache.altitudes[index] = Float.parseFloat(s);
				s = br.readLine();
				cache.times[index] = s;
				index++;
			}
			br.close();
			return true;
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean parse(String gpxPath, TrackCache cache, int timeZoneOffset) {
		try {
			String cachePath = gpxPath + ".cache";
			BufferedReader br = new BufferedReader(new FileReader(new File(gpxPath)));
			String s;
			int count = 0;
			long byteCount = 0;
			ArrayList<MyLatLng2> track = new ArrayList<MyLatLng2>();
			MyLatLng2 latlng = null;
			MyLatLng2 preLatLng = null;
			float maxAltitude = Float.MIN_VALUE;
			float minAltitude = Float.MAX_VALUE;
			ArrayList<Long> times = new ArrayList<Long>();
			boolean first = true;
			float preAltitude = 0;
			float totalAltitude = 0;
			float distance = 0;
			while ((s = br.readLine()) != null) {
				if (stop) {
					br.close();
					return false;
				}
				count++;
				byteCount += s.getBytes().length;
				if (count == 5000) {
					onProgressChanged(byteCount);
					count = 0;
				}
				if (s.contains("<trkpt")) {
					latlng = new MyLatLng2();
					if (s.indexOf("lat") > s.indexOf("lon")) {
						latlng.longitude = Double.parseDouble(s.split("\"")[1]);
						latlng.latitude = Double.parseDouble(s.split("\"")[3]);
					} else {
						latlng.latitude = Double.parseDouble(s.split("\"")[1]);
						latlng.longitude = Double.parseDouble(s.split("\"")[3]);
					}
				} else if (s.contains("<ele>")) {
					float altitude = Float.parseFloat(s.substring(s.indexOf(">") + 1, s.lastIndexOf("<")));
					latlng.altitude = altitude;
					if (altitude > maxAltitude)
						maxAltitude = altitude;
					if (altitude < minAltitude)
						minAltitude = altitude;
				} else if (s.contains("<time>")) {
					MyCalendar time=MyCalendar.getInstance(TimeZone.getTimeZone("UTC"));
					s=s.substring(s.indexOf(">")+1, s.lastIndexOf("<"));
					String year = s.substring(0, s.indexOf("-"));
					String month = s.substring(s.indexOf("-") + 1, s.lastIndexOf("-"));
					String day = s.substring(s.lastIndexOf("-") + 1, s.indexOf("T"));
					String hour = s.substring(s.indexOf("T") + 1, s.indexOf(":"));
					String minute = s.substring(s.indexOf(":") + 1, s.lastIndexOf(":"));
					String second = s.substring(s.lastIndexOf(":") + 1, s.indexOf("Z"));
					time.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hour), Integer.parseInt(minute), (int)Double.parseDouble(second));
					time.setTimeInMillis(time.getTimeInMillis() + timeZoneOffset * 1000);
					latlng.time = time.formatInTimezone("UTC");
					times.add(time.getTimeInMillis()/1000);
				} else if (s.contains("</trkpt>")) {
					if (!first) {
						float altitudeDiffer = latlng.altitude - preAltitude;
						if (Math.abs(altitudeDiffer) > altitudeDifferThreshold) {
							if (altitudeDiffer > 0)
								totalAltitude += altitudeDiffer;
							preAltitude = latlng.altitude;
						}
						distance += distFrom(preLatLng.latitude, preLatLng.longitude, latlng.latitude, latlng.longitude);
					} else {
						first = false;
						preAltitude = latlng.altitude;
					}
					preLatLng = latlng;
					track.add(latlng);
				}
			}
			br.close();
			float maxSpeed = 0;
			int trackSize = track.size();
			int timesSize = times.size();
			for (int i = 0; i + 20 < trackSize && i + 20 < timesSize; i += 20) {
				if (stop)
					return false;
				float dist = distFrom(track.get(i).latitude, track.get(i).longitude, track.get(i + 20).latitude, track.get(i + 20).longitude);
				float seconds = times.get(i + 20) - times.get(i);
				float speed = dist / seconds * 18 / 5;
				/*if (speeds != null) {
					speeds.add(speed);
				}*/
				if (maxSpeed < speed)
					maxSpeed = speed;
			}
			if (stop)
				return false;
			long totalSeconds = times.get(times.size() - 1) - times.get(0);
			String totalTime = "";
			long day = totalSeconds / 86400;
			long hour = totalSeconds % 86400 / 3600;
			long min = totalSeconds % 3600 / 60;
			long sec = totalSeconds % 60;
			if (day != 0) {
				totalTime = String.valueOf(day) + "T";
			}
			totalTime += String.valueOf(hour) + ":" + String.valueOf(min) + ":" + String.valueOf(sec);
			float avgSpeed = distance / totalSeconds * 18 / 5;
			cache.startTime = track.get(0).time;
			cache.endTime = track.get(track.size() - 1).time;
			cache.totalTime = totalTime;
			cache.distance = distance;
			cache.avgSpeed = avgSpeed;
			cache.maxSpeed = maxSpeed;
			cache.climb = totalAltitude;
			cache.maxAltitude = maxAltitude;
			cache.maxAltitude = minAltitude;
			cache.latitudes = new double[track.size()];
			cache.longitudes = new double[track.size()];
			cache.altitudes = new float[track.size()];
			cache.times=new String[track.size()];
			for (int i = 0; i < track.size(); i++) {
				cache.latitudes[i] = track.get(i).latitude;
				cache.longitudes[i] = track.get(i).longitude;
				cache.altitudes[i] = track.get(i).altitude;
				cache.times[i] = track.get(i).time;
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(cachePath)));
			bw.write(cache.startTime + "\n");
			bw.write(cache.endTime + "\n");
			bw.write(cache.totalTime + "\n");
			bw.write(String.valueOf(cache.distance) + "\n");
			bw.write(String.valueOf(cache.avgSpeed) + "\n");
			bw.write(String.valueOf(cache.maxSpeed) + "\n");
			bw.write(String.valueOf(cache.climb) + "\n");
			bw.write(String.valueOf(cache.maxAltitude) + "\n");
			bw.write(String.valueOf(cache.minAltitude) + "\n");
			for (int i = 0; i < trackSize; i++) {
				bw.write(String.valueOf(cache.latitudes[i]) + "\n");
				bw.write(String.valueOf(cache.longitudes[i]) + "\n");
				bw.write(String.valueOf(cache.altitudes[i]) + "\n");
				bw.write(cache.times[i] + "\n");
			}
			bw.flush();
			bw.close();
			return true;
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public static float distFrom(double lat1, double lng1, double lat2, double lng2) { //in meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.pow(Math.sin(dLat / 2), 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.pow(Math.sin(dLng / 2), 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double dist = earthRadius * c;
		return (float) dist;
	}

	/*private void saveGraph(final Context context, final String path, final float[] altitudes, final ArrayList<Float> speeds, Handler handler) {
		if (context == null)
			return;
		final XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		XYSeries altitudeSeries = new XYSeries(context.getString(R.string.Altitude), 0);
		final int altitudesSize = altitudes.length;
		for (int i = 0; i < altitudesSize; i++) {
			float altitude = altitudes[i];
			if (MainActivity.altitude_unit == MainActivity.unit_ft)
				altitude /= 0.3048;
			altitudeSeries.add(i, altitude);
		}
		dataset.addSeries(0, altitudeSeries);
		XYSeries speedSeries = new XYSeries(context.getString(R.string.velocity), 1);
		final int speedsSize = speeds.size();
		for (int i = 0; i < speedsSize; i++) {
			float speed = speeds.get(i);
			if (MainActivity.distance_unit == MainActivity.unit_mile)
				speed /= 1.6;
			speedSeries.add(i, speed);
		}
		dataset.addSeries(1, speedSeries);
		final XYMultipleSeriesRenderer render = new XYMultipleSeriesRenderer(2);
		render.setAntialiasing(true);
		XYSeriesRenderer r1 = new XYSeriesRenderer();
		r1.setColor(Color.GREEN);
		render.addSeriesRenderer(r1);
		XYSeriesRenderer r2 = new XYSeriesRenderer();
		r2.setColor(Color.YELLOW);
		render.addSeriesRenderer(r2);
		render.setBackgroundColor(Color.BLACK);
		render.setApplyBackgroundColor(true);
		render.setLabelsTextSize(30);
		render.setLegendTextSize(30);
		render.setYLabels(10);
		render.setXLabels(10);
		String altitudeUnit = MainActivity.altitude_unit == MainActivity.unit_ft ? "(ft)" : "(m)";
		String distanceUnit = MainActivity.distance_unit == MainActivity.unit_mile ? "(mph)" : "(km/hr)";
		render.setYTitle(context.getString(R.string.Altitude) + altitudeUnit, 0);
		render.setYAxisAlign(Align.LEFT, 0);
		render.setYTitle(context.getString(R.string.velocity) + distanceUnit, 1);
		render.setYAxisAlign(Align.RIGHT, 1);
		render.setYLabelsAlign(Align.RIGHT, 1);
		render.setXTitle(context.getString(R.string.Time) + "(s)");
		render.setAxisTitleTextSize(30);
		String fileName = new File(path).getName();
		render.setChartTitle(fileName.substring(0, fileName.indexOf(".")));
		render.setChartTitleTextSize(40);
		if (handler == null)
			return;
		handler.post(new Runnable() {

			public void run() {

				try {
					View view = ChartFactory.getLineChartView(context, dataset, render);
					view.setDrawingCacheEnabled(true);
					view.measure(1920, 1080);
					view.layout(0, 0, 1920, 1080);
					view.buildDrawingCache();
					Bitmap bitmap = Bitmap.createBitmap(1920, 1080, Bitmap.Config.ARGB_8888);
					Canvas canvas = new Canvas(bitmap);
					view.draw(canvas);
					new File(path).delete();
					if (bitmap != null) {
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
						bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
						bos.flush();
						bos.close();
						bitmap.recycle();
						System.gc();
					} else {
						Toast.makeText(context, "bitmap is null", Toast.LENGTH_SHORT).show();
					}
				} catch (FileNotFoundException e) {

					e.printStackTrace();
				} catch (IOException e) {

					e.printStackTrace();
				} catch (OutOfMemoryError e) {
					e.printStackTrace();
				}

			}
		});
	}*/

	boolean stop;

	public void stop() {
		stop = true;
	}

	public void setOnProgressChangedListener(ProgressChangedListener listener) {
		this.listener = listener;
	}

	public void onProgressChanged(long progress) {
		if (listener != null) {
			listener.onProgressChanged(progress);
		}
	}

	public static interface ProgressChangedListener {
		public void onProgressChanged(long progress);
	}
}