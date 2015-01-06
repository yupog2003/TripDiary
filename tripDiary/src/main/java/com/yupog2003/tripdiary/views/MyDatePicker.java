package com.yupog2003.tripdiary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;
import android.widget.DatePicker;

public class MyDatePicker extends DatePicker{
	public MyDatePicker(Context context,AttributeSet attrs,int defStyle){
		super(context,attrs,defStyle);
	}
	public MyDatePicker(Context context,AttributeSet attrs){
		super(context, attrs);
	}
	public MyDatePicker(Context context){
		super(context);
	}
	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev){
		if (ev.getActionMasked()==MotionEvent.ACTION_DOWN){
			ViewParent p=getParent();
			if (p!=null){
				p.requestDisallowInterceptTouchEvent(true);
			}
		}
		return false;
	}
}
