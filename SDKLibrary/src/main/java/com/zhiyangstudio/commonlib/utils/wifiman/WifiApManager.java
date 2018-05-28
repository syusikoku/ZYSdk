package com.zhiyangstudio.commonlib.utils.wifiman;

import android.content.Context;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.lang.reflect.Method;

public final class WifiApManager {
    private static final int WIFI_AP_STATE_FAILED = 4;
    private final WifiManager mWifiManager;
    private final String TAG = "Wifi Access Manager";
    private Method wifiControlMethod;
    private Method wifiApConfigurationMethod;
    private Method wifiApState;

    public WifiApManager(Context context) throws SecurityException, NoSuchMethodException {
        this.mWifiManager = (WifiManager) context.getSystemService("wifi");
        this.wifiControlMethod = this.mWifiManager.getClass().getMethod("setWifiApEnabled", new
                Class[]{WifiConfiguration.class, Boolean.TYPE});
        this.wifiApConfigurationMethod = this.mWifiManager.getClass().getMethod
                ("getWifiApConfiguration", (Class[]) null);
        this.wifiApState = this.mWifiManager.getClass().getMethod("getWifiApState", new Class[0]);
    }

    public boolean setWifiApState(WifiConfiguration config, boolean enabled) {
        try {
            if (enabled) {
                this.mWifiManager.setWifiEnabled(!enabled);
            }

            return ((Boolean) this.wifiControlMethod.invoke(this.mWifiManager, new
                    Object[]{config, Boolean.valueOf(enabled)})).booleanValue();
        } catch (Exception var4) {
            Log.e("Wifi Access Manager", "", var4);
            return false;
        }
    }

    public WifiConfiguration getWifiApConfiguration() {
        try {
            return (WifiConfiguration) this.wifiApConfigurationMethod.invoke(this.mWifiManager,
                    (Object[]) null);
        } catch (Exception var2) {
            return null;
        }
    }

    public int getWifiApState() {
        try {
            return ((Integer) this.wifiApState.invoke(this.mWifiManager, new Object[0])).intValue();
        } catch (Exception var2) {
            Log.e("Wifi Access Manager", "", var2);
            return 4;
        }
    }
}
