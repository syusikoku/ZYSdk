package com.zhiyangstudio.commonlib.utils.wifiman;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;

public class ELWifiManager {
    private static final int BUILD_VERSION_JELLYBEAN = 17;
    private android.net.wifi.WifiManager mWifiManager = null;
    private WifiInfo mWifiInfo = null;
    private Context mContext = null;

    public ELWifiManager(Context mContext) {
        this.mContext = mContext;
        this.mWifiManager = (android.net.wifi.WifiManager) mContext.getSystemService("wifi");
        this.mWifiInfo = this.mWifiManager.getConnectionInfo();
    }

    public String getBaseSSID() {
        return this.mWifiInfo.getBSSID();
    }

    public String getCurrentSSID() {
        return removeSSIDQuotes(this.mWifiInfo.getSSID());
    }

    public static String removeSSIDQuotes(String connectedSSID) {
        int currentVersion = Build.VERSION.SDK_INT;
        if (currentVersion >= 17 && connectedSSID.startsWith("\"") && connectedSSID.endsWith
                ("\"")) {
            connectedSSID = connectedSSID.substring(1, connectedSSID.length() - 1);
        }

        return connectedSSID;
    }

    public boolean isWifiConnected() {
        @SuppressLint("WrongConstant") ConnectivityManager connManager = (ConnectivityManager) this.mContext.getSystemService
                ("connectivity");
        NetworkInfo mWifi = connManager.getNetworkInfo(1);
        return mWifi.isConnected();
    }

    public String getCurrentIpAddressConnected() {
        int ipval = this.mWifiInfo.getIpAddress();
        String ipString = String.format("%d.%d.%d.%d", new Object[]{Integer.valueOf(ipval & 255),
                Integer.valueOf(ipval >> 8 & 255), Integer.valueOf(ipval >> 16 & 255), Integer
                .valueOf(ipval >> 24 & 255)});
        return ipString.toString();
    }

    public int getCurrentIpAddressConnectedInt() {
        int ipval = this.mWifiInfo.getIpAddress();
        return ipval;
    }

    public String getGatewayIpAddress() {
        int gatwayVal = this.mWifiManager.getDhcpInfo().gateway;
        return String.format("%d.%d.%d.%d", new Object[]{Integer.valueOf(gatwayVal & 255),
                Integer.valueOf(gatwayVal >> 8 & 255), Integer.valueOf(gatwayVal >> 16 & 255),
                Integer.valueOf(gatwayVal >> 24 & 255)}).toString();
    }
}
