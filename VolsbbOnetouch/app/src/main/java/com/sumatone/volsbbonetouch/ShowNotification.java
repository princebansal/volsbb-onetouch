package com.sumatone.volsbbonetouch;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by shalini on 31-01-2015.
 */
public class ShowNotification {
    Intent intentlogin,intentlogout,intent;
    PendingIntent pIntentlogin,pIntentlogout,pIntent;
    NotificationManager manager;
    Notification notification;
    Notification.Builder nBuilder;

    public ShowNotification(){
    }
    public ShowNotification(Context context){
        intent = new Intent(context,MainActivity.class);
        pIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        intentlogin = new Intent(context,NotificationReceiver.class);
        intentlogin.putExtra("action","login");
        pIntentlogin = PendingIntent.getBroadcast(context,1,intentlogin,PendingIntent.FLAG_UPDATE_CURRENT);
        intentlogout = new Intent(context,NotificationReceiver.class);
        intentlogout.putExtra("action","logout");
        pIntentlogout = PendingIntent.getBroadcast(context,2,intentlogout,PendingIntent.FLAG_UPDATE_CURRENT);
        manager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nBuilder=new Notification.Builder(context)
                .setContentTitle("Volsbb Onetouch")
                .setContentText("Login/Logout")
                .setSmallIcon(R.drawable.notiicon2)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(pIntent)
                .setOnlyAlertOnce(true)
                .setAutoCancel(false)
                .setTicker("Login/Logout")
                .addAction(R.drawable.login, "Login", pIntentlogin)
                .addAction(R.drawable.logout, "Logout", pIntentlogout);
        notification=nBuilder.build();
        notification.flags=Notification.FLAG_ONGOING_EVENT;
    }
    public void notifyInstant()
    {
        manager.notify(667,notification);
    }
    public void remove()
    {
        manager.cancel(667);
    }
    public ShowNotification setText(String s)
    {
        nBuilder.setContentText(s).setTicker(s);
        notification=nBuilder.build();
        notification.flags=Notification.FLAG_ONGOING_EVENT;
        return this;
    }
}
