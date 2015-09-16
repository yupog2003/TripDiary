package com.yupog2003.tripdiary;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.DrawableHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.MyImageDownloader;
import com.yupog2003.tripdiary.data.MyImageViewAware;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.views.CheckableLayout;
import com.yupog2003.tripdiary.views.SquareCheckableLayout;
import com.yupog2003.tripdiary.views.SquareImageView;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;
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
    private boolean allowMultiple;
    private GridView gridView;
    private Button save;
    private int gridWidth;
    private MemoryAdapter adapter;
    Drawable folderDrawable;
    Drawable pictureDrawable;
    Drawable videoDrawable;
    Drawable audioDrawable;
    Drawable poiDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_content);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolBar);
        allowMultiple = false;
        String intentAction = getIntent().getAction();
        switch (intentAction) {
            case Intent.ACTION_GET_CONTENT:
                action = Action.get_content;
                if (Build.VERSION.SDK_INT >= 18) {
                    allowMultiple = getIntent().getBooleanExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
                }
                break;
            case Intent.ACTION_SEND:
                action = Action.send;
                break;
            case Intent.ACTION_SEND_MULTIPLE:
                action = Action.send_multiple;
                break;
        }
        String mimeType = getIntent().getType();
        if (mimeType.startsWith("image")) {
            type = Type.picture;
        } else if (mimeType.startsWith("video")) {
            type = Type.video;
        } else if (mimeType.startsWith("audio")) {
            type = Type.audio;
        } else {
            type = Type.other;
        }
        folderDrawable = DrawableHelper.getAccentTintDrawable(this, R.drawable.ic_folder);
        pictureDrawable = DrawableHelper.getAccentTintDrawable(this, R.drawable.ic_picture);
        videoDrawable = DrawableHelper.getAccentTintDrawable(this, R.drawable.ic_takevideo);
        audioDrawable = DrawableHelper.getAccentTintDrawable(this, R.drawable.ic_music);
        poiDrawable = DrawableHelper.getAccentTintDrawable(this, R.drawable.poi);
        save = (Button) findViewById(R.id.save);
        if (action == Action.get_content) {
            save.setVisibility(View.GONE);
        }
        gridView = (GridView) findViewById(R.id.gridView);
        checkHasPermission(new OnGrantPermissionCompletedListener() {
            @Override
            public void onGranted() {
                if (TripDiaryApplication.rootDocumentFile == null) {
                    TripDiaryApplication.updateRootPath();
                }
                save.setOnClickListener(GetContentActivity.this);
                adapter = new MemoryAdapter(type);
                gridView.setAdapter(adapter);
                gridView.setOnItemClickListener(adapter);
                if (allowMultiple) {
                    gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
                    gridView.setMultiChoiceModeListener(adapter);
                }
                adapter.setDir("/");
            }

            @Override
            public void onFailed() {
                finishAndRemoveTask();
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE);
    }

    class MemoryAdapter extends BaseAdapter implements AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {

        Type type;
        String nowDir; // /category/trip/poi
        int nowLevel;
        ArrayList<String> displayNames;
        SharedPreferences categorysp;
        SharedPreferences tripsp;
        POI[] pois;
        DocumentFile nowDirFile;
        int lastPictureIndex;
        int lastVideoIndex;
        int lastAudioIndex;
        DisplayImageOptions options;
        boolean onMultiChoiceMode;
        boolean[] checks;
        boolean checkAll;

        public MemoryAdapter(Type type) {
            this.type = type;
            if (type == Type.picture || type == Type.video || type == Type.other) {
                ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(GetContentActivity.this).imageDownloader(new MyImageDownloader(getActivity())).build();
                ImageLoader.getInstance().init(conf);
            }
            this.categorysp = getSharedPreferences("category", MODE_PRIVATE);
            this.tripsp = getSharedPreferences("trip", MODE_PRIVATE);
            this.displayNames = new ArrayList<>();
            this.lastPictureIndex = -1;
            this.lastVideoIndex = -1;
            this.lastAudioIndex = -1;
            this.onMultiChoiceMode = false;
        }

        public void setDir(String dir) {
            displayNames.clear();
            lastPictureIndex = -1;
            lastVideoIndex = -1;
            lastAudioIndex = -1;
            nowDir = dir;
            setTitle(dir);
            String[] levels = dir.split("/");
            nowLevel = levels.length;
            if (nowLevel > 1) nowLevel--;
            if (nowLevel == 0) { //root
                setGridView(false);
                save.setEnabled(false);
                gridView.setLongClickable(false);
                Set<String> categorySet = categorysp.getAll().keySet();
                String[] categories = categorySet.toArray(new String[categorySet.size()]);
                if (categories.length < 2) {
                    setDir("/" + getString(R.string.nocategory));
                } else {
                    displayNames.addAll(Arrays.asList(categories));
                    nowDir = "/";
                }
                nowDirFile = TripDiaryApplication.rootDocumentFile;
            } else if (nowLevel == 1) { //category
                save.setEnabled(false);
                gridView.setLongClickable(false);
                nowDirFile = TripDiaryApplication.rootDocumentFile;
                String[] tripNames = nowDirFile.listFileNames(DocumentFile.list_dirs);
                String category = levels[1];
                for (String tripName : tripNames) {
                    if (tripsp.getString(tripName, getString(R.string.nocategory)).equals(category)) {
                        displayNames.add(tripName);
                    }
                }
                Collections.sort(displayNames, new Comparator<String>() {
                    @Override
                    public int compare(String lhs, String rhs) {
                        MyCalendar time1 = MyCalendar.getTripTime(lhs);
                        MyCalendar time2 = MyCalendar.getTripTime(rhs);
                        if (time1.after(time2))
                            return 1;
                        else if (time2.after(time1))
                            return -1;
                        else
                            return 0;
                    }
                });
            } else if (nowLevel == 2) {//trip
                save.setEnabled(false);
                gridView.setLongClickable(false);
                setGridView(false);
                String tripName = levels[2];
                nowDirFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName);
                DocumentFile[] poiFiles = nowDirFile.listFiles(DocumentFile.list_dirs);
                pois = new POI[poiFiles.length];
                for (int i = 0; i < pois.length; i++) {
                    pois[i] = new POI(GetContentActivity.this, poiFiles[i], null);
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
                for (POI poi : pois) {
                    displayNames.add(poi.title);
                }
            } else if (nowLevel == 3) {//poi
                save.setEnabled(true);
                gridView.setLongClickable(allowMultiple);
                if (allowMultiple) {
                    Snackbar.make(findViewById(android.R.id.content), R.string.long_click_to_select_multiple_file, Snackbar.LENGTH_SHORT).show();
                }
                String tripName = levels[2];
                String poiName = levels[3];
                switch (type) {
                    case picture:
                        setGridView(true);
                        nowDirFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, poiName, "pictures");
                        String[] pictureNames = nowDirFile.listFileNames(DocumentFile.list_pics);
                        displayNames.addAll(Arrays.asList(pictureNames));
                        options = new DisplayImageOptions.Builder()
                                .cacheInMemory(true)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                                .extraForDownloader(nowDirFile.listFiles(DocumentFile.list_pics))
                                .showImageOnLoading(new ColorDrawable(Color.LTGRAY))
                                .showImageOnFail(DrawableHelper.getAccentTintDrawable(getActivity(), R.drawable.ic_error))
                                .build();
                        break;
                    case video:
                        setGridView(true);
                        nowDirFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, poiName, "videos");
                        String[] videoNames = nowDirFile.listFileNames(DocumentFile.list_videos);
                        displayNames.addAll(Arrays.asList(videoNames));
                        options = new DisplayImageOptions.Builder()
                                .cacheInMemory(true)
                                .bitmapConfig(Bitmap.Config.RGB_565)
                                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                                .extraForDownloader(nowDirFile.listFiles(DocumentFile.list_videos))
                                .showImageOnLoading(new ColorDrawable(Color.LTGRAY))
                                .showImageOnFail(DrawableHelper.getAccentTintDrawable(getActivity(), R.drawable.ic_error))
                                .build();
                        break;
                    case audio:
                        setGridView(false);
                        nowDirFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, poiName, "audios");
                        String[] audioNames = nowDirFile.listFileNames(DocumentFile.list_audios);
                        displayNames.addAll(Arrays.asList(audioNames));
                        break;
                    case other:
                        setGridView(false);
                        nowDirFile = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, poiName);
                        String[] pictureNames2 = FileHelper.findfile(nowDirFile, "pictures").listFileNames(DocumentFile.list_pics);
                        displayNames.addAll(Arrays.asList(pictureNames2));
                        lastPictureIndex = displayNames.size();
                        String[] videoNames2 = FileHelper.findfile(nowDirFile, "videos").listFileNames(DocumentFile.list_videos);
                        displayNames.addAll(Arrays.asList(videoNames2));
                        lastVideoIndex = displayNames.size();
                        String[] audioNames2 = FileHelper.findfile(nowDirFile, "audios").listFileNames(DocumentFile.list_audios);
                        displayNames.addAll(Arrays.asList(audioNames2));
                        lastAudioIndex = displayNames.size();
                        break;
                }
            }
            notifyDataSetChanged();
        }

        public void setGridView(boolean open) {
            if (open) {
                int[] numColumnsAndWidth = new int[2];
                DeviceHelper.getNumColumnsAndWidth(getActivity(), numColumnsAndWidth);
                gridWidth = numColumnsAndWidth[1];
                gridView.setNumColumns(numColumnsAndWidth[0]);
            } else {
                gridWidth = gridView.getWidth();
                gridView.setNumColumns(1);
            }
        }

        @Override
        public int getCount() {
            return displayNames.size();
        }

        @Override
        public Object getItem(int position) {
            switch (nowLevel) {
                case 0:
                    return displayNames.get(position);//category name
                case 1:
                    String tripName = displayNames.get(position);
                    return FileHelper.findfile(nowDirFile, tripName); // trip file
                case 2:
                    String poiName = displayNames.get(position);
                    return FileHelper.findfile(nowDirFile, poiName);//poi file
                case 3:
                    String memoryName = displayNames.get(position);
                    if (type == Type.picture) {
                        return FileHelper.findfile(nowDirFile, memoryName); //picture file
                    } else if (type == Type.video) {
                        return FileHelper.findfile(nowDirFile, memoryName); //video file
                    } else if (type == Type.audio) {
                        return FileHelper.findfile(nowDirFile, memoryName); //audio file
                    } else if (type == Type.other) {
                        if (position >= 0 && position < lastPictureIndex) { //is picture
                            return FileHelper.findfile(nowDirFile, "pictures", memoryName);
                        } else if (position >= lastPictureIndex && position < lastVideoIndex) { //is video
                            return FileHelper.findfile(nowDirFile, "videos", memoryName);
                        } else if (position >= lastVideoIndex && position < lastAudioIndex) { //is audio
                            return FileHelper.findfile(nowDirFile, "audios", memoryName);
                        }
                    }
            }
            return displayNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (nowLevel == 3 && (type == Type.picture || type == Type.video)) {
                SquareImageView imageView = new SquareImageView(GetContentActivity.this);
                imageView.setMaxWidth(gridWidth);
                imageView.setMaxHeight(gridWidth);
                imageView.setScaleType(ImageView.ScaleType.CENTER);
                convertView = imageView;
                ImageLoader.getInstance().displayImage(displayNames.get(position), new MyImageViewAware((ImageView) convertView), options);
            } else {
                TextView textView = new TextView(GetContentActivity.this);
                if (nowLevel == 3 && type == Type.audio) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(audioDrawable, null, null, null);
                } else if (nowLevel == 3 && type == Type.other) {
                    if (position >= 0 && position < lastPictureIndex) { //is picture
                        textView.setCompoundDrawablesWithIntrinsicBounds(pictureDrawable, null, null, null);
                    } else if (position >= lastPictureIndex && position < lastVideoIndex) { //is video
                        textView.setCompoundDrawablesWithIntrinsicBounds(videoDrawable, null, null, null);
                    } else if (position >= lastVideoIndex && position < lastAudioIndex) { //is audio
                        textView.setCompoundDrawablesWithIntrinsicBounds(audioDrawable, null, null, null);
                    }
                } else if (nowLevel == 2) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(poiDrawable, null, null, null);
                } else if (nowLevel == 1) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_launcher, 0, 0, 0);
                } else if (nowLevel == 0) {
                    textView.setCompoundDrawablesWithIntrinsicBounds(folderDrawable, null, null, null);
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
                    textView.setText(displayNames.get(position) + "(" + String.valueOf(num_files) + ")");
                } else {
                    textView.setText(displayNames.get(position));
                }
                convertView = textView;
            }
            if (nowLevel == 3 && allowMultiple) {
                CheckableLayout l;
                if (type == Type.picture || type == Type.video) {
                    l = new SquareCheckableLayout(getActivity());
                    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                    convertView.setLayoutParams(params);
                } else {
                    l = new CheckableLayout(getActivity());
                }
                l.addView(convertView);
                l.setOnMultiChoiceMode(onMultiChoiceMode);
                return l;
            }
            return convertView;
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (action == Action.get_content && nowLevel == 3) {
                DocumentFile file = (DocumentFile) getItem(position);
                if (allowMultiple && Build.VERSION.SDK_INT >= 16) {
                    String[] mimeTypes = new String[]{file.getType()};
                    ClipData.Item item = new ClipData.Item(file.getUri());
                    ClipData clipData = new ClipData(GetContentActivity.class.getName(), mimeTypes, item);
                    Intent intent = new Intent();
                    intent.setClipData(clipData);
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                } else {
                    Intent intent = new Intent();
                    intent.setData(file.getUri());
                    intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    setResult(Activity.RESULT_OK, intent);
                    finish();
                }
            } else if (nowLevel == 0) {
                setDir(nowDir + displayNames.get(position));
            } else {
                setDir(nowDir + "/" + displayNames.get(position));
            }
        }

        @Override
        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
            checks[position] = checked;
        }

        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.getMenuInflater().inflate(R.menu.activity_get_content, menu);
            onMultiChoiceMode = true;
            checks = new boolean[getCount()];
            Arrays.fill(checks, false);
            checkAll = false;
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return true;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            final ArrayList<DocumentFile> checksFile = new ArrayList<>();
            for (int i = 0; i < checks.length; i++) {
                if (checks[i]) {
                    checksFile.add((DocumentFile) adapter.getItem(i));
                }
            }
            if (item.getItemId() == R.id.selectall) {
                for (int i = 0; i < gridView.getCount(); i++) {
                    gridView.setItemChecked(i, !checkAll);
                }
                checkAll = !checkAll;
            } else if (item.getItemId() == R.id.open) {
                if (checksFile.size() < 1) {
                    finish();
                    return true;
                }
                ArrayList<String> mimeTypeList = new ArrayList<>();
                for (DocumentFile file : checksFile) {
                    mimeTypeList.add(file.getType());
                }
                String[] mimeTypes = mimeTypeList.toArray(new String[mimeTypeList.size()]);
                ClipData clipData = new ClipData(GetContentActivity.class.getName(), mimeTypes, new ClipData.Item(checksFile.get(0).getUri()));
                for (int i = 1; i < checksFile.size(); i++) {
                    clipData.addItem(new ClipData.Item(checksFile.get(i).getUri()));
                }
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= 16) {
                    intent.setClipData(clipData);
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            onMultiChoiceMode = false;
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
                new MoveFilesTask(GetContentActivity.this, new Uri[]{uri}, new DocumentFile[]{getToFile(uri)}).execute();
            } else if (action == Action.send_multiple) {
                ArrayList<Uri> uris = getIntent().getParcelableArrayListExtra(Intent.EXTRA_STREAM);
                if (uris != null) {
                    Uri[] uriArray = new Uri[uris.size()];
                    DocumentFile[] toFiles = new DocumentFile[uris.size()];
                    for (int i = 0; i < uris.size(); i++) {
                        uriArray[i] = uris.get(i);
                        toFiles[i] = getToFile(uris.get(i));
                    }
                    new MoveFilesTask(GetContentActivity.this, uriArray, toFiles).execute();
                }
            }
        }
    }

    private DocumentFile getToFile(Uri uri) {
        if (uri != null) {
            String nowDir = adapter.nowDir;
            String poiName = nowDir.split("/")[3];
            String tripName = nowDir.split("/")[2];
            String memoryName = FileHelper.getRealNameFromURI(this, uri);
            DocumentFile toDir = null;
            if (type == Type.picture) {
                toDir = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, poiName, "pictures");
            } else if (type == Type.video) {
                toDir = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, poiName, "videos");
            } else if (type == Type.audio) {
                toDir = FileHelper.findfile(TripDiaryApplication.rootDocumentFile, tripName, poiName, "audios");
            }
            if (toDir != null) {
                DocumentFile toFile = FileHelper.findfile(toDir, memoryName);
                if (toFile == null) {
                    toFile = toDir.createFile("", memoryName);
                }
                return toFile;
            }
        }
        return null;
    }

    class MoveFilesTask extends AsyncTask<String, String, String> {

        Activity activity;
        Uri[] fromUris;
        DocumentFile[] toFiles;
        TextView message;
        ProgressBar progress;
        TextView progressMessage;
        AlertDialog dialog;
        boolean cancel = false;

        public MoveFilesTask(Activity activity, Uri[] fromUris, DocumentFile[] toFiles) {
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
            LinearLayout layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.progressdialog_import_memory, (ViewGroup) findViewById(android.R.id.content), false);
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
                    if (fromUris[i] == null || toFiles[i] == null)
                        continue;
                    publishProgress(toFiles[i].getName(), String.valueOf(i));
                    if (fromUris[i].getPath().equals(toFiles[i].getUri().getPath()))
                        continue;
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(fromUris[i]);
                        OutputStream outputStream = toFiles[i].getOutputStream();
                        FileHelper.copyByStream(inputStream, outputStream);
                    } catch (FileNotFoundException | NullPointerException | IllegalArgumentException e) {
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
