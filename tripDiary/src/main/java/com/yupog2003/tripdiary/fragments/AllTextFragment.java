package com.yupog2003.tripdiary.fragments;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.TimeAnalyzer;
import com.yupog2003.tripdiary.views.FloatingGroupExpandableListView;
import com.yupog2003.tripdiary.views.WrapperExpandableListAdapter;

import java.io.File;
import java.util.Arrays;

public class AllTextFragment extends Fragment {
    POI[] pois;
    boolean[] expand;
    FloatingGroupExpandableListView listView;

    public AllTextFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        listView = new FloatingGroupExpandableListView(getActivity());
        listView.setGroupIndicator(null);
        // listView.setBackgroundColor(getResources().getColor(R.color.item_background));
        setHasOptionsMenu(true);
        refresh();
        return listView;
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();

    }

    public void refresh() {
        this.pois = ViewTripActivity.trip.pois;
        if (expand == null) {
            expand = new boolean[pois.length];
            Arrays.fill(expand, false);
        }
        POIAdapter adapter = new POIAdapter(pois);
        WrapperExpandableListAdapter adapter2 = new WrapperExpandableListAdapter(adapter);
        listView.setAdapter(adapter2);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            if (i >= expand.length)
                continue;
            if (expand[i]) {
                listView.expandGroup(i);
            } else {
                listView.collapseGroup(i);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Auto-generated method stub
        inflater.inflate(R.menu.fragment_all, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        switch (item.getItemId()) {
            case R.id.expandall:
                expandAll();
                break;
            case R.id.collapseall:
                collapseAll();
                break;
        }
        return true;
    }

    public void expandAll() {
        int groupCount = listView.getExpandableListAdapter().getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            listView.expandGroup(i);
        }
    }

    public void collapseAll() {
        int groupCount = listView.getExpandableListAdapter().getGroupCount();
        for (int i = 0; i < groupCount; i++) {
            listView.collapseGroup(i);
        }
    }

    class POIAdapter extends BaseExpandableListAdapter {
        POI[] pois;
        String[] diarys;

        public POIAdapter(POI[] pois) {
            this.pois = pois;
            diarys = new String[pois.length];
            for (int i = 0; i < diarys.length; i++) {
                diarys[i] = pois[i].diary;
                if (diarys[i] == null) {
                    diarys[i] = "";
                }
                if (diarys[i].length() > 0) {
                    diarys[i] = diarys[i].substring(0, diarys[i].length() - 1);
                }
            }
        }

        public Object getChild(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return diarys[groupPosition];
        }

        public long getChildId(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            TextView textView = new TextView(getActivity());
            textView.setText(diarys[groupPosition]);
            textView.setPadding(50, 0, 0, 0);
            File fontFile = new File(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("diaryfont", ""));
            if (fontFile.exists() && fontFile.isFile()) {
                try {
                    textView.setTypeface(Typeface.createFromFile(fontFile));
                } catch (RuntimeException e) {
                    Toast.makeText(getActivity(), getString(R.string.invalid_font), Toast.LENGTH_SHORT).show();
                }
            }
            textView.setTextSize(PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("diaryfontsize", 20));
            return textView;
        }

        public int getChildrenCount(int groupPosition) {
            // TODO Auto-generated method stub
            return 1;
        }

        public Object getGroup(int groupPosition) {
            // TODO Auto-generated method stub
            return pois[groupPosition];
        }

        public int getGroupCount() {
            // TODO Auto-generated method stub
            if (pois == null)
                return 0;
            return pois.length;
        }

        public long getGroupId(int groupPosition) {
            // TODO Auto-generated method stub
            return groupPosition;
        }

        class GroupViewHolder {
            TextView poiName;
            TextView poiExtra;
        }

        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            // TODO Auto-generated method stub
            GroupViewHolder holder;
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(R.layout.poi_group_item, parent, false);
                holder = new GroupViewHolder();
                holder.poiName = (TextView) convertView.findViewById(R.id.poiName);
                holder.poiExtra = (TextView) convertView.findViewById(R.id.poiExtra);
                convertView.setTag(holder);
            }
            holder = (GroupViewHolder) convertView.getTag();
            holder.poiName.setCompoundDrawablesWithIntrinsicBounds(isExpanded ? R.drawable.indicator_expand2 : R.drawable.indicator_collapse2, 0, 0, 0);
            holder.poiName.setText(pois[groupPosition].title + "(" + String.valueOf(pois[groupPosition].diary.length()) + ")");
            holder.poiExtra.setText("-" + TimeAnalyzer.formatInTimezone(pois[groupPosition].time, ViewTripActivity.trip.timezone));
            return convertView;
        }

        public boolean hasStableIds() {
            // TODO Auto-generated method stub
            return false;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            // TODO Auto-generated method stub
            return false;
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            // TODO Auto-generated method stub
            if (expand != null && groupPosition > -1 && groupPosition < expand.length) {
                expand[groupPosition] = true;
            }
            super.onGroupExpanded(groupPosition);
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            // TODO Auto-generated method stub
            if (expand != null && groupPosition > -1 && groupPosition < expand.length) {
                expand[groupPosition] = false;
            }
            super.onGroupCollapsed(groupPosition);
        }
    }

}
