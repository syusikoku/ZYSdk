package com.zysdk.vulture.clib.corel;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;

import com.blankj.utilcode.util.SPUtils;
import com.zysdk.vulture.clib.utils.LoggerUtils;
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
//        if (mContext instanceof BaseApp) {
//            ((BaseApp) mContext).registerActivityLifecycleCallbacks(new AbsActLifecycle() {
//                @Override
//                public void onActivityCreated(Activity activity,
//                                              Bundle savedInstanceState) {
//                    boolean hasLog = CheckUtils.hasLog();
//                    // true 有效限 false 无效期
//                    LoggerUtils.loge("hasLog = " + hasLog);
//                    if (!hasLog) {
//                        Logger.clearLogAdapters();
//                    }
//                }
//            });
//        }

        IntentFilter netFilter = new IntentFilter();
        netFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        netFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        netFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mContext.registerReceiver(new NetReceiver(), netFilter);
        checkVerication();
    }

    private void checkVerication() {
        ThreadUtils.executeBySingleThread(new VericationTask());
    }

    /**
     * 获取连接类型
     */
    private String getConnectionType(int type) {
        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "3G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }
        return connType;
    }

    class NetReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(action)) {// 监听wifi
                // 的打开与关闭，与wifi的连接无关
                int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
                LoggerUtils.loge("wifiState:" + wifiState);
                switch (wifiState) {
                    case WifiManager.WIFI_STATE_ENABLING:
                        LoggerUtils.loge("wifi打开中");
                        break;
                    case WifiManager.WIFI_STATE_DISABLING:
                        LoggerUtils.loge("wifi关闭中");
                        break;
                }
            }
            // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(action)) {
                //获取联网状态的NetworkInfo对象
                NetworkInfo info =
                        intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
                if (info != null) {
                    //如果当前的网络连接成功并且网络连接可用
                    if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                        if (info.getType() == ConnectivityManager.TYPE_WIFI ||
                                info.getType() == ConnectivityManager.TYPE_MOBILE) {
                            LoggerUtils.loge(getConnectionType(info.getType()) + "连上,校验证书");
                            checkVerication();
                        }
                    } else {
                        LoggerUtils.loge(getConnectionType(info.getType()) + "断开");
                    }
                }
            }
        }
    }

    /**
     * 获取网络的api然后校验时间
     */
    static class VericationTask implements Runnable {
        @Override
        public void run() {
            LoggerUtils.loge("VericationTask run ...");
            // 先校验本地的时间 ，如果是未过期则请求服务器时间
            if (CheckUtils.isAvaliable(new Date())) {
                LoggerUtils.loge("本地时间未过期了");
                Date date = CheckUtils.getNetTime();
                LoggerUtils.loge("date = " + date);
                // 以服务器时间和过期时间进行校验
                boolean isAvail = CheckUtils.isAvaliable(date);
                LoggerUtils.loge("VericationTask isAvail = " + isAvail);
                SPUtils.getInstance("sdk_config").put("isExpired", isAvail);
            } else {
                LoggerUtils.loge("本地时间过期了");
                SPUtils.getInstance("sdk_config").put("isExpired", false);
            }

        }
    }

}
