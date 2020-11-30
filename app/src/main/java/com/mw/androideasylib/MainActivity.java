package com.mw.androideasylib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.mw.easylib.Common.DateUtil;
import com.mw.easylib.Net.Udp.IUdpUtil;
import com.mw.easylib.Net.Udp.UdpServer;
import com.mw.easylib.Net.Udp.UdpUtil;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UdpUtil udpUtil = new UdpServer(30001);
        udpUtil.setiUdpUtil(new IUdpUtil() {
            @Override
            public void onCreateFailed() {

            }

            @Override
            public void onData(byte[] data, String host, int port) {

            }

            @Override
            public void onData(String data, String host, int port) {
                Log.i("udp消息", data);
            }
        });
        udpUtil.start();
    }

}