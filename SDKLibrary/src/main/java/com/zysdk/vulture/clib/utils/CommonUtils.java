package com.zysdk.vulture.clib.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.support.design.widget.TabLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zysdk.vulture.clib.CommonConst;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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


    public static String getProcessName(Context cxt, int pid) {
        ActivityManager am = (ActivityManager) cxt.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }

    public static String getProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void requestedOrientation(Activity activity) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//设置竖屏模式
        }
    }

    /**
     * 修复自定义内容的toolbar文字不能水平居中的问题
     */
    public static void fixToolbar(Toolbar toolbar) {
        int contentInsetStartWithNavigation = toolbar.getContentInsetStartWithNavigation();
        LoggerUtils.loge("nav:" + contentInsetStartWithNavigation);
        toolbar.setContentInsetsRelative(contentInsetStartWithNavigation,
                contentInsetStartWithNavigation);
    }

    /**
     * 设置隐藏标题栏
     */
    public static void setNoTitleBar(Activity activity) {
        activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
    }

    /**
     * 设置全屏
     */
    public static void setFullScreen(Activity activity) {
        activity.getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 取消全屏
     */
    public static void cancelFullScreen(Activity activity) {
        activity.getWindow().clearFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置TabLayout indicator宽度
     */
    public static void setUpIndicatorWidth(TabLayout tabLayout, int marginLeft, int marginRight) {
        Class<?> tabLayoutClass = tabLayout.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayoutClass.getDeclaredField("mTabStrip");
            tabStrip.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        LinearLayout layout = null;
        try {
            if (tabStrip != null) {
                layout = (LinearLayout) tabStrip.get(tabLayout);
            }
            for (int i = 0; i < layout.getChildCount(); i++) {
                View child = layout.getChildAt(i);
                child.setPadding(0, 0, 0, 0);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout
                        .LayoutParams.MATCH_PARENT, 1);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                    params.setMarginStart(DisplayUtils.dp2px(marginLeft));
                    params.setMarginEnd(DisplayUtils.dp2px(marginRight));
                }
                child.setLayoutParams(params);
                child.invalidate();
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 进入wifi设置界面
     */
    public static void goWifiSetting() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT > 21) {
            intent = new Intent(Settings.ACTION_HOME_SETTINGS);
        } else {
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings", "com.android" +
                    ".settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        IntentUtils.forwardForResult(intent, CommonConst.REQ_CODE_WIFI_SETTING);
    }

    public static enum ViewState {
        SHOW, HIDE, GONE
    }

    /**
     * 设置view属性
     */
    public static void changeViewState(View view, ViewState state) {
        if (view != null) {
            switch (state) {
                case SHOW:
                    if (view.getVisibility() != View.VISIBLE) {
                        view.setVisibility(View.VISIBLE);
                    }
                    break;
                case HIDE:
                    if (view.getVisibility() != View.INVISIBLE) {
                        view.setVisibility(View.INVISIBLE);
                    }
                    break;
                case GONE:
                    if (view.getVisibility() != View.GONE) {
                        view.setVisibility(View.GONE);
                    }
                    break;
            }
        }
    }

}
