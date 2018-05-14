package com.zhiyangstudio.commonlib.utils;

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
import com.zhiyangstudio.commonlib.corel.BaseApp;


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

    public static View inflateView(int resId) {
        return LayoutInflater.from(getContext()).inflate(resId, null, false);
    }

    public static String[] getStrArrs(int arrsResId) {
        return getResources(getContext()).getStringArray(arrsResId);
    }

    public static float getDimension(int arrsResId) {
        return getResources(getContext()).getDimension(arrsResId);
    }

    public static int getColor(int arrsResId) {
        return getResources(getContext()).getColor(arrsResId);
    }

    public static ColorStateList getColorStateList(int arrsResId) {
        return getResources(getContext()).getColorStateList(arrsResId);
    }

    public static Drawable getDrawable(int resId) {
        return getResources(getContext()).getDrawable(resId);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     */
    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics()
                .scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics()
                .scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
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

    @ColorInt
    public static int getColorAttr(int attr) {
        Context context = getContext();
        Resources.Theme theme = context.getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{attr});
        int color = typedArray.getColor(0, Color.LTGRAY);
        typedArray.recycle();
        return color;
    }

    public static int Dp2Px(Context context, int dpi) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpi, context
                .getResources().getDisplayMetrics());
    }

    public static int Px2Dp(Context context, int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context
                .getResources().getDisplayMetrics());
    }

    public static int Sp2Px(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context
                .getResources().getDisplayMetrics());
    }

    public static int Px2Sp(Context context, int px) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context
                .getResources().getDisplayMetrics());
    }
}
