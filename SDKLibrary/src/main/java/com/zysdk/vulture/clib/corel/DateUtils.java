package com.zysdk.vulture.clib.corel;

import com.zysdk.vulture.clib.utils.LoggerUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 证书检测
 * @hide
 */
final class DateUtils {
    /**
     * 是否过期 - 以服务器时间为准
     */
    public static boolean isExpired(Date date) {
        LoggerUtils.loge("isExpired");
        //如果想比较日期则写成"yyyy-MM-dd"就可以了
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //将字符串形式的时间转化为Date类型的时间
        Date a = null;
        try {
            a = sdf.parse(BuinessConst.VERIFICATION_DATE);
            String t_time = sdf.format(date);
            LoggerUtils.loge("t_time = " + t_time);
            //Date类的一个方法，如果a早于b返回true，否则返回false
            if (date.before(a)) {
                LoggerUtils.loge("未过期");
                return false;
            } else {
                LoggerUtils.loge("已过期");
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            LoggerUtils.loge("已过期");
            return true;
        }
    }
}
