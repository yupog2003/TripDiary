package com.yupog2003.tripdiary.views;

import android.app.Activity;
import android.content.ContentResolver;
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
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Trip;

import java.util.HashMap;

public class POIInfoWindowAdapter implements InfoWindowAdapter {

    Activity activity;
    POI[] pois;
    Trip[] trips;
    HashMap<String, Bitmap> bitmaps;
    View rootView;
    TextView name;
    TextView time;
    TextView diary;
    ImageView img;
    Bitmap poiBitmap;
    ContentResolver contentResolver;
    int imageWidth;
    Rect rect;

    public POIInfoWindowAdapter(Activity activity, POI[] pois, Trip[] trips) {
        this.activity = activity;
        this.pois = pois;
        this.trips = trips;
        this.bitmaps = new HashMap<>();
        this.rootView = activity.getLayoutInflater().inflate(R.layout.poi_infowindow, null);
        this.name = (TextView) rootView.findViewById(R.id.poiName);
        this.time = (TextView) rootView.findViewById(R.id.poiTime);
        this.diary = (TextView) rootView.findViewById(R.id.poiDiary);
        this.img = (ImageView) rootView.findViewById(R.id.poiImage);
        this.poiBitmap = ((BitmapDrawable) img.getDrawable()).getBitmap();
        this.contentResolver=activity.getContentResolver();
        this.imageWidth=(int) DeviceHelper.pxFromDp(activity, 72);
        this.rect=new Rect(0,0,0,0);
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
                for (Trip trip : trips) {
                    if (tripName.equals(trip.tripName)) {
                        for (POI poi : trip.pois) {
                            if (realPOIName.equals(poi.title)) {
                                imgs = poi.picFiles;
                                break;
                            }
                        }
                        break;
                    }
                }
            } else if (pois != null) {
                for (POI poi : pois) {
                    if (poiName.equals(poi.title)) {
                        imgs = poi.picFiles;
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
                BitmapFactory.decodeStream(contentResolver.openInputStream(uri), rect, op);
                op.inJustDecodeBounds = false;
                op.inPreferredConfig = Bitmap.Config.RGB_565;
                op.inPreferQualityOverSpeed = false;
                op.inSampleSize = (int) Math.max((float) op.outWidth / imageWidth, (float) op.outHeight / imageWidth);
                Bitmap bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri), rect, op);
                if (bitmap!=null){
                    img.setImageBitmap(bitmap);
                    bitmaps.put(uri.toString(), bitmap);
                }else{
                    img.setImageBitmap(poiBitmap);
                }
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
    public void setPOIs(POI[] pois){
        this.pois=pois;
    }
}
