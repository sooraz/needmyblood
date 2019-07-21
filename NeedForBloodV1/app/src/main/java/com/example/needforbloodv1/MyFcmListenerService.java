package com.example.needforbloodv1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.Map;

public class MyFcmListenerService extends FirebaseMessagingService {
    private static final String TAG = "MyFcmListenerService";

    @Override

    public void onMessageReceived(RemoteMessage message){
        //String from = message.getFrom();
        Map data = message.getData();
        //String messagenote = data.get("message").toString();
        //String title = data.get("title").toString();
        //String tickerText = data.get("tickerText").toString();
        //String subtitle = data.get("subtitle").toString();
        long when= Calendar.getInstance().getTimeInMillis();


        NotificationManager notificationManager= (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("ms", "pani chudu");
        notificationIntent.putExtras(bundle);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        notificationIntent.setData(Uri.parse("content://" + when));
        PendingIntent intent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        startActivity(notificationIntent);


        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("pani chudu")
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setTicker("1")
                .setSubText("2")
                .setContentIntent(intent)
                .setAutoCancel(true);
                //.setContentText();


        notificationManager.notify((int)when, mBuilder.build());

    }
    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("sooraz","onNewToken"+s);
        // Get updated InstanceID token.
        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app
        // server.

        sendRegistrationToServer(s);

    }

    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
    }

}