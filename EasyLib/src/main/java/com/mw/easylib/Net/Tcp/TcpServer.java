package com.mw.easylib.Net.Tcp;

import android.os.Message;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class TcpServer extends TcpUtil implements TcpRecieve.OnDataReceivedListener {
    ServerSocket serverSocket;
    List<TcpRecieve> tcpRecieveList = new ArrayList<>();

    public TcpServer(int serverPort) {
        super(serverPort);
        start();
    }

    private void start() {
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (true) {
            Socket s = null;//接受一个连接
            try {
                s = serverSocket.accept();
                System.out.println("客户端接入" + s.getInetAddress());  //输出信息

                TcpRecieve tcpRecieve = new TcpRecieve(s, this); //创建多线程类把socket传入
                tcpRecieve.start();
                tcpRecieveList.add(tcpRecieve);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Log.i("tcpServer", e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void send(String msg) {

    }

    @Override
    public void didOnDisconnected(TcpRecieve tcpRecieve) {
        tcpRecieve.onDestroy();
        tcpRecieveList.remove(tcpRecieve);
    }

    @Override
    public void didReceiveData(byte[] data, String host, int port) throws UnsupportedEncodingException {

    }

    @Override
    public void didReceiveError(String message) {
        Log.i("tcp服务", message);
    }

    @Override
    public void didReceiveRuntimeException(RuntimeException exception) {
        Log.i("tcp服务2", exception.getMessage());
    }
}
