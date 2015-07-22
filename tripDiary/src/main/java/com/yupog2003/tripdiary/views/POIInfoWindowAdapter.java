package com.yupog2003.tripdiary.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v4.provider.DocumentFile;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.Trip;

import java.util.HashMap;

public class POIInfoWindowAdapter implements InfoWindowAdapter {

    Activity activity;
    Trip trip;
    Trip[] trips;
    HashMap<String, Bitmap> bitmaps;
    View rootView;
    TextView name;
    TextView time;
    TextView diary;
    ImageView img;
    Bitmap poiBitmap;

    public POIInfoWindowAdapter(Activity activity, Trip trip, Trip[] trips) {
        this.activity = activity;
        this.trip = trip;
        this.trips = trips;
        this.bitmaps = new HashMap<>();
        this.rootView = activity.getLayoutInflater().inflate(R.layout.poi_infowindow, null);
        this.name = (TextView) rootView.findViewById(R.id.poiName);
        this.time = (TextView) rootView.findViewById(R.id.poiTime);
        this.diary = (TextView) rootView.findViewById(R.id.poiDiary);
        this.img = (ImageView) rootView.findViewById(R.id.poiImage);
        this.poiBitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
    }

    @Override
    public View getInfoContents(Marker marker) {
        try {
            String poiName = marker.getTitle();
            String snippet = marker.getSnippet();
            String poiTime = snippet.split("\n")[0];
            String poiDiary = snippet.split("\n")[1];
            name.setText(poiName);
            time.setText(poiTime);
            diary.setText(poiDiary);
            DocumentFile[] imgs = null;
            if (poiName.contains("/") && trips != null) {
                String tripName = poiName.split("/")[0];
                String realPOIName = poiName.split("/")[1];
                for (int i = 0; i < trips.length; i++) {
                    if (tripName.equals(trips[i].tripName)) {
                        for (int j = 0; j < trips[i].pois.length; j++) {
                            if (realPOIName.equals(trips[i].pois[j].title)) {
                                imgs = trips[i].pois[j].picFiles;
                                break;
                            }
                        }
                        break;
                    }
                }
            } else if (trip != null) {
                for (int i = 0; i < trip.pois.length; i++) {
                    if (poiName.equals(trip.pois[i].title)) {
                        imgs = trip.pois[i].picFiles;
                        break;
                    }
                }
            }
            if (imgs != null && imgs.length > 0) {
                Uri uri = imgs[0].getUri();
                if (bitmaps.get(uri.toString()) != null) {
                    img.setImageBitmap(bitmaps.get(uri.toString()));
                    return rootView;
                }
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri), new Rect(0, 0, 0, 0), op);
                op.inJustDecodeBounds = false;
                op.inPreferredConfig = Bitmap.Config.RGB_565;
                op.inPreferQualityOverSpeed = false;
                int imageWidth = (int) DeviceHelper.pxFromDp(activity, 72);
                op.inSampleSize = (int) Math.max((float) op.outWidth / imageWidth, (float) op.outHeight / imageWidth);
                Bitmap bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri), new Rect(0, 0, 0, 0), op);
                img.setImageBitmap(bitmap);
                bitmaps.put(uri.toString(), bitmap);
            } else {
                img.setImageBitmap(poiBitmap);
            }
            return rootView;
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
