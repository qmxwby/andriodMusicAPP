package com.example.musicapplication.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

public class ActivityMananer {
    private static List<Activity> activityList = null;

    //添加activity
    public static void addActivity(Activity activity) {
        if (activityList == null) {
            activityList = new ArrayList<>();
        }
        activityList.add(activity);
    }
    //结束所有activity
    public static void finishActivity(){
        for (int i = 0; i < activityList.size(); i++) {
            if (null != activityList.get(i))
                activityList.get(i).finish();
        }
        activityList.clear();
    }
}
