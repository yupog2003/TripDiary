package com.yupog2003.tripdiary;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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
	EmbossMaskFilter emboss;
	boolean isEmboss = false;
	BlurMaskFilter blur;
	boolean isBlur = false;
	int thicknessValue = 10;
	boolean clearMode;
	String path;

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
		paint = new Paint();
		paint.setAntiAlias(true);
		paint.setDither(true);
		paint.setColor(brushColor);
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeJoin(Paint.Join.ROUND);
		paint.setStrokeCap(Paint.Cap.ROUND);
		paint.setStrokeWidth(thicknessValue);
		LinearLayout layout = (LinearLayout) findViewById(R.id.paintview);
		layout.addView(paintView);
		clearMode = false;
		path = getIntent().getStringExtra("path");
		if (path == null) {
			PaintActivity.this.finish();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_paint, menu);
		return true;
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v.equals(paintColor)) {
			AmbilWarnaDialog.OnAmbilWarnaListener listener = new AmbilWarnaDialog.OnAmbilWarnaListener() {

				public void onOk(AmbilWarnaDialog dialog, int color) {
					// TODO Auto-generated method stub
					brushColor = color;
					paint.setColor(brushColor);
				}

				public void onCancel(AmbilWarnaDialog dialog) {
					// TODO Auto-generated method stub

				}
			};
			AmbilWarnaDialog dialog = new AmbilWarnaDialog(PaintActivity.this, brushColor, listener);
			dialog.show();
		} else if (v.equals(paintSetting)) {
			final int tempThicknessValue = thicknessValue;
			final boolean tempIsEmboss = isEmboss;
			final boolean tempIsBlur = isBlur;
			LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.paint_setting, null);
			SeekBar thicknessBar = (SeekBar) layout.findViewById(R.id.thickness);
			thicknessBar.setMax(50);
			thicknessBar.setProgress(tempThicknessValue);
			thicknessBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

				public void onStopTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				public void onStartTrackingTouch(SeekBar seekBar) {
					// TODO Auto-generated method stub

				}

				public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
					// TODO Auto-generated method stub
					thicknessValue = progress;

				}
			});
			final CheckBox checkEmboss = (CheckBox) layout.findViewById(R.id.checkemboss);
			final CheckBox checkBlur = (CheckBox) layout.findViewById(R.id.checkeblur);
			checkEmboss.setChecked(tempIsEmboss);
			checkBlur.setChecked(tempIsBlur);
			checkEmboss.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					isEmboss = isChecked;
					if (isChecked) {
						checkBlur.setChecked(false);
					}
				}
			});
			checkBlur.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					isBlur = isChecked;
					if (isChecked) {
						checkEmboss.setChecked(false);
					}
				}
			});
			AlertDialog.Builder ab = new AlertDialog.Builder(PaintActivity.this);
			ab.setPositiveButton(getString(R.string.enter), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					paint.setStrokeWidth(thicknessValue);
					if (isEmboss) {
						paint.setMaskFilter(new EmbossMaskFilter(new float[] { 1, 1, 1 }, 0.4f, 6, thicknessValue * 0.3f));
					} else if (isBlur) {
						paint.setMaskFilter(new BlurMaskFilter(thicknessValue * 0.6f, BlurMaskFilter.Blur.NORMAL));
					} else {
						paint.setMaskFilter(null);
					}
				}
			});
			ab.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
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
					// TODO Auto-generated method stub
					backgroundColor = color;
					paintView.onBackgroundColorChanged();
				}

				public void onCancel(AmbilWarnaDialog dialog) {
					// TODO Auto-generated method stub

				}
			};
			AmbilWarnaDialog dialog = new AmbilWarnaDialog(PaintActivity.this, Color.RED, listener);
			dialog.show();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.save && path != null) {
			try {
				BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(path));
				Bitmap bitmap = paintView.getBitmap();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
				bos.flush();
				bos.close();
				bitmap.recycle();
				System.gc();
				setResult(Activity.RESULT_OK);
				PaintActivity.this.finish();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
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
		private ArrayList<Path> paths = new ArrayList<Path>();
		private ArrayList<Paint> paints = new ArrayList<Paint>();
		private static final float TOUCH_TOLERANCE = 4;

		public MyPaintView(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
			path = new Path();
			bitmapPaint = new Paint(Paint.DITHER_FLAG);
		}

		public Bitmap getBitmap() {
			return bitmap;
		}

		public Canvas getCanvas() {
			return canvas;
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
		public boolean onTouchEvent(MotionEvent event) {
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
