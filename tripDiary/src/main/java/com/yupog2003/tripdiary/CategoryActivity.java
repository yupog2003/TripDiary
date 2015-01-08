package com.yupog2003.tripdiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yupog2003.tripdiary.data.ColorHelper;

import java.util.Map;
import java.util.Set;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;

public class CategoryActivity extends MyActivity {
	
	SharedPreferences categorysp;
	SharedPreferences tripsp;
	SharedPreferences.Editor categoryeditor;
	SharedPreferences.Editor tripeditor;
	String[] categories;
	String[] trips;
	ListView listView;
	CategoryAdapter adapter;
	Toolbar toolBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		listView=(ListView)findViewById(R.id.listView);
		toolBar=(Toolbar)findViewById(R.id.toolbar);
		setSupportActionBar(toolBar);
		setTitle(R.string.manage_category);
		loaddata();
	}
	private void loaddata(){
		categorysp=getSharedPreferences("category", MODE_PRIVATE);
		tripsp=getSharedPreferences("trip", MODE_PRIVATE);
		categoryeditor=categorysp.edit();
		tripeditor=tripsp.edit();
		categoryeditor.commit();
		tripeditor.commit();
		Map<String,?> map=categorysp.getAll();
		Set<String> set=map.keySet();
		categories=set.toArray(new String[set.size()]);
		map=tripsp.getAll();
		set=map.keySet();
		trips=set.toArray(new String[set.size()]);
		adapter=new CategoryAdapter();
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(adapter);
		listView.setOnItemLongClickListener(adapter);
	}
	class CategoryAdapter extends BaseAdapter implements OnItemClickListener,OnItemLongClickListener{
		
		public int getCount() {

			return categories.length;
		}

		public Object getItem(int position) {

			return categories[position];
		}

		public long getItemId(int position) {

			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			TextView textView=new TextView(CategoryActivity.this);
			textView.setTextAppearance(CategoryActivity.this, android.R.style.TextAppearance_Large);
			int color=Integer.parseInt(categorysp.getString(categories[position], String.valueOf(Color.WHITE)));
			textView.setCompoundDrawablesWithIntrinsicBounds(ColorHelper.getColorDrawable(CategoryActivity.this, 50, color), null, null, null);
			textView.setGravity(Gravity.CENTER_VERTICAL);
			textView.setText(categories[position]);
			return textView;
		}
		
		public void onItemClick(AdapterView<?> adapterView, View view, final int position,
				long id) {

			AlertDialog.Builder ab=new AlertDialog.Builder(CategoryActivity.this);
			ab.setTitle(getString(R.string.edit_category));
			LinearLayout layout=(LinearLayout)getLayoutInflater().inflate(R.layout.add_category, null);
			final ImageView colorImage=(ImageView)layout.findViewById(R.id.categorycolor);
			final Button pickColor=(Button)layout.findViewById(R.id.pickColor);
			final EditText categoryName=(EditText)layout.findViewById(R.id.categoryname);
			final int color=Integer.parseInt(categorysp.getString(categories[position], String.valueOf(Color.WHITE)));
			colorImage.setImageDrawable(ColorHelper.getColorDrawable(CategoryActivity.this, 100, color));
			colorImage.setTag(color);
			categoryName.setText(categories[position]);
			pickColor.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {

					AmbilWarnaDialog dialog=new AmbilWarnaDialog(CategoryActivity.this, color, new OnAmbilWarnaListener() {
						
						public void onOk(AmbilWarnaDialog dialog, int color) {

							colorImage.setImageDrawable(ColorHelper.getColorDrawable(CategoryActivity.this, 100, color));
							colorImage.setTag(color);
						}
						
						public void onCancel(AmbilWarnaDialog dialog) {

							
						}
					});
					dialog.show();
				}
			});
			ab.setView(layout);
			ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {

					String nameStr=categoryName.getText().toString();
					int color=(Integer)colorImage.getTag();
					for (int i=0;i<trips.length;i++){
						if (tripsp.getString(trips[i], "nocategory").equals(categories[position])){
							tripeditor.putString(trips[i], nameStr);
						}
					}
					tripeditor.commit();
					categoryeditor.remove(categories[position]);
					categoryeditor.commit();
					categoryeditor.putString(nameStr, String.valueOf(color));
					categoryeditor.commit();
					loaddata();
				}
			});
			ab.setNegativeButton(getString(R.string.cancel), null);
			ab.show();
		}

		public boolean onItemLongClick(AdapterView<?> adapterView, View view,
				final int position, long id) {

			AlertDialog.Builder ab=new AlertDialog.Builder(CategoryActivity.this);
			ab.setTitle(getString(R.string.delete));
			ab.setMessage(getString(R.string.are_you_sure_to_delete));
			ab.setIcon(R.drawable.ic_alert);
			ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {

					if (categories[position].equals("nocategory")){
						Toast.makeText(CategoryActivity.this, getString(R.string.cannot_delete_nocategory), Toast.LENGTH_SHORT).show();
					}else{
						for (int i=0;i<trips.length;i++){
							String category=tripsp.getString(trips[i], "nocategory");
							if (category.equals(categories[position])){
								tripeditor.putString(trips[i], "nocategory");
								tripeditor.commit();
							}
						}
						categoryeditor.remove(categories[position]);
						categoryeditor.commit();
						loaddata();
					}
				}
			});
			ab.setNegativeButton(getString(R.string.cancel), null);
			ab.show();
			return true;
		}
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_category, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch(item.getItemId()){
		case R.id.addcategory:
			AlertDialog.Builder ab=new AlertDialog.Builder(CategoryActivity.this);
			ab.setTitle(getString(R.string.edit_category));
			LinearLayout layout=(LinearLayout)getLayoutInflater().inflate(R.layout.add_category, null);
			final ImageView colorImage=(ImageView)layout.findViewById(R.id.categorycolor);
			final Button pickColor=(Button)layout.findViewById(R.id.pickColor);
			final EditText categoryName=(EditText)layout.findViewById(R.id.categoryname);
			final int color=Color.WHITE;
			colorImage.setImageDrawable(ColorHelper.getColorDrawable(CategoryActivity.this, 100, color));
			colorImage.setTag(color);
			pickColor.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {

					AmbilWarnaDialog dialog=new AmbilWarnaDialog(CategoryActivity.this, color, new OnAmbilWarnaListener() {
						
						public void onOk(AmbilWarnaDialog dialog, int color) {

							colorImage.setImageDrawable(ColorHelper.getColorDrawable(CategoryActivity.this, 100, color));
							colorImage.setTag(color);
						}
						
						public void onCancel(AmbilWarnaDialog dialog) {

							
						}
					});
					dialog.show();
				}
			});
			ab.setView(layout);
			ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {

					String nameStr=categoryName.getText().toString();
					if (nameStr.equals("nocategory")){
						Toast.makeText(CategoryActivity.this, getString(R.string.cannot_add_nocategory), Toast.LENGTH_SHORT).show();
					}else{
						int color=(Integer)colorImage.getTag();
						categoryeditor.putString(nameStr, String.valueOf(color));
						categoryeditor.commit();
						loaddata();
					}
				}
			});
			ab.setNegativeButton(getString(R.string.cancel), null);
			ab.show();
			break;
		}
		return true;
	}

}
