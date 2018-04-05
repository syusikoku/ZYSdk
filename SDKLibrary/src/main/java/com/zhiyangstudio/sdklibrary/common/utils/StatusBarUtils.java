package com.zhiyangstudio.sdklibrary.common.utils;

import android.app.Activity;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by example on 2018/3/28.
 * 状态栏工具
 */

public class StatusBarUtils {

    /**
     * 设置StatusBar和BottomBar透明
     * 需要在setContentView->布局文件中设置
     * *    android:fitsSystemWindows="true"
     * android:background="@color/colorPrimary"
     *
     * @param activity
     */
    public static void setStatusBarAndBottomBarTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();

            // 状态栏透明
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            // navigation bar透明
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
    }
}
