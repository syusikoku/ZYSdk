package com.zhiyangstudio.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by zhiyang on 2018/4/8.
 */

public class DisplayUtils {
    private static Activity currentActivity;
    private static Context mContext;
    private static WindowManager mWindowManager;

    static {
        mContext = UiUtils.getContext();
        currentActivity = CommonUtils.getCurrentActivity();
        if (currentActivity != null) {
            mWindowManager = currentActivity.getWindowManager();
        }
        if (mWindowManager == null) {
            mWindowManager = getWindowManager();
        }
    }

    public static int getScreenHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeightWithDecorations() {
        int heightPixes;
        WindowManager windowManager = mWindowManager;
        Display display = windowManager.getDefaultDisplay();
        Point realSize = new Point();
        try {
            Display.class.getMethod("getRealSize", Point.class).invoke(display, realSize);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        heightPixes = realSize.y;
        return heightPixes;
    }

    public static int dip2px(float dpVale) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

    public static int getStatusBarHeight() {
        Resources resources = mContext.getResources();
        int resourcesId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourcesId);
        return height;
    }

    /**
     * Converts sp to px
     *
     * @param
     * @param sp the value in sp
     * @return int
     */
    public static int dip2sp(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mContext.getResources().getDisplayMetrics());
    }

    public static int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽高
     *
     * @return
     */
    public static int[] getScreenWH() {
        WindowManager wm = getWindowManager();
        Display defaultDisplay = wm.getDefaultDisplay();
        return new int[]{defaultDisplay.getWidth(), defaultDisplay.getHeight()};
    }

    private static WindowManager getWindowManager() {
        return (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }
}
