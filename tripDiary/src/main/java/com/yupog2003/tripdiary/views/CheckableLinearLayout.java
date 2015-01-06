package com.yupog2003.tripdiary.views;

import android.content.Context;
import android.graphics.Color;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable{
	public boolean checked;
	public CheckableLinearLayout(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public boolean isChecked() {
		// TODO Auto-generated method stub
		return checked;
	}

	public void setChecked(boolean checked) {
		// TODO Auto-generated method stub
		this.checked=checked;
		setBackgroundColor(checked?Color.parseColor("#FFEC70"):Color.TRANSPARENT);
	}

	public void toggle() {
		// TODO Auto-generated method stub
		setChecked(!checked);
	}

}
