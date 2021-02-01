package com.mw.easylib.NeedOptimized;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/*
 * 主activity的拦截层，将一些基本的程序方法放置在此处*/

public class ActivityMiddleSetting extends Activity {
    private String Tag = "ActivityMiddleSetting";
    private boolean forbidBack = false;

    public boolean isForbidBack() {
        return forbidBack;
    }

    public void setForbidBack(boolean forbidBack) {
        this.forbidBack = forbidBack;
    }

    public void setOnBack(OnBack onBack) {
        this.onBack = onBack;
    }

    private static class config {
        public static Boolean isExitRestart = true;//是否异常退出重启
    }

    private Context curContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏Activity标题栏；
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        //全屏；
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //请求root权限
        //ShellUtil.checkRootPermission();
        //加载native库
       /* IjkMediaPlayer.loadLibrariesOnce(null);
        IjkMediaPlayer.native_profileBegin("libijkplayer.so");*/
        Observable.just(this)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .subscribe(new Subscriber<ActivityMiddleSetting>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ActivityMiddleSetting activityMiddleSetting) {
                        askAuthority();
                    }
                });
        if (config.isExitRestart) {
            setRestarListionner(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    /*
     * 设置app全屏*/
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        //全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }

    }

    /*
     * 设置按两次返回键退出---记录第一次按下的时间*/
    private long firstTime = 0;
    private OnBack onBack;

    /*
     * 判断是否连续的两次返回，如果是则退出程序*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        long secondTime = System.currentTimeMillis();
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (secondTime - firstTime < 2000) {
                if (!isForbidBack()) {
                    System.exit(0);
                }

            } else {
                if (!isForbidBack()) {
                    Toast.makeText(getApplicationContext(), "再按一次返回键退出", Toast.LENGTH_SHORT).show();
                    firstTime = System.currentTimeMillis();
                }
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public interface OnBack {
        void onFirstBack();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults.length >= 1 && PackageManager.PERMISSION_GRANTED == grantResults[0]) {
                Observable.just("")
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<String>() {
                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onNext(String s) {
                                startAppTask();
                            }
                        });
            } else {
                Toast.makeText(getApplicationContext(), "必须权限不足，退出程序", Toast.LENGTH_LONG).show();
                System.exit(0);
            }
        }
    }

    //1,请求读写权限-高版本api使用
    public void askAuthority() {
        //高版本api动态获取权限
        if (Build.VERSION.SDK_INT >= 23 && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            Observable.just("")
                    .subscribeOn(Schedulers.trampoline())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<String>() {
                        @Override
                        public void onCompleted() {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onNext(String s) {
                            startAppTask();
                        }
                    });

        }
       /* try {

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(),"请求权限异常",Toast.LENGTH_SHORT).show();
            Log.e(Tag, "权限请求错误" + e);
        }*/
    }

    /*
     * 获得权限后执行任务*/
    public void startAppTask() {

    }

    //设置app异常退出重启
    private Context restartContext;
    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            restartApp(); //发生崩溃异常时,重启应用
        }
    };

    public void setRestartContext(Context context) {
        curContext = context;
    }

    //重启app
    private void restartApp() {
        if (curContext != null) {
            Intent intent = new Intent();
            intent.setClass(restartContext.getApplicationContext(), curContext.getClass());
            @SuppressLint("WrongConstant") PendingIntent restartIntent = PendingIntent.getActivity(
                    restartContext.getApplicationContext(), 0, intent, Intent.FLAG_ACTIVITY_CLEAR_TASK);
            //退出程序
            AlarmManager mgr = (AlarmManager) restartContext.getApplicationContext().getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 3000,
                    restartIntent); // 3秒钟后重启应用
            //System.exit(1);
            //IjkMediaPlayer.native_profileEnd();
            //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
            //android.os.Process.killProcess(android.os.Process.myPid());
            finishActivity(0);
            this.finish();
        } else {
            ObservableHelper.getInstance(this.getApplicationContext()).mainThreadTask("", new ObservableHelper.methodBody<String>() {
                @Override
                public String run(String params) {
                    Toast.makeText(getApplicationContext(), "未设置重新页面，将无法自动重启", Toast.LENGTH_SHORT).show();
                    return null;
                }
            });
            finishActivity(0);
            this.finish();
        }

    }

    public void setRestarListionner(Context context) {
        restartContext = context;
        //设置异常退出重启
        try {
            Thread.setDefaultUncaughtExceptionHandler(handler);

        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "异常退出重启操作错误" + e, Toast.LENGTH_LONG).show();
        }
    }
}
