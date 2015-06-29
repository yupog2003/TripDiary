package com.yupog2003.tripdiary.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.ListView;
import android.widget.TextView;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewPointActivity;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.FileHelper.MoveFilesTask.OnFinishedListener;
import com.yupog2003.tripdiary.views.CheckableLayout;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class AudioFragment extends Fragment {
    ListView layout;
    AudioAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_audio, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setAudio();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
        if (outState.isEmpty()) {
            outState.putBoolean("bug:fix", true);
        }
    }

    public void setAudio() {
        if (getView() == null)
            return;
        layout = (ListView) getView().findViewById(R.id.audiolistview);
        ViewPointActivity.poi.updateAllFields();
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
                        setAudio();
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
                            setAudio();
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
            } else if (item.getItemId() == R.id.share) {
                ArrayList<Uri> uris = new ArrayList<Uri>();
                for (int i = 0; i < checks.length; i++) {
                    if (checks[i]) {
                        uris.add(Uri.fromFile((File) adapter.getItem(i)));
                    }
                }
                Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
                intent.setType("audio/*");
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
                            toFiles[i] = new File(tripFile.getPath() + "/" + pois[which] + "/audios/" + checksName.get(i).getName());
                        }
                        new FileHelper.MoveFilesTask(getActivity(), fromFiles, toFiles, new OnFinishedListener() {

                            @Override
                            public void onFinish() {

                                setAudio();
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
            menu.findItem(R.id.print).setVisible(false);
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

        }

    }

    class AudioAdapter extends BaseAdapter implements OnItemClickListener {
        File[] audios;

        public AudioAdapter() {
            audios = ViewPointActivity.poi.audioFiles;
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
                textView.setTextSize(30);
                textView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_music, 0, 0, 0);
                textView.setText(audios[position].getName());
                CheckableLayout l = new CheckableLayout(getActivity());
                l.addView(textView);
                convertView = l;
            } else {
                convertView.setTag(convertView.getTag());
            }
            return convertView;
        }

        public void onItemClick(AdapterView<?> av, View view, int position, long id) {

            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.fromFile(audios[position]), "audio/*");
            getActivity().startActivity(intent);
        }
    }

}
