package com.readsense.media;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent appli = context.getPackageManager().getLaunchIntentForPackage("com.readsense.app.rtsp");
        context.startActivity(appli);
    }
}
