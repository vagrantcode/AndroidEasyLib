package com.mw.easylib.Net.Tcp;

public abstract class TcpUtil implements ITcp {
    private int port = 0;
    private String clientIp = "127.0.0.1";
    private ITcp tcpListener;

    public TcpUtil(int serverPort) {
        this.port = serverPort;
    }

    public TcpUtil(String clientIp, int clientPort) {
        this.port = clientPort;
        this.clientIp = clientIp;
    }

    public String getClientIp() {
        return clientIp;
    }

    public int getPort() {
        return port;
    }

    public abstract void onDestroy();

    public abstract void send(String msg);

    public ITcp getTcpListener() {
        return tcpListener;
    }

    public void setTcpListener(ITcp tcpListener) {
        this.tcpListener = tcpListener;
    }

    @Override
    public void onConnectFailed() {
        if (tcpListener != null) {
            tcpListener.onConnectFailed();
        }
    }

    @Override
    public void onConnect() {
        if (tcpListener != null) {
            tcpListener.onConnect();
        }
    }

    @Override
    public void onData(String data, String host, int port) {
        if (tcpListener != null) {
            tcpListener.onData(data, host, port);
        }
    }
}
