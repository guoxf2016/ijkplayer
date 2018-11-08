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

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.util.Log;

import com.readsense.app.model.pushdatabyjson.RequestBody;
import com.readsense.app.model.gettest.RequestBodyTest;
import com.readsense.app.model.pushdatabyjson.RequestEnvelope;
import com.readsense.app.model.gettest.RequestEnvelopeTest;
import com.readsense.app.model.pushdatabyjson.RequestModel;
import com.readsense.app.model.gettest.RequestModelTest;
import com.readsense.app.model.pushdatabyjson.ResponseEnvelope;
import com.readsense.app.model.gettest.ResponseEnvelopeTest;
import com.readsense.app.net.BackendHelper;
import com.readsense.app.net.BackendHelperTest;
import com.squareup.otto.Subscribe;

import java.io.File;
import java.io.IOException;

import com.readsense.media.rtsp.R;
import com.readsense.media.rtsp.application.AppActivity;
import com.readsense.media.rtsp.application.Settings;
import com.readsense.media.rtsp.eventbus.FileExplorerEvents;
import com.readsense.media.rtsp.fragments.FileListFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FileExplorerActivity extends AppActivity {
    private Settings mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mSettings == null) {
            mSettings = new Settings(this);
        }

        String lastDirectory = mSettings.getLastDirectory();
        if (!TextUtils.isEmpty(lastDirectory) && new File(lastDirectory).isDirectory())
            doOpenDirectory(lastDirectory, false);
        else
            doOpenDirectory("/", false);
    }

    @Override
    protected void onResume() {
        super.onResume();

        FileExplorerEvents.getBus().register(this);
        testSoap();
        uploadTest();
    }

    @Override
    protected void onPause() {
        super.onPause();

        FileExplorerEvents.getBus().unregister(this);
    }

    private void doOpenDirectory(String path, boolean addToBackStack) {
        Fragment newFragment = FileListFragment.newInstance(path);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.replace(R.id.body, newFragment);

        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    @Subscribe
    public void onClickFile(FileExplorerEvents.OnClickFile event) {
        File f = event.mFile;
        try {
            f = f.getAbsoluteFile();
            f = f.getCanonicalFile();
            if (TextUtils.isEmpty(f.toString()))
                f = new File("/");
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (f.isDirectory()) {
            String path = f.toString();
            mSettings.setLastDirectory(path);
            doOpenDirectory(path, true);
        } else if (f.exists()) {
            VideoActivity.intentTo(this, f.getPath(), f.getName());
        }
    }

    public void testSoap() {
        RequestEnvelopeTest requestEnvelop = new RequestEnvelopeTest();
        RequestBodyTest requestBody = new RequestBodyTest();
        RequestModelTest requestModel = new RequestModelTest();
        //requestModel.json = "";
        requestModel.getTest = "http://tempuri.org/";
        requestBody.getTest = requestModel;
        requestEnvelop.body = requestBody;
        Call<ResponseEnvelopeTest> call = BackendHelperTest.getService().getTest(requestEnvelop);
        call.enqueue(new Callback<ResponseEnvelopeTest>() {

            @Override
            public void onResponse(@NonNull Call<ResponseEnvelopeTest> call, @NonNull Response<ResponseEnvelopeTest> response) {
                ResponseEnvelopeTest responseEnvelope = response.body();
                if (responseEnvelope != null) {
                    String result = responseEnvelope.body.getTestResponse.getTestResult;
                    Log.d("Test", result);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseEnvelopeTest> call, @NonNull Throwable t) {
                Log.d("Test", " " + t.getMessage());
            }
        });
    }

    private void uploadTest() {
        RequestEnvelope requestEnvelop = new RequestEnvelope();
        RequestBody requestBody = new RequestBody();
        RequestModel requestModel = new RequestModel();
        requestModel.json = "{\n" +
                "  \"CameraID\": \"test\",\n" +
                "  \"LargeImg\": \"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\",\n" +
                "  \"SmallImg\": [\n" +
                "    \"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\"\n" +
                "  ],\n" +
                "  \"PeopleCount\": 3,\n" +
                "  \"PhotoDate\": 636651782593855073,\n" +
                "  \"Pnm\": 1\n" +
                "}";
        requestModel.pushDataByJson = "http://tempuri.org/";
        requestBody.pushDataByJson = requestModel;
        requestEnvelop.body = requestBody;
        Call<ResponseEnvelope> call = BackendHelper.getService().upload(requestEnvelop);
        call.enqueue(new Callback<ResponseEnvelope>() {

            @Override
            public void onResponse(Call<ResponseEnvelope> call, Response<ResponseEnvelope> response) {
                ResponseEnvelope responseEnvelope = response.body();
                if (responseEnvelope != null ) {
                    String result = responseEnvelope.body.pushDataByJsonResponse.result;
                    Log.d("Test", " " + result);
                }
            }

            @Override
            public void onFailure(Call<ResponseEnvelope> call, Throwable t) {
                Log.d("Test", t.getMessage());
            }
        });
    }
}
