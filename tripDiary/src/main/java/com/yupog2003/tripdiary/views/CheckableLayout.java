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
		// TODO Auto-generated constructor stub
	}

	public CheckableLayout(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	public CheckableLayout(Context context, AttributeSet attrs, int defStyle) {

		super(context, attrs, defStyle);
	}

	public boolean isChecked() {
		// TODO Auto-generated method stub
		return checked;
	}

	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub
		this.checked = checked;
		setBackgroundColor(checked ? Color.parseColor("#FFEC70") :Color.TRANSPARENT);
	}

	public void toggle() {
		// TODO Auto-generated method stub
		setChecked(!checked);
	}

}
