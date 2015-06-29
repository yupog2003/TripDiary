package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.TimeZone;

public class TimeAnalyzer {
    public static final int type_gpx = 0;
    public static final int type_time_format3399 = 1;
    public static final int type_self = 2;

    public static long getMinusTimeInSecond(Calendar starttime, Calendar stoptime) {
        return (stoptime.getTimeInMillis() - starttime.getTimeInMillis()) / 1000;
    }

    public static String format3339(Calendar c) {
        if (c == null)
            return "";
        return String.valueOf(c.get(Calendar.YEAR))
                +"-"+String.valueOf(c.get(Calendar.MONTH)+1)
                +"-"+String.valueOf(c.get(Calendar.DAY_OF_MONTH))
                +"T"+String.valueOf(c.get(Calendar.HOUR_OF_DAY))
                +":"+String.valueOf(c.get(Calendar.MINUTE))
                +":"+String.valueOf(c.get(Calendar.SECOND))
                +".000";
    }

    public static Calendar getTime(String timezone, String s, int type) {
        Calendar c=Calendar.getInstance(TimeZone.getTimeZone(timezone));
        String date = "", t = "";
        String[] datetoks, timetoks;
        switch (type) {
            case type_gpx:
                if (s.contains(">") && s.contains("T") && s.contains("Z")) {
                    date = s.substring(s.indexOf(">") + 1, s.indexOf("T"));
                    t = s.substring(s.indexOf("T") + 1, s.indexOf("Z"));
                }
                break;
            case type_time_format3399:
                if (s.contains("=") && s.contains("T") && s.contains(".")) {
                    date = s.substring(s.indexOf("=") + 1, s.lastIndexOf("T"));
                    t = s.substring(s.lastIndexOf("T") + 1, s.lastIndexOf("."));
                }
                break;
            case type_self:
                if (s.contains("T")) {
                    date = s.substring(0, s.indexOf("T"));
                    t = s.substring(s.indexOf("T") + 1, s.length());
                }
                break;
        }
        if (date.contains("-") && t.contains(":")) {
            datetoks = date.split("-");
            timetoks = t.split(":");
            int year = (int) Double.parseDouble(datetoks[0]);
            int month = (int) Double.parseDouble(datetoks[1]) - 1;
            int day = (int) Double.parseDouble(datetoks[2]);
            int hour = (int) Double.parseDouble(timetoks[0]);
            int min = (int) Double.parseDouble(timetoks[1]);
            int second = (int) Double.parseDouble(timetoks[2]);
            c.set(year, month, day, hour, min, second);
            format3339(c);
        }
        return c;
    }

    public static Calendar getTime(String s, int type) {
        return getTime("UTC", s, type);
    }

    public static boolean isTimeMatched(Calendar time1, Calendar time2, Calendar time3) {
        return time1.compareTo(time2) <= 0 && time2.compareTo(time3) <= 0;
    }

    public static String formatInCurrentTimezone(Calendar time) {
        return formatInTimezone(time, TimeZone.getDefault().getID());
    }

    public static String formatInTimezone(Calendar time, String timezone) {
        if (time == null)
            time = Calendar.getInstance();
        if (timezone != null) {
            time = changeTimeZone(time, timezone);
        }
        String format3339=format3339(time);
        return format3339.substring(0,format3339.lastIndexOf("."));
    }

    public static Calendar getTripTime(String path, String tripName) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(path + "/" + tripName + "/" + tripName + ".gpx"));
            String s;
            while ((s = br.readLine()) != null) {
                if (s.contains("<time>")) {
                    br.close();
                    return TimeAnalyzer.getTime(s, TimeAnalyzer.type_gpx);
                }
            }
            br.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return Calendar.getInstance();
    }

    public static String getTimezoneFromLatlng(double lat, double lng) {
        String result = TimeZone.getDefault().getID();
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/timezone/xml?location=" + String.valueOf(lat) + "," + String.valueOf(lng) + "&timestamp=0&sensor=true");
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            InputStream is=connection.getInputStream();
            String s= IOUtils.toString(is);
            if (s.contains("OVER_QUERY_LIMIT")) {
                Thread.sleep(5000);
                return getTimezoneFromLatlng(lat, lng);
            }
            if (s.contains("OK")) {
                result = s.substring(s.indexOf("<time_zone_id>") + 14, s.indexOf("</time_zone_id>"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getTripTimeZone(Context context, String tripName) {
        if (context == null)
            return TimeZone.getDefault().getID();
        SharedPreferences preference = context.getSharedPreferences("tripTimezone", Context.MODE_PRIVATE);
        if (preference == null)
            return TimeZone.getDefault().getID();
        return preference.getString(tripName, TimeZone.getDefault().getID());
    }

    public static String getPOITimeZone(Context context, String poiPath) {
        if (poiPath == null || !poiPath.contains("/")) {
            return TimeZone.getDefault().getID();
        }
        String[] toks = poiPath.split("/");
        if (toks.length < 2) {
            return TimeZone.getDefault().getID();
        }
        String tripName = toks[toks.length - 2];
        return TimeAnalyzer.getTripTimeZone(context, tripName);
    }

    public static void updateTripTimeZoneFromLatLng(Context context, String tripName, double lat, double lng) {
        updateTripTimeZone(context, tripName, getTimezoneFromLatlng(lat, lng));
    }

    public static void updateTripTimeZone(Context context, String tripName, String timezone) {
        if (context == null)
            return;
        SharedPreferences preference = context.getSharedPreferences("tripTimezone", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preference.edit();
        editor.putString(tripName, timezone);
        editor.commit();
    }

    public static Calendar changeTimeZone(Calendar c, String timezone) {
        if (c == null) {
            return Calendar.getInstance();
        }
        if (timezone == null) {
            return c;
        }
        c.setTimeZone(TimeZone.getTimeZone(timezone));
        format3339(c);
        return c;
    }
}
