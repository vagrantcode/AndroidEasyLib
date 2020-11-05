package com.mw.easylib.Net.Udp;

import android.content.Context;
import android.util.Log;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpServer extends UdpUtil implements UdpSend.OnDataSendListener {
    private String Tag="UdpServer";

    private DatagramSocket server;

    {
        try {
            server = new DatagramSocket(this.port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private UdpReceive udpReceiver = new UdpReceive(server, this);

    public UdpServer(int port) {
        super(port);
    }
    public UdpServer() {
        super(0);
    }

    @Override
    public void send(String msg) {
        Log.i(Tag,"udpServer服务不支持直接发送消息,请使用方法send(String targetIp, int targetPort, String msg)");
    }

    @Override
    public void send(byte[] msg) {
        Log.i(Tag,"udpServer服务不支持直接发送消息,请使用方法send(String targetIp, int targetPort, byte[] msg)");
    }


    @Override
    public void send(String targetIp, int targetPort, String msg) {
        try {
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(targetIp), targetPort);
            new UdpSend(server, this).setSendPacket(msg.getBytes(), socketAddress).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void send(String targetIp, int targetPort, byte[] msg) {
        try {
            SocketAddress socketAddress = new InetSocketAddress(InetAddress.getByName(targetIp), targetPort);
            new UdpSend(server, this).setSendPacket(msg, socketAddress).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        if (server != null) {
            server.close();
            server = null;
        }
        if (udpReceiver != null) {
            udpReceiver.cancel();
            udpReceiver = null;
        }
    }

    @Override
    public void start() {
        udpReceiver.start();
        send("192.168.0.153", 8888, "测试发发送");
    }

    @Override
    public void setContext(Context context) {

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
