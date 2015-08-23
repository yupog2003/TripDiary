package com.yupog2003.tripdiary;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
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
import com.yupog2003.tripdiary.data.FileHelper;
import com.yupog2003.tripdiary.data.MyCalendar;
import com.yupog2003.tripdiary.data.POI;
import com.yupog2003.tripdiary.data.documentfile.DocumentFile;

import java.io.File;

public class PlayPointActivity extends MyActivity implements View.OnClickListener {
    POI poi;
    ViewFlipper viewFlipper;
    Thread playThread;
    ImageButton pause;
    ImageButton skip;
    ImageButton next;
    boolean threadpause = false;
    boolean mediafinish = false;
    boolean stop = false;
    int currentIndex = 0;
    Handler handler;
    MediaPlayer mp;
    VideoView videoView;
    int interval;
    private static final int readTextSpeed = 100; //milli seconds per character
    public static final String tag_trip = "tag_trip";
    public static final String tag_poi = "tag_poi";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_play_point);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
        }
        String tripName = getIntent().getStringExtra(tag_trip);
        String poiName = getIntent().getStringExtra(tag_poi);
        try {
            poi = ((TripDiaryApplication) getApplication()).getTrip(tripName).getPOI(poiName);
        } catch (NullPointerException e) {
            e.printStackTrace();
            finish();
            return;
        }
        if (poi == null) {
            finish();
            return;
        }
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
        interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(PlayPointActivity.this).getString("playpoispeed", "1000"));
        setTitle(poi.title + poi.time.formatInTimezone(MyCalendar.getTripTimeZone(this, poi.dir.getParentFile().getName())));
        prepareViews();
        play();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_play_point, menu);
        return true;
    }

    private void play() {
        playThread = new Thread(new Runnable() {

            public void run() {

                for (currentIndex = 0; currentIndex < viewFlipper.getChildCount(); currentIndex++) {
                    if (viewFlipper.getChildAt(currentIndex) instanceof ImageView) {
                        final ImageView img = (ImageView) viewFlipper.getChildAt(currentIndex);
                        final ImgTag tag = (ImgTag) img.getTag();
                        Bitmap bitmap;
                        while (true) {
                            try {
                                bitmap = BitmapFactory.decodeStream(tag.file.getInputStream(), new Rect(0, 0, 0, 0), tag.option);
                                break;
                            } catch (OutOfMemoryError e) {
                                e.printStackTrace();
                                System.gc();
                            } catch (IllegalArgumentException e) {
                                e.printStackTrace();
                            }
                        }
                        final Bitmap b = bitmap;
                        handler.post(new Runnable() {
                            public void run() {
                                img.setImageBitmap(b);
                            }
                        });
                        try {
                            Thread.sleep(interval);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                img.setImageBitmap(null);
                                b.recycle();
                            }
                        });
                    } else if (viewFlipper.getChildAt(currentIndex) instanceof VideoView) {
                        videoView = (VideoView) viewFlipper.getChildAt(currentIndex);
                        videoView.start();
                        mediafinish = false;
                        videoView.setOnCompletionListener(new OnCompletionListener() {

                            public void onCompletion(MediaPlayer mp) {
                                mediafinish = true;
                            }
                        });
                        while (!mediafinish) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {

                                e.printStackTrace();
                            }
                        }
                    } else if (viewFlipper.getChildAt(currentIndex) instanceof TextView && currentIndex == 0) {
                        try {
                            TextView textView = (TextView) viewFlipper.getChildAt(currentIndex);
                            Thread.sleep(textView.getText().length() * readTextSpeed);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else if (viewFlipper.getChildAt(currentIndex) instanceof TextView) {
                        String audioName = ((TextView) viewFlipper.getChildAt(currentIndex)).getText().toString();
                        mp = MediaPlayer.create(PlayPointActivity.this, FileHelper.findfile(poi.audioDir, audioName).getUri());
                        mp.start();
                        mediafinish = false;
                        mp.setOnCompletionListener(new OnCompletionListener() {

                            public void onCompletion(MediaPlayer mp) {
                                mediafinish = true;
                            }
                        });
                        while (!mediafinish) {
                            try {
                                Thread.sleep(50);
                            } catch (InterruptedException e) {
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
                            e.printStackTrace();
                        }
                    }
                    runOnUiThread(new Runnable() {

                        public void run() {
                            viewFlipper.showNext();
                        }
                    });
                }
                PlayPointActivity.this.finish();
            }
        });
        playThread.start();
    }

    private void prepareViews() {
        prepareDiary();
        preparePictures();
        prepareVideos();
        prepareAudios();
    }

    private void prepareDiary() {
        final TextView textView = new TextView(PlayPointActivity.this);
        int padding = (int) DeviceHelper.pxFromDp(PlayPointActivity.this, 10);
        textView.setPadding(padding, padding, padding, padding);
        textView.setText(poi.diary);
        File fontFile = new File(getActivity().getFilesDir(), PreferenceManager.getDefaultSharedPreferences(PlayPointActivity.this).getString("diaryfont", ""));
        if (fontFile.exists() && fontFile.isFile()) {
            try {
                textView.setTypeface(Typeface.createFromFile(fontFile));
            } catch (RuntimeException e) {
                Toast.makeText(PlayPointActivity.this, getString(R.string.invalid_font), Toast.LENGTH_SHORT).show();
            }
        }
        textView.setTextSize(PreferenceManager.getDefaultSharedPreferences(PlayPointActivity.this).getInt("diaryfontsize", 20));
        viewFlipper.addView(textView);
    }

    private void preparePictures() {
        int width = DeviceHelper.getScreenWidth(PlayPointActivity.this);
        int height = DeviceHelper.getScreenHeight(PlayPointActivity.this);
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        for (int i = 0; i < poi.picFiles.length; i++) {
            BitmapFactory.Options option2 = new BitmapFactory.Options();
            DocumentFile file = poi.picFiles[i];
            final ImageView img = new ImageView(PlayPointActivity.this);
            try {
                BitmapFactory.decodeStream(file.getInputStream(), new Rect(0, 0, 0, 0), option);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            if (option.outWidth / width > option.outHeight / height) {
                option2.inSampleSize = option.outWidth / width;
            } else {
                option2.inSampleSize = option.outHeight / height;
            }
            img.setTag(new ImgTag(file, option2));
            viewFlipper.addView(img);

        }
    }

    private void prepareVideos() {
        for (int i = 0; i < poi.videoFiles.length; i++) {
            VideoView videoView = new VideoView(PlayPointActivity.this);
            videoView.setVideoURI(poi.videoFiles[i].getUri());
            videoView.setMediaController(new MediaController(PlayPointActivity.this));

        }
    }

    private void prepareAudios() {
        for (int i = 0; i < poi.audioFiles.length; i++) {
            TextView audiotext = new TextView(PlayPointActivity.this);
            audiotext.setText(poi.audioFiles[i].getName());
            audiotext.setTextSize(30);
            viewFlipper.addView(audiotext);
        }
    }

    public void onClick(View v) {

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
                pause.setImageResource(R.drawable.ic_play);
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
        public DocumentFile file;
        public BitmapFactory.Options option;

        public ImgTag(DocumentFile file, BitmapFactory.Options option) {
            this.file = file;
            this.option = option;
        }
    }
}
