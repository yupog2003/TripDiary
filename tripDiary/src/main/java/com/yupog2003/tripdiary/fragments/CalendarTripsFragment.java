package com.yupog2003.tripdiary.fragments;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateChangedListener;
import com.prolificinteractive.materialcalendarview.OnMonthChangedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.yupog2003.tripdiary.MyActivity;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.DrawableHelper;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.views.CheckableLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class CalendarTripsFragment extends Fragment implements OnDateChangedListener, OnMonthChangedListener {

    MaterialCalendarView calendarView;
    ListView tripList;
    TripAdapter adapter;
    HashMap<CalendarDay, ArrayList<Trip>> tripDateMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_calendar_trips, container, false);
        calendarView = (MaterialCalendarView) rootView.findViewById(R.id.calendarView);
        tripList = (ListView) rootView.findViewById(R.id.tripList);
        adapter = new TripAdapter(new Trip[0]);
        tripList.setAdapter(adapter);
        tripList.setOnItemClickListener(adapter);
        return rootView;
    }

    public void loadData() {
        DocumentFile[] tripFiles = TripDiaryApplication.rootDocumentFile.listFiles(DocumentFile.list_dirs);
        tripDateMap = new HashMap<>();
        HashMap<String, ArrayList<Trip>> tripCategoryMap = new HashMap<>();
        SharedPreferences categorySp = getActivity().getSharedPreferences("category", Context.MODE_PRIVATE);
        Set<String> keySet = categorySp.getAll().keySet();
        String[] categories = keySet.toArray(new String[keySet.size()]);
        HashMap<String, Drawable> categoryDrawables = new HashMap<>();
        for (int i = 0; i < categories.length; i++) {
            try {
                categoryDrawables.put(categories[i], DrawableHelper.getColorDrawable(getActivity(), 40, Integer.valueOf(categorySp.getString(categories[i], String.valueOf(Color.WHITE)))));
            } catch (NumberFormatException e) {
                categorySp.edit().putString(categories[i], String.valueOf(Color.WHITE)).apply();
                i--;
                e.printStackTrace();
            }
        }
        for (DocumentFile tripFile : tripFiles) {
            Trip trip = new Trip(getActivity(), tripFile, true);
            trip.time.setTimeZone(trip.timezone);
            trip.setDrawable(categoryDrawables.get(trip.category));
            CalendarDay c = CalendarDay.from(trip.time);
            if (!tripDateMap.containsKey(c)) {
                tripDateMap.put(c, new ArrayList<Trip>());
            }
            tripDateMap.get(c).add(trip);
            if (!tripCategoryMap.containsKey(trip.category)) {
                tripCategoryMap.put(trip.category, new ArrayList<Trip>());
            }
            tripCategoryMap.get(trip.category).add(trip);
        }
        calendarView.removeDecorators();
        for (String category : tripCategoryMap.keySet()) {
            int color = Integer.valueOf(categorySp.getString(category, String.valueOf(Color.WHITE)));
            ArrayList<CalendarDay> dates = new ArrayList<>();
            for (Trip trip : tripCategoryMap.get(category)) {
                dates.add(CalendarDay.from(trip.time));
            }
            calendarView.addDecorator(new EventDecorator(color, dates));
        }
        calendarView.addDecorator(new TodayDecorator());
        calendarView.setOnDateChangedListener(this);
        calendarView.setOnMonthChangedListener(this);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.year:
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                final NumberPicker numberPicker = new NumberPicker(getActivity());
                numberPicker.setMinValue(1970);
                numberPicker.setMaxValue(2100);
                numberPicker.setValue(calendarView.getCurrentDate().getYear());
                numberPicker.setWrapSelectorWheel(true);
                ab.setView(numberPicker);
                ab.setTitle(R.string.year);
                ab.setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        calendarView.setCurrentDate(CalendarDay.from(numberPicker.getValue(), calendarView.getCurrentDate().getMonth(), 15));
                    }
                });
                ab.setNegativeButton(R.string.cancel, null);
                ab.show();
                break;
            case R.id.today:
                calendarView.setCurrentDate(Calendar.getInstance());
                break;
        }
        return true;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_calendar_trips, menu);
    }

    @Override
    public void onDateChanged(@NonNull MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
        ArrayList<Trip> trips = tripDateMap.get(calendarDay);
        if (trips != null) {
            adapter.setTrips(trips.toArray(new Trip[trips.size()]));
        } else {
            adapter.setTrips(new Trip[0]);
        }
    }

    @Override
    public void onMonthChanged(MaterialCalendarView materialCalendarView, CalendarDay calendarDay) {
        adapter.setTrips(new Trip[0]);
    }

    public class EventDecorator implements DayViewDecorator {

        private int color;
        private HashSet<CalendarDay> dates;
        private int dp3;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
            this.dp3 = (int) DeviceHelper.pxFromDp(getActivity(), 3);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(dp3, color));
        }
    }

    public class TodayDecorator implements DayViewDecorator {

        private CalendarDay date;

        public TodayDecorator() {
            date = CalendarDay.today();
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return date != null && day.equals(date);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new StyleSpan(Typeface.BOLD));
            view.addSpan(new RelativeSizeSpan(1.4f));
        }

    }

    class TripAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        Trip[] trips;

        public TripAdapter(@NonNull Trip[] trips) {
            this.trips = trips;
        }

        public void setTrips(@NonNull Trip[] trips) {
            this.trips = trips;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return trips.length;
        }

        @Override
        public Object getItem(int position) {
            return trips[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class ViewHolder {
            TextView item;
            TextView itemextra;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                CheckableLayout layout = (CheckableLayout) getActivity().getLayoutInflater().inflate(R.layout.trip_list_item, parent, false);
                holder.item = (TextView) layout.findViewById(R.id.tripname);
                holder.itemextra = (TextView) layout.findViewById(R.id.tripextra);
                convertView = layout;
                convertView.setTag(holder);
            }
            holder = (ViewHolder) convertView.getTag();
            Trip trip = trips[position];
            String tripName = trip.tripName;
            holder.item.setText(tripName);
            holder.item.setCompoundDrawablesWithIntrinsicBounds(trip.drawable, null, null, null);
            holder.itemextra.setText("-" + trip.time.formatInTimezone(trip.timezone));
            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            viewTrip(trips[position].tripName);
        }

        private void viewTrip(String tripName) {
            Activity activity = getActivity();
            if (activity == null) return;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ActivityManager.AppTask task = MyActivity.findViewTripActivityTask(activity, tripName);
                if (task != null) {
                    task.moveToFront();
                    return;
                }
            }
            Intent i = new Intent(activity, ViewTripActivity.class);
            i.putExtra(ViewTripActivity.tag_tripName, tripName);
            startActivity(i);
        }
    }

}
