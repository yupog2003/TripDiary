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
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;

import java.io.File;

public class TextFragment extends Fragment implements OnLongClickListener, OnClickListener {
    TextView text;
    EditText editText;
    Button enter;
    Button cancel;
    Button share;
    FloatingActionButton edit;

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
        setEditMode(false);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setText();
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
            enter.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            share.setVisibility(View.VISIBLE);
            edit.setVisibility(View.GONE);
            imm.showSoftInput(editText, 0);
        } else {
            text.setVisibility(View.VISIBLE);
            editText.setVisibility(View.GONE);
            enter.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            share.setVisibility(View.GONE);
            edit.setVisibility(View.VISIBLE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    public void setText() {

        if (!isAdded() || getView() == null || getActivity() == null)
            return;
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

        text.setTextSize(PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("diaryfontsize", 20));
        editText.setTextSize(PreferenceManager.getDefaultSharedPreferences(getActivity()).getInt("diaryfontsize", 20));
        text.setText(ViewPointActivity.poi.diary);
        editText.setText(ViewPointActivity.poi.diary);
    }

    @Override
    public boolean onLongClick(View v) {


        return true;
    }

    public void shareDiary() {
        Toast.makeText(getActivity(), getString(R.string.share_diary), Toast.LENGTH_LONG).show();
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("text/plain");
        i.putExtra(Intent.EXTRA_TEXT, text.getText().toString());
        getActivity().startActivity(i);
    }

    @Override
    public void onClick(View v) {

        if (v.equals(enter)) {
            ViewPointActivity.poi.updateDiary(editText.getText().toString());
            text.setText(ViewPointActivity.poi.diary);
            editText.setText(ViewPointActivity.poi.diary);
            if (getActivity() != null && getActivity() instanceof ViewPointActivity) {
                ((ViewPointActivity) getActivity()).requestUpdatePOI();
            }
            setEditMode(false);
        } else if (v.equals(cancel)) {
            text.setText(ViewPointActivity.poi.diary);
            editText.setText(ViewPointActivity.poi.diary);
            setEditMode(false);
        } else if (v.equals(share)) {
            shareDiary();
            setEditMode(false);
        } else if (v.equals(edit)) {
            setEditMode(true);
        }
    }
}
