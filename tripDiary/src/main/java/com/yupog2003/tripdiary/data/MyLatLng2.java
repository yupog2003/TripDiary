package com.yupog2003.tripdiary.data;

import android.text.format.Time;

import java.io.Serializable;

public class MyLatLng2 implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -5358410160624933498L;
	
	public double latitude;
	public double longitude;
	public float altitude;
	public String time;
	
	public MyLatLng2(double latitude,double longitude,float altitude,String time){
		this.latitude=latitude;
		this.longitude=longitude;
		this.altitude=altitude;
		this.time=time;
	}
	public MyLatLng2(){
		this.latitude=0;
		this.longitude=0;
		this.altitude=0;
		Time t=new Time();
		t.setToNow();
		this.time=TimeAnalyzer.formatTotalTime(t);
	}
	public void setLatitude(double latitude){
		this.latitude=latitude;
	}
	public void setMyLatLng2(double latitude,double longitude,float altitude,String time){
		this.latitude=latitude;
		this.longitude=longitude;
		this.altitude=altitude;
		this.time=time;
	}
	public void setMyLatLng2(MyLatLng2 latlng){
		this.latitude=latlng.getLatitude();
		this.longitude=latlng.getLongitude();
		this.altitude=latlng.getAltitude();
		this.time=latlng.getTime();
	}
	public void setLongtitude(double longitude){
		this.longitude=longitude;
	}
	public void setAltitude(float altitude){
		this.altitude=altitude;
	}
	public void setTime(String time){
		this.time=time;
	}
	public double getLatitude(){
		return this.latitude;
	}
	public double getLongitude(){
		return this.longitude;
	}
	public float getAltitude(){
		return this.altitude;
	}
	public String getTime(){
		return this.time;
	}
}
