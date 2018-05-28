package com.zhiyangstudio.commonlib.net.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.Iterator;
import java.util.List;

public class WifiAutoConnectManager {
    private static final String TAG = WifiAutoConnectManager.class.getSimpleName();
    WifiManager wifiManager;

    public WifiAutoConnectManager(WifiManager wifiManager) {
        this.wifiManager = wifiManager;
    }

    public static WifiAutoConnectManager.WifiCipherType getCipherType(Context context, String
            ssid) {
        WifiManager wifiManager = (WifiManager) context.getSystemService("wifi");
        List<ScanResult> list = wifiManager.getScanResults();
        Iterator var5 = list.iterator();

        while (var5.hasNext()) {
            ScanResult scResult = (ScanResult) var5.next();
            if (!TextUtils.isEmpty(scResult.SSID) && scResult.SSID.equals(ssid)) {
                String capabilities = scResult.capabilities;
                if (!TextUtils.isEmpty(capabilities)) {
                    if (!capabilities.contains("WPA") && !capabilities.contains("wpa")) {
                        if (!capabilities.contains("WEP") && !capabilities.contains("wep")) {
                            Log.i("hefeng", "no");
                            return WifiAutoConnectManager.WifiCipherType.WIFICIPHER_NOPASS;
                        }

                        Log.i("hefeng", "wep");
                        return WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WEP;
                    }

                    Log.i("hefeng", "wpa");
                    return WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA;
                }
            }
        }

        return WifiAutoConnectManager.WifiCipherType.WIFICIPHER_INVALID;
    }

    public void connect(String ssid, String password, WifiAutoConnectManager.WifiCipherType type) {
        Thread thread = new Thread(new WifiAutoConnectManager.ConnectRunnable(ssid, password,
                type));
        thread.start();
    }

    private WifiConfiguration isExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = this.wifiManager.getConfiguredNetworks();
        Iterator var4 = existingConfigs.iterator();

        while (var4.hasNext()) {
            WifiConfiguration existingConfig = (WifiConfiguration) var4.next();
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }

        return null;
    }

    private WifiConfiguration createWifiInfo(String SSID, String Password, WifiAutoConnectManager
            .WifiCipherType Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        if (Type == WifiAutoConnectManager.WifiCipherType.WIFICIPHER_NOPASS) {
            config.allowedKeyManagement.set(0);
        }

        if (Type == WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WEP) {
            if (!TextUtils.isEmpty(Password)) {
                if (isHexWepKey(Password)) {
                    config.wepKeys[0] = Password;
                } else {
                    config.wepKeys[0] = "\"" + Password + "\"";
                }
            }

            config.allowedAuthAlgorithms.set(0);
            config.allowedAuthAlgorithms.set(1);
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        }

        if (Type == WifiAutoConnectManager.WifiCipherType.WIFICIPHER_WPA) {
            config.preSharedKey = "\"" + Password + "\"";
            config.hiddenSSID = true;
            config.allowedAuthAlgorithms.set(0);
            config.allowedGroupCiphers.set(2);
            config.allowedKeyManagement.set(1);
            config.allowedPairwiseCiphers.set(1);
            config.allowedGroupCiphers.set(3);
            config.allowedPairwiseCiphers.set(2);
            config.status = 2;
        }

        return config;
    }

    private static boolean isHexWepKey(String wepKey) {
        int len = wepKey.length();
        return len != 10 && len != 26 && len != 58 ? false : isHex(wepKey);
    }

    private static boolean isHex(String key) {
        for (int i = key.length() - 1; i >= 0; --i) {
            char c = key.charAt(i);
            if ((c < 48 || c > 57) && (c < 65 || c > 70) && (c < 97 || c > 102)) {
                return false;
            }
        }

        return true;
    }

    private boolean openWifi() {
        boolean bRet = true;
        if (!this.wifiManager.isWifiEnabled()) {
            bRet = this.wifiManager.setWifiEnabled(true);
        }

        return bRet;
    }

    public static enum WifiCipherType {
        WIFICIPHER_WEP,
        WIFICIPHER_WPA,
        WIFICIPHER_NOPASS,
        WIFICIPHER_INVALID;

        private WifiCipherType() {
        }
    }

    class ConnectRunnable implements Runnable {
        private String ssid;
        private String password;
        private WifiAutoConnectManager.WifiCipherType type;

        public ConnectRunnable(String ssid, String password, WifiAutoConnectManager
                .WifiCipherType type) {
            this.ssid = ssid;
            this.password = password;
            this.type = type;
        }

        public void run() {
            WifiAutoConnectManager.this.openWifi();

            while (WifiAutoConnectManager.this.wifiManager.getWifiState() == 2) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException var6) {
                    ;
                }
            }

            WifiConfiguration wifiConfig = WifiAutoConnectManager.this.createWifiInfo(this.ssid,
                    this.password, this.type);
            if (wifiConfig == null) {
                Log.d(WifiAutoConnectManager.TAG, "wifiConfig is null!");
            } else {
                WifiConfiguration tempConfig = WifiAutoConnectManager.this.isExsits(this.ssid);
                if (tempConfig != null) {
                    WifiAutoConnectManager.this.wifiManager.removeNetwork(tempConfig.networkId);
                }

                int netID = WifiAutoConnectManager.this.wifiManager.addNetwork(wifiConfig);
                boolean enabled = WifiAutoConnectManager.this.wifiManager.enableNetwork(netID,
                        true);
                Log.d(WifiAutoConnectManager.TAG, "enableNetwork status enable=" + enabled);
                boolean connected = WifiAutoConnectManager.this.wifiManager.reconnect();
                Log.d(WifiAutoConnectManager.TAG, "enableNetwork connected=" + connected);
            }
        }
    }
}
