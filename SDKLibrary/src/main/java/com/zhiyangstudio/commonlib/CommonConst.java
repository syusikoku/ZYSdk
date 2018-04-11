package com.zhiyangstudio.commonlib;

import android.Manifest;
import android.content.pm.PackageManager;

/**
 * Created by zhiyang on 2018/2/14.
 */

public class CommonConst {
    /**
     * 热点网络切换的action
     */
    public static final String ACTION_HOTSPOT_STATE_CHANGED = "android.net.wifi.WIFI_AP_STATE_CHANGED";
    public static String NET_CACHE_DIR_NAME = "";

    /*--------------------------------------常用Acion-------------------------------------*/


    /*-------------------------------------常用常量-------------------------------------*/

    public static String LOG_TAG = "zytech";

    public static class PAGE_STATE {
        // 下拉刷新
        public static final int STATE_REFRESH = 0;
        // 加载更多
        public static final int STATE_LOAD_MORE = 1;
    }

    /**
     * 网络配置
     */
    public static class NET_CONFIG {
        public static final int REQUEST_SUCCESS = 0; //请求成功
        public static final int REQUEST_ERROR = -1;  //请求失败

        /**
         * 连接错误,网络异常
         */
        public static final int CONNECT_ERROR = 1001;
        /**
         * 连接超时
         */
        public static final int CONNECT_TIMEOUT = 1002;

        /**
         * 解析错误
         */
        public static final int PARSE_ERROR = 1003;
        /**
         * 未知错误
         */
        public static final int UNKNOWN_ERROR = 1004;

        /**
         * 请求超时
         */
        public static final int REQUEST_TIMEOUT = 1005;
    }

    /**
     * 权限
     */
    public static class PERMISSION {
        public static final int REQ_SDCARD_PERMISSION = 0x110;
        public static final int REQ_CAMERA_PERMISSION = 0x111;

        /**
         * 创建便携热点所需权限
         */
        public static final String PERMISSION_CREATE_HOTSPOT = Manifest.permission.WRITE_SETTINGS;
        public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;
        public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;

        /**
         * 连接WiFi所需权限
         */
        public static final String[] PERMISSION_CONNECT_WIFI = new String[]{
                Manifest.permission.ACCESS_WIFI_STATE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        /**
         * 权限申请结果
         */
        // 允许
        public static final int RESULT_PERMISSION_GRANT = PackageManager.PERMISSION_GRANTED;
        // 拒绝
        public static final int RESULT_PERMISSION_DENIED = PackageManager.PERMISSION_DENIED;
    }
}
