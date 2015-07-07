package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.text.format.Time;

import com.yupog2003.tripdiary.R;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.TimeZone;

public class Trip {
	public Context context;
	public File dir;
	public File gpxFile;
	public File cacheFile;
	public File graphicFile;
	public File noteFile;
	public POI[] pois;
	public TrackCache cache;
	public String note;
	public String tripName;
	public String category;
	public String timezone;
	public GpxAnalyzerJava analyzer;
	public GpxAnalyzer2 analyzer2;

	public Trip(Context context, File dir) {
		this.dir = dir;
		if (!dir.exists()) {
			dir.mkdirs();
			MyCalendar.updateTripTimeZone(context, dir.getName(), TimeZone.getDefault().getID());
		}
		this.context = context;
		refreshAllFields();
	}

	public void refreshAllFields() {
		this.gpxFile = new File(dir.getPath() + "/" + dir.getName() + ".gpx");
		if (!gpxFile.exists()) {
			try {
				gpxFile.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		this.cacheFile = new File(gpxFile.getPath() + ".cache");
		this.graphicFile = new File(gpxFile.getPath() + ".graph");
		this.noteFile = new File(dir.getPath() + "/note");
		if (!noteFile.exists()) {
			try {
				noteFile.createNewFile();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
		refreshPOIs();
		try {
			BufferedReader br = new BufferedReader(new FileReader(noteFile));
			String s;
			StringBuffer sb = new StringBuffer();
			while ((s = br.readLine()) != null) {
				sb.append(s + "\n");
			}
			note = sb.toString();
			br.close();
		} catch (FileNotFoundException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		this.tripName = dir.getName();
		SharedPreferences p = context.getSharedPreferences("trip", Context.MODE_PRIVATE);
		category = p.getString(tripName, context.getString(R.string.nocategory));
		timezone = MyCalendar.getTripTimeZone(context, tripName);
	}

	public void getCacheJava(Context context, Handler handler, GpxAnalyzerJava.ProgressChangedListener listener) {
		analyzer = new GpxAnalyzerJava(gpxFile.getPath(), context, handler);
		analyzer.setOnProgressChangedListener(listener);
		if (analyzer.analyze()) {
			cache = analyzer.getCache();
		}
		analyzer = null;
	}

	public void getCacheJNI(Context context, Handler handler, GpxAnalyzerJava.ProgressChangedListener listener) {
		try {
			analyzer2 = new GpxAnalyzer2(gpxFile.getPath(), context, handler);
			analyzer2.setOnProgressChangedListener(listener);
			if (analyzer2.analyze()) {
				cache = analyzer2.getCache();
			}
			analyzer = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void stopGetCache() {
		if (analyzer != null) {
			analyzer.stop();
		}
	}

	public void deleteCache() {
		cacheFile.delete();
		graphicFile.delete();
	}

	public void updateNote(String note) {
		if (note != null) {
			try {
				BufferedWriter bw = new BufferedWriter(new FileWriter(noteFile, false));
				bw.write(note);
				bw.flush();
				bw.close();
				this.note = note;
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}

	public void refreshPOIs() {
		if (dir != null) {
			File[] poiFiles = dir.listFiles(FileHelper.getDirFilter());
			if (poiFiles != null) {
				this.pois = new POI[poiFiles.length];
				for (int i = 0; i < pois.length; i++) {
					pois[i] = new POI(poiFiles[i]);
				}
			} else {
				this.pois = new POI[0];
			}
			Arrays.sort(pois, new Comparator<POI>() {

				@Override
				public int compare(POI lhs, POI rhs) {

					if (lhs == null || rhs == null || lhs.time == null || rhs.time == null)
						return 0;
					if (lhs.time.after(rhs.time))
						return 1;
					else if (rhs.time.after(lhs.time))
						return -1;
					return 0;
				}
			});
		}
	}

	public void renameTrip(Context context, String name) {
		SharedPreferences p = context.getSharedPreferences("trip", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = p.edit();
		editor.remove(tripName);
		editor.putString(name, category);
		editor.commit();
		p = context.getSharedPreferences("tripTimezone", Context.MODE_PRIVATE);
		editor = p.edit();
		editor.remove(tripName);
		editor.putString(name, timezone);
		editor.commit();
		String gpxName = gpxFile.getName();
		String cacheName = cacheFile.getName();
		String graphicName = graphicFile.getName();
		dir.renameTo(new File(dir.getParent() + "/" + name));
		dir = new File(dir.getParent() + "/" + name);
		gpxFile = new File(dir.getPath() + "/" + gpxName);
		gpxFile.renameTo(new File(dir.getPath() + "/" + name + ".gpx"));
		gpxFile = new File(dir.getPath() + "/" + name + ".gpx");
		cacheFile = new File(dir.getPath() + "/" + cacheName);
		cacheFile.renameTo(new File(gpxFile.getPath() + ".cache"));
		cacheFile = new File(gpxFile.getPath() + ".cache");
		graphicFile = new File(dir.getPath() + "/" + graphicName);
		graphicFile.renameTo(new File(gpxFile.getPath() + ".graph"));
		graphicFile = new File(gpxFile.getPath() + ".graph");
		refreshAllFields();
	}

	public void setCategory(Context context, String category) {
		this.category = category;
		SharedPreferences.Editor editor = context.getSharedPreferences("trip", Context.MODE_PRIVATE).edit();
		editor.putString(tripName, category);
		editor.commit();
	}

	public void deleteSelf() {
		FileHelper.deletedir(dir.getPath());
	}
}
