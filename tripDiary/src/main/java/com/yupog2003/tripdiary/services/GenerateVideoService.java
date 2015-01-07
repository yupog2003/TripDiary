package com.yupog2003.tripdiary.services;

import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.ViewTripActivity;
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.fragments.ViewMapFragment;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GenerateVideoService extends IntentService {

    NotificationCompat.Builder nb;
    NotificationManagerCompat nm;
    File tempDir;
    File ffmpegFile;
    POI[] pois;
    String tripName;
    String resultVideoPath;
    int num_total_materials;
    int num_processed_materials;
    public static final int fps = 25;
    public static final int secondsPerDiary = 2;
    public static final int secondsPerTrack = 1;
    public static final int secondsPerPicture = 2;
    public static final int videoWidth = 1920;
    public static final int videoHeight = 1080;

    public static final String tag_gmapBitmap = "gmapBitmap";
    public static final String tag_points = "points";

    public GenerateVideoService() {
        super("GenerateVideoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        tripName = ViewTripActivity.trip.tripName;
        nm = NotificationManagerCompat.from(this);
        nb = new NotificationCompat.Builder(this);
        nb.setContentTitle(getString(R.string.encoding) + ":" + tripName);
        nb.setTicker(tripName);
        nb.setSmallIcon(R.drawable.ic_launcher);
        nb.setProgress(100, 0, false);
        nb.setOngoing(true);
        nm.notify(1, nb.build());
        tempDir = new File(getCacheDir(), "VideoCache");
        FileHelper.deletedir(tempDir.getPath());
        tempDir.mkdir();
        ffmpegFile = new File(getFilesDir().getParent(), "ffmpeg");
        if (!ffmpegFile.exists()) {
            copyFFmpeg();
        }
        pois = ViewTripActivity.trip.pois.clone();
        num_total_materials = getNum_total_materials();
        num_processed_materials = 0;
        Bitmap gmapBitmap = ViewMapFragment.gmapBitmap.copy(Bitmap.Config.RGB_565, true);
        Point[] trackPoints = ViewMapFragment.trackPoints.clone();
        ViewMapFragment.gmapBitmap.recycle();
        ViewMapFragment.trackPoints = null;
        String[] trackVideoNames = generateTrackVideos(gmapBitmap, trackPoints);
        String[] poiVideoNames = generatePOIVideos();
        resultVideoPath = generateTripVideos(trackVideoNames, poiVideoNames);
    }

    private String generateTripVideos(String[] trackVideoNames, String[] poiVideoNames) {
        if (trackVideoNames.length - poiVideoNames.length != 1) {
            return null;
        }
        String rootPath = PreferenceManager.getDefaultSharedPreferences(this).getString("rootpath", Environment.getExternalStorageDirectory() + "/TripDiary");
        String resultVideoPath = rootPath + "/" + tripName + ".mp4";
        File tempFile = new File(tempDir, "temp.mpg");
        for (int i = 0; i < poiVideoNames.length; i++) {
            concatFiles(new File(tempDir, trackVideoNames[i]), tempFile);
            concatFiles(new File(tempDir, poiVideoNames[i]), tempFile);
            new File(tempDir, trackVideoNames[i]).delete();
            new File(tempDir, poiVideoNames[i]).delete();
        }
        concatFiles(new File(tempDir, trackVideoNames[trackVideoNames.length - 1]), tempFile);
        new File(tempDir, trackVideoNames[trackVideoNames.length - 1]).delete();
        new File(resultVideoPath).delete();
        runCommand(new String[]{"ffmpeg", "-i", "temp.mpg", "-strict", "-2", "-c", "copy", resultVideoPath}, null, null);
        //runCommand("ffmpeg -i temp.mpg -strict -2 -c copy " + resultVideoPath);
        new File(tempDir, "temp.mpg").delete();
        return resultVideoPath;
    }

    private String[] generatePOIVideos() {
        String[] poiVideoNames = new String[pois.length];
        for (int i = 0; i < pois.length; i++) {
            POI poi = pois[i];
            String poiVideoName = "POI_" + poi.title + ".mpg";
            generateDiaryVideo(poiVideoName, poi);
            generatePicVideo(poiVideoName, poi);
            generateVideoVideo(poiVideoName, poi);
            generateAudioVideo(poiVideoName, poi);
            poiVideoNames[i] = poiVideoName;
        }
        return poiVideoNames;
    }

    private void generateDiaryVideo(String videoName, POI poi) {
        String text = poi.title + "\n" + poi.diary;
        Bitmap bitmap = Bitmap.createBitmap(videoWidth, videoHeight, Bitmap.Config.RGB_565);
        bitmap.eraseColor(Color.BLACK);
        Canvas canvas = new Canvas(bitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize((int) (Math.pow((videoWidth - 40) * (videoHeight - 40) / text.length(), 0.5) * 0.8));
        StaticLayout staticLayout = new StaticLayout(text, textPaint, canvas.getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
        canvas.save();
        canvas.translate(20, 20);
        staticLayout.draw(canvas);
        canvas.restore();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(tempDir, "diary_0.jpg"));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            FileHelper.copyFile(new File(tempDir, "diary_0.jpg"), new File(tempDir, "diary_1.jpg"));
            FileHelper.copyFile(new File(tempDir, "diary_0.jpg"), new File(tempDir, "diary_2.jpg"));
            String cmdDescription = poi.title + "-" + getString(R.string.diary);
            runCommand(new String[]{"ffmpeg", "-r", "1/" + String.valueOf(secondsPerDiary), "-i", "diary_%d.jpg", "-c:v", "mpeg2video", videoName}, cmdDescription, secondsPerDiary);
            new File(tempDir, "diary_0.jpg").delete();
            new File(tempDir, "diary_1.jpg").delete();
            new File(tempDir, "diary_2.jpg").delete();
            num_processed_materials++;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void generatePicVideo(String videoName, POI poi) {
        if (poi.picFiles.length == 0) return;
        try {
            for (int i = 0; i < poi.picFiles.length; i++) {
                nb.setContentText(poi.title + "-" + getString(R.string.photo) + "-" + poi.picFiles[i].getName());
                nm.notify(1, nb.build());
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(poi.picFiles[i].getPath(), options);
                options.inSampleSize = (int) Math.max((float) options.outWidth / videoWidth, (float) options.outHeight / videoHeight);
                options.inJustDecodeBounds = false;
                Bitmap bitmap = BitmapFactory.decodeFile(poi.picFiles[i].getPath(), options);
                float ratio = Math.min((float) videoWidth / bitmap.getWidth(), (float) videoHeight / bitmap.getHeight());
                int destWidth = (int) (bitmap.getWidth() * ratio);
                int destHeight = (int) (bitmap.getHeight() * ratio);
                int destLeft = videoWidth / 2 - destWidth / 2;
                int destTop = videoHeight / 2 - destHeight / 2;
                int destRight = videoWidth / 2 + destWidth / 2;
                int destBottom = videoHeight / 2 + destHeight / 2;
                Rect sourceRect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                Rect destRect = new Rect(destLeft, destTop, destRight, destBottom);
                Bitmap finalBitmap = Bitmap.createBitmap(videoWidth, videoHeight, Bitmap.Config.RGB_565).copy(Bitmap.Config.RGB_565, true);
                finalBitmap.eraseColor(Color.BLACK);
                Canvas canvas = new Canvas(finalBitmap);
                canvas.drawBitmap(bitmap, sourceRect, destRect, null);
                String fileName = "image_" + String.valueOf(i + 1) + ".jpg";
                FileOutputStream fileOutputStream = new FileOutputStream(new File(tempDir, fileName));
                finalBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                bitmap.recycle();
                finalBitmap.recycle();
                if (i == 0) {//for compensate ffmpeg bug - first frame may be skipped
                    FileHelper.copyFile(new File(tempDir, fileName), new File(tempDir, "image_" + String.valueOf(0) + ".jpg"));
                }
                if (i == poi.picFiles.length - 1) { //for compensate ffmpeg bug - last frame may be skipped
                    FileHelper.copyFile(new File(tempDir, fileName), new File(tempDir, "image_" + String.valueOf(i + 2) + ".jpg"));
                }
            }
            String cmdDescription = poi.title + "-" + getString(R.string.photo);
            runCommand(new String[]{"ffmpeg", "-r", "1/" + String.valueOf(secondsPerPicture), "-i", "image_%d.jpg", "-c:v", "mpeg2video", "temp.mpg"}, cmdDescription, secondsPerPicture * poi.picFiles.length);
            concatFiles(new File(tempDir, "temp.mpg"), new File(tempDir, videoName));
            new File(tempDir, "temp.mpg").delete();
            for (int i = 0; i < poi.picFiles.length + 2; i++) {
                new File(tempDir, "image_" + String.valueOf(i) + ".jpg").delete();
            }
            num_processed_materials++;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void generateVideoVideo(String videoName, POI poi) {
        if (poi.videoFiles.length == 0) return;
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        for (int i = 0; i < poi.videoFiles.length; i++) {
            try {
                metaRetriever.setDataSource(poi.videoFiles[i].getPath());
                int height = Integer.valueOf(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
                int width = Integer.valueOf(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
                int length = Integer.valueOf(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
                String cmdDescription = poi.title + "-" + getString(R.string.video) + "-" + poi.videoFiles[i].getName();
                float ratio = Math.min((float) videoWidth / width, (float) videoHeight / height);
                int destWidth = (int) (width * ratio);
                int destHeight = (int) (height * ratio);
                runCommand(new String[]{"ffmpeg", "-i", poi.videoFiles[i].getAbsolutePath(), "-vf", "scale=" + String.valueOf(destWidth) + ":" + String.valueOf(destHeight), "temp.mpg"}, "resize:" + cmdDescription, length);
                num_processed_materials++;
                runCommand(new String[]{"ffmpeg", "-i", "temp.mpg", "-vf", "pad=" + String.valueOf(videoWidth) + ":" + String.valueOf(videoHeight) + ":(ow-iw)/2:(oh-ih)/2", "temp2.mpg"}, "padding:" + cmdDescription, length);
                num_processed_materials++;
                concatFiles(new File(tempDir, "temp2.mpg"), new File(tempDir, videoName));
                new File(tempDir, "temp.mpg").delete();
                new File(tempDir, "temp2.mpg").delete();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

        }
    }

    private void generateAudioVideo(String videoName, POI poi) {
        if (poi.audioFiles.length == 0) return;
        MediaMetadataRetriever metaRetriever = new MediaMetadataRetriever();
        for (int i = 0; i < poi.audioFiles.length; i++) {
            try {
                metaRetriever.setDataSource(poi.audioFiles[i].getPath());
                int length = Integer.valueOf(metaRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000;
                String cmdDescription = poi.title + "-" + getString(R.string.video) + "-" + poi.audioFiles[i].getName();
                runCommand(new String[]{"ffmpeg", "-i", poi.audioFiles[i].getAbsolutePath(), "temp.mpg"}, cmdDescription, length);
                concatFiles(new File(tempDir, "temp.mpg"), new File(tempDir, videoName));
                new File(tempDir, "temp.mpg").delete();
                num_processed_materials++;
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
        }
    }

    private String[] generateTrackVideos(Bitmap gmapBitmap, Point[] trackPoints) {
        int frames_per_track = secondsPerTrack * fps;
        int num_tracks = trackPoints.length / frames_per_track;
        String[] trackVideoNames = new String[num_tracks];
        Bitmap runPoint = BitmapFactory.decodeResource(getResources(), R.drawable.runpoint);
        try {
            for (int i = 0; i < num_tracks; i++) {
                BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(new File(tempDir, "input.txt"), false));
                String[] fileNames = new String[frames_per_track];
                for (int j = 0; j < frames_per_track; j++) {
                    String fileName = "track_" + String.valueOf(i) + "_" + String.valueOf(j) + ".jpg";
                    nb.setContentText(fileName);
                    nm.notify(1, nb.build());
                    Point point = trackPoints[i * frames_per_track + j];
                    Bitmap bitmap = gmapBitmap.copy(Bitmap.Config.RGB_565, true);
                    Canvas canvas = new Canvas(bitmap);
                    canvas.drawBitmap(runPoint, point.x - runPoint.getWidth() / 2, point.y - runPoint.getHeight() / 2, null);
                    fileNames[j] = fileName;
                    FileOutputStream fos = new FileOutputStream(new File(tempDir, fileName));
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    fos.flush();
                    fos.close();
                    bitmap.recycle();
                    bufferedWriter.write("file '" + fileName + "'\n");
                    Log.i("trip", fileName);
                }
                bufferedWriter.flush();
                bufferedWriter.close();
                String trackVideoName = "track_" + String.valueOf(i) + ".mpg";
                trackVideoNames[i] = trackVideoName;
                runCommand(new String[]{"ffmpeg", "-f", "concat", "-i", "input.txt", trackVideoName}, trackVideoName, secondsPerTrack);
                new File(tempDir, "input.txt").delete();
                for (int j = 0; j < fileNames.length; j++) {
                    new File(tempDir, fileNames[j]).delete();
                }
                num_processed_materials++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return trackVideoNames;
    }

    private boolean runCommand(String[] cmds, final String description, final Integer predicetedTimeLength) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < cmds.length; i++) {
            sb.append(cmds[i]).append(" ");
        }
        final String cmd = sb.toString();
        Log.i("trip", "cmd:" + cmd);
        if (description != null) {
            nb.setContentText(description);
        } else {
            nb.setContentText(cmd);
        }
        nm.notify(1, nb.build());
        try {
            if (cmds[0].equals("ffmpeg")) {
                cmds[0] = ffmpegFile.getPath();
            }
            ProcessBuilder processBuilder = new ProcessBuilder(cmds);
            processBuilder.directory(tempDir);
            processBuilder.redirectErrorStream(true);
            final Process process = processBuilder.start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = process.getInputStream();
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                        String s;
                        while ((s = bufferedReader.readLine()) != null) {
                            Log.i("trip", s);
                            if (predicetedTimeLength != null) {
                                String[] toks = s.split(" ");
                                for (int i = 0; i < toks.length; i++) {
                                    if (toks[i].contains("time=")) {
                                        String timeStr = toks[i].substring(toks[i].indexOf("time=") + 5);
                                        String[] timeToks = timeStr.split(":");
                                        float timeLength = Integer.valueOf(timeToks[0]) * 60 * 60 + Integer.valueOf(timeToks[1]) * 60 + Float.valueOf(timeToks[2]);
                                        int progress = (int) (100 * (num_processed_materials + timeLength / predicetedTimeLength) / num_total_materials);
                                        nb.setProgress(100, progress, false);
                                        if (description != null) {
                                            nb.setContentText(description + " " + timeStr);
                                        } else {
                                            nb.setContentText(cmd + " " + timeStr);
                                        }
                                        nm.notify(1, nb.build());
                                        break;
                                    }
                                }
                            }
                        }
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            int exitVal = process.waitFor();
            Log.i("trip", "exit:" + String.valueOf(exitVal));
            if (exitVal == 0) { //success
                return true;
            } else { //failure
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean concatFiles(File source, File dest) {
        Log.i("trip", "concat:" + source.getPath() + ">>" + dest.getPath());
        nb.setContentText("concat" + source.getPath() + ">>" + dest.getPath());
        nm.notify(1, nb.build());
        try {
            FileInputStream fileInputStream = new FileInputStream(source);
            FileOutputStream fileOutputStream = new FileOutputStream(dest, true);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = fileInputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
            fileInputStream.close();
            fileOutputStream.close();
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void copyFFmpeg() {
        try {
            InputStream inputStream = getAssets().open("ffmpeg");
            FileOutputStream fileOutputStream = new FileOutputStream(ffmpegFile);
            byte[] buffer = new byte[4096];
            int len;
            while ((len = inputStream.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
            fileOutputStream.close();
            inputStream.close();
            try {
                ProcessBuilder processBuilder = new ProcessBuilder(new String[]{"chmod", "777", ffmpegFile.getPath()});
                processBuilder.redirectErrorStream(true);
                Process process = processBuilder.start();
                process.waitFor();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int getNum_total_materials() {
        int num = 0;
        num += pois.length + 1; //num tracks
        for (int i = 0; i < pois.length; i++) {
            POI poi = pois[i];
            num++;//diary
            if (poi.picFiles.length > 0) {
                num++;
            }
            num += poi.videoFiles.length * 2;
            num += poi.audioFiles.length;
        }
        return num;
    }

    @Override
    public void onDestroy() {
        if (resultVideoPath != null) {
            nb.setContentTitle(getString(R.string.finish));
            nb.setContentText(resultVideoPath);
            nb.setOngoing(false);
            nb.setProgress(0, 0, false);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(resultVideoPath), "video/*");
            nb.setContentIntent(PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));
            Intent intent2 = new Intent(Intent.ACTION_SEND);
            intent2.setType("video/*");
            intent2.putExtra(Intent.EXTRA_STREAM, Uri.parse(resultVideoPath));
            nb.addAction(android.R.drawable.ic_menu_share, getString(R.string.share), PendingIntent.getActivity(this, 0, intent2, PendingIntent.FLAG_UPDATE_CURRENT));
            nm.notify(1, nb.build());
        } else {
            nm.cancel(1);
        }
        super.onDestroy();
    }
}
