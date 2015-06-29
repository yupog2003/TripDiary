package com.yupog2003.tripdiary.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.print.PrintHelper;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.FileHelper.MoveFilesTask.OnFinishedListener;
import com.yupog2003.tripdiary.views.CheckableLayout;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class PictureFragment extends Fragment implements OnItemClickListener {
    GridView layout;
    PictureAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (GridView) inflater.inflate(R.layout.fragment_picture, container, false);
        layout.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        layout.setMultiChoiceModeListener(new MyMultiChoiceModeListener());
        layout.setOnItemClickListener(this);
        adapter = new PictureAdapter();
        layout.setAdapter(adapter);
        return layout;
    }

    @Override
    public void onPause() {

        // adapter.run=false;
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        File[] picFiles = ViewPointActivity.poi.picDir.listFiles(FileHelper.getPictureFileFilter());
        if (picFiles != null && adapter != null && picFiles.length != adapter.getCount()) {
            adapter.reFresh();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        if (outState.isEmpty()) {
            outState.putBoolean("bug:fix", true);
        }
    }

    class PictureAdapter extends BaseAdapter {
        File[] files;
        int width;
        DisplayImageOptions options;
        Bitmap[] bitmaps;
        int dp2;

        public PictureAdapter() {
            int screenWidth = DeviceHelper.getScreenWidth(getActivity());
            int screenHeight = DeviceHelper.getScreenHeight(getActivity());
            if (screenWidth > screenHeight) {
                width = screenWidth / 5;
                layout.setNumColumns(5);
            } else {
                width = screenWidth / 3;
                layout.setNumColumns(3);
            }
            dp2=(int)DeviceHelper.pxFromDp(getActivity(), 2);
            options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).cacheInMemory(true).cacheOnDisk(false).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).bitmapConfig(Bitmap.Config.RGB_565).build();
            files = ViewPointActivity.poi.picFiles;
            if (files == null) {
                files = new File[0];
            }
            bitmaps = new Bitmap[files.length];
        }

        public void reFresh() {
            ViewPointActivity.poi.updateAllFields();
            files = ViewPointActivity.poi.picFiles;
            bitmaps = new Bitmap[files.length];
            notifyDataSetChanged();

        }

        public int getCount() {

            return files.length;
        }

        public Object getItem(int position) {

            return files[position];
        }

        public long getItemId(int position) {

            return position;
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                ImageView i = new ImageView(getActivity());
                i.setMaxWidth(width);
                i.setMaxHeight(width);
                i.setScaleType(ImageView.ScaleType.CENTER_CROP);
                CheckableLayout l = new CheckableLayout(getActivity());
                l.setLayoutParams(new AbsListView.LayoutParams(width, width));
                l.setPadding(dp2, dp2, dp2, dp2);
                l.addView(i);
                convertView = l;
                convertView.setTag(i);
            }
            if (bitmaps[position] == null) {
                ImageLoader.getInstance().displayImage("file://" + files[position].getPath(), (ImageView) convertView.getTag(), options, new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        bitmaps[position] = loadedImage;
                    }
                });
            } else {
                ((ImageView) convertView.getTag()).setImageBitmap(bitmaps[position]);
            }
            return convertView;
        }

    }

    class MyMultiChoiceModeListener implements MultiChoiceModeListener {
        boolean[] checks;
        boolean checkAll;

        // ShareActionProvider shareProvider;

        public MyMultiChoiceModeListener() {

        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            final ArrayList<File> checksName = new ArrayList<File>();
            for (int i = 0; i < checks.length; i++) {
                if (checks[i]) {
                    checksName.add((File) adapter.getItem(i));
                }
            }
            if (item.getItemId() == R.id.delete) {
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle(getString(R.string.be_careful));
                ab.setMessage(getString(R.string.are_you_sure_to_delete));
                ab.setIcon(R.drawable.ic_alert);
                ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < checksName.size(); i++) {
                            checksName.get(i).delete();
                        }
                        adapter.reFresh();
                        ViewPointActivity.requestUpdatePOI();
                    }
                });
                ab.setNegativeButton(getString(R.string.cancel), null);
                ab.show();
                mode.finish();
            } else if (item.getItemId() == R.id.rename) {
                if (checksName.size() == 1) {
                    final File checkFile = checksName.get(0);
                    AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity());
                    ab2.setTitle(getString(R.string.filename));
                    final EditText name = new EditText(getActivity());
                    name.setText(checkFile.getName());
                    ab2.setView(name);
                    ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            String s = name.getText().toString();
                            checkFile.renameTo(new File(checkFile.getParent() + "/" + s));
                            adapter.reFresh();
                            ViewPointActivity.requestUpdatePOI();
                        }
                    });
                    ab2.show();
                    mode.finish();
                }
            } else if (item.getItemId() == R.id.selectall) {
                for (int i = 0; i < layout.getCount(); i++) {
                    layout.setItemChecked(i, !checkAll);
                }
                checkAll = !checkAll;
            } else if (item.getItemId() == R.id.print) {
                final File picFile = checksName.get(0);
                final String fileName = picFile.getName();
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                final EditText editText = new EditText(getActivity());
                editText.setText(fileName.substring(0, fileName.lastIndexOf(".")));
                ab.setView(editText);
                ab.setTitle(R.string.filename);
                ab.setPositiveButton(R.string.enter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        PrintHelper photoPrinter = new PrintHelper(getActivity());
                        photoPrinter.setScaleMode(PrintHelper.SCALE_MODE_FIT);
                        Bitmap bitmap = BitmapFactory.decodeFile(picFile.getPath());
                        photoPrinter.printBitmap(editText.getText().toString(), bitmap);
                    }
                });
                ab.setNegativeButton(R.string.cancel, null);
                ab.show();
            } else if (item.getItemId() == R.id.share) {
                ArrayList<Uri> uris = new ArrayList<Uri>();
                for (int i = 0; i < checks.length; i++) {
                    if (checks[i]) {
                        uris.add(Uri.fromFile((File) adapter.getItem(i)));
                    }
                }
                Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("image/*");
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                startActivity(intent);
            } else if (item.getItemId() == R.id.move) {
                final File tripFile = ViewPointActivity.poi.dir.getParentFile();
                final String[] pois = tripFile.list(new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String filename) {

                        if (filename.equals(ViewPointActivity.poi.dir.getName())) {
                            return false;
                        }
                        if (new File(dir, filename).isFile()) {
                            return false;
                        }
                        return true;
                    }
                });
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle(R.string.move_to);
                ab.setItems(pois, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        File[] fromFiles = checksName.toArray(new File[checksName.size()]);
                        File[] toFiles = new File[checksName.size()];
                        for (int i = 0; i < toFiles.length; i++) {
                            toFiles[i] = new File(tripFile.getPath() + "/" + pois[which] + "/pictures/" + checksName.get(i).getName());
                        }
                        new FileHelper.MoveFilesTask(getActivity(), fromFiles, toFiles, new OnFinishedListener() {

                            @Override
                            public void onFinish() {
                                adapter.reFresh();
                                ViewPointActivity.requestUpdatePOI();
                            }
                        }).execute();

                    }
                });
                ab.show();
                mode.finish();
            }
            return true;
        }

        public boolean onCreateActionMode(final ActionMode mode, Menu menu) {

            mode.getMenuInflater().inflate(R.menu.poi_menu, menu);
            checks = new boolean[adapter.getCount()];
            for (int i = 0; i < checks.length; i++) {
                checks[i] = false;
            }
            checkAll = false;
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {

            mode = null;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return true;
        }

        public void onItemCheckedStateChanged(final ActionMode mode, int position, long id, boolean checked) {

            checks[position] = checked;
            int selects = 0;
            for (int i = 0; i < checks.length; i++) {
                if (checks[i]) {
                    selects++;
                }
            }
            mode.getMenu().findItem(R.id.rename).setVisible(!(selects > 1));
            mode.getMenu().findItem(R.id.print).setVisible(!(selects > 1));

        }

    }

    public void onItemClick(AdapterView<?> av, View view, int position, long id) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile((File) adapter.getItem(position)), "image/*");
        getActivity().startActivity(intent);
    }

}
