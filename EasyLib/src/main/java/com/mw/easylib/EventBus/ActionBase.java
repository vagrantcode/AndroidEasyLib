package com.mw.easylib.EventBus;

import org.greenrobot.eventbus.EventBus;

public abstract class ActionBase {
    public ActionBase() {
        EventBus.getDefault().register(this);
    }

    public abstract void onDestroy();
}
