package com.mw.easylib.Net.Udp;

public interface IUdpUtil {
    void onCreateFailed();

    void onData(byte[] data, String host, int port);

    void onData(String data, String host, int port);
}
