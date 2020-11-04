package com.easymeta.easylib.Net.Udp;

import android.content.Context;

import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

public class UdpClient extends UdpUtil implements UdpSend.OnDataSendListener {
    public UdpClient(String targetIp, int targetPort, int localPort) {
        super(targetIp, targetPort, localPort);
    }

    public UdpClient(String targetIp, int targetPort) {
        super(targetIp, targetPort);
    }

    private DatagramSocket server;

    {
        try {
            server = new DatagramSocket(this.port);
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private UdpReceive udpReceiver = new UdpReceive(server, this);

    @Override
    public void send(String msg) {
        try {
            new UdpSend(server, this).setSendPacket(msg.getBytes(), new InetSocketAddress(InetAddress.getByName(this.clientIp), this.clientPort)).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(byte[] msg) {
        try {
            new UdpSend(server, this).setSendPacket(msg, new InetSocketAddress(InetAddress.getByName(this.clientIp), this.clientPort)).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String targetIp, int targetPort, String msg) {
        try {
            new UdpSend(server, this).setSendPacket(msg.getBytes(), new InetSocketAddress(InetAddress.getByName(targetIp), targetPort)).start();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void send(String targetIp, int targetPort, byte[] msg) {
        try {
            new UdpSend(server, this).setSendPacket(msg, new InetSocketAddress(InetAddress.getByName(targetIp), targetPort)).start();
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
