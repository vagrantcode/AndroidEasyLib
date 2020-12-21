package com.mw.easylib.Net.Tcp;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.util.ArrayList;

public class TcpRecieve extends Thread {
    private Socket socket;
    OnDataReceivedListener listener;

    public TcpRecieve(Socket socket, OnDataReceivedListener listener) {
        this.socket = socket;
        this.listener = listener;
    }

    @Override
    public void run() {
        super.run();
        while (!isInterrupted()) {
                try {

                    InputStream is = socket.getInputStream();
                    int len = -1;
                    BufferedInputStream br = new BufferedInputStream(is);
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    while ((len = br.read(buf)) != -1) {
                        byteArrayOutputStream.write(buf, 0, len);
                        if (br.available() <= 0) //添加这里的判断
                        {
                            break;
                        }
                    }
                    if(byteArrayOutputStream.toByteArray().length>0){
                        String str = new String(byteArrayOutputStream.toByteArray());
                        Log.i("收到消息", str);
                    }else{
                        if(listener!=null){
                            listener.didOnDisconnected(this);
                        }
                    }


                }catch (RuntimeException e){
                    e.printStackTrace();
                    if (listener != null) {
                        listener.didReceiveRuntimeException(e);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                    if (listener != null) {
                        listener.didReceiveError(e.getMessage());
                    }
                }
        }

    }

    public void onDestroy() {
        interrupt();
    }

    private static String readLine(InputStream is, int contentLe) throws IOException {
        ArrayList lineByteList = new ArrayList();
        byte readByte;
        int total = 0;
        if (contentLe != 0) {
            do {
                readByte = (byte) is.read();
                lineByteList.add(Byte.valueOf(readByte));
                total++;
            } while (total < contentLe);//消息体读还未读完
        } else {
            do {
                readByte = (byte) is.read();
                lineByteList.add(Byte.valueOf(readByte));
            } while (readByte != 10);
        }

        byte[] tmpByteArr = new byte[lineByteList.size()];
        for (int i = 0; i < lineByteList.size(); i++) {
            tmpByteArr[i] = ((Byte) lineByteList.get(i)).byteValue();
        }
        lineByteList.clear();

        return new String(tmpByteArr);
    }

    public interface OnDataReceivedListener {
        void didOnDisconnected(TcpRecieve tcpRecieve);
        void didReceiveData(byte[] data, String host, int port) throws UnsupportedEncodingException;

        void didReceiveError(String message);

        void didReceiveRuntimeException(RuntimeException exception);
    }
}
