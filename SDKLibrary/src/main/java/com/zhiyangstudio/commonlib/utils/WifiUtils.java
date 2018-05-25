package com.zhiyangstudio.commonlib.utils;

import android.Manifest;
import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.annotation.RequiresPermission;

import com.blankj.utilcode.util.ToastUtils;
import com.zhiyangstudio.commonlib.corel.BaseActivity;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiyang on 2018/4/7.
 */

public class WifiUtils extends BaseUtils {
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

    /**
     * 创建wifi热点
     * <p>
     * * java.lang.reflect.InvocationTargetException
     * android m 上会报java.lang.SecurityException: com.example.sash was not granted  either of
     * these
     * permissions: android.permission.CHANGE_NETWORK_STATE, android.permission.WRITE_SETTINGS.
     * <p>
     * 使用以下的方式申请
     * //     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
     * //            if (!Settings.System.canWrite(this)) {
     * //            Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
     * //            intent.setData(Uri.parse("package:" + getPackageName()));
     * //            startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS);
     * //            }
     * //        }
     *
     * @param ssidStr
     * @param pwdStr
     */
    public static boolean createHotspot(String ssidStr, String pwdStr) {
        reGetCurrentActivity();
        LoggerUtils.loge(mCurrentActivity, "createHotspot ");
        if (isWifiEnabled()) {
            // 如果wifi处于打开状态,则先关闭wifi
            closeWifi();
        }
        boolean hasCretaeSucess = false;
        WifiConfiguration config = new WifiConfiguration();
        config.SSID = ssidStr;
        config.preSharedKey = pwdStr;
        config.hiddenSSID = true;
        // 开放系统认证
        config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);//开放系统认证
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
        config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
        config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
        config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
        config.status = WifiConfiguration.Status.ENABLED;
        // 通过反射设置热点
        try {
            Method method = sWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, Boolean.TYPE);
            boolean enable = (boolean) method.invoke(sWifiManager, config, true);
            if (enable) {
                hasCretaeSucess = true;
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            LoggerUtils.loge(mCurrentActivity, "NoSuchMethodException e = " + e.getMessage());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            LoggerUtils.loge(mCurrentActivity, "IllegalAccessException e = " + e.getMessage());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            LoggerUtils.loge(mCurrentActivity, "InvocationTargetException e = " + e.getMessage());
        }
        return hasCretaeSucess;
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
     * 关闭wifi热点
     */
    public static void closeWifiHotspot() {
        // 使用动态代理优化
        reGetCurrentActivity();
        LoggerUtils.loge(mCurrentActivity, "closeWifiHotspot ");
        try {
            Method method = sWifiManager.getClass().getMethod("getWifiApConfiguration");
            method.setAccessible(true);
            WifiConfiguration config = (WifiConfiguration) method.invoke(sWifiManager);
            Method method2 = sWifiManager.getClass().getMethod("setWifiApEnabled",
                    WifiConfiguration.class, boolean.class);
            method2.invoke(sWifiManager, config, false);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到配置好的网络连接
     *
     * @return
     */
    public static List<WifiConfiguration> getConfiguredNetworks() {
        return sWifiManager.getConfiguredNetworks();
    }

    /**
     * 获取可用的wifi列表，去除重复，信号强度在80%以上
     */
    public static List<ScanResult> getWifiList() {
        List<ScanResult> list = getScanResults();
        // TODO: 2018/5/24 对数据进行过滤 ,去除重复
        List<ScanResult> mWifiList = new ArrayList();
        if (list != null && list.size() > 0) {
            LoggerUtils.loge("Wifi onScanResultsAvaliable list = " + list.size());
            // 过滤，其实是对数据进行了排序
            // 获取可用的wifi,信号强度在80以上
            List<ScanResult> results = getAvaliableWifiScanList(list);
            //得到扫描结果
            // 得到配置好的网络连接
            if (results == null) {
                int wifiState = getWifiState();
                if (wifiState == 3) {
                    ToastUtils.showShort("当前区域没有无线网络");
                } else if (wifiState == 2) {
                    ToastUtils.showShort("wifi正在开启，请稍后扫描");
                } else {
                    ToastUtils.showShort("WiFi没有开启");
                }
            } else {
                for (ScanResult result : results) {
                    if (result.SSID == null || result.SSID.length() == 0 ||
                            result.capabilities.contains("[IBSS]")) {
                        continue;
                    }
                    boolean found = false;
                    for (ScanResult item : mWifiList) {
                        if (item.SSID.equals(result.SSID) && item.capabilities
                                .equals(result.capabilities)) {
                            found = true;
                            break;
                        }
                    }
                    if (!found) {
                        mWifiList.add(result);
                    }
                }
            }
        }
        return mWifiList;
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
     * 获取周围信号强度大于-80的wifi列表(wifi强度为负数，值越大信号越好
     */
    @RequiresPermission(Manifest.permission.CHANGE_WIFI_STATE)
    public static List<ScanResult> getAvaliableWifiScanList(List<ScanResult> list) {
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
     * 获取wifi状态
     *
     * @return
     */
    public static int getWifiState() {
        return sWifiManager.getWifiState();
    }

    /**
     * 过滤wifi扫描结果
     */
    public static List<ScanResult> fileterScanReuslt(List<ScanResult> list) {
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
     * 获取热点的加密类型
     */
    public static int getType(ScanResult scanResult) {
        int type = 0;
        if (scanResult.capabilities.contains("WPA"))
            type = 2;
        else if (scanResult.capabilities.contains("WEP"))
            type = 1;
        else
            type = 0;
        return type;
    }

    //通过反射的方式去判断wifi是否已经连接上，并且可以开始传输数据
    public static boolean checkWiFiConnectSuccess() {
        Class classType = WifiInfo.class;
        try {
            Object invo = classType.newInstance();
            Object result = invo.getClass().getMethod("getMeteredHint").invoke(invo);
            return (boolean) result;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return false;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return false;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return false;
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 连接到指定的wifi
     *
     * @param context
     * @param ssid
     * @param pwd
     * @param wifiType
     */
    public static boolean connectWifi(Context context, String ssid, String pwd, int wifiType) {
        // 获取密码，连接wifi,无论成功或失败都关闭对话框
        WifiConfiguration wifiConfiguration = WifiUtils.configWifiInfo(context, ssid, pwd,
                wifiType);
        WifiManager wifiManger = WifiUtils.getWifiManger();
        //createWifiConfig主要用于构建一个WifiConfiguration，代码中的例子主要用于连接不需要密码的Wifi
        //WifiManager的addNetwork接口，传入WifiConfiguration后，得到对应的NetworkId
        int netId = wifiManger.addNetwork(wifiConfiguration);
        if (netId != -1) {
            LoggerUtils.loge("添加成功");
        } else {
            LoggerUtils.loge("添加失败");
        }
        //WifiManager的enableNetwork接口，就可以连接到netId对应的wifi了
        //其中boolean参数，主要用于指定是否需要断开其它Wifi网络
        boolean isConected = wifiManger.enableNetwork(netId, true);
        LoggerUtils.loge("enable: " + isConected);
        //可选操作，让Wifi重新连接最近使用过的接入点
        //如果上文的enableNetwork成功，那么reconnect同样连接netId对应的网络
        //若失败，则连接之前成功过的网络
        boolean reconnect = wifiManger.reconnect();
        LoggerUtils.loge("reconnect: " + reconnect);
        WifiInfo connectionInfo = wifiManger.getConnectionInfo();
        // TODO: 2018/5/25 判断wifi是否连接成功
        boolean hasConnectSuccess = checkWiFiConnectSuccess(connectionInfo);
        LoggerUtils.loge("hasConnectSuccess: " + hasConnectSuccess);
        return isConected;
    }

    /**
     * 更新或创建WifiConfiguration
     * https://blog.csdn.net/gaugamela/article/details/64442989
     */
    public static WifiConfiguration configWifiInfo(Context context, String SSID, String password,
                                                   int type) {
        //初始化WifiConfiguration
        WifiConfiguration config = null;
        WifiManager mWifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        if (mWifiManager != null) {
            List<WifiConfiguration> existingConfigs = mWifiManager.getConfiguredNetworks();
            for (WifiConfiguration existingConfig : existingConfigs) {
                if (existingConfig == null) continue;
                if (existingConfig.SSID.equals("\"" + SSID + "\"")  /*&&  existingConfig
                .preSharedKey.equals("\""  +  password  +  "\"")*/) {
                    config = existingConfig;
                    break;
                }
            }
        }
        if (config == null) {
            config = new WifiConfiguration();
        }
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();

        //指定对应的SSID
        config.SSID = "\"" + SSID + "\"";

        //如果之前有类似的配置
        WifiConfiguration tempConfig = isExist(SSID);
        if (tempConfig != null) {
            //则清除旧有配置
            mWifiManager.removeNetwork(tempConfig.networkId);
        }

        // 分为三种情况：0没有密码1用wep加密2用wpa加密
        if (type == 0) {
            // WIFICIPHER_NOPASSwifiCong.hiddenSSID = false; 不需要密码的场景
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        } else if (type == 1) {
            //  WIFICIPHER_WEP 以WEP加密的场景
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + password + "\"";
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.SHARED);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
            config.allowedGroupCiphers
                    .set(WifiConfiguration.GroupCipher.WEP104);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            config.wepTxKeyIndex = 0;
        } else if (type == 2) {
            // WIFICIPHER_WPA 以WPA加密的场景，自己测试时，发现热点以WPA2建立时，同样可以用这种配置连接
            config.preSharedKey = "\"" + password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms
                    .set(WifiConfiguration.AuthAlgorithm.OPEN);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedPairwiseCiphers
                    .set(WifiConfiguration.PairwiseCipher.CCMP);
            config.status = WifiConfiguration.Status.ENABLED;
        }
        return config;
    }

    public static WifiManager getWifiManger() {
        return sWifiManager;
    }

    /**
     * 通过反射的方式去判断wifi是否已经连接上，并且可以开始传输数据
     * https://www.cnblogs.com/819158327fan/p/6689120.html
     */
    public static boolean checkWiFiConnectSuccess(WifiInfo wifiInfo) {
        Class classType = wifiInfo.getClass();
        try {
            Field field = classType.getDeclaredField("mMeteredHint");//设置为可以访问
            field.setAccessible(true);
            boolean result = (boolean) field.get(wifiInfo);
            return result;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static WifiConfiguration isExist(String ssid) {
        List<WifiConfiguration> configs = sWifiManager.getConfiguredNetworks();

        for (WifiConfiguration config : configs) {
            if (config.SSID.equals("\"" + ssid + "\"")) {
                return config;
            }
        }
        return null;
    }
}
