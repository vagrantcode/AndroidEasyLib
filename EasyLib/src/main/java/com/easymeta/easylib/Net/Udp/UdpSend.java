package com.easymeta.easylib.Net.Udp;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;

public class UdpSend extends Thread {
    private OnDataSendListener listener;
    private DatagramSocket mSocket;
    SenderPacket senderPacket;

    public UdpSend(DatagramSocket socket, OnDataSendListener listener) {
        this.mSocket = socket;
        this.listener = listener;
    }

    public UdpSend setSendPacket(byte[] data, SocketAddress address) {
        senderPacket=new SenderPacket();
        senderPacket.data = data;
        senderPacket.socketAddress = address;
        return this;
    }

    @Override
    public void run() {
        super.run();
        try {
            mSocket.send(new DatagramPacket(senderPacket.data, senderPacket.data.length, senderPacket.socketAddress));

            if (listener != null) {
                listener.onDataSent();
            }
        } catch (RuntimeException rte) {
            if (listener != null) {
                listener.onDataSentRuntimeException(rte);
            }
        } catch (Exception e) {
            if (listener != null) {
                listener.onDataSentError(e.getMessage());
            }
        } finally {
            senderPacket=null;
        }
    }

    public void cancel() {
        this.interrupt();
    }

    private class SenderPacket {
        SocketAddress socketAddress;
        byte[] data;
    }

    public interface OnDataSendListener {
        void onDataSent();

        void onDataSentError(String error);

        void onDataSentRuntimeException(RuntimeException exception);
    }
}
