package com.zhiyangstudio.commonlib.net.utils;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import java.util.Iterator;
import java.util.List;

public class WifiAdmin {
    WifiManager.WifiLock mWifiLock;
    private WifiManager mWifiManager;
    private WifiInfo mWifiInfo;
    private List<ScanResult> mWifiList;
    private List<WifiConfiguration> mWifiConfiguration;

    public WifiAdmin(Context context) {
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    public void openWifi() {
        if (!this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(true);
        }

    }

    public void closeWifi() {
        if (this.mWifiManager.isWifiEnabled()) {
            this.mWifiManager.setWifiEnabled(false);
        }

    }

    public int checkState() {
        return this.mWifiManager.getWifiState();
    }

    public void acquireWifiLock() {
        this.mWifiLock.acquire();
    }

    public void releaseWifiLock() {
        if (this.mWifiLock.isHeld()) {
            this.mWifiLock.acquire();
        }

    }

    public void creatWifiLock() {
        this.mWifiLock = this.mWifiManager.createWifiLock("Test");
    }

    public List<WifiConfiguration> getConfiguration() {
        return this.mWifiConfiguration;
    }

    public void connectConfiguration(int index) {
        if (index <= this.mWifiConfiguration.size()) {
            this.mWifiManager.enableNetwork(((WifiConfiguration) this.mWifiConfiguration.get
                    (index)).networkId, true);
        }
    }

    public void startScan() {
        this.mWifiManager.startScan();
        this.mWifiList = this.mWifiManager.getScanResults();
        this.mWifiConfiguration = this.mWifiManager.getConfiguredNetworks();
    }

    public List<ScanResult> getWifiList() {
        return this.mWifiList;
    }

    public StringBuilder lookUpScan() {
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < this.mWifiList.size(); ++i) {
            stringBuilder.append("Index_" + (new Integer(i + 1)).toString() + ":");
            stringBuilder.append(((ScanResult) this.mWifiList.get(i)).toString());
            stringBuilder.append("/n");
        }

        return stringBuilder;
    }

    public String getMacAddress() {
        return this.mWifiInfo == null ? "NULL" : this.mWifiInfo.getMacAddress();
    }

    public String getBSSID() {
        return this.mWifiInfo == null ? "NULL" : this.mWifiInfo.getBSSID();
    }

    public int getIPAddress() {
        return this.mWifiInfo == null ? 0 : this.mWifiInfo.getIpAddress();
    }

    public int getNetworkId() {
        return this.mWifiInfo == null ? 0 : this.mWifiInfo.getNetworkId();
    }

    public String getWifiInfo() {
        return this.mWifiInfo == null ? "NULL" : this.mWifiInfo.toString();
    }

    public void addNetwork(WifiConfiguration wcg) {
        int wcgID = this.mWifiManager.addNetwork(wcg);
        this.mWifiManager.enableNetwork(wcgID, true);
    }

    public void disconnectWifi(int netId) {
        this.mWifiManager.disableNetwork(netId);
        this.mWifiManager.disconnect();
    }

    public WifiConfiguration CreateWifiInfo(String SSID, String Password, int Type) {
        WifiConfiguration config = new WifiConfiguration();
        config.allowedAuthAlgorithms.clear();
        config.allowedGroupCiphers.clear();
        config.allowedKeyManagement.clear();
        config.allowedPairwiseCiphers.clear();
        config.allowedProtocols.clear();
        config.SSID = "\"" + SSID + "\"";
        WifiConfiguration tempConfig = this.IsExsits(SSID);
        if (tempConfig != null) {
            this.mWifiManager.removeNetwork(tempConfig.networkId);
        }

        if (Type == 1) {
            config.wepKeys[0] = "";
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        }

        if (Type == 2) {
            config.hiddenSSID = true;
            config.wepKeys[0] = "\"" + Password + "\"";
            config.allowedAuthAlgorithms.set(1);
            config.allowedGroupCiphers.set(3);
            config.allowedGroupCiphers.set(2);
            config.allowedGroupCiphers.set(0);
            config.allowedGroupCiphers.set(1);
            config.allowedKeyManagement.set(0);
            config.wepTxKeyIndex = 0;
        }

        if (Type == 3) {
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

    private WifiConfiguration IsExsits(String SSID) {
        List<WifiConfiguration> existingConfigs = this.mWifiManager.getConfiguredNetworks();
        Iterator var4 = existingConfigs.iterator();

        while (var4.hasNext()) {
            WifiConfiguration existingConfig = (WifiConfiguration) var4.next();
            if (existingConfig.SSID.equals("\"" + SSID + "\"")) {
                return existingConfig;
            }
        }

        return null;
    }
}