package com.yupog2003.tripdiary.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.provider.DocumentFile;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.ViewCostActivity;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.ComparatorHelper;
import com.yupog2003.tripdiary.data.CostData;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.Trip;
import com.yupog2003.tripdiary.views.CheckableLinearLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class CostListFragment extends Fragment implements View.OnClickListener {
    int option;
    ListView costsList;
    TextView summary;
    TextView poiColumn;
    TextView typeColumn;
    TextView nameColumn;
    TextView dollarColumn;
    CostAdapter adapter;
    public String tripName;
    public String poiName;
    ViewGroup rootView;
    DocumentFile[] costFiles;
    ViewCostFragment viewCostFragment;

    public CostListFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.option = getArguments().getInt(ViewCostActivity.tag_option);
        this.tripName = getArguments().getString(ViewCostActivity.tag_trip);
        this.poiName = getArguments().getString(ViewCostActivity.tag_poi);
        this.viewCostFragment=(ViewCostFragment)getParentFragment();
        rootView = (ViewGroup) inflater.inflate(R.layout.fragment_cost_list, container, false);
        costsList = (ListView) rootView.findViewById(R.id.costslist);
        poiColumn = (TextView) rootView.findViewById(R.id.poicolumn);
        poiColumn.setOnClickListener(this);
        typeColumn = (TextView) rootView.findViewById(R.id.typecolumn);
        typeColumn.setOnClickListener(this);
        nameColumn = (TextView) rootView.findViewById(R.id.namecolumn);
        nameColumn.setOnClickListener(this);
        dollarColumn = (TextView) rootView.findViewById(R.id.dollarcolumn);
        dollarColumn.setOnClickListener(this);
        summary = (TextView) rootView.findViewById(R.id.summary);
        if (option == ViewCostActivity.optionTrip) {
            summary.setPadding(0, 0, 0, 0);
        }
        refresh();
        return rootView;
    }

    public void refresh() {
        if (option == ViewCostActivity.optionTrip) {
            Trip trip = ViewTripActivity.trip;
            ArrayList<DocumentFile> costList = new ArrayList<>();
            for (POI poi : trip.pois) {
                costList.addAll(Arrays.asList(poi.costFiles));
            }
            costFiles = costList.toArray(new DocumentFile[costList.size()]);
        } else if (option == ViewCostActivity.optionPOI) {
            DocumentFile poiFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, poiName, "costs");
            costFiles = poiFile.listFiles();
        }
        adapter = new CostAdapter(option);
        costsList.setAdapter(adapter);
        if (option == ViewCostActivity.optionPOI) {
            costsList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
            costsList.setLongClickable(true);
            costsList.setMultiChoiceModeListener(new MyMultiChoiceModeListener());
        }
    }

    class CostAdapter extends BaseAdapter {
        public ArrayList<CostData> costDatas = new ArrayList<>();
        private LayoutParams textParams = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1f);
        public String[] types = getResources().getStringArray(R.array.cost_types);
        public int[] colors;
        public float[] totals = new float[5];
        private boolean[] sortState = new boolean[4];

        public CostAdapter(int option) {
            textParams.bottomMargin = 1;
            textParams.topMargin = 1;
            textParams.leftMargin = 1;
            textParams.rightMargin = 1;
            TypedArray array = getResources().obtainTypedArray(R.array.cost_type_colors);
            colors = new int[array.length()];
            for (int i = 0; i < colors.length; i++)
                colors[i] = array.getColor(i, 0);
            array.recycle();
            switch (option) {
                case ViewCostActivity.optionPOI:
                    for (DocumentFile costFile : costFiles) {
                        costDatas.add(readCostData(costFile, poiName));
                    }
                    break;
                case ViewCostActivity.optionTrip:
                    for (DocumentFile costFile : costFiles) {
                        costDatas.add(readCostData(costFile, FileHelper.getFileName(costFile.getParentFile().getParentFile())));
                    }
                    break;
            }
            summary.setText("");
            for (int i = 0; i < 4; i++) {
                summary.append(types[i] + ":" + String.valueOf(totals[i]) + " ");
            }
            summary.append(getString(R.string.total_cost) + ":" + totals[4]);
        }

        private CostData readCostData(DocumentFile file, String POI) {
            String costName = FileHelper.getFileName(file);
            int costType = -1;
            float costDollar = -1;
            CostData data = null;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(getActivity().getContentResolver().openInputStream(file.getUri())));
                String s;
                while ((s = br.readLine()) != null) {
                    if (s.startsWith("type=")) {
                        costType = Integer.parseInt(s.substring(s.indexOf("=") + 1));
                    } else if (s.startsWith("dollar=")) {
                        costDollar = Float.valueOf(s.substring(s.indexOf("=") + 1));
                    }
                }
                br.close();
                totals[4] += costDollar;
                if (costType != -1 && costDollar != -1) {
                    totals[costType] += costDollar;
                }
                data = new CostData(POI, costType, costName, costDollar, file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        public void sort(int sorttype) {
            sortState[sorttype] = !sortState[sorttype];
            Collections.sort(costDatas, ComparatorHelper.getCostDataComparator(sorttype, sortState[sorttype]));
            this.notifyDataSetChanged();
            costsList.setAdapter(this);
        }

        public int getCount() {

            return costDatas.size();
        }

        public Object getItem(int position) {

            return costDatas.get(position);
        }

        public long getItemId(int position) {

            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            TextView column0 = new TextView(getActivity());
            TextView column1 = new TextView(getActivity());
            TextView column2 = new TextView(getActivity());
            TextView column3 = new TextView(getActivity());
            column0.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
            column1.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
            column2.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
            column3.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
            column0.setLayoutParams(textParams);
            column1.setLayoutParams(textParams);
            column2.setLayoutParams(textParams);
            column3.setLayoutParams(textParams);
            column0.setTextColor(Color.BLACK);
            column1.setTextColor(Color.BLACK);
            column2.setTextColor(Color.BLACK);
            column3.setTextColor(Color.BLACK);
            CostData data = costDatas.get(position);
            column0.setText(data.POI);
            column1.setText(types[data.costType]);
            column2.setText(data.costName);
            column3.setText(String.valueOf(data.costDollar));
            column0.setBackgroundColor(colors[data.costType]);
            column1.setBackgroundColor(colors[data.costType]);
            column2.setBackgroundColor(colors[data.costType]);
            column3.setBackgroundColor(colors[data.costType]);
            CheckableLinearLayout layout = new CheckableLinearLayout(getActivity());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            layout.addView(column0);
            layout.addView(column1);
            layout.addView(column2);
            layout.addView(column3);
            convertView = layout;

            return convertView;
        }

    }

    public void onClick(View v) {

        if (v.equals(poiColumn)) {
            adapter.sort(ComparatorHelper.sort_by_cost_POI);
        } else if (v.equals(typeColumn)) {
            adapter.sort(ComparatorHelper.sort_by_cost_type);
        } else if (v.equals(nameColumn)) {
            adapter.sort(ComparatorHelper.sort_by_cost_name);
        } else if (v.equals(dollarColumn)) {
            adapter.sort(ComparatorHelper.sort_by_cost_dollar);
        }
    }

    class MyMultiChoiceModeListener implements ListView.MultiChoiceModeListener {
        boolean[] checks;
        boolean checkAll;

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            final ArrayList<CostData> checkedItems = new ArrayList<>();
            for (int i = 0; i < checks.length; i++) {
                if (checks[i]) {
                    checkedItems.add((CostData) adapter.getItem(i));
                }
            }
            mode.finish();
            if (item.getItemId() == R.id.delete) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle(getString(R.string.be_careful));
                ab.setMessage(getString(R.string.are_you_sure_to_delete));
                ab.setIcon(R.drawable.ic_alert);
                ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < checkedItems.size(); i++) {
                            checkedItems.get(i).file.delete();
                        }
                        viewCostFragment.refreshData(true);
                    }
                });
                ab.setNegativeButton(getString(R.string.cancel), null);
                ab.show();
            } else if (item.getItemId() == R.id.edit) {
                final CostData data = checkedItems.get(0);
                AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity());
                ab2.setTitle(getString(R.string.cost));
                final LinearLayout layout = (LinearLayout) getActivity().getLayoutInflater().inflate(R.layout.take_money, rootView, false);
                final EditText costName = (EditText) layout.findViewById(R.id.costname);
                costName.setText(data.costName);
                final RadioGroup costType = (RadioGroup) layout.findViewById(R.id.costtype);
                switch (data.costType) {
                    case 0:
                        costType.check(R.id.food);
                        break;
                    case 1:
                        costType.check(R.id.lodging);
                        break;
                    case 2:
                        costType.check(R.id.transportation);
                        break;
                    case 3:
                        costType.check(R.id.other);
                        break;
                    default:
                        costType.check(R.id.other);
                        break;
                }
                final EditText costDollar = (EditText) layout.findViewById(R.id.costdollar);
                costDollar.setText(String.valueOf(data.costDollar));
                ab2.setView(layout);
                ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        String name = costName.getText().toString();
                        String dollar = costDollar.getText().toString();
                        if (!name.equals("") && !dollar.equals("")) {
                            int type;
                            if (costType.getCheckedRadioButtonId() == R.id.food) {
                                type = 0;
                            } else if (costType.getCheckedRadioButtonId() == R.id.lodging) {
                                type = 1;
                            } else if (costType.getCheckedRadioButtonId() == R.id.transportation) {
                                type = 2;
                            } else if (costType.getCheckedRadioButtonId() == R.id.other) {
                                type = 3;
                            } else {
                                type = 0;
                            }
                            DocumentFile dataParent = data.file.getParentFile();
                            data.file.delete();
                            try {
                                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(getActivity().getContentResolver().openOutputStream(dataParent.createFile("", name).getUri())));
                                bw.write("type=" + String.valueOf(type) + "\n");
                                bw.write("dollar=" + dollar);
                                bw.flush();
                                bw.close();
                            } catch (IOException e) {

                                e.printStackTrace();
                            }
                            viewCostFragment.refreshData(true);
                        }
                    }
                });
                ab2.setNegativeButton(getString(R.string.cancel), null);
                ab2.show();
            } else if (item.getItemId() == R.id.selectall) {
                for (int i = 0; i < costsList.getCount(); i++) {
                    costsList.setItemChecked(i, !checkAll);
                }
                checkAll = !checkAll;
            }
            return true;
        }

        public boolean onCreateActionMode(ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.cost_menu, menu);
            checks = new boolean[adapter.getCount()];
            for (int i = 0; i < checks.length; i++)
                checks[i] = false;
            checkAll = false;
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {

        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return true;
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            checks[position] = checked;
            int selects = 0;
            for (boolean check : checks) {
                if (check)
                    selects++;
            }
            mode.getMenu().findItem(R.id.edit).setVisible(!(selects > 1));
        }

    }
}
