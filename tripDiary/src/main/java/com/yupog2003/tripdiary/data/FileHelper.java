package com.yupog2003.tripdiary.data;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.AlertDialog;
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

import com.google.android.gms.maps.model.Marker;
import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.TripDiaryApplication;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
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
        } catch (FileNotFoundException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(DocumentFile infile, File outfile) {
        try {
            InputStream is = infile.getInputStream();
            copyByStream(is, new FileOutputStream(outfile));
        } catch (FileNotFoundException | IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(File infile, DocumentFile outfile) {
        try {
            OutputStream os = outfile.getOutputStream();
            copyByStream(new FileInputStream(infile), os);
        } catch (FileNotFoundException | IllegalArgumentException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void copyFile(DocumentFile infile, DocumentFile outfile) {
        try {
            InputStream is = infile.getInputStream();
            OutputStream os = outfile.getOutputStream();
            copyByStream(is, os);
        } catch (IllegalArgumentException | NullPointerException e) {
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
        } catch (FileNotFoundException | NullPointerException | IllegalArgumentException e) {
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
            inputStream.close();
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
            for (File file1 : files) {
                if (file1.isDirectory()) {
                    deletedir(file1.getPath());
                }
                file1.delete();
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

    public static String getRealNameFromURI(Context context, Uri contentUri) {
        String name;
        if ((name = queryForString(context, contentUri, MediaStore.MediaColumns.DISPLAY_NAME, null)) != null) {
            Log.i("trip", "from Media Store");
            return name;
        }
        if ((name = queryForString(context, contentUri, OpenableColumns.DISPLAY_NAME, null)) != null) {
            Log.i("trip", "from Openable");
            return name;
        }
        Log.i("trip", "from Get Path");
        return new File(contentUri.getPath()).getName();
    }

    private static String queryForString(Context context, Uri self, String column, String defaultValue) {
        final ContentResolver resolver = context.getContentResolver();
        Cursor c = null;
        try {
            c = resolver.query(self, new String[]{column}, null, null, null);
            if (c.moveToFirst() && !c.isNull(0)) {
                return c.getString(0);
            } else {
                return defaultValue;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        } finally {
            if (c != null) {
                try {
                    c.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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

    public static boolean isMemory(DocumentFile file) {
        return isPicture(file) || isVideo(file) || isAudio(file);
    }

    public static boolean isPicture(DocumentFile file) {
        return getMIMEtype(file.getUri().toString()).startsWith("image");
    }

    public static boolean isVideo(DocumentFile file) {
        return getMIMEtype(file.getUri().toString()).startsWith("video");
    }

    public static boolean isAudio(DocumentFile file) {
        return getMIMEtype(file.getUri().toString()).startsWith("audio");
    }

    public static boolean isMemory(String fileName) {
        return isPicture(fileName) || isVideo(fileName) || isAudio(fileName);
    }

    public static boolean isPicture(String fileName) {
        return getMIMEtype(fileName).startsWith("image");
    }

    public static boolean isVideo(String fileName) {
        return getMIMEtype(fileName).startsWith("video");
    }

    public static boolean isAudio(String fileName) {
        return getMIMEtype(fileName).startsWith("audio");
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

    public static DocumentFile findfile(DocumentFile dir, String... name) {
        if (dir == null) return null;
        return findfile(dir.listFiles(), name);
    }

    public static DocumentFile findfile(DocumentFile[] files, String... names) {
        if (files == null || names == null) return null;
        for (int i = 0; i < names.length; i++) {
            if (names[i] == null) continue;
            int filesLength = files.length;
            for (int j = 0; j < filesLength; j++) {
                if (files[j] == null) continue;
                if (names[i].equals(files[j].getName())) {
                    if (i == names.length - 1) {
                        return files[j];
                    } else {
                        files = files[j].listFiles();
                        break;
                    }
                }
            }
        }
        return null;
    }

    public static void zip(DocumentFile source, DocumentFile zip) {
        try {
            ZipOutputStream zos = new ZipOutputStream(zip.getOutputStream());
            dozip(source, zos, source.getName());
            zos.close();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private static void dozip(DocumentFile from, ZipOutputStream zos, String entry) {
        try {
            if (from.isDirectory()) {
                zos.putNextEntry(new ZipEntry(entry + "/"));
                zos.closeEntry();
                DocumentFile[] files = from.listFiles();
                for (DocumentFile file : files) {
                    dozip(file, zos, entry + "/" + file.getName());
                }
            } else {
                zos.putNextEntry(new ZipEntry(entry));
                byte[] buffer = new byte[4096];
                int count;
                BufferedInputStream bis = new BufferedInputStream(from.getInputStream());
                while ((count = bis.read(buffer, 0, 4096)) != -1) {
                    zos.write(buffer, 0, count);
                }
                bis.close();
                zos.closeEntry();
            }
        } catch (NullPointerException | IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void unZip(File zip, final DocumentFile target) {
        try {
            unZip(new FileInputStream(zip), target);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static void unZip(InputStream is, DocumentFile target) {
        try {
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            String strEntry;
            int count;
            while ((entry = zis.getNextEntry()) != null) {
                strEntry = entry.getName();
                String[] dirs = strEntry.split("/");
                DocumentFile entryFile = target;
                for (int i = 0; i < dirs.length; i++) {
                    if (i == dirs.length - 1) {  //file
                        if (entry.isDirectory()) {
                            entryFile.createDirectory(dirs[i]);
                        } else {
                            entryFile = entryFile.createFile("", dirs[i]);
                            byte[] data = new byte[4096];
                            BufferedOutputStream bos = new BufferedOutputStream(entryFile.getOutputStream(), 4096);
                            while ((count = zis.read(data, 0, 4096)) != -1) {
                                bos.write(data, 0, count);
                            }
                            bos.flush();
                            bos.close();
                        }
                    } else {
                        DocumentFile temp = FileHelper.findfile(entryFile, dirs[i]);
                        if (temp == null) {
                            temp = entryFile.createDirectory(dirs[i]);
                        }
                        entryFile = temp;
                    }
                }
            }
            zis.close();
        } catch (IOException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    public static void convertToKml(ArrayList<Marker> POIs, TrackCache track, DocumentFile kmlFile, String note) {
        if (POIs == null || track == null || kmlFile == null || note == null)
            return;
        String name = kmlFile.getName();
        name = name.substring(0, name.lastIndexOf("."));
        int lineColor = PreferenceManager.getDefaultSharedPreferences(TripDiaryApplication.instance).getInt("trackcolor", 0xff6699cc);
        String lineColorInKML = String.format("%02x%02x%02x%02x", Color.alpha(lineColor), Color.blue(lineColor), Color.green(lineColor), Color.red(lineColor));
        int iconColor = PreferenceManager.getDefaultSharedPreferences(TripDiaryApplication.instance).getInt("markercolor", 0xffff0000);
        String iconColorInKML = String.format("%02x%02x%02x%02x", Color.alpha(iconColor), Color.blue(iconColor), Color.green(iconColor), Color.red(iconColor));
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(kmlFile.getOutputStream()));
            bw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            bw.write("<kml xmlns=\"http://www.opengis.net/kml/2.2\">\n");
            bw.write("<Document>\n");
            bw.write("<name>" + name + "</name>\n");
            bw.write("<description>" + note + "</description>\n");
            bw.write("<Style id=\"lineColor\">\n");
            bw.write("<LineStyle>\n");
            bw.write("<color>" + lineColorInKML + "</color>\n");
            bw.write("<width>4</width>\n");
            bw.write("</LineStyle>\n");
            bw.write("</Style>\n");
            bw.write("<Style id=\"iconColor\">\n");
            bw.write("<IconStyle>\n");
            bw.write("<color>" + iconColorInKML + "</color>\n");
            bw.write("</IconStyle>\n");
            bw.write("</Style>\n");
            bw.write("<Folder>\n");
            bw.write("<Placemark>\n");
            bw.write("<name>" + name + "</name>\n");
            bw.write("<description>Generated by TripDiary</description>\n");
            bw.write("<styleUrl>#lineColor</styleUrl>\n");
            bw.write("<LineString>\n");
            bw.write("<coordinates>");
            int length = Math.min(track.latitudes.length, track.longitudes.length);
            length = Math.min(length, track.altitudes.length);
            double maxLatitude = -Double.MAX_VALUE;
            double minLatitude = Double.MAX_VALUE;
            double maxLongitude = -Double.MAX_VALUE;
            double minLongitude = Double.MAX_VALUE;
            for (int i = 0; i < length; i++) {
                double longitude = track.longitudes[i];
                double latitude = track.latitudes[i];
                if (maxLatitude < latitude) {
                    maxLatitude = latitude;
                }
                if (minLatitude > latitude) {
                    minLatitude = latitude;
                }
                if (maxLongitude < longitude) {
                    maxLongitude = longitude;
                }
                if (minLongitude > longitude) {
                    minLongitude = longitude;
                }
                bw.write(String.format(" %f,%f,%f\n", longitude, latitude, track.altitudes[i]));
            }
            bw.write("</coordinates>\n");
            bw.write("</LineString>\n");
            bw.write("</Placemark>\n");
            final int POIsSize = POIs.size();
            for (int i = 0; i < POIsSize; i++) {
                bw.write("<Placemark>\n");
                bw.write("<name>" + POIs.get(i).getTitle() + "</name>\n");
                bw.write("<styleUrl>#iconColor</styleUrl>\n");
                bw.write("<description>" + POIs.get(i).getSnippet() + "</description>\n");
                bw.write("<Point>\n");
                bw.write("<coordinates>" + String.valueOf(POIs.get(i).getPosition().longitude) + "," + String.valueOf(POIs.get(i).getPosition().latitude) + ",0</coordinates>\n");
                bw.write("</Point>\n");
                bw.write("</Placemark>\n");
            }
            bw.write("</Folder>\n");
            bw.write("<LookAt>\n");
            bw.write("<longitude>" + String.valueOf((maxLongitude + minLongitude) / 2) + "</longitude>\n");
            bw.write("<latitude>" + String.valueOf((maxLatitude + minLatitude) / 2) + "</latitude>\n");
            bw.write("<altitude>0</altitude>\n");
            bw.write("<heading>0</heading>\n");
            bw.write("<tilt>45</tilt>\n");
            bw.write("<range>20000</range>\n");
            bw.write("<altitudeMode>clampToGround</altitudeMode>\n");
            bw.write("</LookAt>\n");
            bw.write("</Document>\n");
            bw.write("</kml>");
            bw.flush();
            bw.close();
        } catch (IOException | IllegalArgumentException e) {

            e.printStackTrace();
        }
    }

    public static int getNumOfLinesInFile(File file) {
        try {
            return getNumOfLinesInStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static int getNumOfLinesInFile(DocumentFile file) {
        return getNumOfLinesInStream(file.getInputStream());
    }

    public static int getNumOfLinesInStream(InputStream is) {
        try {
            BufferedInputStream bis = new BufferedInputStream(is);
            byte[] c = new byte[4096];
            int count = 0;
            int readChars;
            boolean empty = true;
            while ((readChars = bis.read(c)) != -1) {
                empty = false;
                for (int i = 0; i < readChars; ++i) {
                    if (c[i] == '\n') {
                        ++count;
                    }
                }
            }
            bis.close();
            return count == 0 && !empty ? 1 : count;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static boolean checkHasWritePermission(Activity a, String path) {
        File dir = new File(path);
        dir.mkdirs();
        File testFile = new File(path + "/" + String.valueOf(System.currentTimeMillis()) + ".txt");
        try {
            testFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (testFile.exists()) {
            testFile.delete();
            return true;
        } else {
            Toast.makeText(a, "TripDiary has no write permission to this directory", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    public static class DirAdapter extends BaseAdapter implements OnItemClickListener {

        File root;
        File[] dirs;
        Context context;
        boolean showHideDir;
        Drawable dirDrawable;

        public DirAdapter(Context context, boolean showHideDir, File root) {
            this.context = context;
            this.showHideDir = showHideDir;
            this.dirDrawable = ColorHelper.getAccentTintDrawable(context, R.drawable.ic_folder);
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
            System.arraycopy(dirss, 0, dirs, 2, dirss.length);
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
            textView.setCompoundDrawablesWithIntrinsicBounds(position > 1 ? dirDrawable : null, null, null, null);
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
        DocumentFile[] fromFiles;
        DocumentFile[] toFiles;
        TextView message;
        ProgressBar progress;
        TextView progressMessage;
        AlertDialog dialog;
        boolean cancel = false;
        OnFinishedListener listener;

        public interface OnFinishedListener {
            void onFinish();
        }

        public MoveFilesTask(Activity activity, DocumentFile[] fromFiles, DocumentFile[] toFiles, OnFinishedListener listener) {
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
                    if (fromFiles[i].getUri().toString().equals(toFiles[i].getUri().toString()))
                        continue;
                    try {
                        copyByStream(fromFiles[i].getInputStream(), toFiles[i].getOutputStream());
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
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
