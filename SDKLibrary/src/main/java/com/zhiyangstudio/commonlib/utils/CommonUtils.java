package com.zhiyangstudio.commonlib.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created by zhiyang on 2018/2/24.
 */

public class CommonUtils {

    /**
     * 使用反射从系统中获取当前的activity
     */
    public static Activity getCurrentActivity() {
        Class activityThreadClass = null;
        try {
            activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = (Map) activitiesField.get(activityThread);
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    Activity activity = (Activity) activityField.get(activityRecord);
                    return activity;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int getScreenHeight(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }

    public static int getScreenWidth(Context context) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    public static int getScreenHeightWithDecorations(Context context) {
        int heightPixes;
        WindowManager windowManager = ((Activity) context).getWindowManager();
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

    public static int dip2px(Context context, float dpVale) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpVale * scale + 0.5f);
    }

    public static int getStatusBarHeight(Context context) {
        Resources resources = context.getResources();
        int resourcesId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourcesId);
        return height;
    }

    /**
     * Converts sp to px
     *
     * @param context Context
     * @param sp      the value in sp
     * @return int
     */
    public static int dip2sp(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 获取屏幕宽高
     *
     * @param context
     * @return
     */
    public static int[] getScreenWH(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        return new int[]{defaultDisplay.getWidth(), defaultDisplay.getHeight()};
    }

    /**
     * 是否需要校验权限
     *
     * @return
     */
    public static boolean hasNeedCheckPermission() {
        return hasMVersion();
    }

    /**
     * 是否大于等于M版本
     *
     * @return
     */
    private static boolean hasMVersion() {
        return Build.VERSION.SDK_INT >= 23;
    }

    /**
     * 检测某个应用是否在运行
     *
     * @param packName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return
     */
    public static boolean isAppRunning(String packName) {
        boolean hasWork = false;
        ActivityManager am = (ActivityManager) UiUtils.getContext().getSystemService(Context
                .ACTIVITY_SERVICE);
        // TODO: 2018/1/31 获取任务栈
        List<ActivityManager.RunningTaskInfo> taskInfoList = am.getRunningTasks(100);
        if (taskInfoList == null || taskInfoList.isEmpty()) {
            return hasWork;
        }

        for (ActivityManager.RunningTaskInfo taskInfo : taskInfoList) {
            if (packName.equals(taskInfo.baseActivity.getPackageName())) {
                hasWork = true;
                break;
            }
        }
        return hasWork;
    }

    /**
     * 检测某个服务是否在运行
     *
     * @param serviceName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
     * @return
     */
    public static boolean isServiceWork(String serviceName) {
        boolean hasWork = false;
        Context context = UiUtils.getContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // TODO: 2018/1/31 获取真正运行的服务
        List<ActivityManager.RunningServiceInfo> serviceInfoList = am.getRunningServices(100);
        if (serviceInfoList == null || serviceInfoList.isEmpty()) {
            return hasWork;
        }

        for (ActivityManager.RunningServiceInfo serviceInfo : serviceInfoList) {
            if (serviceName.equals(serviceInfo.service.getClassName().toString())) {
                hasWork = true;
                break;
            }
        }
        return hasWork;
    }

    public static void showSnackbar(View parentView, String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        Snackbar.make(parentView, msg, Snackbar.LENGTH_LONG).show();
    }

}
