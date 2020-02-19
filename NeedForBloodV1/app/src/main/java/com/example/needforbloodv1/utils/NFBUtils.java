package com.example.needforbloodv1.utils;

import android.os.Handler;

public class NFBUtils {
    private static Handler requestHandler = null;
    public static void setRequestHandler(Handler handler){
        requestHandler = handler;
    }
    public static Handler getRequestHandler(){
        return requestHandler;
    }
}
