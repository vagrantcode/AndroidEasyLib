package com.easymeta.androideasylib;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.easymeta.easylib.Net.Udp.UdpServer;
import com.easymeta.easylib.Net.Udp.UdpUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UdpUtil udpUtil=new UdpServer(8080);
        udpUtil.start();
    }
}