package com.yupog2003.tripdiary;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.widget.ViewFlipper;

import com.yupog2003.tripdiary.data.DeviceHelper;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.TimeAnalyzer;

import java.io.File;
import java.util.ArrayList;

public class PlayPointActivity extends MyActivity implements View.OnClickListener {
    POI poi;
    String pointPath;
    String picturePath;
    String videoPath;
    String audioPath;
    String textPath;
    ProgressDialog pd;
    ViewFlipper viewFlipper;
    Thread playThread;
    ImageButton pause;
    ImageButton skip;
    ImageButton next;
    String name;
    boolean threadpause = false;
    boolean mediafinish = false;
    boolean stop = false;
    int currentIndex = 0;
    Handler handler;
    MediaPlayer mp;
    VideoView videoView;
    int interval;
    private static final int readTextSpeed=100; //milli seconds per character

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_point);
        this.pointPath = getIntent().getStringExtra("path");
        poi = new POI(new File(pointPath));
        this.name = poi.title;
        this.picturePath = poi.picDir.getPath();
        this.videoPath = poi.videoDir.getPath();
        this.audioPath = poi.audioDir.getPath();
        this.textPath = poi.diaryFile.getPath();
        viewFlipper = (ViewFlipper) findViewById(R.id.pointviewflipper);
        viewFlipper.setInAnimation(this, android.R.anim.fade_in);
        viewFlipper.setOutAnimation(this, android.R.anim.fade_out);
        pause = (ImageButton) findViewById(R.id.pause);
        pause.setOnClickListener(this);
        skip = (ImageButton) findViewById(R.id.skip);
        skip.setOnClickListener(this);
        next = (ImageButton) findViewById(R.id.next);
        next.setOnClickListener(this);
        handler = new Handler();
        setTitle(name + TimeAnalyzer.formatInTimezone(poi.time, TimeAnalyzer.getPOITimeZone(PlayPointActivity.this, pointPath)));
        new PrepareViewsTask().execute("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_play_point, menu);
        return true;
    }

    class PrepareViewsTask extends AsyncTask<String, String, ArrayList<View>> {

        @Override
        protected ArrayList<View> doInBackground(String... params) {
            // TODO Auto-generated method stub
            // ---------------------------------text-------------------------------
            publishProgress(getString(R.string.prepare_diary));
            final TextView textView = new TextView(PlayPointActivity.this);
            int padding = (int) DeviceHelper.pxFromDp(PlayPointActivity.this, 10);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText(poi.diary);
            File fontFile = new File(PreferenceManager.getDefaultSharedPreferences(PlayPointActivity.this).getString("diaryfont", ""));
            if (fontFile.exists() && fontFile.isFile()) {
                try {
                    textView.setTypeface(Typeface.createFromFile(fontFile));
                } catch (RuntimeException e) {
                    Toast.makeText(PlayPointActivity.this, getString(R.string.invalid_font), Toast.LENGTH_SHORT).show();
                }
            }

            textView.setTextSize(PreferenceManager.getDefaultSharedPreferences(PlayPointActivity.this).getInt("diaryfontsize", 20));
            runOnUiThread(new Runnable() {

                public void run() {
                    // TODO Auto-generated method stub
                    viewFlipper.addView(textView);
                }

            });
            // -------------------------------------picture--------------------------
            publishProgress(getString(R.string.prepare_pictures));

            int width = DeviceHelper.getScreenWidth(PlayPointActivity.this);
            int height = DeviceHelper.getScreenHeight(PlayPointActivity.this);
            BitmapFactory.Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = true;
            for (int i = 0; i < poi.picFiles.length; i++) {
                BitmapFactory.Options option2 = new BitmapFactory.Options();
                File file = poi.picFiles[i];
                final ImageView img = new ImageView(PlayPointActivity.this);
                BitmapFactory.decodeFile(file.getPath(), option);
                if (option.outWidth / width > option.outHeight / height) {
                    option2.inSampleSize = option.outWidth / width;
                } else {
                    option2.inSampleSize = option.outHeight / height;
                }
                img.setTag(new ImgTag(file, option2));
                runOnUiThread(new Runnable() {

                    public void run() {
                        // TODO Auto-generated method stub
                        viewFlipper.addView(img);
                    }
                });
            }

            // ------------------------------------------video------------------------
            publishProgress(getString(R.string.prepare_video));
            for (int i = 0; i < poi.videoFiles.length; i++) {
                final int index = i;
                handler.post(new Runnable() {

                    public void run() {
                        // TODO Auto-generated method stub
                        VideoView videoView = new VideoView(PlayPointActivity.this);
                        videoView.setVideoPath(poi.videoFiles[index].getPath());
                        videoView.setMediaController(new MediaController(PlayPointActivity.this));
                        viewFlipper.addView(videoView);
                    }

                });
            }

            // ----------------------------------audio--------------------------
            publishProgress(getString(R.string.prepare_audio));
            for (int i = 0; i < poi.audioFiles.length; i++) {
                final TextView audiotext = new TextView(PlayPointActivity.this);
                audiotext.setText(poi.audioFiles[i].getName());
                audiotext.setTextSize(30);
                runOnUiThread(new Runnable() {

                    public void run() {
                        // TODO Auto-generated method stub
                        viewFlipper.addView(audiotext);
                    }
                });
            }

            return null;
        }

        @Override
        protected void onProgressUpdate(String... progress) {
            pd.setMessage(progress[0]);
        }

        @Override
        protected void onPreExecute() {
            interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(PlayPointActivity.this).getString("playpoispeed", "1000"));
            pd = new ProgressDialog(PlayPointActivity.this);
            pd.setTitle(getString(R.string.prepare_data));
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pd.setMessage("");
            pd.setCancelable(false);
            pd.show();
        }

        @Override
        protected void onPostExecute(ArrayList<View> result) {
            pd.dismiss();
            playThread = new Thread(new Runnable() {

                public void run() {
                    // TODO Auto-generated method stub
                    for (currentIndex = 0; currentIndex < viewFlipper.getChildCount(); currentIndex++) {
                        if (viewFlipper.getChildAt(currentIndex) instanceof ImageView) {
                            final ImageView img = (ImageView) viewFlipper.getChildAt(currentIndex);
                            final ImgTag tag = (ImgTag) img.getTag();
                            Bitmap bitmap = null;
                            while (true) {
                                try {
                                    bitmap = BitmapFactory.decodeFile(tag.file.getPath(), tag.option);
                                    break;
                                } catch (OutOfMemoryError e) {
                                    e.printStackTrace();
                                    System.gc();
                                    continue;
                                }
                            }
                            final Bitmap b = bitmap;
                            handler.post(new Runnable() {
                                public void run() {
                                    // TODO Auto-generated method stub
                                    img.setImageBitmap(b);
                                }
                            });
                            try {
                                Thread.sleep(interval);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (viewFlipper.getChildAt(currentIndex) instanceof VideoView) {
                            videoView = (VideoView) viewFlipper.getChildAt(currentIndex);
                            videoView.start();
                            mediafinish = false;
                            videoView.setOnCompletionListener(new OnCompletionListener() {

                                public void onCompletion(MediaPlayer mp) {
                                    // TODO Auto-generated method stub
                                    mediafinish = true;
                                }
                            });
                            while (!mediafinish) {
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        } else if (viewFlipper.getChildAt(currentIndex) instanceof TextView && currentIndex == 0) {
                            try {
                                TextView textView=(TextView)viewFlipper.getChildAt(currentIndex);
                                Thread.sleep(textView.getText().length()*readTextSpeed);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        } else if (viewFlipper.getChildAt(currentIndex) instanceof TextView) {
                            mp = MediaPlayer.create(PlayPointActivity.this, Uri.fromFile(new File(audioPath + "/" + ((TextView) viewFlipper.getChildAt(currentIndex)).getText().toString())));
                            mp.start();
                            mediafinish = false;
                            mp.setOnCompletionListener(new OnCompletionListener() {

                                public void onCompletion(MediaPlayer mp) {
                                    // TODO Auto-generated method stub
                                    mediafinish = true;
                                }
                            });
                            while (!mediafinish) {
                                try {
                                    Thread.sleep(50);
                                } catch (InterruptedException e) {
                                    // TODO Auto-generated catch block
                                    e.printStackTrace();
                                }
                            }
                        }
                        if (stop)
                            break;
                        while (threadpause) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                        runOnUiThread(new Runnable() {

                            public void run() {
                                // TODO Auto-generated method stub
                                viewFlipper.showNext();
                            }
                        });
                    }
                    PlayPointActivity.this.finish();
                }
            });
            playThread.start();
        }
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        if (v.equals(skip)) {
            if (mp != null) {
                mp.stop();
                mp.release();
            }
            stop = true;
            PlayPointActivity.this.finish();
        } else if (v.equals(pause)) {
            if (mp != null) {
                if (mp.isPlaying()) {
                    mp.pause();
                } else {
                    mp.start();
                }
            } else if (videoView != null) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                } else {
                    videoView.start();
                }
            }
            if (threadpause) {
                pause.setImageResource(R.drawable.ic_pause);
                threadpause = false;
            } else {
                pause.setImageResource(R.drawable.ic_resume);
                threadpause = true;
            }

        } else if (v.equals(next)) {
            if (!mediafinish) {
                mediafinish = true;
            }
            if (threadpause) {
                pause.setImageResource(R.drawable.ic_pause);
                threadpause = false;
            }
            if (playThread != null) {
                playThread.interrupt();
            }
            if (mp != null) {
                if (mp.isPlaying()) {
                    mp.stop();
                    mp.release();
                }
            }
        }
    }

    private class ImgTag {
        public File file;
        public BitmapFactory.Options option;

        public ImgTag(File file, BitmapFactory.Options option) {
            this.file = file;
            this.option = option;
        }
    }
}
