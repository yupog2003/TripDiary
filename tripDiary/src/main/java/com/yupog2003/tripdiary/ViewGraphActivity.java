package com.yupog2003.tripdiary;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.MenuItem;

import com.yupog2003.tripdiary.fragments.ViewGraphFragment;

public class ViewGraphActivity extends MyActivity {

    public static final String tag_tripname = "tag_tripname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_graph);
        String tripName = getIntent().getStringExtra(tag_tripname);
        Bundle bundle = new Bundle();
        bundle.putString(ViewGraphFragment.tag_tripname, tripName);
        ViewGraphFragment viewGraphFragment = new ViewGraphFragment();
        viewGraphFragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, viewGraphFragment);
        ft.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_graph_acivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }


}
