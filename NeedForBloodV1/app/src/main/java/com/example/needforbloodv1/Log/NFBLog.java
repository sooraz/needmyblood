package com.example.needforbloodv1.Log;

import android.util.Log;

public class NFBLog {
    private static final String TAG="NFB";
    static StackTraceElement[] stackTraceElements;
    public static void Log(String message){
        stackTraceElements = Thread.currentThread().getStackTrace();
        Log.d(TAG,stackTraceElements[1].getClassName()+stackTraceElements[1].getLineNumber()+" "+message);
    }
}
