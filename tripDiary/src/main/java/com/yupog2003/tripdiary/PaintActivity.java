package com.yupog2003.tripdiary;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import yuku.ambilwarna.AmbilWarnaDialog;

public class PaintActivity extends MyActivity implements OnClickListener {
    ImageButton paintColor;
    ImageButton paintSetting;
    ImageButton paintEraser;
    ImageButton paintBackground;
    MyPaintView paintView;
    Paint paint;
    int backgroundColor = Color.WHITE;
    int brushColor = Color.RED;
    boolean isEmboss = false;
    boolean isBlur = false;
    int thicknessValue = 10;
    boolean clearMode;
    Uri outputUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paint);
        Toolbar toolBar = (Toolbar) findViewById(R.id.toolbar);
        if (toolBar != null) {
            setSupportActionBar(toolBar);
        }
        paintColor = (ImageButton) findViewById(R.id.paint_color);
        paintSetting = (ImageButton) findViewById(R.id.paint_setting);
        paintEraser = (ImageButton) findViewById(R.id.paint_eraser);
        paintBackground = (ImageButton) findViewById(R.id.paint_background);
        paintColor.setOnClickListener(this);
        paintSetting.setOnClickListener(this);
        paintEraser.setOnClickListener(this);
        paintBackground.setOnClickListener(this);
        paintView = new MyPaintView(PaintActivity.this);
        paintView.setDrawingCacheEnabled(true);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setColor(brushColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(thicknessValue);
        paintView.setLayerType(View.LAYER_TYPE_SOFTWARE, paint);
        LinearLayout layout = (LinearLayout) findViewById(R.id.paintview);
        layout.addView(paintView);
        clearMode = false;
        outputUri = getIntent().getParcelableExtra(MediaStore.EXTRA_OUTPUT);
        if (outputUri == null) {
            outputUri = Uri.fromFile(new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), String.valueOf(System.currentTimeMillis()) + ".png"));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_paint, menu);
        return true;
    }

    public void onClick(View v) {

        if (v.equals(paintColor)) {
            AmbilWarnaDialog.OnAmbilWarnaListener listener = new AmbilWarnaDialog.OnAmbilWarnaListener() {

                public void onOk(AmbilWarnaDialog dialog, int color) {
                    brushColor = color;
                    paint.setColor(brushColor);
                }

                public void onCancel(AmbilWarnaDialog dialog) {

                }
            };
            AmbilWarnaDialog dialog = new AmbilWarnaDialog(PaintActivity.this, brushColor, listener);
            dialog.show();
        } else if (v.equals(paintSetting)) {
            final int tempThicknessValue = thicknessValue;
            final boolean tempIsEmboss = isEmboss;
            final boolean tempIsBlur = isBlur;
            LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.paint_setting, (ViewGroup) findViewById(android.R.id.content), false);
            SeekBar thicknessBar = (SeekBar) layout.findViewById(R.id.thickness);
            thicknessBar.setMax(50);
            thicknessBar.setProgress(tempThicknessValue);
            thicknessBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

                public void onStopTrackingTouch(SeekBar seekBar) {


                }

                public void onStartTrackingTouch(SeekBar seekBar) {


                }

                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                    thicknessValue = progress;

                }
            });
            final CheckBox checkEmboss = (CheckBox) layout.findViewById(R.id.checkemboss);
            final CheckBox checkBlur = (CheckBox) layout.findViewById(R.id.checkeblur);
            checkEmboss.setChecked(tempIsEmboss);
            checkBlur.setChecked(tempIsBlur);
            checkEmboss.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    isEmboss = isChecked;
                    if (isChecked) {
                        checkBlur.setChecked(false);
                    }
                }
            });
            checkBlur.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    isBlur = isChecked;
                    if (isChecked) {
                        checkEmboss.setChecked(false);
                    }
                }
            });
            AlertDialog.Builder ab = new AlertDialog.Builder(PaintActivity.this);
            ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    paint.setStrokeWidth(thicknessValue);
                    if (isEmboss) {
                        paint.setMaskFilter(new EmbossMaskFilter(new float[]{1, 1, 1}, 0.4f, 6, thicknessValue * 0.3f));
                    } else if (isBlur) {
                        paint.setMaskFilter(new BlurMaskFilter(thicknessValue * 0.6f, BlurMaskFilter.Blur.NORMAL));
                    } else {
                        paint.setMaskFilter(null);
                    }
                }
            });
            ab.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {

                    thicknessValue = tempThicknessValue;
                    isEmboss = tempIsEmboss;
                    isBlur = tempIsBlur;
                }
            });
            ab.setView(layout);
            ab.show();
        } else if (v.equals(paintEraser)) {
            clearMode = !clearMode;
            paint.setXfermode(clearMode ? new PorterDuffXfermode(PorterDuff.Mode.CLEAR) : null);
            Toast.makeText(PaintActivity.this, clearMode ? getString(R.string.eraser_mode) : getString(R.string.brush_mode), Toast.LENGTH_LONG).show();
        } else if (v.equals(paintBackground)) {
            AmbilWarnaDialog.OnAmbilWarnaListener listener = new AmbilWarnaDialog.OnAmbilWarnaListener() {

                public void onOk(AmbilWarnaDialog dialog, int color) {

                    backgroundColor = color;
                    paintView.onBackgroundColorChanged();
                }

                public void onCancel(AmbilWarnaDialog dialog) {


                }
            };
            AmbilWarnaDialog dialog = new AmbilWarnaDialog(PaintActivity.this, Color.RED, listener);
            dialog.show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.save) {
            if (outputUri == null) PaintActivity.this.finish();
            try {
                BufferedOutputStream bos = new BufferedOutputStream(getContentResolver().openOutputStream(outputUri));
                paintView.buildDrawingCache();
                Bitmap bitmap = paintView.getDrawingCache();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
                bos.flush();
                bos.close();
                bitmap.recycle();
                System.gc();
                Intent intent = new Intent();
                intent.setData(outputUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                setResult(Activity.RESULT_OK, intent);
                PaintActivity.this.finish();
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public class MyPaintView extends View {

        private Bitmap bitmap;
        private Canvas canvas;
        private Path path;
        private Paint bitmapPaint;
        private float mX, mY;
        private int mW, mH;
        private ArrayList<Path> paths = new ArrayList<>();
        private ArrayList<Paint> paints = new ArrayList<>();
        private static final float TOUCH_TOLERANCE = 4;

        public MyPaintView(Context context) {
            super(context);
            path = new Path();
            bitmapPaint = new Paint(Paint.DITHER_FLAG);
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);
            mW = w;
            mH = h;
            bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.drawColor(backgroundColor);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawColor(backgroundColor);
            canvas.drawBitmap(bitmap, 0, 0, bitmapPaint);
            canvas.drawPath(path, paint);
        }

        public void onBackgroundColorChanged() {
            bitmap = Bitmap.createBitmap(mW, mH, Bitmap.Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.drawColor(backgroundColor);
            for (int i = 0; i < paths.size(); i++) {
                canvas.drawPath(paths.get(i), paints.get(i));
            }
            invalidate();
        }

        private void onTouchStart(float x, float y) {
            path.reset();
            path.moveTo(x, y);
            mX = x;
            mY = y;
        }

        private void onTouchMove(float x, float y) {
            float dx = Math.abs(x - mX);
            float dy = Math.abs(y - mY);
            if (dx > TOUCH_TOLERANCE || dy > TOUCH_TOLERANCE) {
                path.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
                mX = x;
                mY = y;
            }
        }

        private void onTouchUp() {
            path.lineTo(mX, mY);
            canvas.drawPath(path, paint);
            paths.add(path);
            Paint p = new Paint();
            p.set(paint);
            paints.add(p);
            path = new Path();
        }

        @Override
        public boolean onTouchEvent(@NonNull MotionEvent event) {
            float x = event.getX();
            float y = event.getY();
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    onTouchStart(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    onTouchMove(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    onTouchUp();
                    invalidate();
                    break;
            }
            return true;
        }
    }
}
