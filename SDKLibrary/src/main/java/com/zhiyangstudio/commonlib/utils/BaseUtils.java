package com.zhiyangstudio.commonlib.utils;

import android.app.Activity;

import com.zhiyangstudio.commonlib.corel.BaseActivity;

/**
 * Created by example on 2018/4/8.
 */

public class BaseUtils {
    protected static BaseActivity mCurrentActivity;

    static {
        mCurrentActivity = reGetCurrentActivity();
    }

    public static BaseActivity reGetCurrentActivity() {
        BaseActivity baseActivity = mCurrentActivity;
        // activity为空或是finish状态
        if (mCurrentActivity == null || mCurrentActivity.isFinishing()) {
            Activity activity = CommonUtils.getCurrentActivity();
            if (activity instanceof BaseActivity) {
                baseActivity = (BaseActivity) activity;
            }
        }
        return baseActivity;
    }
}
