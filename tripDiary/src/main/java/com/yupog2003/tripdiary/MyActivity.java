package com.yupog2003.tripdiary;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MyActivity extends AppCompatActivity {

    public Activity getActivity() {
        return this;
    }

    public static List<ActivityManager.AppTask> getMyTasks(Context context){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ActivityManager activityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
            return activityManager.getAppTasks();
        }else{
            return new ArrayList<>();
        }

    }

    public static ActivityManager.AppTask findViewTripActivityTask(Context context, String tripName){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            for (ActivityManager.AppTask task : getMyTasks(context)){
                String s = task.getTaskInfo().baseIntent.getStringExtra(ViewTripActivity.tag_tripName);
                if (s != null && s.equals(tripName)) {
                    return task;
                }
            }
        }
        return null;
    }
}
