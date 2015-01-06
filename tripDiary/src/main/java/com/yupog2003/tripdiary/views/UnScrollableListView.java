package com.yupog2003.tripdiary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class UnScrollableListView extends ListView{
	public UnScrollableListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public UnScrollableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public UnScrollableListView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		int heightSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightSpec);
		getLayoutParams().height=getMeasuredHeight();
	}
	

}
