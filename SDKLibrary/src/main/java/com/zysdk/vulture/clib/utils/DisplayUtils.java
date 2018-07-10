package com.zysdk.vulture.clib.utils;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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


    /**
     * 获取手机屏幕的真实高度
     */
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
     * @param sp the value in sp
     * @return int
     */
    public static int dip2sp(float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, mContext
                .getResources().getDisplayMetrics());
    }

    public static int px2dip(float pxValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
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
        final float scale = UiUtils.getContext().getResources().getDisplayMetrics().density;
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

    /**
     * 获取屏幕宽高
     */
    public static int[] getScreenWH() {
        WindowManager wm = getWindowManager();
        Display defaultDisplay = wm.getDefaultDisplay();
        return new int[]{defaultDisplay.getWidth(), defaultDisplay.getHeight()};
    }

    private static WindowManager getWindowManager() {
        return (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
    }


    //获取屏幕原始尺寸高度，包括虚拟功能键高度
    public static int getDpi(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context
                .WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * 获取 虚拟按键的高度
     */
    public static int getBottomStatusHeight(Context context) {
        int totalHeight = getDpi(context);
        int contentHeight = getScreenHeight();
        return totalHeight - contentHeight;
    }

    /**
     * 标题栏高度
     */
    public static int getTitleHeight(Activity activity) {
        return activity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
    }

    /**
     * 获得状态栏的高度
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }
}
