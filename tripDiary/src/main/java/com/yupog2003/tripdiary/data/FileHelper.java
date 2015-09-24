package com.yupog2003.tripdiary.data;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
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

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

public class FileHelper {

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
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void deleteDir(File dir) {
        if (dir == null) return;
        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files == null) return;
            for (File file : files) {
                deleteDir(file);
            }
        }
        dir.delete();
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

    public static final NumberFormat sizeFormat = new DecimalFormat("#.##");

    public static String getSizeString(int bytes) {
        if (bytes < 1024) {
            return String.valueOf(bytes) + "bytes";
        } else if (bytes < 1024 * 1024) {
            return sizeFormat.format(bytes / 1024f) + "KB";
        } else {
            return sizeFormat.format(bytes / 1024 / 1024) + "MB";
        }
    }

    public static String getSizeProgressString(int bytes, int total) {
        return getSizeString(bytes) + "/" + getSizeString(total);
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

    public static DocumentFile findfile(DocumentFile[] files, final String... names) {
        if (files == null || names == null) return null;
        int namesLength = names.length;
        for (int i = 0; i < namesLength; i++) {
            if (names[i] == null) continue;
            int filesLength = files.length;
            for (int j = 0; j < filesLength; j++) {
                if (files[j] == null) continue;
                if (names[i].equals(files[j].getName())) {
                    if (i == namesLength - 1) {
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

    public static void unZip(File zip, DocumentFile target) {
        try {
            unZip(new FileInputStream(zip), target);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void unZip(InputStream is, DocumentFile target) {
        if (target == null) return;
        try {
            ZipInputStream zis = new ZipInputStream(is);
            ZipEntry entry;
            String strEntry;
            int count;
            while ((entry = zis.getNextEntry()) != null) {
                strEntry = entry.getName();
                String[] dirs = strEntry.split("/");
                DocumentFile entryFile = target;
                for (int i = 0; i < dirs.length - 1; i++) { //find dir
                    DocumentFile temp = FileHelper.findfile(entryFile, dirs[i]);
                    if (temp == null) {
                        temp = entryFile.createDirectory(dirs[i]);
                    }
                    if (temp == null) {
                        return;
                    }
                    entryFile = temp;
                }
                final String entryFileName = dirs[dirs.length - 1];
                if (entry.isDirectory()) {
                    entryFile.createDirectory(entryFileName);
                } else {
                    entryFile = entryFile.createFile("", entryFileName);
                    if (entryFile == null) {
                        return;
                    }
                    byte[] data = new byte[4096];
                    BufferedOutputStream bos = new BufferedOutputStream(entryFile.getOutputStream(), 4096);
                    while ((count = zis.read(data, 0, 4096)) != -1) {
                        bos.write(data, 0, count);
                    }
                    bos.flush();
                    bos.close();
                }
            }
            zis.close();
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
        Drawable pictureDrawable;
        Drawable videoDrawable;
        Drawable audioDrawable;
        Drawable fileDrawable;

        public DirAdapter(Context context, boolean showHideDir, File root) {
            this.context = context;
            this.showHideDir = showHideDir;
            this.dirDrawable = DrawableHelper.getAccentTintDrawable(context, R.drawable.ic_folder);
            this.pictureDrawable = DrawableHelper.getAccentTintDrawable(context, R.drawable.ic_picture);
            this.videoDrawable = DrawableHelper.getAccentTintDrawable(context, R.drawable.ic_takevideo);
            this.audioDrawable = DrawableHelper.getAccentTintDrawable(context, R.drawable.ic_music);
            this.fileDrawable = DrawableHelper.getAccentTintDrawable(context, R.drawable.ic_description);
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
                    return showHideDir || !pathname.getName().startsWith(".");
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
            File file = dirs[position];
            TextView textView = new TextView(context);
            textView.setTextAppearance(context, android.R.style.TextAppearance_Large);
            textView.setCompoundDrawablesWithIntrinsicBounds(position > 1 ? getDrawable(file) : null, null, null, null);
            textView.setGravity(Gravity.CENTER_VERTICAL);
            if (position == 0)
                textView.setText(root.getPath());
            else if (position == 1)
                textView.setText("...");
            else
                textView.setText(file.getName());
            return textView;
        }

        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            if (position > 0) {
                setDir(dirs[position]);
                notifyDataSetChanged();
            }
        }

        private Drawable getDrawable(File file) {
            if (file.isDirectory()) {
                return dirDrawable;
            } else if (FileHelper.isPicture(file)) {
                return pictureDrawable;
            } else if (FileHelper.isVideo(file)) {
                return videoDrawable;
            } else if (FileHelper.isAudio(file)) {
                return audioDrawable;
            } else {
                return fileDrawable;
            }
        }

    }

    public static class MoveFilesTask extends AsyncTask<Void, String, String> {

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
            LinearLayout layout = (LinearLayout) activity.getLayoutInflater().inflate(R.layout.progressdialog_import_memory, (ViewGroup) activity.findViewById(android.R.id.content), false);
            message = (TextView) layout.findViewById(R.id.message);
            progress = (ProgressBar) layout.findViewById(R.id.progressBar);
            progressMessage = (TextView) layout.findViewById(R.id.progress);
            ab.setView(layout);
            ab.setCancelable(false);
            ab.setNegativeButton(activity.getString(R.string.cancel), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    cancel = true;
                }
            });
            dialog = ab.create();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... params) {
            if (fromFiles != null && toFiles != null) {
                int length = Math.min(fromFiles.length, toFiles.length);
                publishProgress("setMax", String.valueOf(length));
                for (int i = 0; i < length; i++) {
                    DocumentFile fromFile = fromFiles[i];
                    DocumentFile toFile = toFiles[i];
                    if (fromFile == null || toFile == null) continue;
                    if (cancel)
                        break;
                    publishProgress(fromFile.getName(), String.valueOf(i));
                    if (fromFile.getUri().toString().equals(toFile.getUri().toString()))
                        continue;
                    copyFile(fromFile, toFile);
                    fromFile.delete();
                }
            }
            return null;
        }

        public void cancel() {
            cancel = true;
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
