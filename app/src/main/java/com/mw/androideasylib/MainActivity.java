package com.mw.androideasylib;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.mw.easylib.Common.DateUtil;
import com.mw.easylib.Net.Tcp.TcpClient;
import com.mw.easylib.Net.Tcp.TcpServer;
import com.mw.easylib.Net.Tcp.TcpUtil;
import com.mw.easylib.Net.Udp.IUdpUtil;
import com.mw.easylib.Net.Udp.UdpServer;
import com.mw.easylib.Net.Udp.UdpUtil;
import com.mw.easylib.ViewUtil.ZoomView;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      /*  UdpUtil udpUtil = new UdpServer(30001);
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
        udpUtil.start();*/
        RelativeLayout box=findViewById(R.id.content_box);
        ZoomView view=new ZoomView(getApplicationContext());
        view.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        //view.setBackgroundColor(Color.parseColor("#fff000"));
        box.addView(view);
       /* TcpUtil tcpUtil=new TcpClient("192.168.0.135",3030);
        for(int i=0;i<10;i++){
            try {
                Thread.sleep(3000);
                tcpUtil.send("ceshi fa song tcp");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
       new Thread(new Runnable() {
           @Override
           public void run() {
               TcpUtil tcpUtil=new TcpServer(8090);

           }
       }).start();
        //tcpUtil.send("ceshi fa song tcp");

    }

}