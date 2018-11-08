package com.readsense.app.cameraupload;

import android.util.Log;

import com.google.gson.Gson;
import com.readsense.app.model.DetectCondition;
import com.readsense.app.model.pushdatabyjson.RequestBody;
import com.readsense.app.model.pushdatabyjson.RequestEnvelope;
import com.readsense.app.model.pushdatabyjson.RequestModel;
import com.readsense.app.model.pushdatabyjson.ResponseEnvelope;
import com.readsense.app.net.BackendHelper;
import com.readsense.app.utils.WifiUtil;
import com.readsense.media.App;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;

public class CameraUpload {

    private static final String TAG = "CameraUpload";

    public CameraUpload() {

    }

    public boolean upload(String largeImage, List<String> smallImages, int bodyCount, int pnm) {
        Log.d(TAG, "upload");
        RequestEnvelope requestEnvelop = new RequestEnvelope();
        RequestBody requestBody = new RequestBody();
        RequestModel requestModel = new RequestModel();
        long time = getUTC();
        requestModel.json = new Gson().toJson(new DetectCondition(getMacAddress(),
                largeImage, bodyCount, time, pnm, smallImages));
        requestModel.pushDataByJson = "http://tempuri.org/";
        requestBody.pushDataByJson = requestModel;
        requestEnvelop.body = requestBody;
        if (App.isNetworkAvailable) {
            Call<ResponseEnvelope> call = BackendHelper.getService().upload(requestEnvelop);
            ResponseEnvelope responseEnvelope = null;
            try {
                responseEnvelope = call.execute().body();
            } catch (IOException e) {
                Log.d(TAG, "exception " + e.getMessage());
                saveJsonToFile(requestModel.json, time);
                return false;
            }
            if (responseEnvelope != null) {
                String result = responseEnvelope.body.pushDataByJsonResponse.result;
                Log.d(TAG, "onResponse " + result + " count " + bodyCount);
                return "true".equals(result);
            }

        }
        saveJsonToFile(requestModel.json, time);
        /*try {
            Thread.sleep(2 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        return false;
    }


    public boolean upload(String json) {
        Log.d(TAG, "upload json");
        if (json == null) {
            Log.d(TAG, "json is null");
            return false;
        }
        RequestEnvelope requestEnvelop = new RequestEnvelope();
        RequestBody requestBody = new RequestBody();
        RequestModel requestModel = new RequestModel();
        requestModel.json = json;
        requestModel.pushDataByJson = "http://tempuri.org/";
        requestBody.pushDataByJson = requestModel;
        requestEnvelop.body = requestBody;
        Call<ResponseEnvelope> call = BackendHelper.getService().upload(requestEnvelop);
        ResponseEnvelope responseEnvelope = null;
        try {
            responseEnvelope = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        if (responseEnvelope != null) {
            String result = responseEnvelope.body.pushDataByJsonResponse.result;
            Log.d(TAG, "onResponse " + result);
            return "true".equals(result);
        }
        return false;
    }

    private void saveJsonToFile(String json, long time) {
        File file = new File(App.sInstance.getExternalCacheDir(), "" + time + ".json");
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(json.getBytes());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String readJsonFromFile(File file) {
        int length = (int) file.length();
        byte[] bytes = new byte[length];
        InputStream in = null;
        try {
            in = new FileInputStream(file);
            in.read(bytes);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return new String(bytes);
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
