package com.yupog2003.tripdiary;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.yupog2003.tripdiary.data.DrawableHelper;
import com.yupog2003.tripdiary.data.FileHelper;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MultiFileChooseActivity extends MyActivity implements OnClickListener {
    public FileAdapter adapter;
    public ArrayList<String> chosenFiles;
    public ListView listView;
    public Button ok;
    public Button cancel;
    public Button selectAll;
    public Button upDir;
    Drawable folderDrawable;
    Drawable pictureDrawable;
    Drawable videoDrawable;
    Drawable audioDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_file_choose);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        selectAll = (Button) findViewById(R.id.selectall);
        selectAll.setOnClickListener(this);
        upDir = (Button) findViewById(R.id.up);
        upDir.setOnClickListener(this);
        listView = (ListView) findViewById(R.id.listView);
        chosenFiles = new ArrayList<>();
        folderDrawable = DrawableHelper.getAccentTintDrawable(this, R.drawable.ic_folder);
        pictureDrawable = DrawableHelper.getAccentTintDrawable(this, R.drawable.ic_picture);
        videoDrawable = DrawableHelper.getAccentTintDrawable(this, R.drawable.ic_takevideo);
        audioDrawable = DrawableHelper.getAccentTintDrawable(this, R.drawable.ic_music);
        adapter = new FileAdapter(Environment.getExternalStorageDirectory(), FileHelper.getNoStartWithDotFilter());
        setTitle(adapter.dir.getPath());
        listView.setAdapter(adapter);
    }

    class FileAdapter extends BaseAdapter {

        File dir;
        File[] files;
        FileFilter filter;
        FileData[] filedatas;
        boolean isSelectAll;

        public FileAdapter(File root, FileFilter filter) {
            setDir(root, filter);
        }

        public void setDir(File root, FileFilter filter) {
            this.dir = root;
            this.filter = filter;
            this.isSelectAll = false;
            if (filter != null) {
                this.files = root.listFiles(filter);
            } else {
                this.files = root.listFiles();
            }
            if (files == null) {
                files = new File[0];
            }
            Arrays.sort(files, new Comparator<File>() {

                @Override
                public int compare(File lhs, File rhs) {
                    return lhs.getName().compareTo(rhs.getName());
                }
            });
            this.filedatas = new FileData[files.length];
            for (int i = 0; i < filedatas.length; i++) {
                filedatas[i] = new FileData(files[i]);
            }
            setTitle(dir.getPath());
            notifyDataSetChanged();
        }

        public void selectAll() {
            if (filedatas != null) {
                isSelectAll = !isSelectAll;
                for (FileData fileData : filedatas) {
                    fileData.checkBox.setChecked(isSelectAll);
                    fileData.isSelected = isSelectAll;
                }
                notifyDataSetChanged();
            }
        }

        public void upDir() {
            if (dir.getParentFile() != null) {
                setDir(dir.getParentFile(), filter);
            }
        }

        @Override
        public int getCount() {
            return filedatas.length;
        }

        @Override
        public Object getItem(int position) {
            return filedatas[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        class FileData implements OnCheckedChangeListener, android.view.View.OnClickListener {
            File file;
            LinearLayout layout;
            TextView textView;
            CheckBox checkBox;
            boolean isSelected;

            public FileData(File file) {
                this.file = file;
                this.layout = (LinearLayout) getLayoutInflater().inflate(R.layout.multifile_chooser_list_item, (ViewGroup) findViewById(android.R.id.content), false);
                this.textView = (TextView) layout.findViewById(R.id.filename);
                this.checkBox = (CheckBox) layout.findViewById(R.id.checkbox);
                this.isSelected = chosenFiles.contains(file.getPath());
                checkBox.setChecked(isSelected);
                checkBox.setOnCheckedChangeListener(this);
                textView.setText(file.getName());
                if (file.isDirectory()) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(folderDrawable, null, null, null);
                } else if (FileHelper.isPicture(file)) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(pictureDrawable, null, null, null);
                } else if (FileHelper.isVideo(file)) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(videoDrawable, null, null, null);
                } else if (FileHelper.isAudio(file)) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(audioDrawable, null, null, null);
                } else {
                    textView.setCompoundDrawablesWithIntrinsicBounds(folderDrawable, null, null, null);
                    checkBox.setVisibility(View.GONE);
                }
                textView.setOnClickListener(this);
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    addFile(file);
                } else if (chosenFiles.contains(file.getPath())) {
                    removeFile(file);
                }
            }

            @Override
            public void onClick(View v) {
                if (file.isDirectory()) {
                    setDir(file, filter);
                } else if (FileHelper.isPicture(file)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(FileProvider.getUriForFile(getActivity(), TripDiaryApplication.fileProviderAuthority, file), "image/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else if (FileHelper.isVideo(file)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(FileProvider.getUriForFile(getActivity(), TripDiaryApplication.fileProviderAuthority, file), "video/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                } else if (FileHelper.isAudio(file)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(FileProvider.getUriForFile(getActivity(), TripDiaryApplication.fileProviderAuthority, file), "audio/*");
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    startActivity(intent);
                }
            }
        }

        private void addFile(File file) {
            if (file.isDirectory() && !chosenFiles.contains(file.getPath())) {
                chosenFiles.add(file.getPath());
                File[] files = file.listFiles(filter);
                if (files != null) {
                    for (File file1 : files) {
                        addFile(file1);
                    }
                }
            } else if (FileHelper.isMemory(file) && !chosenFiles.contains(file.getPath())) {
                chosenFiles.add(file.getPath());
            }
        }

        private void removeFile(File file) {
            if (file.isDirectory() && chosenFiles.contains(file.getPath())) {
                chosenFiles.remove(file.getPath());
                File[] files = file.listFiles(filter);
                if (files != null) {
                    for (File file1 : files) {
                        removeFile(file1);
                    }
                }
            } else if (chosenFiles.contains(file.getPath())) {
                chosenFiles.remove(file.getPath());
            }
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            return filedatas[position].layout;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(ok)) {
            if (chosenFiles.size() < 1) {
                finish();
                return;
            }
            Intent intent = new Intent();
            ArrayList<Uri> uris = new ArrayList<>();
            for (String path : chosenFiles) {
                uris.add(FileProvider.getUriForFile(getActivity(), TripDiaryApplication.fileProviderAuthority, new File(path)));
            }
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else if (v.equals(cancel)) {
            finish();
        } else if (v.equals(selectAll)) {
            adapter.selectAll();
        } else if (v.equals(upDir)) {
            adapter.upDir();
        }
    }

    @Override
    public void onBackPressed() {
        if (adapter.dir.getParentFile() != null) {
            upDir.performClick();
        } else {
            super.onBackPressed();
        }
    }
}
