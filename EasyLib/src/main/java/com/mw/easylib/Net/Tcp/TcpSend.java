package com.mw.easylib.Net.Tcp;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class TcpSend extends Thread {
    private Socket socket;
    private SendListener listener;
    private OutputStream outputStream;
    private byte[] bytes;

    public TcpSend(Socket socket, SendListener listener) {
        this.socket = socket;
        this.listener = listener;
    }

    public TcpSend setSendMsg(byte[] bytes) {
        this.bytes = bytes;
        return this;
    }

    @Override
    public void run() {
        super.run();
        try {
            outputStream = socket.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            if (listener != null) {
                listener.onDataSend();
            }
        } catch (RuntimeException ioE) {
            if (listener != null) {
                listener.onDataSendRuntimeException(ioE);
            }
             ioE.printStackTrace();
        } catch (Exception e) {
            if (listener != null) {
                listener.onDataSendError(e.getMessage());
            }
             e.printStackTrace();
        }finally {
            outputStream=null;
        }
    }

    public void cancel() {
        this.interrupt();
    }

    public interface SendListener {
        void onDataSend();

        void onDataSendError(String error);

        void onDataSendRuntimeException(RuntimeException exception);
    }
}
