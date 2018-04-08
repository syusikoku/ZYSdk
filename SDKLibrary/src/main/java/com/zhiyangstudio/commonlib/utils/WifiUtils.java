package com.zhiyangstudio.commonlib.utils;

import android.Manifest;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.RequiresPermission;

import com.zhiyangstudio.commonlib.corel.BaseActivity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zzg on 2018/4/7.
 */

public class WifiUtils {
    private static WifiManager sWifiManager;

    static {
        sWifiManager = (WifiManager) InternalUtils.getTargetService(Context.WIFI_SERVICE);
    }

    /**
     * 获取开启热点后自身热点Ip地址
     */
    @RequiresPermission(android.Manifest.permission.ACCESS_WIFI_STATE)
    public static String getLocalAddress() {
        DhcpInfo dhcpInfo = sWifiManager.getDhcpInfo();
        if (dhcpInfo != null) {
            int address = dhcpInfo.serverAddress;
            return (address & 0XFF)
                    + "." + ((address >> 8) & 0XFF)
                    + "." + ((address >> 16) & 0XFF)
                    + "." + ((address >> 24) & 0XFF);
        }
        return "";
    }

    /**
     * 关闭wifi
     */
    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public static void closeWifi() {
        if (sWifiManager.isWifiEnabled()) {
            sWifiManager.setWifiEnabled(false);
        }
    }

    /**
     * 开启热点
     */
    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public static boolean openAp(String ssidStr, String pwdStr) {
        if (EmptyUtils.isEmpty(ssidStr))
            return false;

        if (sWifiManager.isWifiEnabled()) {
            sWifiManager.setWifiEnabled(false);
        }

        WifiConfiguration wifiConfiguration = getApConfig(ssidStr, pwdStr);
        try {
            if (isApOn()) {
                sWifiManager.setWifiEnabled(false);
                closeAp();
            }

            // 使用反射开启wifi热点
            Method setWifiApEnabledMethod = sWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            setWifiApEnabledMethod.invoke(sWifiManager, wifiConfiguration, true);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 设置有密码的热点信息
     */
    private static WifiConfiguration getApConfig(String ssidStr, String pwdStr) {
        if (EmptyUtils.isEmpty(pwdStr))
            return null;
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = ssidStr;
        config.preSharedKey = pwdStr;
        // config.hiddenSSID=true;
        config.status = WifiConfiguration.Status.ENABLED;
        // 组加密方式
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
        return config;
    }

    /**
     * 热点是否开启
     */
    public static boolean isApOn() {
        try {
            Method isWifiApEnabledMethod = sWifiManager.getClass().getDeclaredMethod
                    ("isWifiApEnabledMethod");
            isWifiApEnabledMethod.setAccessible(true);
            return (Boolean) isWifiApEnabledMethod.invoke(sWifiManager);
        } catch (NoSuchMethodException pE) {
            pE.printStackTrace();
        } catch (IllegalAccessException pE) {
            pE.printStackTrace();
        } catch (InvocationTargetException pE) {
            pE.printStackTrace();
        }
        return false;
    }

    /**
     * 关闭热点
     */
    public static void closeAp() {
        try {
            Method setWifiApEnabledMethod = sWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            setWifiApEnabledMethod.invoke(sWifiManager, null, false);
        } catch (NoSuchMethodException pE) {
            pE.printStackTrace();
        } catch (IllegalAccessException pE) {
            pE.printStackTrace();
        } catch (InvocationTargetException pE) {
            pE.printStackTrace();
        }
    }

    /**
     * 清除当前连接的WiFi网络
     */
    public static void clearWifiConfig() {
        String ssid = sWifiManager.getConnectionInfo().getSSID().replace("\"", "");
        List<WifiConfiguration> wifiConfigurations = sWifiManager.getConfiguredNetworks();
        if (wifiConfigurations != null && wifiConfigurations.size() > 0) {
            for (WifiConfiguration configuration : wifiConfigurations) {
                if (configuration.SSID.replace("\"", "").contains(ssid)) {
                    sWifiManager.removeNetwork(configuration.networkId);
                    sWifiManager.saveConfiguration();
                }
            }
        }
    }

    /**
     * 扫描周围可用wifi
     */
    public static List<ScanResult> getScanResults() {
        List<ScanResult> list = sWifiManager.getScanResults();
        if (list != null && list.size() > 0) {
            return fileterScanReuslt(list);
        } else {
            return new ArrayList<>();
        }
    }

    /**
     * 过滤wifi扫描结果
     */
    private static List<ScanResult> fileterScanReuslt(List<ScanResult> list) {
        List<ScanResult> results = new ArrayList<>();
        if (results == null) {
            return results;
        }

        for (ScanResult scanResult : list) {
            if (EmptyUtils.isNotEmpty(scanResult.SSID) && scanResult.level > -80) {
                results.add(scanResult);
            }
        }

        // TODO: 2018/4/7 对结果进行排序
        for (int i = 0; i < results.size(); i++) {
            for (int j = 0; j < results.size(); j++) {
                // 将搜索到的wifi根据信号强度从强到弱进行排序
                ScanResult tmp = results.get(i);
                results.set(i, results.get(j));
                results.set(j, tmp);
            }
        }
        return results;
    }

    /**
     * 获取周围信号强度大于-80的wifi列表(wifi强度为负数，值越大信号越好
     */
    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    private static List<ScanResult> getWifiScanList(List<ScanResult> list) {
        List<ScanResult> resultList = new ArrayList<>();
        if (sWifiManager.startScan()) {
            List<ScanResult> tmpList = sWifiManager.getScanResults();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (tmpList != null && tmpList.size() > 0) {
                for (ScanResult scanResult : tmpList) {
                    if (scanResult.level > -80) {
                        resultList.add(scanResult);
                    }
                }
            } else {
                LoggerUtils.loge((BaseActivity) CommonUtils.getCurrentActivity(), "扫描为空");
            }
        }
        return resultList;
    }

    /**
     * 获取当前连接wifi的SSID
     */
    public static String getConnectedSSID() {
        WifiInfo wifiInfo = sWifiManager.getConnectionInfo();
        return wifiInfo != null ? wifiInfo.getSSID().replaceAll("\"", "") : "";
    }

    /**
     * 扫描周围可用WiFi
     */
    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public static boolean startScan() {
        if (isWifiEnabled()) {
            return sWifiManager.startScan();
        }
        return false;
    }

    /**
     * 当前wifi是否开启
     */
    public static boolean isWifiEnabled() {
        return sWifiManager.isWifiEnabled();
    }

    /**
     * 打开wifi
     */
    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public static void openWifi() {
        if (!isWifiEnabled()) {
            sWifiManager.setWifiEnabled(true);
        }
    }

    /**
     * 获取连接wifi后的ip地址
     */
    public static String getIpAddressFromHotSpot() {
        DhcpInfo dhcpInfo = sWifiManager.getDhcpInfo();
        if (dhcpInfo != null) {
            int address = dhcpInfo.gateway;
            return (address & 0XFF)
                    + "." + ((address >> 8) & 0XFF)
                    + "." + ((address >> 16) & 0XFF)
                    + "." + ((address >> 24) & 0XFF);
        }
        return "";
    }
}
