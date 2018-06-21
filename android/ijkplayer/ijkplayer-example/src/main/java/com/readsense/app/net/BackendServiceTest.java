package com.readsense.app.net;

import com.readsense.app.model.RequestEnvelopeTest;
import com.readsense.app.model.ResponseEnvelopeTest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface BackendServiceTest {

    @Headers({
            "Content-Type: text/xml; charset=utf-8",
            "SOAPAction: http://tempuri.org/getTest"
    })
    @POST("CameraDetect/DetectData.asmx")
    Call<ResponseEnvelopeTest> getTest(@Body RequestEnvelopeTest body);

}
