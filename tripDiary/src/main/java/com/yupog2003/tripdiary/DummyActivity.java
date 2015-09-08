package com.yupog2003.tripdiary;

import android.os.Bundle;

public class DummyActivity extends MyActivity {

    public static DummyActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    @Override
    public void finishAndRemoveTask() {
        instance = null;
        super.finishAndRemoveTask();
    }
}
