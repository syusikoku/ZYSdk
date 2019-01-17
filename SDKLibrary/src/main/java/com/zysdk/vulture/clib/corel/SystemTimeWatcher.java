package com.zysdk.vulture.clib.corel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.SPUtils;
import com.zysdk.vulture.clib.utils.LoggerUtils;

import java.util.Date;


/**
 * 系统日期实时检测
 *
 * @hide
 */
final class SystemTimeWatcher extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        LoggerUtils.loge("action = " + action);
        if (Intent.ACTION_DATE_CHANGED.equalsIgnoreCase(action) ||
                Intent.ACTION_TIME_TICK.equalsIgnoreCase(action) ||
                Intent.ACTION_TIMEZONE_CHANGED.equalsIgnoreCase(action)) {
            LoggerUtils.loge("验证授权是否过期");
            boolean isExpired = DateUtils.isExpired(new Date());
            LoggerUtils.loge("BUINESS_VERSION = " + BuinessConst.BUINESS_VERSION + "isExpired = " + isExpired);
            SPUtils.getInstance("sdk_config").put("isExpired", isExpired);
        }
    }
}
