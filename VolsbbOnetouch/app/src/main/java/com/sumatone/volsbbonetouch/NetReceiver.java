package com.sumatone.volsbbonetouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Entity;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by shalini on 05-02-2015.
 */
public class NetReceiver extends BroadcastReceiver {
    private Context c;
    private ShowNotification s;
    private String url="http://www.facebook.com";

    @Override
    public void onReceive(Context context, Intent intent) {
        c=context;
        s=new ShowNotification(c);
        //Toast.makeText(context,"netreceived",Toast.LENGTH_SHORT).show();
        if(checkConnection())
        {
            new Checknet().execute();
            s.notifyInstant();
        }
        else
        {
            s.remove();
        }
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
        final WifiManager wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
        final ConnectivityManager connMgr = (ConnectivityManager) c
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiManager.isWifiEnabled())
        {
            Log.d("enabled", "enabled");
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            final SupplicantState supp= wifiInfo.getSupplicantState();
            Log.d("wifi",wifiInfo.getSSID());
            if((wifiInfo.getSSID().toLowerCase().contains("volsbb")||wifiInfo.getSSID().equalsIgnoreCase("\"VOLS\""))&&wifi.isConnected()) {
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
    private class Checknet extends AsyncTask<Void, Void,String> {
        @Override
        protected String doInBackground(Void... params) {
            HttpGet httpGet = new HttpGet(url);
            HttpParams httparams=new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httparams,1000);
            HttpConnectionParams.setSoTimeout(httparams,2000);
            DefaultHttpClient httpclient=new DefaultHttpClient(httparams);
            try {
                HttpResponse hr=httpclient.execute(httpGet);
                HttpEntity he =hr.getEntity();
                Log.d("conn0", EntityUtils.toString(he));
                return "success";
            }
            catch(ClientProtocolException c){

            } catch (IOException e) {
                e.printStackTrace();
            }

            return "fail";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if(result.equals("success"))
                s.setText("Logged In").notifyInstant();
        }
    }
}
