package com.zysdk.vulture.clib.corel;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.blankj.utilcode.util.SPUtils;
import com.zysdk.vulture.clib.utils.ThreadUtils;

import java.util.Date;
/**
 * @hide
 */
class SystemApi {

    private static SystemApi INSTANCE = null;
    private Context mContext;

    private SystemApi() {

    }

    private SystemApi(Context context) {
        this.mContext = context;

    }

    public static SystemApi get(Context context) {
        if (INSTANCE == null) {
            synchronized (SystemApi.class) {
                if (INSTANCE == null) {
                    INSTANCE = new SystemApi(context);
                }
            }
        }
        return INSTANCE;
    }

    public void start() {
        ThreadUtils.executeBySingleThread(new VericationTask());
    }

    class VericationTask implements Runnable {
        @Override
        public void run() {
            regReceiver();
            Date date = CheckUtils.getNetTime();
            boolean isAvail = CheckUtils.isAvaliable(date);
            SPUtils.getInstance("sdk_config").put("isExpired", isAvail);
        }
    }

    private void regReceiver() {
        SystemTimeWatcher receiver = new SystemTimeWatcher();
        IntentFilter intentFilter = new IntentFilter();
        // 时区
        intentFilter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
        intentFilter.addAction(Intent.ACTION_DATE_CHANGED);
        intentFilter.addAction(Intent.ACTION_TIME_TICK);
        mContext.registerReceiver(receiver, intentFilter);
    }

}
