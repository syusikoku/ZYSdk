package com.zhiyangstudio.sdklibrary.common.utils;

import android.content.Context;
import android.os.Build;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by zhiyang on 2018/2/24.
 */

public class CommonUtils {
    public static int[] getScreenWH(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display defaultDisplay = wm.getDefaultDisplay();
        return new int[]{defaultDisplay.getWidth(), defaultDisplay.getHeight()};
    }

    public static boolean hasNeedCheckPermission() {
        return hasMVersion();
    }

    private static boolean hasMVersion() {
        return Build.VERSION.SDK_INT >= 23;
    }
}
