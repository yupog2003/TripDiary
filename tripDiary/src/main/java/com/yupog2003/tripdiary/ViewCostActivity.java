package com.yupog2003.tripdiary;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.yupog2003.tripdiary.fragments.ViewCostFragment;

public class ViewCostActivity extends MyActivity {
	ViewCostFragment viewCostFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_view_cost);
		Toolbar toolBar=(Toolbar)findViewById(R.id.toolbar);
		if (toolBar!=null){
			setSupportActionBar(toolBar);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		int option = getIntent().getIntExtra("option", 0);
		String path = getIntent().getStringExtra("path");
		String title = getIntent().getStringExtra("title");
		setTitle(title + " " + getString(R.string.cost));
		viewCostFragment = new ViewCostFragment();
		Bundle args = new Bundle();
		args.putString("path", path);
		args.putInt("option", option);
		args.putString("title", title);
		viewCostFragment.setArguments(args);
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.fragmentLayout, viewCostFragment);
		ft.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home:
			ViewCostActivity.this.finish();
			return true;
		}
		return false;
	}

}
