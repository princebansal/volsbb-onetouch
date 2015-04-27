package com.sumatone.volsbbonetouch;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by shalini on 01-02-2015.
 */
public class AboutDialog extends Activity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_layout);
        tv=(TextView)findViewById(R.id.fulltv);
        tv.setText(R.string.fulltv);
    }
}
