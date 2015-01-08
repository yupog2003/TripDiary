package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Handler;
import android.text.format.Time;
import android.view.View;
import android.widget.Toast;

import com.yupog2003.tripdiary.MainActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.data.GpxAnalyzerJava.ProgressChangedListener;

import org.achartengine.ChartFactory;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class GpxAnalyzer2 {

	private TrackCache cache;
	String gpxPath;
	Context context;
	Handler contextHandler;
	ProgressChangedListener listener;

	public GpxAnalyzer2(String gpxPath, Context context, Handler contextHandler) {
		this.gpxPath = gpxPath;
		this.context = context;
		this.contextHandler = contextHandler;
	}

	public boolean analyze() {
		String tripPath = gpxPath.substring(0, gpxPath.lastIndexOf("/"));
		String rootPath = tripPath.substring(0, tripPath.lastIndexOf("/"));
		String tripName = gpxPath.substring(gpxPath.lastIndexOf("/") + 1, gpxPath.lastIndexOf("."));
		int timeZoneOffset = 0;
		cache = new TrackCache();
		boolean cacheExist = new File(gpxPath + ".cache").exists();
		if (cacheExist) {
			int numOfLines = FileHelper.getNumOfLinesInFile(gpxPath + ".cache");
			int size = (numOfLines - 9) / 4;
			cache.latitudes = new double[size];
			cache.longitudes = new double[size];
			cache.altitudes = new float[size];
			cache.times = new String[size];
		} else {
			String timezone = TimeAnalyzer.getTripTimeZone(context, tripName);
			Time tempTime = TimeAnalyzer.getTripTime(rootPath, tripName);
			tempTime.switchTimezone(timezone);
			timeZoneOffset = (int) tempTime.gmtoff;
		}
		ArrayList<Float> speeds = cacheExist ? null : new ArrayList<Float>();
		boolean success = cacheExist ? getCache(gpxPath + ".cache", cache) : parse(gpxPath, cache, speeds, timeZoneOffset);
		if (!cacheExist && success) {
			saveGraph(context, gpxPath + ".graph", cache.altitudes, speeds, contextHandler);
		}
		return success;
	}

	private void saveGraph(final Context context, final String path, final float[] altitudes, final ArrayList<Float> speeds, Handler handler) {
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
	}

	public void onProgressChanged(long progress) {
		if (listener != null) {
			listener.onProgressChanged(progress);
		}
	}

	public TrackCache getCache() {
		return this.cache;
	}

	public void setOnProgressChangedListener(ProgressChangedListener listener) {
		this.listener = listener;
	}

	public static final DecimalFormat doubleFormat = new DecimalFormat("#.###");

	public static String getDistanceString(float kmNumber, String unit) {
		if (MainActivity.distance_unit == MainActivity.unit_mile) {
			kmNumber /= 1.6;
			if (unit.equals("km"))
				unit = "mi";
			else if (unit.equals("km/hr"))
				unit = "mph";
		}
		return doubleFormat.format(kmNumber) + unit;
	}

	public static String getAltitudeString(float mNumber, String unit) {
		if (MainActivity.altitude_unit == MainActivity.unit_ft) {
			mNumber /= 0.3048;
			if (unit.equals("m"))
				unit = "ft";
		}
		return String.valueOf(mNumber) + unit;
	}

	public native boolean parse(String gpxPath, TrackCache cache, ArrayList<Float> speeds, int timezoneOffset);

	public native boolean getCache(String cachePath, TrackCache cache);
	
	public native void stop();

}
