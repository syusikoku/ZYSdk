package com.zhiyangstudio.commonlib.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.ColorInt;
import android.support.annotation.RequiresPermission;
import android.support.design.internal.NavigationMenuPresenter;
import android.support.design.internal.NavigationMenuView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Pattern;

/**
 * Created by zhiyang on 2018/2/24.
 */

public class CommonUtils {
    public static final String REGEX_MINI_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,16}(?<!_)$";
    public static final String REGEX_F_USERNAME = "^[\\w\\u4e00-\\u9fa5]{6,20}(?<!_)$";

    /**
     * 是否需要校验权限
     */
    public static boolean hasNeedCheckPermission() {
        return hasMVersion();
    }

    /**
     * 是否大于等于M版本
     */
    private static boolean hasMVersion() {
        return Build.VERSION.SDK_INT >= 23;
    }

    /**
     * 检测某个应用是否在运行
     *
     * @param packName 是包名+服务的类名（例如：net.loonggg.testbackstage.TestService）
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
     * 判断某个Activity 界面是否在前台
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个Activity 界面是否在前台
     *
     * @param className 某个界面名称
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className)) {
            return false;
        }

        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName())) {
                return true;
            }
        }

        return false;

    }

    /**
     * 是否有权限写入系统配置
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
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke
                    (null);
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
     * 获取栈顶Activity
     *
     * @return 栈顶Activity
     */
    public static Activity getTopActivity() {
        try {
            Class activityThreadClass = Class.forName("android.app.ActivityThread");
            Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke
                    (null);
            Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
            activitiesField.setAccessible(true);
            Map activities = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                activities = (HashMap) activitiesField.get(activityThread);
            } else {
                activities = (ArrayMap) activitiesField.get(activityThread);
            }
            for (Object activityRecord : activities.values()) {
                Class activityRecordClass = activityRecord.getClass();
                Field pausedField = activityRecordClass.getDeclaredField("paused");
                pausedField.setAccessible(true);
                if (!pausedField.getBoolean(activityRecord)) {
                    Field activityField = activityRecordClass.getDeclaredField("activity");
                    activityField.setAccessible(true);
                    return (Activity) activityField.get(activityRecord);
                }
            }
        } catch (Exception e) {
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
        IntentUtils.forward(intent);
    }

    /**
     * 使用浏览器打开
     */
    public static void openByWebBroswer(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse(url);
        intent.setData(uri);
        IntentUtils.forward(intent);
    }

    /**
     * 分享文本
     */
    public static void shareText(String txt) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, txt);
        intent.setType("text/plan");
        IntentUtils.forward(intent);
    }

    /**
     * 让toolbar菜单同时显示图标和文字
     */
    public static void makeHeightMenu(Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method m = menu.getClass().getDeclaredMethod("setOptionalIconsVisible",
                            Boolean.TYPE);
                    m.setAccessible(true);
                    m.invoke(menu, true);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 设置navigationview menu item的分割线的颜色和高度
     *
     * @param navigationView
     * @param color
     * @param height
     */
    public static void setNavigationMenuLineStyle(NavigationView navigationView, @ColorInt final
    int color, final int height) {
        try {
            Field fieldByPressenter = navigationView.getClass().getDeclaredField("mPresenter");
            fieldByPressenter.setAccessible(true);
            NavigationMenuPresenter menuPresenter = (NavigationMenuPresenter) fieldByPressenter
                    .get(navigationView);
            Field fieldByMenuView = menuPresenter.getClass().getDeclaredField("mMenuView");
            fieldByMenuView.setAccessible(true);
            final NavigationMenuView mMenuView = (NavigationMenuView) fieldByMenuView.get
                    (menuPresenter);
            mMenuView.addOnChildAttachStateChangeListener(new RecyclerView
                    .OnChildAttachStateChangeListener() {
                @Override
                public void onChildViewAttachedToWindow(View view) {
                    RecyclerView.ViewHolder viewHolder = mMenuView.getChildViewHolder(view);
                    if (viewHolder != null && "SeparatorViewHolder".equals(viewHolder.getClass()
                            .getSimpleName()) && viewHolder.itemView != null) {
                        if (viewHolder.itemView instanceof FrameLayout) {
                            FrameLayout frameLayout = (FrameLayout) viewHolder.itemView;
                            View line = frameLayout.getChildAt(0);
                            line.setBackgroundColor(color);
                            line.getLayoutParams().height = height;
                            line.setLayoutParams(line.getLayoutParams());
                        }
                    }
                }

                @Override
                public void onChildViewDetachedFromWindow(View view) {

                }
            });
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除navigationview的滚动条
     *
     * @param navigationView
     */
    public static void disableNavigationViewScrollbars(NavigationView navigationView) {
        if (navigationView != null) {
            NavigationMenuView navigationMenuView = (NavigationMenuView) navigationView
                    .getChildAt(0);
            if (navigationMenuView != null) {
                navigationMenuView.setVerticalScrollBarEnabled(false);
            }
        }
    }

    /**
     * 删除搜索框背景
     */
    public static void deleteSearchPlate(SearchView searchView) {
        if (searchView != null) {
            try {
                Class<?> aClass = searchView.getClass();
                Field mSearchPlateField = aClass.getDeclaredField("mSearchPlate");
                mSearchPlateField.setAccessible(true);
                Object o = mSearchPlateField.get(searchView);
                if (o != null && o instanceof View) {
                    View view = (View) o;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        view.setBackground(null);
                    }
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 设置随机的字体颜色
     */
    public static void setRandomTextColor(TextView view) {
        int color = getRandomColor();
        view.setTextColor(color);
    }

    /**
     * 获取随机颜色
     */
    public static int getRandomColor() {
        int red, green, blue;
        Random random = new Random();
        red = random.nextInt(255);
        green = random.nextInt(255);
        blue = random.nextInt(255);
        return Color.rgb(red, green, blue);
    }

    /**
     * 设置随机的字体颜色
     */
    public static void setRandomBgColor(TextView view) {
        int color = getRandomColor();
        view.setBackgroundColor(color);
    }

    /**
     * 6-16位校验
     */
    public static boolean miniSecucrityCheck(String str) {
        boolean match = isMatch(REGEX_MINI_USERNAME, str);
        return match;
    }

    /**
     * 判断是否匹配正则
     *
     * @param regex 正则表达式
     * @param input 要匹配的字符串
     * @return {@code true}: 匹配<br>{@code false}: 不匹配
     */
    public static boolean isMatch(final String regex, final CharSequence input) {
        return input != null && input.length() > 0 && Pattern.matches(regex, input);
    }

    /**
     * 6-20位校验
     */
    public static boolean fullSecucrityCheck(String str) {
        boolean match = isMatch(REGEX_F_USERNAME, str);
        return match;
    }


}
