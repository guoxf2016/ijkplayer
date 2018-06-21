package com.readsense.app.net;

import android.support.annotation.NonNull;
import android.util.Log;

import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.convert.AnnotationStrategy;
import org.simpleframework.xml.core.Persister;
import org.simpleframework.xml.strategy.Strategy;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

public class BackendHelperTest {
    public final static String BASE_URL = "http://210.5.155.249:82/";
    public static BackendServiceTest mService;

    private static Strategy strategy = new AnnotationStrategy();
    private static Serializer serializer = new Persister(strategy);

    private static OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder();

    private static Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
            .addConverterFactory(SimpleXmlConverterFactory.create(serializer))
            .baseUrl(BASE_URL);

    public static <S> S createService(Class<S> serviceClass) {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(@NonNull String message) {
                if (message.length() > 4000) {
                    for (int i = 0; i < message.length(); i += 4000) {
                        if (i + 4000 < message.length())
                            Log.i("BackendHelperTest" + i, message.substring(i, i + 4000));
                        else
                            Log.i("BackendHelperTest" + i, message.substring(i, message.length()));
                    }
                } else {
                    Log.i("BackendHelperTest", message);
                }

            }
        });
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okHttpClient.interceptors().add(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request.Builder requestBuilder = original.newBuilder()
                        .header("Content-Type", "text/xml;charset=UTF-8")   // 对于SOAP 1.1， 如果是soap1.2 应是Content-Type: application/soap+xml; charset=utf-8
                        .method(original.method(), original.body());

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });

        okHttpClient.interceptors().add(interceptor);
        OkHttpClient client = okHttpClient.connectTimeout(2, TimeUnit.MINUTES)
                .writeTimeout(2, TimeUnit.MINUTES)
                .readTimeout(2, TimeUnit.MINUTES)
                .build();
        Retrofit retrofit = retrofitBuilder.client(client).build();
        return retrofit.create(serviceClass);
    }

    public static BackendServiceTest getService() {
        if (mService == null) {
            mService = createService(BackendServiceTest.class);
        }
        return mService;

    }


}
