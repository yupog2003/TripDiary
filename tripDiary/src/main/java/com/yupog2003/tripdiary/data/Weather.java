package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.yupog2003.tripdiary.R;

public class Weather {

    public static final String appId = "ec4b3a3dc6f24ebd3e9ba68b30c6d907";

    public static int getIconForId(String id, int hour) {
        if (id == null) return R.drawable.ic_question_mark;
        boolean isDaytime = MyCalendar.isDayTime(hour);
        switch (id) {
            case "clear_sky":
                return isDaytime ? R.drawable.ic_sun : R.drawable.ic_moon;
            case "few_clouds":
                return isDaytime ? R.drawable.ic_cloud0 : R.drawable.ic_night_cloud;
            case "scattered_clouds":
                return R.drawable.ic_cloud1;
            case "broken_clouds":
                return R.drawable.ic_cloud2;
            case "shower_rain":
                return R.drawable.ic_rain;
            case "rain":
                return isDaytime ? R.drawable.ic_light_rain : R.drawable.ic_night_rain;
            case "thunderstorm":
                return R.drawable.ic_thunder;
            case "snow":
                return R.drawable.ic_snow;
            case "mist":
                return R.drawable.ic_mist;
            default:
                return R.drawable.ic_question_mark;
        }
    }

    public static String getDescriptionForId(Context context, String id) {
        switch (id) {
            case "clear_sky":
                return context.getString(R.string.clear_sky);
            case "few_clouds":
                return context.getString(R.string.few_clouds);
            case "scattered_clouds":
                return context.getString(R.string.scattered_clouds);
            case "broken_clouds":
                return context.getString(R.string.broken_clouds);
            case "shower_rain":
                return context.getString(R.string.shower_rain);
            case "rain":
                return context.getString(R.string.rain);
            case "thunderstorm":
                return context.getString(R.string.thunderstorm);
            case "snow":
                return context.getString(R.string.snow);
            case "mist":
                return context.getString(R.string.mist);
            default:
                return "unknown";
        }
    }

    public static String getIdFromCode(int id) {
        int group = id / 100;
        if (group == 2) {
            return "thunderstorm";
        } else if (group == 3) {
            return "shower_rain";
        } else if (group == 5) {
            if (id >= 500 && id <= 505) {
                return "rain";
            } else if (id == 511) {
                return "snow";
            } else {
                return "shower_rain";
            }
        } else if (group == 6) {
            return "snow";
        } else if (group == 7) {
            return "mist";
        } else if (group == 8) {
            int n = id % 10;
            if (n == 0) {
                return "clear_sky";
            } else if (n == 1) {
                return "few_clouds";
            } else if (n == 2) {
                return "scattered_clouds";
            } else if (n == 3 || n == 4) {
                return "broken_clouds";
            }
        }
        return "unknown";
    }

    public static class WeatherAdapter extends ArrayAdapter<Integer> implements AdapterView.OnItemSelectedListener {

        int[] images;
        POI poi;
        int dp5;

        public WeatherAdapter(Context context, int resource, int hour, POI poi) {
            super(context, resource);
            boolean daytime = MyCalendar.isDayTime(hour);
            images = new int[]{R.drawable.ic_question_mark,
                    daytime ? R.drawable.ic_sun : R.drawable.ic_moon,
                    daytime ? R.drawable.ic_cloud0 : R.drawable.ic_night_cloud,
                    R.drawable.ic_cloud1,
                    R.drawable.ic_cloud2,
                    R.drawable.ic_rain,
                    daytime ? R.drawable.ic_light_rain : R.drawable.ic_night_rain,
                    R.drawable.ic_thunder,
                    R.drawable.ic_snow,
                    R.drawable.ic_mist};
            this.poi = poi;
            this.dp5 = (int) DeviceHelper.pxFromDp(context, 5);
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getViewFromPosition(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getViewFromPosition(position);
        }

        @Override
        public int getCount() {
            return images.length;
        }

        public View getViewFromPosition(int position) {
            ImageView imageView = new ImageView(getContext());
            imageView.setImageDrawable(DrawableHelper.getAccentTintDrawable(getContext(), images[position]));
            imageView.setPadding(dp5, dp5, dp5, dp5);
            return imageView;
        }

        public static int getPositionFromId(String id) {
            if (id == null) return 0;
            switch (id) {
                case "clear_sky":
                    return 1;
                case "few_clouds":
                    return 2;
                case "scattered_clouds":
                    return 3;
                case "broken_clouds":
                    return 4;
                case "shower_rain":
                    return 5;
                case "rain":
                    return 6;
                case "thunderstorm":
                    return 7;
                case "snow":
                    return 8;
                case "mist":
                    return 9;
                default:
                    return 0;
            }
        }

        public static String getIdFromPosition(int position) {
            switch (position) {
                case 0:
                    return "unknown";
                case 1:
                    return "clear_sky";
                case 2:
                    return "few_clouds";
                case 3:
                    return "scattered_clouds";
                case 4:
                    return "broken_clouds";
                case 5:
                    return "shower_rain";
                case 6:
                    return "rain";
                case 7:
                    return "thunderstorm";
                case 8:
                    return "snow";
                case 9:
                    return "mist";
                default:
                    return "unknown";

            }
        }

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (poi != null) {
                poi.updateBasicInformation(null, null, null, null, null, getIdFromPosition(position));
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }
}
