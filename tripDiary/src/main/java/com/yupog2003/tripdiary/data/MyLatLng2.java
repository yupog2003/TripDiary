package com.yupog2003.tripdiary.data;

import java.io.Serializable;

public class MyLatLng2 implements Serializable {

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

}
