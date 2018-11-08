package com.readsense.media;

import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;

import com.readsense.media.rtsp.activities.BodyRecoSettings;
import com.readsense.media.rtsp.services.DaemonService;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class App extends Application implements Thread.UncaughtExceptionHandler{

    public static final String TAG = App.class.getSimpleName();

    public static final String ACTION_MIDNIGHT = "com.readsense.app.rtsp.action.midnight";

    public static final int REQUEST_CODE_MIDNIGHT = 0xF1;

    public static App sInstance = null;

    public static long timestamp = System.currentTimeMillis();

    public static final boolean DEBUG = true;

    public static String url = null;
    public static String ip = null;

    public static volatile boolean isDetectorDestroy = false;

    public static final Object LOCK = new Object();

    public static boolean isNetworkAvailable = false;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        File file = getExternalCacheDir();
        if (file != null && !file.exists()) {
            file.mkdirs();
        }
        File file1 = getExternalFilesDir("");
        if (file1 != null && !file1.exists()) {
            file1.mkdirs();
        }
        Thread.setDefaultUncaughtExceptionHandler(this);
        Intent intent = new Intent(this, DaemonService.class);
        intent.putExtra("pid", android.os.Process.myPid());
        startService(intent);
        initAlarm();
    }

    private void initAlarm() {
        Intent intentStart = new Intent(ACTION_MIDNIGHT);
        PendingIntent pendingIntentStart = PendingIntent.getBroadcast(this,
                REQUEST_CODE_MIDNIGHT, intentStart, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager == null) {
            Log.d(TAG, "alarm manager null");
            return;
        }
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        //alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() + 6 * 60 * 1000, 1000, pendingIntentStart);
        // today
        Calendar date = new GregorianCalendar();
        // reset hour, minutes, seconds and millis
        date.set(Calendar.HOUR_OF_DAY, 0);
        date.set(Calendar.MINUTE, 0);
        date.set(Calendar.SECOND, 0);
        date.set(Calendar.MILLISECOND, 0);

        // next day
        date.add(Calendar.DAY_OF_MONTH, 1);
        //alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 10 * 1000, pendingIntentStart);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), 24 * 3600 * 1000, pendingIntentStart);
    }


    @Override
    public void uncaughtException(Thread t, Throwable e) {

        Log.d(TAG, "uncaughtException ", e);

        logOut("");
        restartApp(30 * 1000);
    }

    public void logOut(String tag) {
        File logFile = new File(this.getExternalFilesDir(""),
                DateFormat.format("yyyy-MM-dd-HH-mm-ss", System.currentTimeMillis()) + tag + ".log");
        try {
            Process process = Runtime.getRuntime().exec("logcat -d -f " + logFile.getAbsolutePath());
            process.waitFor();
            Runtime.getRuntime().exec("logcat -c");
        } catch (IOException e1) {
            e1.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void restartApp(long delay) {
        System.exit(0);
        //reboot(delay);
        /*try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, BodyRecoSettings.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //this.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid());
        //System.exit(0);*/
    }

    public void reboot(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String cmd = "reboot";
        //String cmd = "su -c shutdown";
        try {
            Runtime.getRuntime().exec(cmd);
        } catch (IOException e) {
            Log.d(TAG, e.getMessage());
        }

    }
}
