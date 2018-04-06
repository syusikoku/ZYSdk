package com.zhiyangstudio.sdklibrary;

import android.Manifest;
import android.content.pm.PackageManager;

/**
 * Created by zhiyang on 2018/2/14.
 */

public class CommonConst {
    public static String LOG_TAG = "zytech";

    /*--------------------------------------常用Acion-------------------------------------*/
    /**
     * 热点网络切换的action
     */
    public static final String ACTION_HOTSPOT_STATE_CHANGED = "android.net.wifi.WIFI_AP_STATE_CHANGED";


    /*-------------------------------------常用常量-------------------------------------*/
    /**
     * 连接WiFi所需权限
     */
    public static final String[] PERMISSION_CONNECT_WIFI = new String[]{
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION};

    /**
     * 创建便携热点所需权限
     */
    public static final String PERMISSION_CREATE_HOTSPOT = Manifest.permission.WRITE_SETTINGS;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;


    /**
     * 权限申请结果
     */
    // 允许
    public static final int RESULT_PERMISSION_GRANT = PackageManager.PERMISSION_GRANTED;

    // 拒绝
    public static final int RESULT_PERMISSION_DENIED = PackageManager.PERMISSION_DENIED;

}
