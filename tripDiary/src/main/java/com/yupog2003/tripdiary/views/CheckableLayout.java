package com.yupog2003.tripdiary.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.FrameLayout;

public class CheckableLayout extends FrameLayout implements Checkable {
	public boolean checked;

	public CheckableLayout(Context context) {
		super(context);
		 Auto-generated constructor stub
	}

	public CheckableLayout(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	public CheckableLayout(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
	}

	public boolean isChecked() {

		return checked;
	}

	public void setChecked(boolean checked) {

		this.checked = checked;
		setBackgroundColor(checked ? Color.parseColor("#FFEC70") :Color.TRANSPARENT);
	}

	public void toggle() {

		setChecked(!checked);
	}

}
