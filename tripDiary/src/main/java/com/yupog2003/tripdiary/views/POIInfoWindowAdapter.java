package com.yupog2003.tripdiary.views;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.model.Marker;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.data.documentfile.RestDriveDocumentFile;
import com.yupog2003.tripdiary.data.documentfile.WebDocumentFile;

import java.util.HashMap;

public class POIInfoWindowAdapter implements InfoWindowAdapter {

    POI[] pois;
    Trip[] trips;
    HashMap<String, Bitmap> bitmaps;
    View rootView;
    TextView name;
    TextView time;
    TextView diary;
    ImageView img;
    Drawable poiDrawable;
    int imageWidth;
    Rect rect;

    public POIInfoWindowAdapter(Activity activity, POI[] pois, Trip[] trips) {
        this.pois = pois;
        this.trips = trips;
        this.bitmaps = new HashMap<>();
        this.rootView = activity.getLayoutInflater().inflate(R.layout.poi_infowindow, (ViewGroup) activity.findViewById(android.R.id.content), false);
        this.name = (TextView) rootView.findViewById(R.id.poiName);
        this.time = (TextView) rootView.findViewById(R.id.poiTime);
        this.diary = (TextView) rootView.findViewById(R.id.poiDiary);
        this.img = (ImageView) rootView.findViewById(R.id.poiImage);
        this.poiDrawable = img.getDrawable().mutate();
        this.imageWidth = (int) DeviceHelper.pxFromDp(activity, 72);
        this.rect = new Rect(0, 0, 0, 0);
        DrawableCompat.setTint(DrawableCompat.wrap(poiDrawable), PreferenceManager.getDefaultSharedPreferences(activity).getInt("markercolor", 0xffff0000));
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
                DocumentFile imgFile = imgs[0];
                Uri uri = imgFile.getUri();
                if (bitmaps.get(uri.toString()) != null) {
                    img.setImageBitmap(bitmaps.get(uri.toString()));
                    return rootView;
                }
                BitmapFactory.Options op = new BitmapFactory.Options();
                op.inJustDecodeBounds = true;
                if (imgFile instanceof WebDocumentFile) {
                    BitmapFactory.decodeStream(((WebDocumentFile) imgFile).getThumbInputStream(), rect, op);
                } else if (imgFile instanceof RestDriveDocumentFile) {
                    BitmapFactory.decodeStream(((RestDriveDocumentFile) imgFile).getThumbInputStream(), rect, op);
                } else {
                    BitmapFactory.decodeStream(imgFile.getInputStream(), rect, op);
                }
                op.inJustDecodeBounds = false;
                op.inPreferredConfig = Bitmap.Config.RGB_565;
                op.inSampleSize = (int) Math.max((float) op.outWidth / imageWidth, (float) op.outHeight / imageWidth);
                Bitmap bitmap;
                if (imgFile instanceof WebDocumentFile) {
                    bitmap = BitmapFactory.decodeStream(((WebDocumentFile) imgFile).getThumbInputStream(), rect, op);
                } else if (imgFile instanceof RestDriveDocumentFile) {
                    bitmap = BitmapFactory.decodeStream(((RestDriveDocumentFile) imgFile).getThumbInputStream(), rect, op);
                } else {
                    bitmap = BitmapFactory.decodeStream(imgFile.getInputStream(), rect, op);
                }
                if (bitmap != null) {
                    img.setImageBitmap(bitmap);
                    bitmaps.put(uri.toString(), bitmap);
                } else {
                    img.setImageDrawable(poiDrawable);
                }
            } else {
                img.setImageDrawable(poiDrawable);
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

    public void setPOIs(POI[] pois) {
        this.pois = pois;
    }

}
