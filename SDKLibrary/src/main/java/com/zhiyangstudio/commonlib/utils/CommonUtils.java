package com.zhiyangstudio.commonlib.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

/**
 * Created by zhiyang on 2018/2/24.
 */

public class CommonUtils {

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

    /**
     * 是否有权限写入系统配置
     *
     * @param context
     * @return
     */
    public static boolean canWriteOsSetting(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.System.canWrite(context);
        }
        return true;
    }

    /**
     * 跳转到修改系统设置
     */
    @RequiresPermission(android.Manifest.permission.WRITE_SETTINGS)
    public static void goManageWriteSettings() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        Context context;
        Activity activity = getCurrentActivity();
        context = activity;
        if (activity == null) {
            context = UiUtils.getContext();
        }
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            if (activity == null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                activity.startActivity(intent);
            }
        }
    }

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

    /**
     * 跳转到应用程序详情
     */
    public static void goAppDetailSetting() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Context context;
        Activity activity = getCurrentActivity();
        context = activity;
        if (activity == null) {
            context = UiUtils.getContext();
        }
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            if (activity == null) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                activity.startActivity(intent);
            }
        }
    }
}