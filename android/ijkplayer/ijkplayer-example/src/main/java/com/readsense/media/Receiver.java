package com.readsense.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("Receiver", "onReceiver " + context.getPackageName());
        Intent appli = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
        context.startActivity(appli);
    }
}
