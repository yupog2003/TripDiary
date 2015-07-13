package com.yupog2003.tripdiary.data;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.yupog2003.tripdiary.R;

import org.apache.commons.io.input.CountingInputStream;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileHelper {
    public static FilenameFilter getDirFilter() {
        return new FilenameFilter() {

            public boolean accept(File dir, String filename) {

                return new File(dir, filename).isDirectory() && !filename.startsWith(".");
            }
        };
    }

    public static void copyFile(File infile, File outfile) {
        try {
            copyByStream(new FileInputStream(infile), new FileOutputStream(outfile));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static File copyFromUriToFile(Context context, Uri uri, File dirFile, String fileName) {
        if (fileName == null) {
            fileName = getRealNameFromURI(context, uri);
        }
        if (context == null || uri == null || dirFile == null) return null;
        try {
            if (dirFile.isFile()) {
                dirFile.delete();
            }
            dirFile.mkdirs();
            File resultFile = new File(dirFile, fileName);
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream fileOutputStream = new FileOutputStream(resultFile);
            copyByStream(inputStream, fileOutputStream);
            return resultFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

    public static void copyByStream(InputStream inputStream, OutputStream outputStream) {
        if (inputStream == null || outputStream == null) return;
        byte[] buffer = new byte[4096];
        int len;
        try {
            while ((len = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, len);
            }
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void deletedir(String path) {
        if (path == null)
            return;
        File file = new File(path);
        File[] files = file.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
                if (files[i].isDirectory()) {
                    deletedir(files[i].getPath());
                }
                files[i].delete();
            }
        }
        file.delete();
    }

    @SuppressLint("DefaultLocale")
    public static String getMIMEtype(String filename) {
        if (!filename.contains("."))
            return "file/*";
        String extension = filename.substring(filename.lastIndexOf(".") + 1);
        String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        if (mime == null)
            return "file/*";
        return mime;
    }

    public static String getMimeFromFile(File file) {
        return getMIMEtype(file.getName());
    }

    public static String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getRealNameFromURI(Context context, Uri contentUri) {
        try {
            String[] proj = {MediaStore.MediaColumns.DATA};
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if (cursor != null) {
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                if (column_index != -1) {
                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    Log.i("trip", "fromMediaStore");
                    cursor.close();
                    return new File(path).getName();
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
            if (cursor != null) {
                int nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                if (nameIndex != -1) {
                    cursor.moveToFirst();
                    String name = cursor.getString(nameIndex);
                    cursor.close();
                    Log.i("trip", "fromOpenable");
                    return name;
                }
            }
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        Log.i("trip", "fromGetPath");
        return new File(contentUri.getPath()).getName();
    }

    public static boolean isMemory(File file) {
        return isPicture(file) || isVideo(file) || isAudio(file);
    }

    public static boolean isPicture(File file) {
        return file.isFile() && getMimeFromFile(file).startsWith("image");
    }

    public static boolean isVideo(File file) {
        return file.isFile() && getMimeFromFile(file).startsWith("video");
    }

    public static boolean isAudio(File file) {
        return file.isFile() && getMimeFromFile(file).startsWith("audio");
    }

    public static FileFilter getPictureFileFilter() {
        return new FileFilter() {

            public boolean accept(File pathname) {

                return isPicture(pathname);
            }
        };
    }

    public static FileFilter getVideoFileFilter() {
        return new FileFilter() {

            public boolean accept(File pathname) {

                return isVideo(pathname);
            }
        };
    }

    public static FileFilter getAudioFileFilter() {
        return new FileFilter() {

            public boolean accept(File pathname) {

                return isAudio(pathname);
            }
        };
    }


    public static FileFilter getNoStartWithDotFilter() {
        return new FileFilter() {

            public boolean accept(File pathname) {

                return !pathname.getName().startsWith(".");
            }
        };
    }

    public static FileFilter getMemoryFilter() {
        return new FileFilter() {

            @Override
            public boolean accept(File pathname) {

                return isMemory(pathname);
            }
        };
    }

    public static void saveObjectToFile(Object obj, File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(obj);
            oos.flush();
            oos.close();
            fos.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static Object readObjectFromFile(File file, final GpxAnalyzerJava.ProgressChangedListener listener) {
        Object result = new Object();
        try {
            FileInputStream fis = new FileInputStream(file);
            final CountingInputStream cis = new CountingInputStream(fis);
            ObjectInputStream ois = new ObjectInputStream(cis);
            final long fileSize = file.length();
            if (listener != null) {
                new Thread(new Runnable() {

                    public void run() {

                        long count;
                        while ((count = cis.getByteCount()) < fileSize) {
                            listener.onProgressChanged(count);
                            try {
                                Thread.sleep(100);
                            } catch (InterruptedException e) {

                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
            }
            result = ois.readObject();
            ois.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        return result;
    }

    public static void zip(File source, File zip) {
        try {
            ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zip));
            dozip(source, zos, source.getName());
            zos.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private static void dozip(File from, ZipOutputStream zos, String entry) {
        try {
            if (from.isDirectory()) {

                zos.putNextEntry(new ZipEntry(entry + "/"));
                zos.closeEntry();
                File[] files = from.listFiles();
                for (int i = 0; i < files.length; i++) {
                    dozip(files[i], zos, entry + "/" + files[i].getName());
                }
            } else {
                zos.putNextEntry(new ZipEntry(entry));
                byte[] buffer = new byte[4096];
                int count = -1;
                BufferedInputStream bis = new BufferedInputStream(new FileInputStream(from));
                while ((count = bis.read(buffer, 0, 4096)) != -1) {
                    zos.write(buffer, 0, count);
                }
                bis.close();
                zos.closeEntry();
            }
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void unZip(String zip, String target) {
        BufferedOutputStream bos = null;
        try {
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zip)));
            ZipEntry entry;
            String strEntry;
            int count;
            while ((entry = zis.getNextEntry()) != null) {
                byte[] data = new byte[4096];
                strEntry = entry.getName();
                if (entry.isDirectory()) {
                    new File(target + strEntry).mkdirs();
                } else {
                    File entryFile = new File(target + strEntry);
                    File entryDir = new File(entryFile.getParent());
                    if (!entryDir.exists()) {
                        entryDir.mkdirs();
                    }
                    FileOutputStream fos = new FileOutputStream(entryFile);
                    bos = new BufferedOutputStream(fos, 4096);
                    while ((count = zis.read(data, 0, 4096)) != -1) {
                        bos.write(data, 0, count);
                    }
                    bos.flush();
                    bos.close();
                }
            }
            zis.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void converToPlayableKml(File gpxFile, File kmlFile) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(gpxFile));
            StringBuffer sb = new StringBuffer();
            String s;
            while ((s = br.readLine()) != null) {
                if (s.contains("<trkpt")) {
                    String[] toks = s.split("\"");
                    if (s.indexOf("lat") > s.indexOf("lon")) {
                        sb.append("<gx:coord>" + toks[1] + " " + toks[3] + " 0</gx:coord>\n");
                    } else {
                        sb.append("<gx:coord>" + toks[3] + " " + toks[1] + " 0</gx:coord>\n");
                    }
                } else if (s.contains("<time>")) {
                    sb.append("<when>" + s.substring(s.indexOf(">") + 1, s.lastIndexOf("<")) + "</when>\n");
                }
            }
            br.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(kmlFile, false));
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\"\n");
            bw.write("xmlns:gx=\"http://www.google.com/kml/ext/2.2\">\n");
            bw.write("<Folder>\n");
            bw.write("<Placemark id=\"track\">\n");
            bw.write("<gx:Track>\n");
            bw.write(sb.toString());
            bw.write("</gx:Track>\n");
            bw.write("</Placemark>\n");
            bw.write("</Folder>\n");
            bw.write("</kml>");
            bw.flush();
            bw.close();
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static void convertToKml(ArrayList<Marker> POIs, LatLng[] track, File kmlFile, String note) {
        if (POIs == null || track == null || kmlFile == null || note == null)
            return;
        String name = kmlFile.getName();
        name = name.substring(0, name.lastIndexOf("."));
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(kmlFile, false));
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
            bw.write("<Document>\n");
            bw.write("<name>" + name + "</name>\n");
            bw.write("<description>" + note + "</description>\n");
            bw.write("<Folder>\n");
            bw.write("<Placemark>\n");
            bw.write("<name>" + name + "</name>\n");
            bw.write("<description>Generated by TripDiary</description>\n");
            bw.write("<LineString>\n");
            bw.write("<coordinates>");
            for (int i = 0; i < track.length; i++) {
                bw.write(" " + String.valueOf(track[i].longitude) + "," + String.valueOf(track[i].latitude) + ",0\n");
            }
            bw.write("</coordinates>\n");
            bw.write("</LineString>\n");
            bw.write("</Placemark>\n");
            final int POIsSize = POIs.size();
            for (int i = 0; i < POIsSize; i++) {
                bw.write("<Placemark>\n");
                bw.write("<name>" + POIs.get(i).getTitle() + "</name>\n");
                bw.write("<description>" + POIs.get(i).getSnippet() + "</description>\n");
                bw.write("<Point>\n");
                bw.write("<coordinates>" + String.valueOf(POIs.get(i).getPosition().longitude) + "," + String.valueOf(POIs.get(i).getPosition().latitude) + ",0</coordinates>\n");
                bw.write("</Point>\n");
                bw.write("</Placemark>\n");
            }
            bw.write("</Folder>\n");
            bw.write("</Document>\n");
            bw.write("</kml>");
            bw.flush();
            bw.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public static int getNumOfLinesInFile(String filepath) {
        try {
            InputStream is = new BufferedInputStream(new FileInputStream(filepath));
            byte[] c = new byte[1024];
            int count = 0;
            int readChars = 0;
            boolean empty = true;
            while ((readChars = is.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            is.close();
            return count == 0 && !empty ? 1 : count;
        } catch (FileNotFoundException e) {

            e.printStackTrace();
        } catch (IOException e) {

            e.printStackTrace();
        }
        return 0;
    }

    public static void maintenDir(File dir) {
        if (dir == null) {
            return;
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (dir.isFile() || !dir.isDirectory()) {
            dir.delete();
            dir.mkdirs();
        }
        dir = new File(dir.getPath());
    }
    public static boolean checkHasWritePermission(Activity a, String path){
        File dir=new File(path);
        dir.mkdirs();
        File testFile=new File(path+"/"+String.valueOf(System.currentTimeMillis())+".txt");
        try {
            testFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (testFile.exists()){
            testFile.delete();
            return true;
        }else{
            Toast.makeText(a, "TripDiary has no write permission to this directory", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public static class DirAdapter extends BaseAdapter implements OnItemClickListener {

        File root;
        File[] dirs;
        Context context;
        boolean showHideDir;

        public DirAdapter(Context context, boolean showHideDir, File root) {
            this.context = context;
            this.showHideDir = showHideDir;
            setDir(root);
        }

        public void setDir(File root) {
            if (root == null)
                return;
            if (root.isFile())
                return;
            this.root = root;
            File[] dirss = root.listFiles(new FileFilter() {

                public boolean accept(File pathname) {

                    if (!showHideDir && pathname.getName().startsWith("."))
                        return false;
                    return pathname.isDirectory();
                }
            });
            if (dirss == null)
                dirss = new File[0];
            Arrays.sort(dirss, new Comparator<File>() {

                public int compare(File lhs, File rhs) {

                    return lhs.getName().compareToIgnoreCase(rhs.getName());
                }
            });
            dirs = new File[dirss.length + 2];
            dirs[0] = root;
            dirs[1] = root.getParentFile();
            for (int i = 2; i < dirs.length; i++) {
                dirs[i] = dirss[i - 2];
            }
        }

        public File getRoot() {
            return root;
        }

        public int getCount() {

            return dirs.length;
        }

        public Object getItem(int position) {

            return dirs[position];
        }

        public long getItemId(int position) {

            return position;
        }

        public View getView(int position, View convertView, ViewGroup viewGroup) {

            TextView textView = new TextView(context);
            textView.setTextAppearance(context, android.R.style.TextAppearance_Large);
            textView.setCompoundDrawablesWithIntrinsicBounds(position > 1 ? R.drawable.ic_folder : 0, 0, 0, 0);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            if (position == 0)
                textView.setText(root.getPath());
            else if (position == 1)
                textView.setText("...");
            else
                textView.setText(dirs[position].getName());
            return textView;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {

            if (position > 0) {
                setDir(dirs[position]);
                notifyDataSetChanged();
            }
        }

    }

    public static class MoveFilesTask extends AsyncTask<String, String, String> {

        Activity activity;
        File[] fromFiles;
        File[] toFiles;
        TextView message;
        ProgressBar progress;
        TextView progressMessage;
        AlertDialog dialog;
        boolean cancel = false;
        OnFinishedListener listener;

        public static interface OnFinishedListener {
            public void onFinish();
        }

        public MoveFilesTask(Activity activity, File[] fromFiles, File[] toFiles, OnFinishedListener listener) {
            this.activity = activity;
            this.listener = listener;
            if (fromFiles != null && toFiles != null && fromFiles.length == toFiles.length) {
                this.fromFiles = fromFiles;
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

            if (fromFiles != null && toFiles != null) {
                publishProgress("setMax", String.valueOf(Math.min(fromFiles.length, toFiles.length)));
                for (int i = 0; i < Math.min(fromFiles.length, toFiles.length); i++) {
                    if (cancel)
                        break;
                    publishProgress(fromFiles[i].getName(), String.valueOf(i));
                    if (fromFiles[i].getPath().equals(toFiles[i].getPath()))
                        continue;
                    copyFile(fromFiles[i], toFiles[i]);
                    fromFiles[i].delete();
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
            if (listener != null) {
                listener.onFinish();
            }
        }
    }

}
