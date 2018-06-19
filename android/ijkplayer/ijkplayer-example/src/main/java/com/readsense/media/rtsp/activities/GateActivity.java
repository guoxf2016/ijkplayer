package com.readsense.media.rtsp.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.readsense.app.control.ControlGate;
import com.readsense.app.utils.WifiUtil;
import com.readsense.media.App;
import com.readsense.media.rtsp.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GateActivity extends AppCompatActivity {

    public static final String TAG = GateActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText rtspServer = (EditText) findViewById(R.id.rtspServer);
        //rtspServer.setText("rtmp://live.hkstv.hk.lxdns.com/live/hks");

        if (App.DEBUG) {
            rtspServer.setText("rtsp://192.168.1.18:554/1/h264major");
        }
        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String url = rtspServer.getText().toString().trim();
                /*if (!URLUtil.isValidUrl(url)) {
                    Toast.makeText(GateActivity.this, "rtsp server is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }*/
                EditText recoServer = (EditText) findViewById(R.id.recoServer);
                if (App.DEBUG) {
                    recoServer.setText("http://lightyear.readsense.cn/v1/event/create_from_device_by_raw_data");
                }
                final String recoUrl = recoServer.getText().toString().trim();
                if (URLUtil.isValidUrl(recoUrl)) {
                    App.url = recoUrl;
                } else {
                    Toast.makeText(GateActivity.this, "recognition server is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }
                EditText ipEt = (EditText) findViewById(R.id.controlIp);
                if (App.DEBUG) {
                    ipEt.setText("192.168.1.200");
                }
                final String ip = ipEt.getText().toString().trim();
                if (isIP(ip)) {
                    App.ip = ip;
                } else {
                    Toast.makeText(GateActivity.this, "ip is not valid", Toast.LENGTH_SHORT).show();
                    return;
                }
                VideoActivity.intentTo(GateActivity.this, url, "rtsp");
            }
        });
        Log.d(TAG, "mac " + WifiUtil.getMacAddress(App.sInstance.getApplicationContext()));
        if (App.DEBUG) {
            button.postDelayed(new Runnable() {
                @Override
                public void run() {
                    App.ip = "192.168.1.200";
                    //rtsp://192.168.1.18:554/1/h264major
                    VideoActivity.intentTo(GateActivity.this, "rtmp://live.hkstv.hk.lxdns.com/live/hks", "rtsp");
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
