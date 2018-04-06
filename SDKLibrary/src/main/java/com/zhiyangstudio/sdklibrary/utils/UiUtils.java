package com.zhiyangstudio.sdklibrary.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyangstudio.sdklibrary.common.corel.BaseApp;


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

    public static int getMainThreadId() {
        return BaseApp.getMainThreadId();
    }

    public static Looper getAppLooper() {
        return BaseApp.getAppLooper();
    }

    public static Handler getAppHandler() {
        return BaseApp.getAppHandler();
    }

    public static BaseApp getAppInstance() {
        return BaseApp.getAppInstance();
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

    public static Resources getResources(Context context) {
        return context.getResources();
    }

    public static String getStr(int arrsResId) {
        return getResources(getContext()).getString(arrsResId);
    }

    public static float getDimension(int arrsResId) {
        return getResources(getContext()).getDimension(arrsResId);
    }

    public static float getColor(int arrsResId) {
        return getResources(getContext()).getColor(arrsResId);
    }

    public static ColorStateList getColorStateList(int arrsResId) {
        return getResources(getContext()).getColorStateList(arrsResId);
    }

    public static Drawable getDrawable(int resId) {
        return getResources(getContext()).getDrawable(resId);
    }
}
