package com.readsense.app.net;


import android.util.Log;

import com.readsense.media.App;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendHelper {
    public static String HOST = "http://lightyear.readsense.cn/";
    private static long DEFAULT_TIMEOUT = 20L;

    private static Retrofit mRetrofit;

    private static BackendService mService;

    private static Retrofit getApiRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

            @Override
            public void log(String message) {
                Log.d("BackendHelper", message);
            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder().addInterceptor(interceptor);
        okHttpBuilder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
        mRetrofit = new Retrofit.Builder()
                .client(okHttpBuilder.build())
                .baseUrl(HOST)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return mRetrofit;
    }

    private static BackendService getApiService() {
        mService = getApiRetrofit().create(BackendService.class);
        return mService;
    }

    public static Call<ResponseBody> upload(List<MultipartBody.Part> partList) {
        if (!App.DEBUG) {
            return upload(partList, App.url);
        }
        return getApiService().upload(partList);
    }

    public static Call<ResponseBody> upload(List<MultipartBody.Part> partList, String url) {
        return getApiService().upload(partList, url);
    }
}
