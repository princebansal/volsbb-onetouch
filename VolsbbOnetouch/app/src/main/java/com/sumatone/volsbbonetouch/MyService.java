package com.sumatone.volsbbonetouch;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

/**
 * Created by shalini on 31-01-2015.
 */
public class MyService extends Service {
    private boolean CHECK=true,tcheck=true;
    private Thread t1;
    @Override
    public void onCreate() {
        super.onCreate();
        final ShowNotification s=new ShowNotification(this);
        //Toast.makeText(this,"Service was created",Toast.LENGTH_SHORT).show();
        t1=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    CHECK=checkConnection();
                    if (CHECK==true&&tcheck==true) {
                        Log.d("status", "connected");
                        s.notifyInstant();
                        tcheck=false;
                    }
                    else if (CHECK==false) {
                        Log.d("status", "disconnected");
                        s.remove();
                        tcheck=true;
                    }
                }
            }
        });
        /*t2=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    checkConnection();
                    if (!CHECK) {
                        Log.d("status", "disconnected");
                        t1.notify();
                        try {
                            t2.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });*/

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //Toast.makeText(this,"Service was started",Toast.LENGTH_SHORT).show();

        Log.d("start","onstart");
        t1.start();
        return super.onStartCommand(intent,flags,startId);
    }
    public boolean checkConnection() {
        /*final ConnectivityManager connMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        final android.net.NetworkInfo mobile = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if(wifi.isConnected())
        {
            Log.d("wifi",wifi.getExtraInfo());
            if(wifi.getExtraInfo().equalsIgnoreCase("\"VOLSBB\"")) {
                Log.d("wifistate","connected");
                return true;
            }
            return false;
        }
        else {
            return false;
        }*/
        final WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        final ConnectivityManager connMgr = (ConnectivityManager) this
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiManager.isWifiEnabled())
        {
            Log.d("enabled","enabled");
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            final SupplicantState supp= wifiInfo.getSupplicantState();
            Log.d("wifi",wifiInfo.getSSID());
            if((wifiInfo.getSSID().equalsIgnoreCase("\"VOLSBB\"")||wifiInfo.getSSID().equalsIgnoreCase("\"VOLS\"")||wifiInfo.getSSID().equalsIgnoreCase("VOLSBB"))&&wifi.isConnected()) {
                Log.d("wifistate","connected");
                return true;
            }
            return false;
        }
        else {
            Log.d("wifistate","notconnected");
            return false;
        }
    }

    @Override
    public void onDestroy() {
        Log.d("destroy","destroyed");
        //Toast.makeText(this,"Service was destroyed",Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
