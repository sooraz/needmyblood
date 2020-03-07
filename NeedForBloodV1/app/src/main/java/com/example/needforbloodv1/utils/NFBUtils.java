package com.example.needforbloodv1.utils;

import android.content.Context;
import android.os.Handler;

import com.example.needforbloodv1.Log.NFBLog;

public class NFBUtils {

    private static Context mContext;
    private static Handler requestHandler = null;
    private static Handler activityHandler = null;
    public static void setRequestHandler(Handler handler){
        requestHandler = handler;
    }
    public static Handler getRequestHandler(){
        return requestHandler;
    }

    public static void setActivityHandler(Handler handler) {
        activityHandler = handler;
    }
    public static Handler getActivityHandler(){
        return activityHandler;
    }

    public static void setContext(Context c) {
    mContext =c;
    }
    public static Context getContext(){
        return mContext;
    }
}
