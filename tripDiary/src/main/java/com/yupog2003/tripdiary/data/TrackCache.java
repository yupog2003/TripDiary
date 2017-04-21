package com.yupog2003.tripdiary.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;

public class TrackCache implements Serializable, Parcelable {

    private static final long serialVersionUID = 7483356498452412154L;

    public double[] latitudes;
    public double[] longitudes;
    public float[] altitudes;
    public String[] times;
    public String startTime;
    public String endTime;
    public String totalTime;
    public float distance;   // meters
    public float avgSpeed;   // km/hr
    public float maxSpeed;   // km/hr
    public float climb;      // meters
    public float maxAltitude;// meters
    public float minAltitude;// meters


    public TrackCache() {

    }

    protected TrackCache(Parcel in) {
        latitudes = in.createDoubleArray();
        longitudes = in.createDoubleArray();
        altitudes = in.createFloatArray();
        times = in.createStringArray();
        startTime = in.readString();
        endTime = in.readString();
        totalTime = in.readString();
        distance = in.readFloat();
        avgSpeed = in.readFloat();
        maxSpeed = in.readFloat();
        climb = in.readFloat();
        maxAltitude = in.readFloat();
        minAltitude = in.readFloat();
    }

    public static final Creator<TrackCache> CREATOR = new Creator<TrackCache>() {
        @Override
        public TrackCache createFromParcel(Parcel in) {
            return new TrackCache(in);
        }

        @Override
        public TrackCache[] newArray(int size) {
            return new TrackCache[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDoubleArray(latitudes);
        dest.writeDoubleArray(longitudes);
        dest.writeFloatArray(altitudes);
        dest.writeStringArray(times);
        dest.writeString(startTime);
        dest.writeString(endTime);
        dest.writeString(totalTime);
        dest.writeFloat(distance);
        dest.writeFloat(avgSpeed);
        dest.writeFloat(maxSpeed);
        dest.writeFloat(climb);
        dest.writeFloat(maxAltitude);
        dest.writeFloat(minAltitude);
    }

    @NonNull
    public LatLng[] getLats() {
        if (latitudes == null || longitudes == null) return new LatLng[0];
        int length = Math.min(latitudes.length, longitudes.length);
        LatLng[] result = new LatLng[length];
        for (int i = 0; i < length; i++) {
            result[i] = new LatLng(latitudes[i], longitudes[i]);
        }
        return result;
    }

    public int getTrackLength() {
        if (latitudes == null || longitudes == null || altitudes == null || times == null) {
            return 0;
        } else {
            return Math.min(Math.min(latitudes.length, longitudes.length), Math.min(altitudes.length, times.length));
        }
    }
}
