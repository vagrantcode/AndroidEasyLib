package com.mw.easylib.Net.Udp;

import android.content.Context;
import android.util.Log;

import java.nio.charset.Charset;

public abstract class UdpUtil implements UdpReceive.OnDataReceivedListener {
    private UdpResultEm type;
    public String clientIp = "";
    public int clientPort = 0;
    public int port = 0;
    private IUdpUtil iUdpUtil = null;

    public UdpUtil() {
        this.type = UdpResultEm.String;
    }

    public UdpUtil(int port) {
        this.type = UdpResultEm.String;
        this.port = port;
    }

    public UdpUtil(String clientIp, int clientPort) {
        this.type = UdpResultEm.String;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
    }

    public UdpUtil(String clientIp, int clientPort, int localPort) {
        this.type = UdpResultEm.String;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.port = localPort;
    }

    public UdpUtil(UdpResultEm resultType) {
        this.type = resultType;
    }

    public UdpUtil(UdpResultEm resultType, int port) {
        this.type = resultType;
        this.port = port;
    }

    public UdpUtil(UdpResultEm resultType, String clientIp, int clientPort) {
        this.type = resultType;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
    }

    public UdpUtil(UdpResultEm resultType, String clientIp, int clientPort, int localPort) {
        this.type = resultType;
        this.clientIp = clientIp;
        this.clientPort = clientPort;
        this.port = localPort;
    }

    public abstract void send(String msg);
    public abstract void send(byte[] msg);

    public abstract void send(String targetIp, int targetPort, String msg);
    public abstract void send(String targetIp, int targetPort, byte[] msg);

    public abstract void onDestroy();
    public  void onDestroyDelay(){
       onDestroyDelay(100);
    }
    public  void onDestroyDelay(final long millis){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(millis);
                    onDestroy();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public abstract void start();

    public abstract void setContext(Context context);
    @Override
    public void didReceiveData(byte[] data, String host, int port) {
        Log.i("udp收到消息",new String(data));
        if (getiUdpUtil() != null) {
            if (getType().equals(UdpResultEm.String)) {
                getiUdpUtil().onData(new String(data, Charset.defaultCharset()), host, port);
            } else {
                getiUdpUtil().onData(data, host, port);
            }

        }
    }

    @Override
    public void didReceiveError(String message) {

    }

    @Override
    public void didReceiveRuntimeException(RuntimeException exception) {

    }

    public IUdpUtil getiUdpUtil() {
        return iUdpUtil;
    }

    public void setiUdpUtil(IUdpUtil iUdpUtil) {
        this.iUdpUtil = iUdpUtil;
    }

    public UdpResultEm getType() {
        return type;
    }

}
