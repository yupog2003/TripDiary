package com.yupog2003.tripdiary;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.yupog2003.tripdiary.data.FileHelper;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MultiFileChooseActivity extends MyActivity implements OnClickListener {
    public FileAdapter adapter;
    public ArrayList<String> choosedFiles;
    public ListView listView;
    public TextView currentDir;
    public Button ok;
    public Button cancel;
    public Button selectAll;
    public Button upDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi_file_choose);
        ok = (Button) findViewById(R.id.ok);
        ok.setOnClickListener(this);
        cancel = (Button) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        selectAll = (Button) findViewById(R.id.selectall);
        selectAll.setOnClickListener(this);
        upDir = (Button) findViewById(R.id.up);
        upDir.setOnClickListener(this);
        currentDir = (TextView) findViewById(R.id.currentDir);
        listView = (ListView) findViewById(R.id.listView);
        choosedFiles = new ArrayList<String>();
        adapter = new FileAdapter(new File(getIntent().getStringExtra("root")), FileHelper.getNoStartWithDotFilter());
        currentDir.setText(adapter.dir.getPath());
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
            if (currentDir != null) {
                currentDir.setText(dir.getPath());
            }
            notifyDataSetChanged();
        }

        public void selectAll() {
            if (filedatas != null) {
                for (int i = 0; i < filedatas.length; i++) {
                    filedatas[i].checkBox.setChecked(!isSelectAll);
                    filedatas[i].isSelected = !isSelectAll;
                }
                isSelectAll = !isSelectAll;
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
                this.layout = (LinearLayout) getLayoutInflater().inflate(R.layout.multifile_chooser_list_item, null);
                this.textView = (TextView) layout.findViewById(R.id.filename);
                this.checkBox = (CheckBox) layout.findViewById(R.id.checkbox);
                this.isSelected = choosedFiles.contains(file.getPath());
                checkBox.setChecked(isSelected);
                checkBox.setOnCheckedChangeListener(this);
                textView.setText(file.getName());
                if (file.isDirectory()) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_folder, 0, 0, 0);
                } else if (FileHelper.isPicture(file)) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_picture, 0, 0, 0);
                } else if (FileHelper.isVideo(file)) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_takevideo, 0, 0, 0);
                } else if (FileHelper.isAudio(file)) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_takeaudio, 0, 0, 0);
                } else {
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_file, 0, 0, 0);
                    checkBox.setVisibility(View.GONE);
                }
                textView.setOnClickListener(this);
            }

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    addFile(file);
                } else if (choosedFiles.contains(file)) {
                    removeFile(file);
                }
            }

            @Override
            public void onClick(View v) {
                if (file.isDirectory()) {
                    setDir(file, filter);
                } else if (FileHelper.isPicture(file)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "image/*");
                    startActivity(intent);
                } else if (FileHelper.isVideo(file)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "video/*");
                    startActivity(intent);
                } else if (FileHelper.isAudio(file)) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(file), "audio/*");
                    startActivity(intent);
                }
            }
        }

        private void addFile(File file) {
            if (file.isDirectory() && !choosedFiles.contains(file.getPath())) {
                choosedFiles.add(file.getPath());
                File[] files = file.listFiles(filter);
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        addFile(files[i]);
                    }
                }
            } else if (FileHelper.isMemory(file) && !choosedFiles.contains(file.getPath())) {
                choosedFiles.add(file.getPath());
            }
        }

        private void removeFile(File file) {
            if (file.isDirectory() && choosedFiles.contains(file.getPath())) {
                choosedFiles.remove(file.getPath());
                File[] files = file.listFiles(filter);
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        removeFile(files[i]);
                    }
                }
            } else if (choosedFiles.contains(file.getPath())) {
                choosedFiles.remove(file.getPath());
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
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("files", choosedFiles);
            Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        } else if (v.equals(cancel)) {
            finish();
        } else if (v.equals(selectAll)) {
            adapter.selectAll();
        } else if (v.equals(upDir)) {
            adapter.upDir();
        }
    }
}
