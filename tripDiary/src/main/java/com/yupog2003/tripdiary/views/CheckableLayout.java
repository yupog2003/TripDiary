package com.yupog2003.tripdiary.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.yupog2003.tripdiary.R;
import com.yupog2003.tripdiary.data.DeviceHelper;

public class CheckableLayout extends RelativeLayout implements Checkable {
    public boolean checked;
    ImageView checkImage;

    public CheckableLayout(Context context) {
        super(context);
        initialCheckImage(context);
    }

    public CheckableLayout(Context context, AttributeSet attrs) {

        super(context, attrs);
        initialCheckImage(context);
    }

    public CheckableLayout(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
        initialCheckImage(context);
    }

    private void initialCheckImage(Context context) {
        checkImage = new ImageView(context);
        checkImage.setImageResource(R.drawable.ic_unchecked);
        checkImage.setScaleType(ImageView.ScaleType.FIT_XY);
        int dp30 = (int) DeviceHelper.pxFromDp(context, 30);
        int dp5 = (int) DeviceHelper.pxFromDp(context, 5);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dp30, dp30);
        params.addRule(ALIGN_PARENT_TOP);
        params.addRule(ALIGN_PARENT_RIGHT);
        params.setMargins(0, dp5, dp5, 0);
        checkImage.setLayoutParams(params);
    }

    public boolean isChecked() {
        return checked;
    }

    boolean onMultiChoiceMode = false;

    public void setOnMultiChoiceMode(boolean onMultiChoiceMode) {
        this.onMultiChoiceMode = onMultiChoiceMode;
        if (onMultiChoiceMode) {
            if (checkImage.getParent() == null) {
                this.addView(checkImage);
            }
        } else {
            this.removeView(checkImage);
        }
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
        checkImage.setImageResource(checked ? R.drawable.ic_checked : R.drawable.ic_unchecked);
    }

    public void toggle() {
        setChecked(!checked);
    }

}
