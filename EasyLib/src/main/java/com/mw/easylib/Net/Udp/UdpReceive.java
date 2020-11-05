package com.mw.easylib.Net.Udp;

import android.util.Log;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;

public class UdpReceive extends Thread {
    DatagramSocket socket;
    OnDataReceivedListener receiverListener;
    boolean isCancel = false;

    public UdpReceive(DatagramSocket socket, OnDataReceivedListener listener) {
        this.socket = socket;
        this.receiverListener = listener;
    }


    @Override
    public void run() {
        super.run();
        while (!isInterrupted() && !isCancel) {
            byte[] recvBuf = new byte[1024];
            DatagramPacket recvPacket = new DatagramPacket(recvBuf, recvBuf.length);
            try {
                socket.receive(recvPacket);
                if (receiverListener != null) {

                    byte[] buf = recvPacket.getData();
                    recvPacket.getLength();
                    byte[] b = new byte[recvPacket.getLength()];
                    receiverListener.didReceiveData(Arrays.copyOf(recvPacket.getData(),recvPacket.getLength()), recvPacket.getAddress().getHostAddress(), recvPacket.getPort());
                }
            } catch (IOException e) {
                if (receiverListener != null) {
                    receiverListener.didReceiveError(e.getMessage());
                }
                Log.i("Udp接收数据错误：", e.getMessage() + "");
                e.printStackTrace();
            } catch (RuntimeException rte) {
                if (receiverListener != null) {
                    receiverListener.didReceiveRuntimeException(rte);
                }
            }
        }
    }

    public void cancel() {
        isCancel = true;
        this.interrupt();
    }

    public interface OnDataReceivedListener {
        void didReceiveData(byte[] data, String host, int port) throws UnsupportedEncodingException;

        void didReceiveError(String message);

        void didReceiveRuntimeException(RuntimeException exception);
    }
}
