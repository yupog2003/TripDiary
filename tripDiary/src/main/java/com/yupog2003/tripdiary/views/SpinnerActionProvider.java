package com.yupog2003.tripdiary.views;

import android.content.Context;
import android.support.v4.view.ActionProvider;
import android.view.View;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;

import java.util.ArrayList;

public class SpinnerActionProvider extends ActionProvider {
    Spinner spinner;
    Context context;

    public SpinnerActionProvider(Context context) {
        super(context);
        this.context = context;
        this.spinner = new Spinner(context);
    }

    @Override
    public View onCreateActionView() {
        return spinner;
    }

    public void setItems(ArrayList<String> itemNames, OnItemSelectedListener listener) {
        SpinnerAdapter adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, itemNames);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(listener);

    }

}
