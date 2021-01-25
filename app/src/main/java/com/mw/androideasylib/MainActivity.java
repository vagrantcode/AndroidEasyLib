package com.mw.androideasylib;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mw.easylib.Common.DateUtil;
import com.mw.easylib.Net.Tcp.TcpClient;
import com.mw.easylib.Net.Tcp.TcpServer;
import com.mw.easylib.Net.Tcp.TcpUtil;
import com.mw.easylib.Net.Udp.IUdpUtil;
import com.mw.easylib.Net.Udp.UdpServer;
import com.mw.easylib.Net.Udp.UdpUtil;
import com.mw.easylib.ViewUtil.ZoomView;


import java.util.List;


public class MainActivity extends AppCompatActivity {
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = new TextView(this);
        addContentView(text, new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        text.setText("name");
        setDatabase();
        RelativeLayout box = findViewById(R.id.content_box);
        ZoomView view = new ZoomView(getApplicationContext());
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        box.addView(view);
        new Thread(new Runnable() {
            @Override
            public void run() {
                TcpUtil tcpUtil = new TcpServer(8090);
            }
        }).start();
    }

    private void setDatabase() {

    }
}