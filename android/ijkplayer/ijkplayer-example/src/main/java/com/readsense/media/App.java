package com.readsense.media;

import android.app.Application;
import android.content.Intent;
import android.text.format.DateFormat;

import com.readsense.media.rtsp.activities.BodyRecoSettings;

import java.io.File;
import java.io.IOException;

public class App extends Application implements Thread.UncaughtExceptionHandler{

    public static App sInstance = null;

    public static long timestamp = System.currentTimeMillis();

    public static final boolean DEBUG = true;

    public static String url = null;
    public static String ip = null;

    public static volatile boolean isDetectorDestroy = false;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        File file = getExternalCacheDir();
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {

        logOut("");
        restartApp();
    }

    public void logOut(String tag) {
        File logFile = new File(this.getExternalCacheDir(),
                DateFormat.format("yyyy-MM-dd-HH-mm-ss", System.currentTimeMillis()) + tag + ".log");
        try {
            Process process = Runtime.getRuntime().exec("logcat -d -f " + logFile.getAbsolutePath());
            process.waitFor();
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void restartApp() {
        Intent intent = new Intent(this, BodyRecoSettings.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(0);
    }
}
