package com.zhiyangstudio.commonlib.components.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;

import com.zhiyangstudio.commonlib.CommonConst;
import com.zhiyangstudio.commonlib.utils.LogListener;
import com.zhiyangstudio.commonlib.utils.LoggerUtils;


/**
 * Created by zzg on 2018/4/6.
 */

public abstract class HotSpotBroadcastReceiver extends BroadcastReceiver implements LogListener {

    @Override
    public void onReceive(Context context, Intent intent) {
        String intentAction = intent.getAction();
        if (intentAction.equals(CommonConst.ACTION_HOTSPOT_STATE_CHANGED)) {
            int state = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            LoggerUtils.loge(this, "state = " + state);
            if (WifiManager.WIFI_STATE_ENABLED == state % 10) {
                LoggerUtils.loge(this, "热点启用");
                // 热点可用
                onHotSpotEnabled();
            }
        }

    }

    /**
     * 热点可用
     */
    protected abstract void onHotSpotEnabled();
}