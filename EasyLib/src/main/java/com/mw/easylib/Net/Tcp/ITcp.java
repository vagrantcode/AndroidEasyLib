package com.mw.easylib.Net.Tcp;

public interface ITcp {
    void onConnectFailed();
    void onConnect();
    void onData(String data,String host,int port);

}
