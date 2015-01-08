package com.yupog2003.tripdiary.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.data.DeviceHelper;

import java.io.File;

public class POIInfoWindowAdapter implements InfoWindowAdapter {

	String rootPath;
	Activity activity;

	public POIInfoWindowAdapter(Activity activity, String rootPath) {
		this.rootPath = rootPath;
		this.activity = activity;
	}

	@Override
	public View getInfoContents(Marker marker) {

		try {
			String poiName = marker.getTitle();
			String snippet = marker.getSnippet();
			String poiTime = snippet.split("\n")[0];
			String poiDiary = snippet.split("\n")[1];
			View view = activity.getLayoutInflater().inflate(R.layout.poi_infowindow, null);
			TextView name = (TextView) view.findViewById(R.id.poiName);
			name.setText(poiName);
			TextView time = (TextView) view.findViewById(R.id.poiTime);
			time.setText(poiTime);
			TextView diary = (TextView) view.findViewById(R.id.poiDiary);
			diary.setText(poiDiary);
			File[] imgs = new File(rootPath + "/" + poiName + "/pictures").listFiles();
			if (imgs != null && imgs.length > 0) {
				String imagePath = imgs[0].getPath();
				ImageView img = (ImageView) view.findViewById(R.id.poiImage);
				BitmapFactory.Options op = new BitmapFactory.Options();
				op.inJustDecodeBounds = true;
				BitmapFactory.decodeFile(imagePath, op);
				op.inJustDecodeBounds = false;
				op.inPreferredConfig = Bitmap.Config.RGB_565;
				op.inPreferQualityOverSpeed = false;
				int imageWidth = (int) DeviceHelper.pxFromDp(activity, 72);
				op.inSampleSize = (int) Math.max((float) op.outWidth / imageWidth, (float) op.outHeight / imageWidth);
				Bitmap bitmap = BitmapFactory.decodeFile(imagePath, op);
				img.setImageBitmap(bitmap);
			}
			return view;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public View getInfoWindow(Marker marker) {

		return null;
	}

}
