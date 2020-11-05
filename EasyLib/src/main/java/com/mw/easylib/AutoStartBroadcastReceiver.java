package com.mw.easylib;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;


/**
 * Created by wang on 2018/10/4.
 */
/*
* 使用方法：在manifests里加入监听：
* <receiver android:name=".BaseUtil.AutoStartBroadcastReceiver"
            android:enabled="true"
            android:exported="true"
            android:permission="android.permission.RECEIVE_BOOT_COMPLETED">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
                <category android:name="android.intent.category.HOME"/>
            </intent-filter>
        </receiver>
* */
public class AutoStartBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        PackageManager packageManager = context.getPackageManager();

        if (intent.getAction().equals(action_boot)) {
            Intent sayHelloIntent =  packageManager.getLaunchIntentForPackage(context.getPackageName());
            sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(sayHelloIntent);
        }
    }
}
