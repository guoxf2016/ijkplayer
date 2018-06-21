package com.readsense.app.net;

import com.readsense.app.model.RequestEnvelope;
import com.readsense.app.model.ResponseEnvelope;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BackendService {

    @Headers({
            "Content-Type: text/xml; charset=utf-8",
            "SOAPAction: http://tempuri.org/pushDataByJson"
    })
    @POST("CameraDetect/DetectData.asmx")
    Call<ResponseEnvelope> upload(@Body RequestEnvelope body);

}
