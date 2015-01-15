package com.yupog2003.tripdiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.OpenableColumns;
import android.support.v7.widget.Toolbar;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.decode.ImageDecoder;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyImageDecoder;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.TimeAnalyzer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

public class GetContentActivity extends MyActivity implements View.OnClickListener {

    private enum Type {picture, video, audio, other}

    private enum Action {get_content, send, send_multiple}

    private Type type;
    private Action action;
    private GridView gridView;
    private Toolbar toolBar;
    private Button save;
    private int gridWidth;
    private MemoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_content);
        toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        String intentAction = getIntent().getAction();
        Log.i("trip", intentAction);
        if (intentAction.equals(Intent.ACTION_GET_CONTENT)) {
            action = Action.get_content;
        } else if (intentAction.equals(Intent.ACTION_SEND)) {
            action = Action.send;
        } else if (intentAction.equals(Intent.ACTION_SEND_MULTIPLE)) {
            action = Action.send_multiple;
        }
        String mimeType = getIntent().getType();
        Log.i("trip", mimeType);
        if (mimeType.startsWith("image")) {
            type = Type.picture;
        } else if (mimeType.startsWith("video")) {
            type = Type.video;
        } else if (mimeType.startsWith("audio")) {
            type = Type.audio;
        } else {
            type = Type.other;
        }

        save = (Button) findViewById(R.id.save);
        if (action == Action.get_content) {
            save.setVisibility(View.GONE);
        }
        save.setOnClickListener(this);
        gridView = (GridView) findViewById(R.id.gridView);
        adapter = new MemoryAdapter(type);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_get_content, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    class MemoryAdapter extends BaseAdapter implements AdapterView.OnItemClickListener {

        Type type;
        String rootPath;
        String nowDir; // /category/trip/poi
        int nowLevel;
        ArrayList<String> files;
        SharedPreferences categorysp;
        SharedPreferences tripsp;
        DisplayImageOptions options;
        POI[] pois;
        int lastPictureIndex;
        int lastVideoIndex;
        int lastAudioIndex;

        public MemoryAdapter(Type type) {
            this.type = type;
            if (type == Type.picture || type == Type.video || type == Type.other) {
                ImageDecoder myImageDecoder = new MyImageDecoder(getApplicationContext(), new BaseImageDecoder(false));
                ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(GetContentActivity.this).imageDecoder(myImageDecoder).build();
                ImageLoader.getInstance().init(conf);
                options = new DisplayImageOptions.Builder().displayer(new FadeInBitmapDisplayer(500)).cacheInMemory(true).cacheOnDisk(false).bitmapConfig(Bitmap.Config.RGB_565).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
            }
            this.categorysp = getSharedPreferences("category", MODE_PRIVATE);
            this.tripsp = getSharedPreferences("trip", MODE_PRIVATE);
            this.rootPath = PreferenceManager.getDefaultSharedPreferences(GetContentActivity.this).getString("rootpath", Environment.getExternalStorageDirectory() + "/TripDiary");
            this.files = new ArrayList<>();
            this.lastPictureIndex = -1;
            this.lastVideoIndex = -1;
            this.lastAudioIndex = -1;
            setDir("/");
        }

        public void setDir(String dir) {
            files.clear();
            lastPictureIndex = -1;
            lastVideoIndex = -1;
            lastAudioIndex = -1;
            nowDir = dir;
            setTitle(dir);
            String[] levels = dir.split("/");
            int levelLength = levels.length;
            if (levelLength > 1) levelLength--;
            nowLevel = levelLength;
            if (levelLength == 0) { //root
                setGridView(false);
                save.setEnabled(false);
                Set<String> categorySet = categorysp.getAll().keySet();
                String[] categories = categorySet.toArray(new String[categorySet.size()]);
                if (categories.length < 2) {
                    setDir("/" + getString(R.string.nocategory));
                } else {
                    files.addAll(Arrays.asList(categories));
                    nowDir = "/";
                }
            } else if (levelLength == 1) { //category
                save.setEnabled(false);
                String[] tripNames = new File(rootPath).list(FileHelper.getDirFilter());
                String category = levels[1];
                for (String tripName : tripNames) {
                    if (tripsp.getString(tripName, getString(R.string.nocategory)).equals(category)) {
                        files.add(tripName);
                    }
                }
                Collections.sort(files, new Comparator<String>() {
                    @Override
                    public int compare(String lhs, String rhs) {
                        Time time1 = TimeAnalyzer.getTripTime(rootPath, lhs);
                        Time time2 = TimeAnalyzer.getTripTime(rootPath, rhs);
                        if (time1 == null || time2 == null)
                            return 0;
                        else if (time1.after(time2))
                            return 1;
                        else if (time2.after(time1))
                            return -1;
                        else
                            return 0;
                    }
                });
            } else if (levelLength == 2) {//trip
                save.setEnabled(false);
                setGridView(false);
                String tripName = levels[2];
                File[] poiFiles = new File(rootPath + "/" + tripName).listFiles(FileHelper.getDirFilter());
                pois = new POI[poiFiles.length];
                for (int i = 0; i < pois.length; i++) {
                    pois[i] = new POI(poiFiles[i]);
                }
                Arrays.sort(pois, new Comparator<POI>() {
                    @Override
                    public int compare(POI lhs, POI rhs) {
                        if (lhs.time.after(rhs.time)) {
                            return 1;
                        } else if (rhs.time.after(lhs.time)) {
                            return -1;
                        } else {
                            return 0;
                        }
                    }
                });
                String[] poiNames = new String[pois.length];
                for (int i = 0; i < poiNames.length; i++) {
                    poiNames[i] = pois[i].title;
                }
                files.addAll(Arrays.asList(poiNames));
            } else if (levelLength == 3) {//poi
                save.setEnabled(true);
                String tripName = levels[2];
                String poiName = levels[3];
                switch (type) {
                    case picture:
                        setGridView(true);
                        files.addAll(Arrays.asList(new File(rootPath + "/" + tripName + "/" + poiName + "/pictures").list()));
                        break;
                    case video:
                        setGridView(true);
                        files.addAll(Arrays.asList(new File(rootPath + "/" + tripName + "/" + poiName + "/videos").list()));
                        break;
                    case audio:
                        setGridView(false);
                        files.addAll(Arrays.asList(new File(rootPath + "/" + tripName + "/" + poiName + "/audios").list()));
                        break;
                    case other:
                        setGridView(false);
                        files.addAll(Arrays.asList(new File(rootPath + "/" + tripName + "/" + poiName + "/pictures").list()));
                        lastPictureIndex = files.size();
                        files.addAll(Arrays.asList(new File(rootPath + "/" + tripName + "/" + poiName + "/videos").list()));
                        lastVideoIndex = files.size();
                        files.addAll(Arrays.asList(new File(rootPath + "/" + tripName + "/" + poiName + "/audios").list()));
                        lastAudioIndex = files.size();
                        break;
                }
            }
            notifyDataSetChanged();
        }

        public void setGridView(boolean open) {
            if (open) {
                int screenWidth = DeviceHelper.getScreenWidth(getActivity());
                int screenHeight = DeviceHelper.getScreenHeight(getActivity());
                if (screenWidth > screenHeight) {
                    gridWidth = screenWidth / 5;
                    gridView.setNumColumns(5);
                } else {
                    gridWidth = screenWidth / 3;
                    gridView.setNumColumns(3);
                }
            } else {
                gridWidth = gridView.getWidth();
                gridView.setNumColumns(1);
            }

        }

        @Override
        public int getCount() {
            return files.size();
        }

        @Override
        public Object getItem(int position) {
            switch (nowLevel) {
                case 0:
                    return files.get(position);//category name
                case 1:
                    String tripName = files.get(position);
                    return new File(rootPath + "/" + tripName);// trip file
                case 2:
                    String poiName = files.get(position);
                    String tripName2 = nowDir.split("/")[2];
                    return new File(rootPath + "/" + tripName2 + "/" + poiName);//poi file
                case 3:
                    String poiName2 = nowDir.split("/")[3];
                    String tripName3 = nowDir.split("/")[2];
                    String memoryName = files.get(position);
                    if (type == Type.picture) {
                        return new File(rootPath + "/" + tripName3 + "/" + poiName2 + "/pictures/" + memoryName); //picture file
                    } else if (type == Type.video) {
                        return new File(rootPath + "/" + tripName3 + "/" + poiName2 + "/videos/" + memoryName); //video file
                    } else if (type == Type.audio) {
                        return new File(rootPath + "/" + tripName3 + "/" + poiName2 + "/audios/" + memoryName); //audio file
                    } else if (type == Type.other) {
                        if (position >= 0 && position < lastPictureIndex) { //is picture
                            return new File(rootPath + "/" + tripName3 + "/" + poiName2 + "/pictures/" + memoryName);
                        } else if (position >= lastPictureIndex && position < lastVideoIndex) { //is video
                            return new File(rootPath + "/" + tripName3 + "/" + poiName2 + "/videos/" + memoryName);
                        } else if (position >= lastVideoIndex && position < lastAudioIndex) { //is audio
                            return new File(rootPath + "/" + tripName3 + "/" + poiName2 + "/audios/" + memoryName);
                        }
                    }
            }
            return files.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (nowLevel == 3 && (type == Type.picture || type == Type.video)) {
                ImageView imageView = new ImageView(GetContentActivity.this);
                imageView.setLayoutParams(new ViewGroup.LayoutParams(gridWidth, gridWidth));
                imageView.setMaxWidth(gridWidth);
                imageView.setMaxHeight(gridWidth);
                convertView = imageView;
                String memoryPath = ((File) getItem(position)).getPath();
                ImageLoader.getInstance().displayImage("file://" + memoryPath, (ImageView) convertView, options);
            } else {
                TextView textView = new TextView(GetContentActivity.this);
                if (nowLevel == 3 && type == Type.audio) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_music, 0, 0, 0);
                } else if (nowLevel == 3 && type == Type.other) {
                    if (position >= 0 && position < lastPictureIndex) { //is picture
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_picture, 0, 0, 0);
                    } else if (position >= lastPictureIndex && position < lastVideoIndex) { //is video
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_takevideo, 0, 0, 0);
                    } else if (position >= lastVideoIndex && position < lastAudioIndex) { //is audio
                        textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_music, 0, 0, 0);
                    }
                } else if (nowLevel == 2) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.poi, 0, 0, 0);
                } else if (nowLevel == 1) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_launcher, 0, 0, 0);
                } else if (nowLevel == 0) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_folder, 0, 0, 0);
                }
                textView.setTextAppearance(GetContentActivity.this, android.R.style.TextAppearance_Large);
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setSingleLine(true);
                if (nowLevel == 2 && action == Action.get_content) {
                    int num_files = 0;
                    switch (type) {
                        case picture:
                            num_files = pois[position].picFiles.length;
                            break;
                        case video:
                            num_files = pois[position].videoFiles.length;
                            break;
                        case audio:
                            num_files = pois[position].audioFiles.length;
                            break;
                        case other:
                            num_files = pois[position].picFiles.length + pois[position].videoFiles.length + pois[position].audioFiles.length;
                            break;
                    }
                    textView.setText(files.get(position) + "(" + String.valueOf(num_files) + ")");
                } else {
                    textView.setText(files.get(position));
                }
                convertView = textView;
            }
            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (nowLevel == 3) {
                if (action == Action.get_content) {
                    Intent intent = new Intent();
                    intent.setData(Uri.fromFile((File) getItem(position)));
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } else if (nowLevel == 0) {
                setDir(nowDir + files.get(position));
            } else {
                setDir(nowDir + "/" + files.get(position));
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (adapter.nowLevel == 0) {
            finish();
        } else if (adapter.nowLevel == 1) {
            if (adapter.categorysp.getAll().keySet().size() < 2) {
                finish();
            } else {
                adapter.setDir("/");
            }
        } else {
            adapter.setDir(adapter.nowDir.substring(0, adapter.nowDir.lastIndexOf("/")));
        }
    }

    @Override
    public void onClick(View v) {
        if (v.equals(save)) {
            if (action == Action.send) {
                Uri uri = getIntent().getParcelableExtra(Intent.EXTRA_STREAM);
                new MoveFilesTask(GetContentActivity.this, new Uri[]{uri}, new File[]{getToFile(uri)}).execute();
            } else if (action == Action.send_multiple) {
                ArrayList<Uri> uris = getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                if (uris != null) {
                    Uri[] uriArray = new Uri[uris.size()];
                    File[] toFiles = new File[uris.size()];
                    for (int i = 0; i < uris.size(); i++) {
                        uriArray[i] = uris.get(i);
                        toFiles[i] = getToFile(uris.get(i));
                    }
                    new MoveFilesTask(GetContentActivity.this, uriArray, toFiles).execute();
                }

            }
        }
    }

    private File getToFile(Uri uri) {
        if (uri != null) {
            File toFile = null;
            String rootPath = adapter.rootPath;
            String nowDir = adapter.nowDir;
            String poiName = nowDir.split("/")[3];
            String tripName = nowDir.split("/")[2];
            String memoryName = FileHelper.getRealNameFromURI(this, uri);
            if (type == Type.picture) {
                toFile = new File(rootPath + "/" + tripName + "/" + poiName + "/pictures/" + memoryName); //picture file
            } else if (type == Type.video) {
                toFile = new File(rootPath + "/" + tripName + "/" + poiName + "/videos/" + memoryName); //video file
            } else if (type == Type.audio) {
                toFile = new File(rootPath + "/" + tripName + "/" + poiName + "/audios/" + memoryName); //audio file
            }
            return toFile;
        }
        return null;
    }

    class MoveFilesTask extends AsyncTask<String, String, String> {

        Activity activity;
        Uri[] fromUris;
        File[] toFiles;
        TextView message;
        ProgressBar progress;
        TextView progressMessage;
        AlertDialog dialog;
        boolean cancel = false;

        public MoveFilesTask(Activity activity, Uri[] fromUris, File[] toFiles) {
            this.activity = activity;
            if (fromUris != null && toFiles != null && fromUris.length == toFiles.length) {
                this.fromUris = fromUris;
                this.toFiles = toFiles;
            }
        }

        @Override
        protected void onPreExecute() {
            AlertDialog.Builder ab = new AlertDialog.Builder(activity);
            ab.setTitle(activity.getString(R.string.move_to));
            LinearLayout layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.progressdialog_import_memory, null);
            message = (TextView) layout.findViewById(R.id.message);
            progress = (ProgressBar) layout.findViewById(R.id.progressBar);
            progressMessage = (TextView) layout.findViewById(R.id.progress);
            ab.setView(layout);
            ab.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    cancel = true;
                }
            });
            dialog = ab.create();
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            if (fromUris != null && toFiles != null) {
                publishProgress("setMax", String.valueOf(Math.min(fromUris.length, toFiles.length)));
                for (int i = 0; i < Math.min(fromUris.length, toFiles.length); i++) {
                    if (cancel)
                        break;
                    publishProgress(toFiles[i].getName(), String.valueOf(i));
                    if (fromUris[i] == null || toFiles == null)
                        continue;
                    if (fromUris[i].getPath().equals(toFiles[i].getPath()))
                        continue;
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(fromUris[i]);
                        FileOutputStream fileOutputStream = new FileOutputStream(toFiles[i]);
                        FileHelper.copyByStream(inputStream, fileOutputStream);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }

                }
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {

            if (values[0].equals("setMax")) {
                progress.setMax(Integer.valueOf(values[1]));
                progressMessage.setText("0/" + values[1]);
            } else {
                message.setText(values[0]);
                progress.setProgress(Integer.valueOf(values[1]));
                progressMessage.setText(values[1] + "/" + String.valueOf(progress.getMax()));
            }
        }

        @Override
        protected void onPostExecute(String result) {

            dialog.dismiss();
            GetContentActivity.this.finish();
        }
    }
}
