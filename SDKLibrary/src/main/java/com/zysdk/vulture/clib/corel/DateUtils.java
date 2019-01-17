package com.zysdk.vulture.clib.corel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 证书检测
 * @hide
 */
final class DateUtils {
    /**
     * 是否过期
     */
    public static boolean isExpired(Date date) {
        //如果想比较日期则写成"yyyy-MM-dd"就可以了
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        //将字符串形式的时间转化为Date类型的时间
        Date a = null;
        try {
            a = sdf.parse(BuinessConst.VERIFICATION_DATE);
            Date b = sdf.parse(sdf.format(date));
            //Date类的一个方法，如果a早于b返回true，否则返回false
            if (a.before(b))
                return true;
            else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
