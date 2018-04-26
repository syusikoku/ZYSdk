package com.zhiyangstudio.commonlib.utils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;

/**
 * Created by example on 2018/4/12.
 */

public class IntentUtils {
    private static Context mContext;

    static {
        mContext = UiUtils.getContext();
    }

    public static void forward(Class<? extends Activity> activityCls) {
        forward(activityCls, null);
    }

    public static void forward(Class<? extends Activity> activityCls, Bundle bundle) {
        if (activityCls == null) {
            return;
        }

        Intent intent = new Intent(UiUtils.getContext(), activityCls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        forward(intent);
    }

    public static void forward(Intent intent) {
        Activity currentActivity = CommonUtils.getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.startActivity(intent);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    public static void forward(String aciton) {
        forward(aciton, null);
    }

    public static void forward(String aciton, Bundle bundle) {
        if (EmptyUtils.isEmpty(aciton)) {
            return;
        }

        Intent intent = new Intent(aciton);
        if (intent != null) {
            intent.putExtras(bundle);
        }
        forward(intent);
    }

    public static void forwardForResult(Class<? extends Activity> activityCls, int reqCode) {
        forwardForResult(activityCls, null, reqCode);
    }

    public static void forwardForResult(Class<? extends Activity> activityCls, Bundle bundle, int reqCode) {
        if (activityCls == null) {
            return;
        }

        Intent intent = new Intent(UiUtils.getContext(), activityCls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        forwardForResult(intent, reqCode);
    }

    public static void forwardForResult(Intent intent, int reqCode) {
        Activity currentActivity = CommonUtils.getCurrentActivity();
        if (currentActivity != null) {
            currentActivity.startActivityForResult(intent, reqCode);
        } else {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
        }
    }

    public static void forwardForResult(String aciton, int reqCode) {
        forwardForResult(aciton, null, reqCode);
    }

    public static void forwardForResult(String aciton, Bundle bundle, int reqCode) {
        if (EmptyUtils.isEmpty(aciton)) {
            return;
        }

        Intent intent = new Intent(aciton);
        if (intent != null) {
            intent.putExtras(bundle);
        }
        forwardForResult(intent, reqCode);
    }

    public static void startService(Intent intent) {
        mContext.startService(intent);
    }

    public static void stopService(Intent intent) {
        mContext.stopService(intent);
    }

    public static void bindService(String serviceFullName, Intent intent, ServiceConnection connection) {
        if (!CommonUtils.isServiceWork(serviceFullName)) {
            mContext.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        }
    }

    public static void unBindService(ServiceConnection connection) {
        mContext.unbindService(connection);
    }

    public static void regReceiver(BroadcastReceiver receiver, IntentFilter intentFilter) {
        mContext.registerReceiver(receiver, intentFilter);
    }

    public static void unRegReceiver(BroadcastReceiver receiver) {
        mContext.unregisterReceiver(receiver);
    }
}
