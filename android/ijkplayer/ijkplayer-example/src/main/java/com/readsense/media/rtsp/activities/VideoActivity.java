/*
 * Copyright (C) 2015 Bilibili
 * Copyright (C) 2015 Zhang Rui <bbcallen@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.readsense.media.rtsp.activities;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TableLayout;
import android.widget.TextView;

import com.readsense.app.cameraupload.CameraUpload;

import com.readsense.app.model.heartbeat.RequestBody;
import com.readsense.app.model.heartbeat.RequestEnvelope;
import com.readsense.app.model.heartbeat.RequestModel;
import com.readsense.app.model.heartbeat.ResponseEnvelope;
import com.readsense.app.net.BackendHelper;
import com.readsense.app.utils.WifiUtil;
import com.readsense.media.App;
import com.readsense.media.rtsp.view.RectanglesView;
import com.readsense.media.rtsp.widget.media.AndroidMediaController;
import com.readsense.media.rtsp.widget.media.IjkVideoView;

import cn.readsense.body.ReadBody;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import retrofit2.Call;
import tv.danmaku.ijk.media.player.IjkMediaPlayer;
import tv.danmaku.ijk.media.player.misc.ITrackInfo;

import com.readsense.media.rtsp.R;
import com.readsense.media.rtsp.application.Settings;
import com.readsense.media.rtsp.content.RecentMediaStorage;
import com.readsense.media.rtsp.fragments.TracksFragment;
import com.readsense.media.rtsp.widget.media.MeasureHelper;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.reactivex.schedulers.Schedulers.io;
import static io.reactivex.schedulers.Schedulers.newThread;
import static io.reactivex.schedulers.Schedulers.single;

public class VideoActivity extends AppCompatActivity implements TracksFragment.ITrackHolder {
    private static final String TAG = "VideoActivity";

    public static final int MSG_HEART_BEAT = 1;

    private String mVideoPath;
    private Uri mVideoUri;

    private AndroidMediaController mMediaController;
    private IjkVideoView mVideoView;
    private TextView mToastTextView;
    private TableLayout mHudView;
    private DrawerLayout mDrawerLayout;
    private ViewGroup mRightDrawer;

    private Settings mSettings;
    private boolean mBackPressed;

    private RectanglesView mRectanglesView;

    private final MyHandler mHander = new MyHandler(this);

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                uploadCachedFile(context);
            }
        }
    };
    ExecutorService WORKER = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(1));


    private boolean isNetworkAvailable() {
        ConnectivityManager manager = (ConnectivityManager) App.sInstance
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager == null) return false;
        NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.isConnected()) {
                App.isNetworkAvailable = true;
                return true;
            } else {
                Log.e(TAG, "当前没有网络连接，请确保你已经打开网络 ");
                return false;
            }

        } else {   // not connected to the internet
            App.isNetworkAvailable = false;
            return false;
        }
    }

    private void uploadCachedFile(Context context) {
        if (isNetworkAvailable()) {
            App.isNetworkAvailable = true;
            try {
                WORKER.execute(new Runnable() {
                    @Override
                    public void run() {
                        final CameraUpload cameraUpload = new CameraUpload();
                        File file = getExternalCacheDir();
                        File[] files = file.listFiles();
                        int size = files.length;
                        for (int i=0; i < size; i++) {
                            File file1 = files[i];
                            if (!file1.getName().endsWith(".json")) {
                                continue;
                            }
                            final String json = cameraUpload.readJsonFromFile(file1);
                            boolean result = cameraUpload.upload(json);
                            if (result) {
                                file1.delete();
                            }
                        }

                    }
                });
            } catch (RejectedExecutionException e) {
                Log.d(TAG, "is busy");
            }
        } else {
            App.isNetworkAvailable = false;
        }
    }

    public static Intent newIntent(Context context, String videoPath, String videoTitle) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra("videoPath", videoPath);
        intent.putExtra("videoTitle", videoTitle);
        return intent;
    }

    public static void intentTo(Context context, String videoPath, String videoTitle) {
        context.startActivity(newIntent(context, videoPath, videoTitle));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.
                LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_player);

        mSettings = new Settings(this);

        // handle arguments
        mVideoPath = getIntent().getStringExtra("videoPath");

        Intent intent = getIntent();
        String intentAction = intent.getAction();
        if (!TextUtils.isEmpty(intentAction)) {
            if (intentAction.equals(Intent.ACTION_VIEW)) {
                mVideoPath = intent.getDataString();
            } else if (intentAction.equals(Intent.ACTION_SEND)) {
                mVideoUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    String scheme = mVideoUri.getScheme();
                    if (TextUtils.isEmpty(scheme)) {
                        Log.e(TAG, "Null unknown scheme\n");
                        finish();
                        return;
                    }
                    if (scheme.equals(ContentResolver.SCHEME_ANDROID_RESOURCE)) {
                        mVideoPath = mVideoUri.getPath();
                    } else if (scheme.equals(ContentResolver.SCHEME_CONTENT)) {
                        Log.e(TAG, "Can not resolve content below Android-ICS\n");
                        finish();
                        return;
                    } else {
                        Log.e(TAG, "Unknown scheme " + scheme + "\n");
                        finish();
                        return;
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(mVideoPath)) {
            new RecentMediaStorage(this).saveUrlAsync(mVideoPath);
        }

        // init UI
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        mMediaController = new AndroidMediaController(this, false);
        mMediaController.setSupportActionBar(actionBar);

        mToastTextView = (TextView) findViewById(R.id.toast_text_view);
        mHudView = (TableLayout) findViewById(R.id.hud_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mRightDrawer = (ViewGroup) findViewById(R.id.right_drawer);

        mDrawerLayout.setScrimColor(Color.TRANSPARENT);

        // init player
        IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");

        mRectanglesView = (RectanglesView) findViewById(R.id.rectangle_view);
        mVideoView = (IjkVideoView) findViewById(R.id.video_view);
        mVideoView.setRectanglesView(mRectanglesView);
        mVideoView.setMediaController(mMediaController);
        mVideoView.setHudView(mHudView);
        // prefer mVideoPath
        if (mVideoPath != null)
            mVideoView.setVideoPath(mVideoPath);
        else if (mVideoUri != null)
            mVideoView.setVideoURI(mVideoUri);
        else {
            Log.e(TAG, "Null Data Source\n");
            finish();
            return;
        }
        mVideoView.start();
        mHander.sendEmptyMessage(MSG_HEART_BEAT);
        long result = ReadBody.nativeCreateObject(this);
        /*long result = ReadBody.nativeCreateObjectWithLicense(this,
                "ffb601a7a83ad2d1279a29d40e2c4247",
                "8502ed184f17b9b8ff7abf09af63759dba103d69");*/
        long result2 = ReadBody.nativeBodySenseInit(this);
        App.isDetectorDestroy = false;
        if (result == 0 && result2 == 2) {
            Log.d(TAG, "init body track success " + result);
        } else {
            Log.d(TAG, "init body track failed " + result + " " + result2);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, intentFilter);
        //when start upload cached file
        uploadCachedFile(this);
        /*String test = null;
        test.length();*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);
    }

    @Override
    public void onBackPressed() {
        mBackPressed = true;

        super.onBackPressed();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mBackPressed || !mVideoView.isBackgroundPlayEnabled()) {
            mVideoView.stopPlayback();
            mVideoView.release(true);
            mVideoView.stopBackgroundPlay();
        } else {
            mVideoView.enterBackground();
        }
        IjkMediaPlayer.native_profileEnd();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        App.isDetectorDestroy = true;
        synchronized (App.LOCK) {
            ReadBody.nativeDestroyObject();
            ReadBody.nativeBodySenseUnInit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_toggle_ratio) {
            int aspectRatio = mVideoView.toggleAspectRatio();
            String aspectRatioText = MeasureHelper.getAspectRatioText(this, aspectRatio);
            mToastTextView.setText(aspectRatioText);
            mMediaController.showOnce(mToastTextView);
            return true;
        } else if (id == R.id.action_toggle_player) {
            int player = mVideoView.togglePlayer();
            String playerText = IjkVideoView.getPlayerText(this, player);
            mToastTextView.setText(playerText);
            mMediaController.showOnce(mToastTextView);
            return true;
        } else if (id == R.id.action_toggle_render) {
            int render = mVideoView.toggleRender();
            String renderText = IjkVideoView.getRenderText(this, render);
            mToastTextView.setText(renderText);
            mMediaController.showOnce(mToastTextView);
            return true;
        } else if (id == R.id.action_show_info) {
            mVideoView.showMediaInfo();
        } else if (id == R.id.action_show_tracks) {
            if (mDrawerLayout.isDrawerOpen(mRightDrawer)) {
                Fragment f = getSupportFragmentManager().findFragmentById(R.id.right_drawer);
                if (f != null) {
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    transaction.remove(f);
                    transaction.commit();
                }
                mDrawerLayout.closeDrawer(mRightDrawer);
            } else {
                Fragment f = TracksFragment.newInstance();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.right_drawer, f);
                transaction.commit();
                mDrawerLayout.openDrawer(mRightDrawer);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public ITrackInfo[] getTrackInfo() {
        if (mVideoView == null)
            return null;

        return mVideoView.getTrackInfo();
    }

    @Override
    public void selectTrack(int stream) {
        mVideoView.selectTrack(stream);
    }

    @Override
    public void deselectTrack(int stream) {
        mVideoView.deselectTrack(stream);
    }

    @Override
    public int getSelectedTrack(int trackType) {
        if (mVideoView == null)
            return -1;

        return mVideoView.getSelectedTrack(trackType);
    }

    private void heartBeat() {
        Log.d(TAG, "heartBeat");
        Disposable disposable = Flowable.just(new Object()).subscribeOn(single()).map(new Function<Object, Object>() {
            @Override
            public Object apply(Object o) {
                RequestEnvelope requestEnvelop = new RequestEnvelope();
                RequestBody requestBody = new RequestBody();
                RequestModel requestModel = new RequestModel();
                requestModel.cameraID = WifiUtil.getMacAddress(VideoActivity.this);
                requestModel.Heartbeat = "http://tempuri.org/";
                requestBody.Heartbeat = requestModel;
                requestEnvelop.body = requestBody;
                if (isNetworkAvailable()) {
                    Call<ResponseEnvelope> call = BackendHelper.getService().heartBeat(requestEnvelop);
                    ResponseEnvelope responseEnvelope = null;
                    try {
                        responseEnvelope = call.execute().body();
                    } catch (IOException e) {
                        e.printStackTrace();
                        mHander.sendEmptyMessageDelayed(MSG_HEART_BEAT, 5 * 60 * 1000);
                        return new Object();
                    }
                    if (responseEnvelope != null) {
                /*String result = responseEnvelope.body.HeartbeatResponse.result;
                Log.d(TAG, "onResponse " + result);*/
                        Log.d(TAG, "heartbeat success");
                        mHander.sendEmptyMessageDelayed(MSG_HEART_BEAT, 60 * 60 * 1000);
                        return new Object();
                    }

                }
                mHander.sendEmptyMessageDelayed(MSG_HEART_BEAT, 5 * 60 * 1000);
                return new Object();
            }
        }).subscribe();

    }


    private static class MyHandler extends Handler {
        private WeakReference<VideoActivity> mOuter;

        MyHandler(VideoActivity outer) {
            mOuter = new WeakReference<>(outer);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            VideoActivity outer = mOuter.get();
            if (outer == null) return;
            switch (msg.what) {
                case MSG_HEART_BEAT:
                    outer.heartBeat();
                    break;
                default:
                    throw new UnsupportedOperationException("wtf, unsupported");
            }
        }
    }
}
