package com.mw.easylib.NeedOptimized;

import android.content.Context;
import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ObservableHelper {
    private String Tag = "ObservableHelper";
    private static ObservableHelper instance = null;

    public static ObservableHelper getInstance(Context context) {
        synchronized (ObservableHelper.class) {
            if (instance == null) {
                instance = new ObservableHelper(context.getApplicationContext());
            }
        }
        return instance;
    }

    public ObservableHelper(Context context) {

    }

    /**
     * 定时任务
     *
     * @param initialDelay 第一次延迟多久执行
     * @param period       执行周期
     * @param prepareRun   需要异步执行的任务
     * @param afterRun     需要在主线程执行的任务
     */
    public <T> Subscription interval(long initialDelay, long period, final methodBody<T> prepareRun, final methodBody<T> afterRun) {
        return Observable.interval(initialDelay, period, TimeUnit.MILLISECONDS).observeOn(Schedulers.newThread()).subscribe(new Subscriber<Long>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                Log.i(Tag, "循环任务异常" + e);
            }

            @Override
            public void onNext(Long aLong) {
                T t= null;
                try {
                    t = prepareRun.run(null);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mainThreadTask(t, new methodBody<T>() {
                   @Override
                   public T run(T params) {
                       try {
                           afterRun.run(params);
                       } catch (IOException e) {
                           e.printStackTrace();
                       }
                       return null;
                   }
               });

            }
        });
    }

    /**
     * @param initialDelay 第一次延迟多久执行
     * @param period       执行周期
     * @param task         需要异步执行的任务
     */
    public <T> Subscription interval(long initialDelay, long period, final methodBody<T> task) {
        return Observable.interval(initialDelay, period, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        try {
                            task.run(null);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    /**
     * 异步执行任务
     *
     * @param task 需要在新线程执行的任务
     */
    public <T> void asyncTask(T param, final methodBody<T> task) {
        Observable.just(param).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.newThread()).subscribe(new Subscriber<T>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(T t) {
                try {
                    task.run(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public <T> void asyncTask(T param, final methodBody<T> task, Boolean flag) {
        Observable.just(param).subscribeOn(Schedulers.newThread()).observeOn(Schedulers.trampoline()).subscribe(new Subscriber<T>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(T t) {
                try {
                    task.run(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    public <T> void mainThreadTask(T param, final methodBody<T> task) {
        Subscription ls = Observable.just(param).subscribeOn(AndroidSchedulers.mainThread()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<T>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(T t) {
                try {
                    task.run(t);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    public interface methodBody<T> {
        T run(T params) throws IOException;

    }
}
