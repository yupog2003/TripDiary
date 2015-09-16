package com.yupog2003.tripdiary.views;

import android.content.Context;
import android.util.AttributeSet;

public class SquareCheckableLayout extends CheckableLayout{
    public SquareCheckableLayout(Context context) {
        super(context);
    }

    public SquareCheckableLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SquareCheckableLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, widthMeasureSpec);
    }
}
