package com.zysdk.vulture.clib.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.ColorInt;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.zysdk.vulture.clib.corel.BaseApp;


/**
 * Created by zhiyang on 2018/3/29.
 */

public class UiUtils {

    public static String getMainThreadName() {
        return BaseApp.getMainThreadName();
    }

    public static int getAppProcessId() {
        return BaseApp.getAppProcessId();
    }

    public static Looper getAppLooper() {
        return BaseApp.getAppLooper();
    }

    public static BaseApp getAppInstance() {
        return BaseApp.getAppInstance();
    }

    /**
     * 延时在主线程执行runnable
     */
    public static boolean postDelayed(Runnable runnable, long delayMillis) {
        return getAppHandler().postDelayed(runnable, delayMillis);
    }

    public static Handler getAppHandler() {
        return BaseApp.getAppHandler();
    }

    /**
     * 从主线程looper里面移除runnable
     */
    public static void removeCallbacks(Runnable runnable) {
        getAppHandler().removeCallbacks(runnable);
    }

    public static void runInMainThread(Runnable runnable) {
        if (isRunInMainThread()) {
            runnable.run();
        } else {
            post(runnable);
        }
    }

    // 判断当前的线程是不是在主线程
    public static boolean isRunInMainThread() {
        return android.os.Process.myTid() == getMainThreadId();
    }

    /**
     * 在主线程执行runnable
     */
    public static boolean post(Runnable runnable) {
        return getAppHandler().post(runnable);
    }

    public static int getMainThreadId() {
        return BaseApp.getMainThreadId();
    }

    /**
     * 对toast的简易封装。线程安全，可以在非UI线程调用。
     */
    public static void showToastSafe(final int resId) {
        showToastSafe(getStr(resId));
    }

    /**
     * 对toast的简易封装。线程安全，可以在非UI线程调用。
     */
    public static void showToastSafe(final String str) {
        if (isRunInMainThread()) {
            showToast(str);
        } else {
            post(new Runnable() {
                @Override
                public void run() {
                    showToast(str);
                }
            });
        }
    }

    public static String getStr(int arrsResId) {
        return getResources(getContext()).getString(arrsResId);
    }

    private static void showToast(String str) {
        ToastUtils.showShort(str);
    }

    public static Resources getResources(Context context) {
        return context.getResources();
    }

    public static Context getContext() {
        return BaseApp.getContext();
    }

    public static View inflateView(int resId, ViewGroup parent) {
        return LayoutInflater.from(getContext()).inflate(resId, parent, false);
    }

    public static View inflateViewY(int resId, ViewGroup parent) {
        return LayoutInflater.from(getContext()).inflate(resId, parent, true);
    }

    public static View inflateView(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null, false);
    }

    /**
     * 通过父局移除自己
     */
    public static void removeSelfByParent(View view) {
        if (view != null && view.getParent() != null && view.getParent() instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            if (viewGroup != null) {
                viewGroup.removeView(view);
            }
        }
    }

}
