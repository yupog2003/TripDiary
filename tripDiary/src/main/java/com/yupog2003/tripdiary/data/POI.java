package com.yupog2003.tripdiary.data;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.provider.DocumentFile;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class POI implements Comparable<POI> {

    Context context;
    public DocumentFile parentTrip;
    public DocumentFile dir;
    public DocumentFile picDir;
    public DocumentFile audioDir;
    public DocumentFile videoDir;
    public DocumentFile costDir;
    public DocumentFile diaryFile;
    public DocumentFile basicInformationFile;
    public DocumentFile[] picFiles;
    public DocumentFile[] audioFiles;
    public DocumentFile[] videoFiles;
    public DocumentFile[] costFiles;
    public String title;
    public MyCalendar time; //in UTC
    public double latitude;
    public double longitude;
    public double altitude;
    public String diary;

    public POI(Context context, DocumentFile dir) throws NullPointerException{
        this.context = context;
        this.dir = dir;
        if (context == null || dir == null) {
            throw new NullPointerException();
        } else {
            updateAllFields();
        }
    }

    public void updateAllFields() {
        DocumentFile[] files = dir.listFiles();
        this.parentTrip = dir.getParentFile();
        this.picDir = FileHelper.findfile(files, "pictures");
        if (picDir == null)
            this.picDir = dir.createDirectory("pictures");
        this.audioDir = FileHelper.findfile(files, "audios");
        if (audioDir == null)
            this.audioDir = dir.createDirectory("audios");
        this.videoDir = FileHelper.findfile(files, "videos");
        if (videoDir == null)
            this.videoDir = dir.createDirectory("videos");
        this.costDir = FileHelper.findfile(files, "costs");
        if (costDir == null)
            this.costDir = dir.createDirectory("costs");
        this.diaryFile = FileHelper.findfile(files, "text");
        if (diaryFile == null) {
            this.diaryFile = dir.createFile("", "text");
        }
        this.basicInformationFile = FileHelper.findfile(files, "basicinformation");
        if (basicInformationFile == null) {
            this.basicInformationFile = dir.createFile("", "basicinformation");
        }
        this.picFiles = FileHelper.listFiles(picDir, FileHelper.list_pics);
        this.audioFiles = FileHelper.listFiles(audioDir, FileHelper.list_audios);
        this.videoFiles = FileHelper.listFiles(videoDir, FileHelper.list_videos);
        this.costFiles = FileHelper.listFiles(costDir, FileHelper.list_all);
        this.title = FileHelper.getFileName(dir);
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(basicInformationFile.getUri())));
            String s;
            this.time = MyCalendar.getInstance();
            this.latitude = 0;
            this.longitude = 0;
            this.altitude = 0;
            this.diary = "";
            while ((s = br.readLine()) != null) {
                if (s.startsWith("Title")) {
                    this.title = s.substring(s.indexOf("=") + 1);
                } else if (s.startsWith("Time")) {
                    this.time = MyCalendar.getTime(s, MyCalendar.type_time_format3399);
                } else if (s.startsWith("Latitude")) {
                    this.latitude = Double.parseDouble(s.substring(s.indexOf("=") + 1));
                } else if (s.startsWith("Longitude")) {
                    this.longitude = Double.parseDouble(s.substring(s.indexOf("=") + 1));
                } else if (s.startsWith("Altitude")) {
                    this.altitude = Double.parseDouble(s.substring(s.indexOf("=") + 1));
                }
            }
            br.close();
            br = new BufferedReader(new InputStreamReader(context.getContentResolver().openInputStream(diaryFile.getUri())));
            StringBuilder sb = new StringBuilder();
            while ((s = br.readLine()) != null) {
                sb.append(s).append("\n");
            }
            br.close();
            diary = sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateBasicInformation(String title, MyCalendar time, Double latitude, Double longitude, Double altitude) {
        if (title != null)
            this.title = title;
        if (time != null)
            this.time = time;
        if (latitude != null)
            this.latitude = latitude;
        if (longitude != null)
            this.longitude = longitude;
        if (altitude != null)
            this.altitude = altitude;
        try {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(context.getContentResolver().openOutputStream(basicInformationFile.getUri())));
            bw.write("Title=" + this.title + "\n");
            bw.write("Time=" + this.time.format3339() + "\n");
            bw.write("Latitude=" + String.valueOf(this.latitude) + "\n");
            bw.write("Longitude=" + String.valueOf(this.longitude) + "\n");
            bw.write("Altitude=" + String.valueOf(this.altitude) + "\n");
            bw.flush();
            bw.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    public void updateDiary(String text) {
        if (text != null) {
            this.diary = text;
            try {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(context.getContentResolver().openOutputStream(diaryFile.getUri())));
                bw.write(text);
                bw.flush();
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void importMemories(ImportMemoriesListener listener, File... inFiles) {
        int size = inFiles.length;
        for (int i = 0; i < size; i++) {
            File inFile = inFiles[i];
            if (inFile == null) continue;
            if (!FileHelper.isMemory(inFile)) continue;
            DocumentFile outFile = null;
            if (FileHelper.isPicture(inFile)) {
                outFile = FileHelper.findfile(picFiles, inFile.getName());
                if (outFile == null) {
                    outFile = picDir.createFile("", inFile.getName());
                }
            } else if (FileHelper.isVideo(inFile)) {
                outFile = FileHelper.findfile(videoFiles, inFile.getName());
                if (outFile == null) {
                    outFile = videoDir.createFile("", inFile.getName());
                }
            } else if (FileHelper.isAudio(inFile)) {
                outFile = FileHelper.findfile(audioFiles, inFile.getName());
                if (outFile == null) {
                    outFile = audioDir.createFile("", inFile.getName());
                }
            }
            if (outFile != null) {
                FileHelper.copyFile(inFile, outFile);
            }
            if (listener != null) {
                listener.onProgressUpdate(i);
            }
        }
    }

    public void importMemories(ImportMemoriesListener listener, Uri... inUris) {
        if (inUris == null) return;
        int size = inUris.length;
        for (int i = 0; i < size; i++) {
            Uri inUri = inUris[i];
            if (inUri == null) continue;
            String fileName = FileHelper.getRealNameFromURI(context, inUri);
            if (!FileHelper.isMemory(fileName)) continue;
            DocumentFile outFile = null;
            if (FileHelper.isPicture(fileName)) {
                outFile = FileHelper.findfile(picFiles, fileName);
                if (outFile == null) {
                    outFile = picDir.createFile("", fileName);
                }
            } else if (FileHelper.isVideo(fileName)) {
                outFile = FileHelper.findfile(videoFiles, fileName);
                if (outFile == null) {
                    outFile = videoDir.createFile("", fileName);
                }
            } else if (FileHelper.isAudio(fileName)) {
                outFile = FileHelper.findfile(audioFiles, fileName);
                if (outFile == null) {
                    outFile = audioDir.createFile("", fileName);
                }
            }
            if (outFile != null) {
                try {
                    InputStream is = context.getContentResolver().openInputStream(inUri);
                    OutputStream os = context.getContentResolver().openOutputStream(outFile.getUri());
                    FileHelper.copyByStream(is, os);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
            if (listener != null) {
                listener.onProgressUpdate(i);
            }
        }
    }

    @Override
    public int compareTo(@NonNull POI another) {
        if (this.time == null || another.time == null) {
            return 0;
        } else {
            return time.compareTo(another.time);
        }
    }

    public interface ImportMemoriesListener {
        void onProgressUpdate(int progess);
    }

    public void addCost(int type, String name, float dollar) {
        DocumentFile cost = FileHelper.findfile(costDir, name);
        if (cost == null) {
            cost = costDir.createFile("", name);
        }
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(context.getContentResolver().openOutputStream(cost.getUri())));
            bw.write("type=" + String.valueOf(type) + "\n");
            bw.write("dollar=" + String.valueOf(dollar));
            bw.flush();
            bw.close();
            this.costFiles = costDir.listFiles();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void renamePOI(String name) {
        dir.renameTo(name);
        updateAllFields();
        updateBasicInformation(name, null, null, null, null);
    }

    public void deleteSelf() {
        dir.delete();
    }

}
