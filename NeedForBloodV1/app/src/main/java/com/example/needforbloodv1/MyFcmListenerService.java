package com.example.needforbloodv1;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.Map;

public class MyFcmListenerService extends FirebaseMessagingService {
    private static final String TAG = "MyFcmListenerService";
//    private Context acContext=null;

    @Override

    public void onMessageReceived(RemoteMessage message){
        //String from = message.getFrom();


        Map data = message.getData();
        //String messagenote = data.get("message").toString();
        //String title = data.get("title").toString();
        //String tickerText = data.get("tickerText").toString();
        //String subtitle = data.get("subtitle").toString();
        long when= Calendar.getInstance().getTimeInMillis();


        super.onMessageReceived(message);
        Log.d("msg", "onMessageReceived: " + message.getData().get("message"));
        Intent intent = new Intent(this, After_Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
        String channelId = "Default";
        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(message.getNotification().getTitle())
                .setContentText(message.getNotification().getBody()).setAutoCancel(true).setContentIntent(pendingIntent);;
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Default channel", NotificationManager.IMPORTANCE_DEFAULT);
            manager.createNotificationChannel(channel);
        }
        manager.notify(0, builder.build());
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