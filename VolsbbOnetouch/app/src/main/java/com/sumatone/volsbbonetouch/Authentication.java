package com.sumatone.volsbbonetouch;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.auth.AUTH;
import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shalini on 31-01-2015.
 */
public class Authentication {
    private String url="http://phc.prontonetworks.com/cgi-bin/authlogin?URI=http://www.msftncsi.com/redirect";
    List<NameValuePair> details;
    String service="ProntoAuthentication";
    private Context context;
    private String toasttext="";
    private SharedPreferences s;
    private String u,p;
    private String[] contentText={"Login/Logout",""};
    private ShowNotification sn;
    public Authentication(){

    }
    public Authentication(Context c)
    {
        context=c;
        sn= new ShowNotification(context);
        s= PreferenceManager.getDefaultSharedPreferences(context);
        u=s.getString("prontousername","a");
        p=s.getString("prontopassword","a");
        details=new ArrayList<NameValuePair>();
        details=null;
    }
    public void login(){
        url="http://phc.prontonetworks.com/cgi-bin/authlogin?URI=http://www.msftncsi.com/redirect";
        contentText[0]="Logging In";
        contentText[1]="Logged In";
        details=new ArrayList<NameValuePair>();
        details.add(new BasicNameValuePair("userId",u));
        details.add(new BasicNameValuePair("password",p));
        details.add(new BasicNameValuePair("serviceName", service));
        new GetEvents().execute();
    }
    public void login(List<NameValuePair> det){
        url="http://phc.prontonetworks.com/cgi-bin/authlogin?URI=http://www.msftncsi.com/redirect";
        contentText[0]="Logging In";
        contentText[1]="Logged In";
        details=new ArrayList<NameValuePair>();
        details=det;
        new GetEvents().execute();
    }
    public void logout(){
        details=null;
        contentText[0]="Logging Out";
        contentText[1]="Logged Out";
        url="http://phc.prontonetworks.com/cgi-bin/authlogout";
        new GetEvents().execute();
    }
    private class GetEvents extends AsyncTask<Void, Void,String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            sn.setText(contentText[0]).notifyInstant();
            // Showing progress dialog
           /* pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Signing In");
            pDialog.setCancelable(false);
            pDialog.show();*/

        }
        @Override
        protected String doInBackground(Void... params) {
            ServiceHandler sh = new ServiceHandler();
            sn.setText(contentText[0]).notifyInstant();
            // Making a request to url and getting response
            String valresponse = sh.makeServiceCall(url, ServiceHandler.POST,details);
            Log.d("Response: ", ">" + valresponse);
            return valresponse;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if(result==null)
            {
                Toast.makeText(context,"Unknown network",Toast.LENGTH_SHORT).show();
            }
            else {
                if (result.contains("Logout successful")) {
                    toasttext = "Logged out";
                    Log.d("enter", "0");
                    sn.setText(contentText[1]).notifyInstant();
                } else if (result.contains("Successful Pronto Authentication")) {
                    toasttext = "Logged in";
                    Log.d("enter", "1");
                    sn.setText(contentText[1]).notifyInstant();
                } else if (result.contains("There is no active session to logout")) {
                    toasttext = "There is no active session";
                    Log.d("enter", "2");
                    sn.setText("Login/Logout").notifyInstant();
                } else if (result.contains("Sorry, please check your username and password")||result.contains("Sorry, that password was not accepted")||result.contains("Sorry, that account does not exist")) {
                    toasttext = "Invalid username/password";
                    Log.d("enter", "3");
                    sn.nBuilder.setContentText("Login/Logout");
                } else if (result.contains("Sorry, your free access quota is over")) {
                    toasttext = "Your free access quota is over";
                    Log.d("enter", "4");
                    sn.setText("Login/Logout").notifyInstant();
                } else {
                    toasttext = "Already Logged in";
                    Log.d("enter", "5");
                    sn.setText(contentText[1]).notifyInstant();
                }
                Toast.makeText(context, toasttext, Toast.LENGTH_SHORT).show();
            }
            // Dismiss the progress dialog
            /*if (pDialog.isShowing())
                pDialog.dismiss();
            res.setText(result);*/
        }



    }
}
