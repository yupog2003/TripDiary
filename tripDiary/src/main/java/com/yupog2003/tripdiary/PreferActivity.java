package com.yupog2003.tripdiary;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.yupog2003.tripdiary.data.MyBackupAgent;
import com.yupog2003.tripdiary.fragments.PreferFragment;

public class PreferActivity extends PreferenceActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new PreferFragment())
                .commit();
    }

    @Override
    public void onPause() {
        MyBackupAgent.requestBackup(this);
        super.onPause();
    }
}
