package com.mw.easylib.Net.Udp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class UdpGroup extends UdpUtil implements UdpSend.OnDataSendListener, UdpReceive.OnDataReceivedListener {
    private String Tag="udpGroup";
    MulticastSocket socket;
    WifiManager wifiManager;
    WifiManager.MulticastLock multicastLock;
    private Context curContext;

    {
        try {
            socket = new MulticastSocket(clientPort);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public UdpGroup(String groupIp, int groupPort) {
        super(groupIp, groupPort);
        try {
            socket.joinGroup(InetAddress.getByName(groupIp));
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private UdpReceive udpReceiver = new UdpReceive(socket, this);

    @Override
    public void send(String msg) {
        try {
            new UdpSend(socket, this).setSendPacket(msg.getBytes(), new InetSocketAddress(InetAddress.getByName(clientIp), clientPort)).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(byte[] msg) {
        try {
            new UdpSend(socket, this).setSendPacket(msg, new InetSocketAddress(InetAddress.getByName(clientIp), clientPort)).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String targetIp, int targetPort, String msg) {
        Log.i(Tag,"udp组播只支持组内数据发送请使用send(String msg)直接发送组内数据");
    }

    @Override
    public void send(String targetIp, int targetPort, byte[] msg) {
        Log.i(Tag,"udp组播只支持组内数据发送请使用send(byte[] msg)直接发送组内数据");
    }

    @Override
    public void onDestroy() {
        if (multicastLock != null) {
            multicastLock.release();
            multicastLock = null;
            wifiManager = null;
        }
        if (udpReceiver != null) {
            udpReceiver.cancel();
            udpReceiver = null;
        }
    }

    @SuppressLint("WifiManagerPotentialLeak")
    @Override
    public void start() {
        wifiManager = (WifiManager) curContext.getSystemService(Context.WIFI_SERVICE);
        multicastLock = wifiManager.createMulticastLock("multicast.test");
        multicastLock.acquire();
        udpReceiver.start();

    }

    @Override
    public void setContext(Context context) {
        curContext = context;

    }
    @Override
    public void onDataSent() {

    }

    @Override
    public void onDataSentError(String error) {

    }

    @Override
    public void onDataSentRuntimeException(RuntimeException exception) {

    }
}
