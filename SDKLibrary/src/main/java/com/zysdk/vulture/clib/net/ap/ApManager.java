package com.zysdk.vulture.clib.net.ap;

import com.zysdk.vulture.clib.utils.WifiUtils;

/**
 * Created by zhiyang on 2018/4/6.
 */

public class ApManager {
    /**
     * 获取开启热点后自身热点Ip地址
     */
    public static String getHotspotLocalIpAddress() {
        return WifiUtils.getLocalAddress();
    }


    public static boolean isApOn() {
        return WifiUtils.isApOn();
    }

    public static void closeAp() {
        WifiUtils.closeAp();
    }

    public static boolean openAp(String ssidStr, String pwdStr) {
        return WifiUtils.openAp(ssidStr, pwdStr);
    }
}
