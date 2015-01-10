package com.yupog2003.tripdiary.views;

import android.content.Context;
import android.graphics.Color;
import android.widget.Checkable;
import android.widget.LinearLayout;

public class CheckableLinearLayout extends LinearLayout implements Checkable{
	public boolean checked;
	public CheckableLinearLayout(Context context) {
		super(context);

	}

	public boolean isChecked() {

		return checked;
	}

	public void setChecked(boolean checked) {

		this.checked=checked;
		setBackgroundColor(checked?Color.parseColor("#FFEC70"):Color.TRANSPARENT);
	}

	public void toggle() {

		setChecked(!checked);
	}

}
