package com.yupog2003.tripdiary.data;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.provider.DocumentFile;

import com.yupog2003.tripdiary.TripDiaryApplication;

import org.apache.commons.io.IOUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

public class MyCalendar extends GregorianCalendar{
    public static final int type_gpx = 0;
    public static final int type_time_format3399 = 1;
    public static final int type_self = 2;
    public static final int type_exif = 3;

    public static long getMinusTimeInSecond(Calendar starttime, Calendar stoptime) {
        return (stoptime.getTimeInMillis() - starttime.getTimeInMillis()) / 1000;
    }

    public static synchronized MyCalendar getInstance() {
        return new MyCalendar();
    }

    /**
     * Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * given {@code Locale} and default {@code TimeZone}, set to the current date and time.
     */
    public static synchronized MyCalendar getInstance(Locale locale) {
        return new MyCalendar(locale);
    }

    /**
     * Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * default {@code Locale} and given {@code TimeZone}, set to the current date and time.
     */
    public static synchronized MyCalendar getInstance(TimeZone timezone) {
        return new MyCalendar(timezone);
    }

    /**
     * Constructs a new instance of the {@code Calendar} subclass appropriate for the
     * given {@code Locale} and given {@code TimeZone}, set to the current date and time.
     */
    public static synchronized MyCalendar getInstance(TimeZone timezone, Locale locale) {
        return new MyCalendar(timezone, locale);
    }

    public MyCalendar() {
    }

    public MyCalendar(int year, int month, int day) {
        super(year, month, day);
    }

    public MyCalendar(int year, int month, int day, int hour, int minute) {
        super(year, month, day, hour, minute);
    }

    public MyCalendar(int year, int month, int day, int hour, int minute, int second) {
        super(year, month, day, hour, minute, second);
    }

    public MyCalendar(Locale locale) {
        super(locale);
    }

    public MyCalendar(TimeZone timezone) {
        super(timezone);
    }

    public MyCalendar(TimeZone timezone, Locale locale) {
        super(timezone, locale);
    }

    public String format3339() {
        String result = "";
        try {
            result = String.valueOf(get(Calendar.YEAR))
                    + "-" + String.valueOf(get(Calendar.MONTH) + 1)
                    + "-" + String.valueOf(get(Calendar.DAY_OF_MONTH))
                    + "T" + String.valueOf(get(Calendar.HOUR_OF_DAY))
                    + ":" + String.valueOf(get(Calendar.MINUTE))
                    + ":" + String.valueOf(get(Calendar.SECOND))
                    + ".000";
            if (getTimeZone().getID().equals("UTC")) {
                result += "Z";
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static MyCalendar getTime(String timezone, String s, int type) {
        MyCalendar c = MyCalendar.getInstance(TimeZone.getTimeZone(timezone));
        String date, t;
        String[] datetoks = new String[3], timetoks = new String[3];
        switch (type) {
            case type_gpx:
                if (s.contains(">") && s.contains("T") && s.contains("Z")) {
                    date = s.substring(s.indexOf(">") + 1, s.indexOf("T"));
                    t = s.substring(s.indexOf("T") + 1, s.indexOf("Z"));
                    datetoks = date.split("-");
                    timetoks = t.split(":");
                }
                break;
            case type_time_format3399:
                if (s.contains("=") && s.contains("T") && s.contains(".")) {
                    date = s.substring(s.indexOf("=") + 1, s.lastIndexOf("T"));
                    t = s.substring(s.lastIndexOf("T") + 1, s.lastIndexOf("."));
                    datetoks = date.split("-");
                    timetoks = t.split(":");
                }
                break;
            case type_self:
                if (s.contains("T")) {
                    date = s.substring(0, s.indexOf("T"));
                    t = s.substring(s.indexOf("T") + 1, s.length());
                    datetoks = date.split("-");
                    timetoks = t.split(":");
                }
                break;
            case type_exif:
                if (s.contains(":") && s.contains(" ")) {
                    date = s.split("\\s+")[0];
                    t = s.split("\\s+")[1];
                    datetoks = date.split(":");
                    timetoks = t.split(":");
                }
                break;
        }
        if (datetoks.length > 2 && timetoks.length > 2) {
            int year = (int) Double.parseDouble(datetoks[0]);
            int month = (int) Double.parseDouble(datetoks[1]) - 1;
            int day = (int) Double.parseDouble(datetoks[2]);
            int hour = (int) Double.parseDouble(timetoks[0]);
            int min = (int) Double.parseDouble(timetoks[1]);
            int second = (int) Double.parseDouble(timetoks[2]);
            c.set(year, month, day, hour, min, second);
            c.format3339();
        }
        return c;
    }

    public static MyCalendar getTime(String s, int type) {
        return getTime("UTC", s, type);
    }

    public String formatInCurrentTimezone() {
        return formatInTimezone(TimeZone.getDefault().getID());
    }

    public String formatInTimezone(String timezone) {
        if (timezone != null) {
            setTimeZone(timezone);
        }
        String format3339 = format3339();
        return format3339.substring(0, format3339.lastIndexOf("."));
    }

    public static String formatTotalTime(long totalSeconds) {
        int day = (int) (totalSeconds / 86400);
        int hour = (int) (totalSeconds % 86400 / 3600);
        int min = (int) (totalSeconds % 86400 % 3600 / 60);
        int sec = (int) (totalSeconds % 86400 % 3600 % 60);
        return String.format("%dT%d:%d:%d", day, hour, min, sec);
    }

    public static MyCalendar getTripTime(String tripName) { //in UTC
        try {
            SharedPreferences tripTimePreference = TripDiaryApplication.instance.getSharedPreferences("tripTime", 0);
            String s;
            if ((s = tripTimePreference.getString(tripName, null)) != null) {
                return getTime(s, type_gpx);
            }
            DocumentFile gpxFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, tripName + ".gpx");
            ContentResolver c = TripDiaryApplication.instance.getContentResolver();
            BufferedReader br = new BufferedReader(new InputStreamReader(c.openInputStream(gpxFile.getUri())));
            while ((s = br.readLine()) != null) {
                if (s.contains("<time>")) {
                    tripTimePreference.edit().putString(tripName, s).commit();
                    return getTime(s, type_gpx);
                }
            }
            br.close();
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
        return MyCalendar.getInstance();
    }

    public static String getTimezoneFromLatlng(double lat, double lng) {
        String result = TimeZone.getDefault().getID();
        try {
            URL url = new URL("https://maps.googleapis.com/maps/api/timezone/xml?location=" + String.valueOf(lat) + "," + String.valueOf(lng) + "&timestamp=0&sensor=true");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            InputStream is = connection.getInputStream();
            String s = IOUtils.toString(is, "UTF-8");
            if (s.contains("OVER_QUERY_LIMIT")) {
                Thread.sleep(5000);
                return getTimezoneFromLatlng(lat, lng);
            }
            if (s.contains("OK")) {
                result = s.substring(s.indexOf("<time_zone_id>") + 14, s.indexOf("</time_zone_id>"));
            }
        } catch (IOException | InterruptedException e) {
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

    public static void updateTripTimeZoneFromLatLng(Context context, String tripName, double lat, double lng) {
        updateTripTimeZone(context, tripName, getTimezoneFromLatlng(lat, lng));
    }

    public static void updateTripTimeZone(Context context, String tripName, String timezone) {
        if (context == null)
            return;
        context.getSharedPreferences("tripTimezone", Context.MODE_PRIVATE).edit().putString(tripName, timezone).commit();
        context.getSharedPreferences("tripTime", Context.MODE_PRIVATE).edit().remove(tripName).commit();
    }

    @Override
    public void setTimeZone(TimeZone timezone) {
        super.setTimeZone(timezone);
        format3339();
    }

    public void setTimeZone(String timeZoneID) {
        setTimeZone(TimeZone.getTimeZone(timeZoneID));
    }
}
