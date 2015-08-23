package com.yupog2003.tripdiary.data;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class MyLatLng2 implements Serializable, Parcelable {

    private static final long serialVersionUID = -5358410160624933498L;

    public double latitude;
    public double longitude;
    public float altitude;
    public String time;

    public MyLatLng2(double latitude, double longitude, float altitude, String time) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.altitude = altitude;
        this.time = time;
    }

    public MyLatLng2() {
        this.latitude = 0;
        this.longitude = 0;
        this.altitude = 0;
        this.time = MyCalendar.getInstance().formatInCurrentTimezone();
    }

    protected MyLatLng2(Parcel in) {
        latitude = in.readDouble();
        longitude = in.readDouble();
        altitude = in.readFloat();
        time = in.readString();
    }

    public static final Creator<MyLatLng2> CREATOR = new Creator<MyLatLng2>() {
        @Override
        public MyLatLng2 createFromParcel(Parcel in) {
            return new MyLatLng2(in);
        }

        @Override
        public MyLatLng2[] newArray(int size) {
            return new MyLatLng2[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeFloat(altitude);
        dest.writeString(time);
    }
}
