package com.example.android.tryingout;

/**
 * Created by Faria huq on 08-Sep-17.
 */

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message

        Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
        arg0.sendBroadcast(new Intent("INTERNET_LOST"));
    }

}