package com.sumatone.volsbbonetouch;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shalini on 29-01-2015.
 */
public class MainActivity extends Activity implements View.OnClickListener {
    private String url="http://phc.prontonetworks.com/cgi-bin/authlogin?URI=http://www.msftncsi.com/redirect";
    EditText uname,password;
    TextView res;
    Button login,logout,slogin;
    List<NameValuePair> details;
    ProgressDialog pDialog;
    SharedPreferences s;
    Authentication a;
    String u,p,toasttext,session;
    ImageView about;
    String service="ProntoAuthentication";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //if(!isMyServiceRunning(MyService.class))
        //startService(new Intent(this,MyService.class));
        about=(ImageView)findViewById(R.id.about);
        uname=(EditText)findViewById(R.id.user);
        password=(EditText)findViewById(R.id.pass);
        s= PreferenceManager.getDefaultSharedPreferences(this);
        uname.setText(s.getString("prontousername",null));
        password.setText(s.getString("prontopassword",null));
        a= new Authentication(this);
        login=(Button)findViewById(R.id.login);
        logout=(Button)findViewById(R.id.logout);
        slogin=(Button)findViewById(R.id.savelogin);
        res=(TextView)findViewById(R.id.res);
        login.setOnClickListener(this);
        logout.setOnClickListener(this);
        slogin.setOnClickListener(this);
        about.setOnClickListener(this);
        session=s.getString("session","first");
        if(session.equals("first")){
            about.performClick();
            new NetReceiver().onReceive(this,new Intent());
            SharedPreferences.Editor editor=s.edit();
            editor.putString("session","used");
            editor.commit();
        }

    }

    @Override
    public void onClick(View v) {

            if (v.getId() == R.id.login) {
                if(checkConnection()) {
                    details = new ArrayList<NameValuePair>();
                    url = "http://phc.prontonetworks.com/cgi-bin/authlogin?URI=http://www.msftncsi.com/redirect";
                    u = uname.getText().toString();
                    p = password.getText().toString();
                    details.add(new BasicNameValuePair("userId", u));
                    details.add(new BasicNameValuePair("password", p));
                    details.add(new BasicNameValuePair("serviceName", service));
                    new Authentication(this).login(details);
                }
                else
                    Toast.makeText(this,"Not connected to Volsbb",Toast.LENGTH_SHORT).show();
            }
            if (v.getId() == R.id.logout) {
                if(checkConnection()) {
                    details = null;
                    url = "http://phc.prontonetworks.com/cgi-bin/authlogout";
                    new Authentication(this).logout();
                }
                else
                    Toast.makeText(this,"Not connected to Volsbb",Toast.LENGTH_SHORT).show();

            }
            if (v.getId() == R.id.savelogin) {
                if (checkConnection()) {
                    details = new ArrayList<NameValuePair>();
                    url = "http://phc.prontonetworks.com/cgi-bin/authlogin?URI=http://www.msftncsi.com/redirect";
                    u = uname.getText().toString();
                    p = password.getText().toString();
                    SharedPreferences.Editor editor = s.edit();
                    editor.putString("prontousername", u);
                    editor.putString("prontopassword", p);
                    editor.commit();
                    details.add(new BasicNameValuePair("userId", u));
                    details.add(new BasicNameValuePair("password", p));
                    details.add(new BasicNameValuePair("serviceName", service));
                    new Authentication(this).login(details);
                } else
                    Toast.makeText(this, "Not connected to Volsbb", Toast.LENGTH_SHORT).show();
            }


            if(v.getId()==R.id.about)
            {
                Intent i= new Intent(this,AboutDialog.class);
                startActivity(i);
            }

    }
    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
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
        final ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        final android.net.NetworkInfo wifi = connMgr
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if(wifiManager.isWifiEnabled())
        {
            final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            Log.d("wifi",wifiInfo.getSSID());
            if((wifiInfo.getSSID().equalsIgnoreCase("\"VOLSBB\"")||wifiInfo.getSSID().equalsIgnoreCase("VOLSBB"))&&wifi.isConnected()) {
                Log.d("wifistate","connected");
                return true;
            }
            return false;
        }
        else {
            return false;
        }
    }

    /*private class GetEvents extends AsyncTask<Void, Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Processing");
            pDialog.setCancelable(false);
            pDialog.show();

        }
        @Override
        protected String doInBackground(Void... params) {
            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            String valresponse = sh.makeServiceCall(url, ServiceHandler.POST,details);
            Log.d("Response: ", ">" + valresponse);
            return valresponse;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            if(result.contains("Logout successful"))
                toasttext="Logged out";
            else if(result.contains("Successful Pronto Authentication"))
                toasttext="Logged in";
            else if(result.contains("There is no active session to logout"))
                toasttext="There is no active session";
            else if(result.contains("Sorry, please check your username and password"))
                toasttext="Invalid username/password";
            else if(result.contains("Sorry, your free access quota is over"))
                toasttext="Your free access qouta is over";
            else
                toasttext="Already Logged in";
            Toast.makeText(getApplicationContext(),toasttext,Toast.LENGTH_SHORT).show();
        }



}*/
}
