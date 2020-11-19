package com.mw.easylib.ViewUtil;

import android.os.Handler;
import android.os.Message;
import android.view.View;

public class ViewUtil {
    public void ViewMeasure(final View v, final Handler handler, final int flag) {
        v.post(new Runnable() {
            @Override
            public void run() {
                int width = v.getMeasuredWidth();
                int height = v.getMeasuredHeight();
                Message message = new Message();
                message.what = flag;
                message.obj = new RectWH(width, height);
                handler.sendMessage(message);
            }
        });
    }

    public class RectWH {
        private int width;
        private int height;

        public RectWH() {
        }

        public RectWH(int width, int height) {
            this.width = width;
            this.height = height;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
    }
}
