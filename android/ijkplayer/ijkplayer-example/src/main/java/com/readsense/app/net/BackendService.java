package com.readsense.app.net;

import com.readsense.app.model.pushdatabyjson.RequestEnvelope;
import com.readsense.app.model.pushdatabyjson.ResponseEnvelope;


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

    @Headers({
            "Content-Type: text/xml; charset=utf-8",
            "SOAPAction: http://tempuri.org/Heartbeat"
    })
    @POST("CameraDetect/DetectData.asmx")
    Call<com.readsense.app.model.heartbeat.ResponseEnvelope> heartBeat(@Body com.readsense.app.model.heartbeat.RequestEnvelope body);

}
