package com.yupog2003.tripdiary.fragments;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AlertDialog;
import android.view.ActionMode;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.data.DrawableHelper;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.FileHelper.MoveFilesTask.OnFinishedListener;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;
import com.yupog2003.tripdiary.views.CheckableLayout;

import java.util.ArrayList;
import java.util.Arrays;

public class AudioFragment extends Fragment {
    ListView layout;
    AudioAdapter adapter;
    POI poi;
    Drawable audioDrawable;
    FileHelper.MoveFilesTask moveFilesTask;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = (ListView) inflater.inflate(R.layout.fragment_audio, container, false);
        ViewCompat.setNestedScrollingEnabled(layout, true);
        audioDrawable = DrawableHelper.getAccentTintDrawable(getActivity(), R.drawable.ic_music);
        refresh();
        return layout;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (outState.isEmpty()) {
            outState.putBoolean("bug:fix", true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void refresh() {
        if (getActivity() == null)
            return;
        this.poi = ((ViewPointActivity) getActivity()).poi;
        if (poi == null) {
            return;
        }
        adapter = new AudioAdapter();
        layout.setAdapter(adapter);
        layout.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        layout.setMultiChoiceModeListener(new MyMultiChoiceModeListener());
        layout.setOnItemClickListener(adapter);
    }

    class MyMultiChoiceModeListener implements ListView.MultiChoiceModeListener {
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
                ab.setIcon(DrawableHelper.getAlertDrawable(getActivity()));
                ab.setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {

                        for (int i = 0; i < checksName.size(); i++) {
                            checksName.get(i).delete();
                        }
                        if (getActivity() != null && getActivity() instanceof ViewPointActivity) {
                            ((ViewPointActivity) getActivity()).requestUpdatePOI();
                        }
                    }
                });
                ab.setNegativeButton(getString(R.string.no), null);
                ab.show();
                mode.finish();
            } else if (item.getItemId() == R.id.rename) {
                if (checksName.size() == 1) {
                    final DocumentFile checkFile = checksName.get(0);
                    AlertDialog.Builder ab2 = new AlertDialog.Builder(getActivity());
                    ab2.setTitle(getString(R.string.filename));
                    final EditText name = new EditText(getActivity());
                    name.setText(checkFile.getName());
                    ab2.setView(name);
                    ab2.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {

                            String s = name.getText().toString();
                            checkFile.renameTo(s);
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
                if (uris.size() == 0) return true;
                Intent intent = new Intent();
                intent.setType("audio/*");
                if (uris.size() == 1) {
                    intent.setAction(Intent.ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_STREAM, uris.get(0));
                } else if (uris.size() > 1) {
                    intent.setAction(Intent.ACTION_SEND_MULTIPLE);
                    intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
                }
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(intent);
            } else if (item.getItemId() == R.id.move) {
                if (poi == null || poi.parentTrip == null || poi.parentTrip.pois == null)
                    return true;
                final POI[] pois = poi.parentTrip.pois;
                final String[] poiNames = new String[pois.length];
                for (int i = 0; i < poiNames.length; i++) {
                    poiNames[i] = pois[i].title;
                }
                AlertDialog.Builder ab = new AlertDialog.Builder(getActivity());
                ab.setTitle(R.string.move_to);
                ab.setItems(poiNames, new OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        DocumentFile[] fromFiles = new DocumentFile[checksName.size()];
                        DocumentFile[] toFiles = new DocumentFile[checksName.size()];
                        DocumentFile toDir = pois[which].audioDir;
                        if (toDir == null) return;
                        DocumentFile[] filesInToDir = toDir.listFiles();
                        for (int i = 0; i < toFiles.length; i++) {
                            toFiles[i] = FileHelper.findfile(filesInToDir, checksName.get(i).getName());
                            if (toFiles[i] == null) {
                                toFiles[i] = toDir.createFile("", checksName.get(i).getName());
                            }
                            fromFiles[i] = checksName.get(i);
                        }
                        moveFilesTask = new FileHelper.MoveFilesTask(getActivity(), fromFiles, toFiles, new OnFinishedListener() {

                            @Override
                            public void onFinish() {
                                if (getActivity() != null && getActivity() instanceof ViewPointActivity) {
                                    ((ViewPointActivity) getActivity()).requestUpdatePOIs(false);
                                    moveFilesTask = null;
                                }
                            }
                        });
                        moveFilesTask.execute();

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
            Arrays.fill(checks, false);
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

        public void onItemCheckedStateChanged(final ActionMode mode, int position, long id, boolean checked) {
            checks[position] = checked;
            int selects = 0;
            for (boolean check : checks) {
                if (check) {
                    selects++;
                }
            }
            mode.getMenu().findItem(R.id.rename).setVisible(selects == 1);
        }
    }

    class AudioAdapter extends BaseAdapter implements OnItemClickListener {

        DocumentFile[] audios;
        boolean onMultiChoiceMode;

        public AudioAdapter() {
            audios = poi.audioFiles;
            onMultiChoiceMode = false;
        }

        public int getCount() {
            if (audios == null)
                return 0;
            return audios.length;
        }

        public Object getItem(int position) {
            return audios[position];
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                TextView textView = new TextView(getActivity());
                textView.setTextAppearance(getActivity(), android.R.style.TextAppearance_Large);
                textView.setTextColor(ContextCompat.getColor(getActivity(), R.color.textColorDarkPrimary));
                textView.setGravity(Gravity.CENTER_VERTICAL);
                textView.setCompoundDrawablesWithIntrinsicBounds(audioDrawable, null, null, null);
                CheckableLayout l = new CheckableLayout(getActivity());
                l.addView(textView);
                convertView = l;
                convertView.setTag(textView);
            }
            ((TextView) convertView.getTag()).setText(audios[position].getName());
            ((CheckableLayout) convertView).setOnMultiChoiceMode(onMultiChoiceMode);
            return convertView;
        }

        public void onItemClick(AdapterView<?> av, View view, int position, long id) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(audios[position].getUri(), "audio/*");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            getActivity().startActivity(intent);
        }
    }


    @Override
    public void onDestroy() {
        if (moveFilesTask != null) {
            moveFilesTask.cancel();
            moveFilesTask = null;
        }
        super.onDestroy();
    }
}
