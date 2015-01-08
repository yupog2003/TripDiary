package com.yupog2003.tripdiary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class UnScrollableGridView extends GridView{
	public UnScrollableGridView(Context context) {
		super(context);
		 Auto-generated constructor stub
	}
	public UnScrollableGridView(Context context, AttributeSet attrs) {
		super(context, attrs);
		 Auto-generated constructor stub
	}
	public UnScrollableGridView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
		 Auto-generated constructor stub
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		int heightSpec=MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, heightSpec);
		getLayoutParams().height=getMeasuredHeight();
	}
	

}
