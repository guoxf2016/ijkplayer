package com.readsense;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.readsense.media.App;


public class MidnightReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MidnightReceiver", "onReceiver " + intent.getAction());
        App.sInstance.reboot(0);
    }

}


