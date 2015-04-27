package com.sumatone.volsbbonetouch;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by shalini on 31-01-2015.
 */
public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        //Toast.makeText(context,"recieved",Toast.LENGTH_SHORT).show();
        Authentication a=new Authentication(context);
        String action=intent.getStringExtra("action");
        if(action.equals("login")){
            //Toast.makeText(context,"loginfun",Toast.LENGTH_SHORT).show();
            a.login();
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
        }
        else if(action.equals("logout")){
            //Toast.makeText(context,"logoutfun",Toast.LENGTH_SHORT).show();
            a.logout();
            Intent it = new Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
            context.sendBroadcast(it);
        }
    }
}
