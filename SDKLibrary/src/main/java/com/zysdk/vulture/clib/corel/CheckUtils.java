package com.zysdk.vulture.clib.corel;

import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
/**
 * @hide
 */
final class CheckUtils {
    /**
     * 是否有效
     */
    public static boolean isAvaliable() {
        // 客户端或商业版未过期
        return isAvaliable(new Date());
    }

    /**
     * 是否有效
     */
    public static boolean isAvaliable(Date date) {
        return !BuinessConst.BUINESS_VERSION || !DateUtils.isExpired(date);
    }

    /**
     * 获取网络时间
     */
    public static Date getNetTime() {
        String webUrl = "http://www.ntsc.ac.cn";//中国科学院国家授时中心
        try {
            URL url = new URL(webUrl);
            URLConnection uc = url.openConnection();
            uc.setReadTimeout(5000);
            uc.setConnectTimeout(5000);
            uc.connect();
            long correctTime = uc.getDate();
            Date date = new Date(correctTime);
            return date;
        } catch (Exception e) {
            return new Date();
        }
    }
}
