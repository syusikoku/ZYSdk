package com.zysdk.vulture.clib.corel;

import com.blankj.utilcode.util.SPUtils;
import com.zysdk.vulture.clib.utils.LoggerUtils;
import com.zysdk.vulture.clib.utils.ThreadUtils;

import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * @hide
 */
final class CheckUtils {
    public static List<String> webUrlList = new ArrayList<>();

    /**
     * "http://www.baidu.com";//百度
     * "http://www.taobao.com";//淘宝
     * "http://www.360.cn";//360
     */
    static {
        webUrlList.add("http://www.baidu.com");
        webUrlList.add("http://www.taobao.com");
        webUrlList.add("http://www.360.cn");
    }

    private static Random random = new Random();

    /**
     * 是否有效
     */
    public static boolean isAvaliable(Date date) {
        return !BuinessConst.BUINESS_VERSION || !DateUtils.isExpired(date);
    }

    /**
     * 是否过期
     */
    public static boolean hasLog() {
        // 普通用户版才能显示日志
        return !BuinessConst.BUINESS_VERSION;
    }

    private static boolean isExpired() {
        return SPUtils.getInstance("sdk_config").getBoolean("isExpired");
    }

    /**
     * 是否发布
     */
    public static boolean hasPublish() {
        return !BuinessConst.BUINESS_VERSION || isExpired();
    }

    /**
     * 获取网络时间
     */
    public static Date getNetTime() {
        try {
            ThreadUtils.doSleep(300);
            String url = webUrlList.get(random.nextInt(webUrlList.size()));
            URL uRL = new URL(url);
            URLConnection uc = uRL.openConnection();
            uc.setReadTimeout(5000);
            uc.setConnectTimeout(5000);
            uc.connect();
            long correctTime = uc.getDate();
            Date date = new Date(correctTime);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String str = sdf.format(date);
            LoggerUtils.loge("serverTime  = " + str);
            return date;
        } catch (Exception e) {
            LoggerUtils.loge(e.getMessage());
            return new Date();
        }
    }
}
