package com.readsense.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent == null) {
            App.sInstance.reboot(0);
            return;
        }
        Log.d("Receiver", "onReceiver " + intent.getAction());
        if ("com.readsense.media.rtsp.action.RESTART".equals(intent.getAction())) {
            App.sInstance.reboot(0);
            return;
        }
        Intent appli = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        context.startActivity(appli);
    }
}
