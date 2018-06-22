package com.readsense.media.rtsp.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.TimeUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.readsense.app.utils.WifiUtil;
import com.readsense.media.App;
import com.readsense.media.rtsp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BodyRecoSettings extends AppCompatActivity {

    public static final String TAG = BodyRecoSettings.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.body_reco_settings);

        final SharedPreferences sp = getSharedPreferences("com.readsense.media.rtsp", Context.MODE_PRIVATE);
        final EditText rtspServer = (EditText) findViewById(R.id.rtspServer);
        //rtspServer.setText("rtmp://live.hkstv.hk.lxdns.com/live/hks");
        //rtsp://192.168.1.18:554/1/h264major"
        if (App.DEBUG) {
            rtspServer.setText("rtsp://192.168.1.18:554/1/h264major");
        }
        final String savedUrl = sp.getString("rtspServer", "");
        Log.d(TAG, "savedUrl " + savedUrl);
        /*if (!TextUtils.isEmpty(savedUrl)) {
            rtspServer.setText(savedUrl);
        }*/
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = rtspServer.getText().toString().trim();
                /*if (!URLUtil.isValidUrl(url)) {
                    Toast.makeText(GateActivity.this, "rtsp server is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if (!url.equals(savedUrl)) {
                    sp.edit().putString("rtspServer", url).apply();
                }

                VideoActivity.intentTo(BodyRecoSettings.this, url, "rtsp");
            }
        });
        Log.d(TAG, "mac " + WifiUtil.getMacAddress(App.sInstance.getApplicationContext()));
        if (App.DEBUG) {
            button.postDelayed(new Runnable() {
                @Override
                public void run() {
                    App.ip = "192.168.1.200";
                    //rtsp://192.168.1.18:554/1/h264major
                    final String url = rtspServer.getText().toString().trim();
                    VideoActivity.intentTo(BodyRecoSettings.this, url, "rtsp");
                    //ControlGate.sendCmd(App.ip, 1);
                }
            }, 2000);
        }

    }

    public boolean isIP(String ip){
        Pattern ipPattern=Pattern.compile("([1-9]|[1-9]\\d|1\\d{2}|2[0-1]\\d|22[0-3])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}");
        Matcher matcher=ipPattern.matcher(ip);
        return matcher.matches();
    }
}
