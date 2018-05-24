package com.zhiyangstudio.commonlib.utils;

import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhiyang on 2018/1/31.
 */

public class RxTimerUtils {
    private static Disposable mDisposable;

    /**
     * 延迟执行
     */
    public static void timer(long timeMill, final IRxNext next) {
        Logger.e("timer");
        Observable.timer(timeMill, TimeUnit.MILLISECONDS)
                .compose(RxUtils.io_main())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Logger.e("onSubscribe");
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Logger.e("onNext");
                        if (next != null) {
                            next.onNext(aLong);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError");
                        // TODO: 2018/1/31 取消订阅
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        Logger.e("onComplete");
                        // TODO: 2018/1/31 取消订阅
                        cancel();
                    }
                });
    }

    /*取消订阅*/
    public static void cancel() {
        if (mDisposable != null && !mDisposable.isDisposed()) {
            mDisposable.dispose();
            Logger.e("取消订阅");
        }
    }

    /**
     * 循环执行
     */
    public static void interval(long initDelay, long timeMill, final IRxNext next) {
        Logger.e("interval");
        Observable.interval(initDelay, timeMill, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        Logger.e("onSubscribe");
                        mDisposable = d;
                    }

                    @Override
                    public void onNext(Long aLong) {
                        Logger.e("onNext");
                        if (next != null) {
                            next.onNext(aLong);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Logger.e("onError");
                        // TODO: 2018/1/31 取消订阅
                        cancel();
                    }

                    @Override
                    public void onComplete() {
                        Logger.e("onComplete");
                        // TODO: 2018/1/31 取消订阅
                        cancel();
                    }
                });
    }

    public interface IRxNext {
        void onNext(long number);
    }
}
