package com.readsense.media.rtsp.services;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.os.Process;
import android.util.Log;

import java.io.IOException;
import java.util.List;


public class DaemonService extends Service {
    private final static String TAG = DaemonService.class.getSimpleName();

    // 定时唤醒的时间间隔，这里为了自己测试方边设置了一分钟
    private final static int ALARM_INTERVAL = 1 * 60 * 1000;
    // 发送唤醒广播请求码
    private final static int WAKE_REQUEST_CODE = 5121;
    // 守护进程 Service ID
    private final static int DAEMON_SERVICE_ID = -5121;

    private volatile boolean mDaemonFlag = false;

    private MyDaemonThread myDaemonThread = new MyDaemonThread();

    private int appPid = -1;

    @Override
    public void onCreate() {
        Log.i(TAG, "onCreate");
        myDaemonThread.start();
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand");
        // 利用 Android 漏洞提高进程优先级，
        startForeground(DAEMON_SERVICE_ID, new Notification());
        // 当 SDk 版本大于18时，需要通过内部 Service 类启动同样 id 的 Service
        if (Build.VERSION.SDK_INT >= 18) {
            Intent innerIntent = new Intent(this, DaemonInnerService.class);
            startService(innerIntent);
        }
        if (appPid < 0) {
            appPid = intent.getIntExtra("pid", -1);
        }
        mDaemonFlag = false;
        /*// 发送唤醒广播来促使挂掉的UI进程重新启动起来
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent();
        //alarmIntent.setAction(VMWakeReceiver.DAEMON_WAKE_ACTION);

        PendingIntent operation = PendingIntent.getBroadcast(this, WAKE_REQUEST_CODE, alarmIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
                ALARM_INTERVAL, operation);
*/
        /**
         * 这里返回值是使用系统 Service 的机制自动重新启动，不过这种方式以下两种方式不适用：
         * 1.Service 第一次被异常杀死后会在5秒内重启，第二次被杀死会在10秒内重启，第三次会在20秒内重启，一旦在短时间内 Service 被杀死达到5次，则系统不再拉起。
         * 2.进程被取得 Root 权限的管理工具或系统工具通过 forestop 停止掉，无法重启。
         */
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("onBind 未实现");
    }

    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy");
        super.onDestroy();
    }

    /**
     * 实现一个内部的 Service，实现让后台服务的优先级提高到前台服务，这里利用了 android 系统的漏洞，
     * 不保证所有系统可用，测试在7.1.1 之前大部分系统都是可以的，不排除个别厂商优化限制
     */
    public static class DaemonInnerService extends Service {

        @Override
        public void onCreate() {
            Log.i(TAG, "DaemonInnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i(TAG, "DaemonInnerService -> onStartCommand");
            startForeground(DAEMON_SERVICE_ID, new Notification());
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("onBind 未实现");
        }

        @Override
        public void onDestroy() {
            Log.i(TAG, "DaemonInnerService -> onDestroy");
            super.onDestroy();
        }
    }

    private class MyDaemonThread extends Thread {

        @Override
        public void run() {
            Log.d(TAG, "MyDaemonThread run");
            while (!Thread.currentThread().isInterrupted()) {
                mDaemonFlag = true;
                Log.d(TAG, "mDaemonFlag " + mDaemonFlag);
                try {
                    Thread.sleep(60 * 1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    //Thread.currentThread().interrupt();
                    mDaemonFlag = false;
                }
                Log.d(TAG, "mDaemonFlag " + mDaemonFlag);
                if (!isAppAlive(DaemonService.this, "com.readsense.app.rtsp")) {
                    //restart app
                    //sendBroadcast(new Intent("com.readsense.media.rtsp.action.RESTART"));
                    //sendRestartAction();
                    reboot(0);
                    appPid = -1;
                }
            }
        }
    }

    public static void reboot(long delay) {
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

    public static boolean isRunningProcess(Context context, String appInfo) {
        ActivityManager myManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppPs = myManager.getRunningAppProcesses();
        if (runningAppPs != null && runningAppPs.size() > 0) {
            if (runningAppPs.contains(appInfo)) {
                return true;
            }
        }
        return false;
    }

    private boolean isAppAlive(Context context, String packageName){
        ActivityManager activityManager =
                (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processInfos
                = activityManager.getRunningAppProcesses();
        for(int i = 0; i < processInfos.size(); i++){
            if(processInfos.get(i).processName.equals(packageName)){
                Log.i("NotificationLaunch",
                        String.format("the %s is running, isAppAlive return true", packageName));
                return true;
            }
        }
        Log.i("NotificationLaunch",
                String.format("the %s is not running, isAppAlive return false", packageName));
        return false;
    }

    private void sendRestartAction() {
        // 发送唤醒广播来促使挂掉的UI进程重新启动起来
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent();
        alarmIntent.setAction("com.readsense.media.rtsp.action.RESTART");

        PendingIntent operation = PendingIntent.getBroadcast(this, WAKE_REQUEST_CODE, alarmIntent,
                PendingIntent.FLAG_ONE_SHOT);

        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), operation);
    }

}
