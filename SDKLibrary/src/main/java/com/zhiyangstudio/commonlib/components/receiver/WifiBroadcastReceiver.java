package com.zhiyangstudio.commonlib.components.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import com.zhiyangstudio.commonlib.utils.LogListener;
import com.zhiyangstudio.commonlib.utils.LoggerUtils;
import com.zhiyangstudio.commonlib.utils.UiUtils;
import com.zhiyangstudio.commonlib.utils.WifiUtils;

import java.util.List;

/**
 * Created by zhiyang on 2018/4/7.
 */

public abstract class WifiBroadcastReceiver extends BroadcastReceiver implements LogListener {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            String action = intent.getAction();
            if (action.equalsIgnoreCase(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
                LoggerUtils.loge(this, "action -> WifiManager.WIFI_STATE_CHANGED_ACTION");
                // 监听wifi开启，关闭事件
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                if (wifiState == WifiManager.WIFI_STATE_ENABLED) {
                    // wifi已开启
                    onWifiEnabled();
                } else if (wifiState == WifiManager.WIFI_STATE_DISABLED) {
                    // wifi已关闭
                    onWifiDisabled();
                }
            } else if (action.equalsIgnoreCase(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                LoggerUtils.loge(this, "action -> WifiManager.SCAN_RESULTS_AVAILABLE_ACTION");
                List<ScanResult> wifiList = WifiUtils.getScanResults();
                if (WifiUtils.isWifiEnabled() && wifiList != null && wifiList.size() > 0) {
                    // 成功扫描
                    onScanResultsAvaliable(wifiList);
                }
            } else if (action.equalsIgnoreCase(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
                LoggerUtils.loge(this, "action -> WifiManager.NETWORK_STATE_CHANGED_ACTION");
                // 网络状态改变的广播
                NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
                if (networkInfo != null) {
                    if (networkInfo.getState().equals(NetworkInfo.State.CONNECTED)) {
                        // wifi已连接
                        String connectedSSID = WifiUtils.getConnectedSSID();
                        onWifiConnected(connectedSSID);
                    } else if (networkInfo.getState().equals(NetworkInfo.State.DISCONNECTED)) {
                        // wifi已断开连接
                        onWifiDisconnected();
                    }
                }
            }
        }
    }

    protected abstract void onWifiEnabled();

    protected abstract void onWifiDisabled();

    protected abstract void onScanResultsAvaliable(List<ScanResult> list);

    protected abstract void onWifiConnected(String ssid);

    protected abstract void onWifiDisconnected();

    public void register() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        UiUtils.getContext().registerReceiver(this, intentFilter);
    }

    public void unRegister() {
        UiUtils.getContext().unregisterReceiver(this);
    }
}
