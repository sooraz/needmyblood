package com.example.needforbloodv1.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class NFBSharedPreference {
    static final String PREF_USER_NAME= "nfb-user";
    static final String PREF_USER_FCM= "nfb-fcm";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }
    //name
    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "0");
    }
    public static void clearData(Context ctx){
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear(); //clear all stored data
        editor.commit();
    }
    //fcm
    public static void setFCMKey(Context ctx, String Key)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_FCM, Key);
        editor.commit();
    }

    public static String getFCMKey(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_FCM, null);
    }



}
