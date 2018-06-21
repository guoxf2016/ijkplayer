package com.readsense.media;

import android.app.Application;
import android.support.annotation.NonNull;
import android.util.Log;

import com.readsense.app.model.RequestBodyTest;
import com.readsense.app.model.RequestEnvelopeTest;
import com.readsense.app.model.RequestModelTest;
import com.readsense.app.model.ResponseEnvelopeTest;
import com.readsense.app.net.BackendHelperTest;

import java.io.File;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
