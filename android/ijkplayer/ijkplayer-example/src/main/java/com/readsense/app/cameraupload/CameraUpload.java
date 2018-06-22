package com.readsense.app.cameraupload;

import android.util.Log;

import com.google.gson.Gson;
import com.readsense.app.model.DetectCondition;
import com.readsense.app.model.RequestBody;
import com.readsense.app.model.RequestEnvelope;
import com.readsense.app.model.RequestModel;
import com.readsense.app.model.ResponseEnvelope;
import com.readsense.app.net.BackendHelper;
import com.readsense.app.utils.WifiUtil;
import com.readsense.media.App;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;

public class CameraUpload {

    private static final String TAG = "CameraUpload";

    public CameraUpload() {

    }

    public boolean upload(String largeImage, List<String> smallImages, int bodyCount, int pnm) {
        RequestEnvelope requestEnvelop = new RequestEnvelope();
        RequestBody requestBody = new RequestBody();
        RequestModel requestModel = new RequestModel();
        requestModel.json = new Gson().toJson(new DetectCondition(getMacAddress(),
                largeImage, bodyCount, getUTC(), pnm, smallImages));
        requestModel.pushDataByJson = "http://tempuri.org/";
        requestBody.pushDataByJson = requestModel;
        requestEnvelop.body = requestBody;
        Call<ResponseEnvelope> call = BackendHelper.getService().upload(requestEnvelop);
        ResponseEnvelope responseEnvelope = null;
        try {
            responseEnvelope = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (responseEnvelope != null) {
            String result = responseEnvelope.body.pushDataByJsonResponse.result;
            Log.d(TAG, "onResponse " + result);
            return "true".equals(result);
        }
        return false;
    }

    private String mac = null;

    private String getMacAddress() {
        //return "c8:25:e1:83:57:3d";
        if (mac == null) {
            mac = WifiUtil.getMacAddress(App.sInstance.getApplicationContext()).replaceAll(":", "_");
        }
        Log.d(TAG, "mac " + mac);
        return mac;
    }

    private long getUTC() {
        return Calendar.getInstance().getTimeInMillis();
    }
}
