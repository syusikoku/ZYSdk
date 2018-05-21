package com.zhiyangstudio.commonlib.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Created by example on 2018/5/21.
 */

public class KeyboardUtils {

    /**
     * 显示软键盘
     *
     * @param activity 当前Activity
     */

    public static void showSoftInput(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

    }

    /**
     * 隐藏软键盘
     *
     * @param activity 当前Activity
     */

    public static void hideSoftInput(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getWindow().getDecorView().getWindowToken(), 0);
    }


    /**
     * 判断软键盘是否弹出
     */
    public static boolean isShowKeyboard(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(context
                .INPUT_METHOD_SERVICE);
        if (imm.hideSoftInputFromWindow(v.getWindowToken(), 0)) {
            imm.showSoftInput(v, 0);
            return true;
            //软键盘已弹出
        } else {
            return false;
            //软键盘未弹出
        }
    }
}
