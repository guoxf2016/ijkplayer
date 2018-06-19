package com.readsense.app.net;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Url;

public interface BackendService {

    @POST("v1/event/create_from_device_by_raw_data")
    @Multipart
    Call<ResponseBody> upload(@Part List<MultipartBody.Part> partList);

    @POST
    @Multipart
    Call<ResponseBody> upload(@Part List<MultipartBody.Part> partList, @Url String url);
}
