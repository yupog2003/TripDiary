package com.yupog2003.tripdiary;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
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

import com.yupog2003.tripdiary.data.DrawableHelper;

import java.util.Map;
import java.util.Set;

import yuku.ambilwarna.AmbilWarnaDialog;
import yuku.ambilwarna.AmbilWarnaDialog.OnAmbilWarnaListener;

public class CategoryActivity extends MyActivity implements View.OnClickListener {

    SharedPreferences categorySp;
    SharedPreferences tripSp;
    String[] categories;
    String[] trips;
    ListView listView;
    CategoryAdapter adapter;
    FloatingActionButton add;
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        listView = (ListView) findViewById(R.id.listView);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        add = (FloatingActionButton) findViewById(R.id.add);
        add.setOnClickListener(this);
        setSupportActionBar(toolBar);
        setTitle(R.string.manage_category);
        loaddata();
    }

    private void loaddata() {
        categorySp = getSharedPreferences("category", MODE_PRIVATE);
        tripSp = getSharedPreferences("trip", MODE_PRIVATE);
        Map<String, ?> map = categorySp.getAll();
        Set<String> set = map.keySet();
        categories = set.toArray(new String[set.size()]);
        map = tripSp.getAll();
        set = map.keySet();
        trips = set.toArray(new String[set.size()]);
        adapter = new CategoryAdapter();
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(adapter);
        listView.setOnItemLongClickListener(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v.equals(add)) {
            AlertDialog.Builder ab = new AlertDialog.Builder(CategoryActivity.this);
            ab.setTitle(getString(R.string.edit_category));
            LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.add_category, (ViewGroup) findViewById(android.R.id.content), false);
            final ImageView colorImage = (ImageView) layout.findViewById(R.id.categorycolor);
            final Button pickColor = (Button) layout.findViewById(R.id.pickColor);
            final EditText categoryName = (EditText) layout.findViewById(R.id.categoryname);
            final int color = Color.WHITE;
            colorImage.setImageDrawable(DrawableHelper.getColorDrawable(CategoryActivity.this, 100, color));
            colorImage.setTag(color);
            pickColor.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    AmbilWarnaDialog dialog = new AmbilWarnaDialog(CategoryActivity.this, color, new OnAmbilWarnaListener() {

                        public void onOk(AmbilWarnaDialog dialog, int color) {

                            colorImage.setImageDrawable(DrawableHelper.getColorDrawable(CategoryActivity.this, 100, color));
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

                    String nameStr = categoryName.getText().toString();
                    if (nameStr.equals(getString(R.string.nocategory))) {
                        Toast.makeText(CategoryActivity.this, getString(R.string.cannot_add_nocategory), Toast.LENGTH_SHORT).show();
                    } else {
                        int color = (Integer) colorImage.getTag();
                        categorySp.edit().putString(nameStr, String.valueOf(color)).apply();
                        loaddata();
                    }
                }
            });
            ab.setNegativeButton(getString(R.string.cancel), null);
            ab.show();
        }
    }

    class CategoryAdapter extends BaseAdapter implements OnItemClickListener, OnItemLongClickListener {

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
            TextView textView = new TextView(CategoryActivity.this);
            textView.setTextAppearance(CategoryActivity.this, android.R.style.TextAppearance_Large);
            int color = Integer.parseInt(categorySp.getString(categories[position], String.valueOf(Color.WHITE)));
            textView.setCompoundDrawablesWithIntrinsicBounds(DrawableHelper.getColorDrawable(CategoryActivity.this, 50, color), null, null, null);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            textView.setText(categories[position]);
            return textView;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, final int position, long id) {

            AlertDialog.Builder ab = new AlertDialog.Builder(CategoryActivity.this);
            ab.setTitle(getString(R.string.edit_category));
            LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.add_category, (ViewGroup) findViewById(android.R.id.content), false);
            final ImageView colorImage = (ImageView) layout.findViewById(R.id.categorycolor);
            final Button pickColor = (Button) layout.findViewById(R.id.pickColor);
            final EditText categoryName = (EditText) layout.findViewById(R.id.categoryname);
            final int color = Integer.parseInt(categorySp.getString(categories[position], String.valueOf(Color.WHITE)));
            colorImage.setImageDrawable(DrawableHelper.getColorDrawable(CategoryActivity.this, 100, color));
            colorImage.setTag(color);
            categoryName.setText(categories[position]);
            pickColor.setOnClickListener(new View.OnClickListener() {

                public void onClick(View v) {

                    AmbilWarnaDialog dialog = new AmbilWarnaDialog(CategoryActivity.this, color, new OnAmbilWarnaListener() {

                        public void onOk(AmbilWarnaDialog dialog, int color) {

                            colorImage.setImageDrawable(DrawableHelper.getColorDrawable(CategoryActivity.this, 100, color));
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

                    String nameStr = categoryName.getText().toString();
                    int color = (Integer) colorImage.getTag();
                    for (String trip : trips) {
                        if (tripSp.getString(trip, getString(R.string.nocategory)).equals(categories[position])) {
                            tripSp.edit().putString(trip, nameStr).apply();
                        }
                    }
                    categorySp.edit().remove(categories[position]).apply();
                    categorySp.edit().putString(nameStr, String.valueOf(color)).apply();
                    loaddata();
                }
            });
            ab.setNegativeButton(getString(R.string.cancel), null);
            ab.show();
        }

        public boolean onItemLongClick(AdapterView<?> adapterView, View view,final int position, long id) {
            AlertDialog.Builder ab = new AlertDialog.Builder(CategoryActivity.this);
            ab.setTitle(getString(R.string.delete));
            ab.setMessage(getString(R.string.are_you_sure_to_delete));
            ab.setIcon(DrawableHelper.getAlertDrawable(getActivity()));
            ab.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    if (categories[position].equals(getString(R.string.nocategory))) {
                        Toast.makeText(CategoryActivity.this, getString(R.string.cannot_delete_nocategory), Toast.LENGTH_SHORT).show();
                    } else {
                        for (String trip : trips) {
                            String category = tripSp.getString(trip, getString(R.string.nocategory));
                            if (category.equals(categories[position])) {
                                tripSp.edit().putString(trip, getString(R.string.nocategory)).apply();
                            }
                        }
                        categorySp.edit().remove(categories[position]).apply();
                        loaddata();
                    }
                }
            });
            ab.setNegativeButton(getString(R.string.no), null);
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
        return true;
    }

}
