package com.mw.easylib.Net.Tcp;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;

public class TcpClient extends TcpUtil implements TcpSend.SendListener, TcpRecieve.OnDataReceivedListener {
    private TcpRecieve tcpRecieve;
    private boolean isReady = false;
    private boolean destroy = false;

    public TcpClient(String clientIp, int clientPort) {
        super(clientIp, clientPort);
        new Thread(new Runnable() {
            @Override
            public void run() {
                start();
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        destroy = true;
        if (tcpRecieve != null) {
            tcpRecieve.onDestroy();
            tcpRecieve = null;
        }
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }

    @Override
    public void send(final String msg) {
        if (destroy) return;
        if (isReady) {
            if (socket != null) {
                new TcpSend(socket, this).setSendMsg(msg.getBytes()).start();
            }
        } else {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(20);
                        send(msg);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }


    }

    private Socket socket;

    public void start() {
        if (socket != null) return;
        try {
            socket = new Socket(getClientIp(), getPort());
            isReady = true;
            onConnect();
        } catch (IOException e) {
            socket = null;
            isReady = false;
            onConnectFailed();
            e.printStackTrace();
        }
        if (isReady) {
            tcpRecieve = new TcpRecieve(socket, this);
            tcpRecieve.start();
        }
    }

    private void reconnect() {

    }


    @Override
    public void onDataSend() {

    }

    @Override
    public void onDataSendError(String error) {
        Log.i("数据发送错误", error);
    }

    @Override
    public void onDataSendRuntimeException(RuntimeException exception) {
        Log.i("数据发送错误1", exception.getMessage());
    }

    @Override
    public void didOnDisconnected(TcpRecieve tcpRecieve) {
        tcpRecieve.onDestroy();
        this.tcpRecieve = null;
    }

    @Override
    public void didReceiveData(byte[] data, String host, int port) throws UnsupportedEncodingException {

    }

    @Override
    public void didReceiveError(String message) {

    }

    @Override
    public void didReceiveRuntimeException(RuntimeException exception) {

    }
}
