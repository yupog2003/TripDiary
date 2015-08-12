package com.yupog2003.tripdiary.fragments;

import android.app.Service;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.data.POI;

import java.io.File;

public class TextFragment extends Fragment implements OnClickListener {
    TextView text;
    EditText editText;
    Button enter;
    Button cancel;
    Button share;
    LinearLayout buttomBar;
    View shadow;
    FloatingActionButton edit;
    POI poi;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_text, container, false);
        text = (TextView) view.findViewById(R.id.Text);
        editText = (EditText) view.findViewById(R.id.EditText);
        enter = (Button) view.findViewById(R.id.enter);
        enter.setOnClickListener(this);
        cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        share = (Button) view.findViewById(R.id.share);
        share.setOnClickListener(this);
        edit = (FloatingActionButton) view.findViewById(R.id.edit);
        edit.setOnClickListener(this);
        buttomBar = (LinearLayout) view.findViewById(R.id.buttonbar);
        shadow = view.findViewById(R.id.shadow);
        setEditMode(false);
        refresh();
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState.isEmpty()) {
            outState.putBoolean("bug:fix", true);
        }
    }

    public void setEditMode(boolean isEdit) {
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Service.INPUT_METHOD_SERVICE);
        if (isEdit) {
            text.setVisibility(View.GONE);
            editText.setVisibility(View.VISIBLE);
            buttomBar.setVisibility(View.VISIBLE);
            shadow.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            imm.showSoftInput(editText, 0);
        } else {
            text.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            buttomBar.setVisibility(View.GONE);
            shadow.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }
    @Override
    public void onResume() {
        super.onResume();
    }
    public void refresh() {
        if (getActivity() == null)
            return;
        this.poi = ((ViewPointActivity)getActivity()).poi;
        if (poi == null) {
            return;
        }
        text.setText("");
        editText.setText("");
        File fontFile = new File(getActivity().getFilesDir(), PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("diaryfont", ""));
        if (fontFile.exists() && fontFile.isFile()) {
            try {
                text.setTypeface(Typeface.createFromFile(fontFile));
                editText.setTypeface(Typeface.createFromFile(fontFile));
            } catch (RuntimeException e) {
                Toast.makeText(getActivity(), getString(R.string.invalid_font), Toast.LENGTH_SHORT).show();
            }
        }
        int textSize = PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("diaryfontsize", 20);
        text.setTextSize(textSize);
        editText.setTextSize(textSize);
        text.setText(poi.diary);
        editText.setText(poi.diary);
    }

    public void shareDiary() {
        Toast.makeText(getActivity(), getString(R.string.share_diary), Toast.LENGTH_LONG).show();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, text.getText().toString());
        startActivity(i);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(enter)) {
            poi.updateDiary(editText.getText().toString());
            text.setText(poi.diary);
            editText.setText(poi.diary);
            if (getActivity() != null && getActivity() instanceof ViewPointActivity) {
                ((ViewPointActivity) getActivity()).requestUpdatePOI();
            }
            setEditMode(false);
        } else if (v.equals(cancel)) {
            text.setText(poi.diary);
            editText.setText(poi.diary);
            setEditMode(false);
        } else if (v.equals(share)) {
            shareDiary();
            setEditMode(false);
        } else if (v.equals(edit)) {
            setEditMode(true);
        }
    }
}
