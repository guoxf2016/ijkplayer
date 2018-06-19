package com.readsense.media;

import android.app.Application;

import java.io.File;

public class App extends Application {

    public static Application sInstance = null;

    public static long timestamp = System.currentTimeMillis();

    public static final boolean DEBUG = true;

    public static String url = null;
    public static String ip = null;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        File file = getExternalCacheDir();
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
    }
}
