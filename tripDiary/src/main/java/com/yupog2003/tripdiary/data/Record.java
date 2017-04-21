package com.yupog2003.tripdiary.data;


import android.os.Parcel;
import android.os.Parcelable;

public class Record implements Parcelable {

    public long totalTime;
    public float totalDistance;
    public float totalClimb;
    public float maxAltitude;
    public float minAltitude;
    public double maxLatitude;
    public double minLatitude;
    public int num_Trips;
    public int num_POIs;
    public int num_Pictures;
    public int num_Videos;
    public int num_Audios;

    public Record() {

    }

    protected Record(Parcel in) {
        totalTime = in.readLong();
        totalDistance = in.readFloat();
        totalClimb = in.readFloat();
        maxAltitude = in.readFloat();
        minAltitude = in.readFloat();
        maxLatitude = in.readDouble();
        minLatitude = in.readDouble();
        num_Trips = in.readInt();
        num_POIs = in.readInt();
        num_Pictures = in.readInt();
        num_Videos = in.readInt();
        num_Audios = in.readInt();
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(totalTime);
        dest.writeFloat(totalDistance);
        dest.writeFloat(totalClimb);
        dest.writeFloat(maxAltitude);
        dest.writeFloat(minAltitude);
        dest.writeDouble(maxLatitude);
        dest.writeDouble(minLatitude);
        dest.writeInt(num_Trips);
        dest.writeInt(num_POIs);
        dest.writeInt(num_Pictures);
        dest.writeInt(num_Videos);
        dest.writeInt(num_Audios);
    }
}
