package com.readsense.app.cameraupload;

import android.content.Context;
import android.os.HandlerThread;
import android.util.Log;

import com.google.gson.Gson;
import com.readsense.app.control.ControlGate;
import com.readsense.app.model.Result;
import com.readsense.app.net.BackendHelper;
import com.readsense.app.utils.WifiUtil;
import com.readsense.media.App;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import mobile.ReadFace.YMFace;
import mobile.ReadFace.YMFaceTrack;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class CameraUpload {

    private static final String TAG = "CameraUpload";

    public static final int IMAGE_QUALITY_THRESHOLD = 85;

    private YMFaceTrack mFaceTrack;

    private ExecutorService mExecutor = new ThreadPoolExecutor(1,
            1,
            0L, TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(1));

    public CameraUpload() {
        mFaceTrack = new YMFaceTrack();
        mFaceTrack.setCropScale(2.0f);

        int initResult = mFaceTrack.initTrack(App.sInstance.getApplicationContext(), YMFaceTrack.FACE_0,
                YMFaceTrack.RESIZE_WIDTH_640);
        if (initResult < 0) {
            Log.e(TAG, "initTrack error");
        } else {
            Log.d(TAG, "initTrack success");

        }
    }

    public List<YMFace> faces = null;

    public List<YMFace> startTrack(final byte[] data, final int width, final int height) {

        try {
            mExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    faces = mFaceTrack.trackMulti(data, width, height);
                    if (faces != null && !faces.isEmpty()) {
                        final int index = getValidIndex(faces);
                        final YMFace face = faces.get(index);
                        if (face == null) return;
                        //Log.d(TAG, "track face " + face.toString() + " trackId " + face.getTrackId());
                        final int trackId = face.getTrackId();
                        if (mSuccessIds.contains(trackId)) {
                            Log.d(TAG, "has uploaded " + trackId);
                            return;
                        }
                        String filePath = cropNormFace(face, index, data, width, height);
                        if (filePath == null) {
                            return;
                        }
                        //mSuccessIds.add(trackId);
                        Log.d(TAG, "filePath " + filePath);
                        boolean result = upload(filePath, trackId);
                        if (result) {
                            mSuccessIds.add(trackId);
                            //todo control gate
                            Log.d(TAG, "sendCmd");
                            ControlGate.sendCmd(App.ip, 1);
                        }

                        File file = new File(filePath);
                        file.delete();

                    }
                    //Log.d(TAG, "width " + width + " height " + height);
                }
            });
        } catch (RejectedExecutionException re) {
            //Log.d(TAG, "is busy...");
        }
        return faces;
    }

    private int getValidIndex(List<YMFace> faces) {
        int size = faces.size();
        float maxWidth = faces.get(0).getRect()[2];
        int index = 0;
        for (int i = 1; i < size; i++) {
            float width = faces.get(i).getRect()[2];
            if (width > maxWidth) {
                maxWidth = width;
                index = i;
            }
        }
        return index;
    }

    public String cropNormFace(YMFace face, int index, byte[] data, int width, int height) {

        float facialOri[] = face.getHeadpose();
        int x = (int) facialOri[0];
        int y = (int) facialOri[1];
        int z = (int) facialOri[2];

        /*if ((Math.abs(z) >= 25) || (y > 10 || y < -30) || (Math.abs(x) > 15)) {
            return null;
        }*/
        boolean notCompare = false;
        if (Math.abs(z) >= 25) notCompare = true;
        if (y > 10 || y < -30) notCompare = true;
        if (Math.abs(x) > 15) notCompare = true;
        if (notCompare) return null;
        int imgQuality = mFaceTrack.getFaceQuality(index);
        //Log.d(TAG, "cropNormFace imgQuality " + imgQuality);
        if (imgQuality > IMAGE_QUALITY_THRESHOLD) {
            Log.d(TAG, "cropNormFace imgQuality success " + imgQuality);

            String filePath = App.sInstance.getExternalCacheDir().getAbsolutePath() + File.separator + System.currentTimeMillis() + ".jpg";
            mFaceTrack.cropFace(data, width, height, face.getRect(),
                    filePath);
            return filePath;
        }
        return null;
    }


    private static final String MAC_ADDRESS = "mac_address";
    private static final String CAPTURE_AT = "capture_at";
    private static final String TRACK_ID = "track_id";
    private static final String CAPTURE_FACE = "capture_face";
    private static final String FACE_SCORE = "face_score";
    private static final String IMG_WIDTH = "img_width";
    private static final String IMG_HEIGHT = "img_height";
    private static final String FACE_WIDTH = "face_width";
    private static final String FACE_HEIGHT = "face_height";
    private static final String FACE_BLUR = "face_blur";
    private static final String FACE_FRONT = "face_front";
    private static final String CONFIDENCE_VALUE = "confidence_value";
    private static final String SYNCHRONIZED = "synchronized";

    private ArrayBlockingQueue<Integer> mSuccessIds = new ArrayBlockingQueue<>(100);

    private boolean upload(String filePath, int trackId) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(MAC_ADDRESS, getMacAddress())
                .addFormDataPart(CAPTURE_AT, getUTC())
                .addFormDataPart(TRACK_ID, App.timestamp + "" + trackId)
                .addFormDataPart(FACE_SCORE, "0.4")
                .addFormDataPart(IMG_WIDTH, "200")
                .addFormDataPart(IMG_HEIGHT, "200")
                .addFormDataPart(FACE_WIDTH, "200")
                .addFormDataPart(FACE_HEIGHT, "200")
                .addFormDataPart(FACE_BLUR, "0.8")
                .addFormDataPart(FACE_FRONT, "0.8")
                .addFormDataPart(CONFIDENCE_VALUE, "0.8")
                .addFormDataPart(SYNCHRONIZED, "true");
        RequestBody imageBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        builder.addFormDataPart(CAPTURE_FACE, file.getName(), imageBody);

        List<MultipartBody.Part> parts = builder.build().parts();
        try {
            Response<ResponseBody> response = BackendHelper.upload(parts).execute();
            if (response.body() == null) {
                Log.d(TAG, "upload failed");
                return false;
            }
            String body = response.body().string();
            Result result = new Gson().fromJson(body, Result.class);

            Log.d(TAG, "upload success " + trackId + " " + body);
            if (response.isSuccessful() /*&& checkResultConfidence(result)*/) {
                return true;
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
            return false;
        }
        return false;
    }

    private boolean checkResultConfidence(Result result) {
        List<Result.CandidatesBean> candidatesList = result.getCandidates();
        for (Result.CandidatesBean candidates : candidatesList) {
            if (candidates.getConfidence() > 0.52) {
                return true;
            }
        }
        return false;
    }

    private String mac = null;

    private String getMacAddress() {
        //return "c8:25:e1:83:57:3d";
        if (mac == null) {
            mac = WifiUtil.getMacAddress(App.sInstance.getApplicationContext());
        }
        Log.d(TAG, "mac " + mac);
        return mac;
    }

    private String getUTC() {
        return "" + Calendar.getInstance().getTimeInMillis();
    }
}
