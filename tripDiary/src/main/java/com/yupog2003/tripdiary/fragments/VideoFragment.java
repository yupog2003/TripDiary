package com.yupog2003.tripdiary.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.provider.DocumentFile;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.FileHelper.MoveFilesTask.OnFinishedListener;
import com.yupog2003.tripdiary.views.CheckableLayout;
import com.yupog2003.tripdiary.views.SquareImageView;

import java.util.ArrayList;

public class VideoFragment extends Fragment implements OnItemClickListener {
    GridView layout;
    VideoAdapter adapter;
    int width;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_video, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setVideo();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        if (outState.isEmpty()) {
            outState.putBoolean("bug:fix", true);
        }
    }

    public void setVideo() {

        if (getView() == null)
            return;
        layout = (GridView) getView().findViewById(R.id.videogrid);
        ViewPointActivity.poi.updateAllFields();
        adapter = new VideoAdapter();
        layout.setAdapter(adapter);
        layout.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        layout.setMultiChoiceModeListener(new MyMultiChoiceModeListener());
        layout.setOnItemClickListener(this);
        int screenWidth = DeviceHelper.getScreenWidth(getActivity());
        int screenHeight = DeviceHelper.getScreenHeight(getActivity());
        if (screenWidth > screenHeight) {
            width = screenWidth / 5;
            layout.setNumColumns(5);
        } else {
            width = screenWidth / 3;
            layout.setNumColumns(3);
        }
    }

    class VideoAdapter extends BaseAdapter {
        DocumentFile[] videos;
        MediaMetadataRetriever mmr;
        DisplayImageOptions options;
        int dp2;
        boolean onMultiChoiceMode;

        public VideoAdapter() {
            videos = ViewPointActivity.poi.videoFiles;
            mmr = new MediaMetadataRetriever();
            options = new DisplayImageOptions.Builder()
                    .displayer(new FadeInBitmapDisplayer(500))
                    .showImageOnFail(R.drawable.ic_play)
                    .cacheInMemory(true)
                    .cacheOnDisk(false)
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .extraForDownloader(ViewPointActivity.poi.videoDir)
                    .build();
            dp2 = (int) DeviceHelper.pxFromDp(getActivity(), 2);
            onMultiChoiceMode = false;
        }

        public int getCount() {

            if (videos == null)
                return 0;
            return videos.length;
        }

        public Object getItem(int position) {

            return videos[position];
        }

        public long getItemId(int position) {

            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            SquareImageView image = new SquareImageView(getActivity());
            image.setMaxWidth(width);
            image.setMaxHeight(width);
            image.setScaleType(ImageView.ScaleType.CENTER_CROP);
            ImageLoader.getInstance().displayImage(FileHelper.getFileName(videos[position]), image, options);
            CheckableLayout l = new CheckableLayout(getActivity());
            l.setLayoutParams(new ListView.LayoutParams(width, width));
            l.setPadding(dp2, dp2, dp2, dp2);
            l.addView(image);
            l.setOnMultiChoiceMode(onMultiChoiceMode);
            convertView = l;
            return convertView;
        }

    }

    class MyMultiChoiceModeListener implements GridView.MultiChoiceModeListener {
        boolean[] checks;
        boolean checkAll;

        public MyMultiChoiceModeListener() {

        }

        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {

            final ArrayList<DocumentFile> checksName = new ArrayList<>();
            for (int i = 0; i < checks.length; i++) {
                if (checks[i]) {
                    checksName.add((DocumentFile) adapter.getItem(i));
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
                        setVideo();
                        if (getActivity() != null && getActivity() instanceof ViewPointActivity) {
                            ((ViewPointActivity) getActivity()).requestUpdatePOI();
                        }
                    }
                });
                ab.setNegativeButton(getString(R.string.cancel), null);
                ab.show();
                mode.finish();
            } else if (item.getItemId() == R.id.rename) {
                if (checksName.size() == 1) {
                    final DocumentFile checkFile = checksName.get(0);
                    AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity());
                    ab2.setTitle(getString(R.string.filename));
                    final EditText name = new EditText(getActivity());
                    name.setText(FileHelper.getFileName(checkFile));
                    ab2.setView(name);
                    ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            String s = name.getText().toString();
                            checkFile.renameTo(s);
                            setVideo();
                            if (getActivity() != null && getActivity() instanceof ViewPointActivity) {
                                ((ViewPointActivity) getActivity()).requestUpdatePOI();
                            }
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
            } else if (item.getItemId() == R.id.share) {
                ArrayList<Uri> uris = new ArrayList<>();
                for (int i = 0; i < checks.length; i++) {
                    if (checks[i]) {
                        uris.add(((DocumentFile) adapter.getItem(i)).getUri());
                    }
                }
                Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("video/*");
                intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                startActivity(intent);
            } else if (item.getItemId() == R.id.move) {
                final DocumentFile tripFile = ViewPointActivity.poi.dir.getParentFile();
                final String[] pois = FileHelper.listFileNames(tripFile, FileHelper.list_dirs);
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle(R.string.move_to);
                ab.setItems(pois, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DocumentFile[] fromFiles = new DocumentFile[checksName.size()];
                        DocumentFile[] toFiles = new DocumentFile[checksName.size()];
                        DocumentFile toDir = FileHelper.findfile(tripFile, pois[which], "videos");
                        DocumentFile[] filesInToDir = toDir.listFiles();
                        for (int i = 0; i < toFiles.length; i++) {
                            toFiles[i] = FileHelper.findfile(filesInToDir, FileHelper.getFileName(checksName.get(i)));
                            if (toFiles[i] == null) {
                                toFiles[i] = toDir.createFile("", FileHelper.getFileName(checksName.get(i)));
                            }
                            fromFiles[i] = checksName.get(i);
                        }
                        new FileHelper.MoveFilesTask(getActivity(), fromFiles, toFiles, new OnFinishedListener() {

                            @Override
                            public void onFinish() {

                                setVideo();
                                if (getActivity() != null && getActivity() instanceof ViewPointActivity) {
                                    ((ViewPointActivity) getActivity()).requestUpdatePOIs();
                                }
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
            menu.findItem(R.id.print).setVisible(false);
            checks = new boolean[adapter.getCount()];
            for (int i = 0; i < checks.length; i++) {
                checks[i] = false;
            }
            checkAll = false;
            adapter.onMultiChoiceMode = true;
            return true;
        }

        public void onDestroyActionMode(ActionMode mode) {
            adapter.onMultiChoiceMode = false;
        }

        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            return true;
        }

        public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

            checks[position] = checked;
            int selects = 0;
            for (boolean check : checks) {
                if (check) {
                    selects++;
                }
            }
            mode.getMenu().findItem(R.id.rename).setVisible(!(selects > 1));

        }
    }

    public void onItemClick(AdapterView<?> av, View view, int position, long id) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(((DocumentFile) adapter.getItem(position)).getUri(), "video/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        getActivity().startActivity(intent);
    }
}
