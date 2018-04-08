package com.zhiyangstudio.commonlib.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by syusikoku on 2018/1/19.
 */

public class DateUtils {
    public static String format(String dateStr) {
        if (TextUtils.isEmpty(dateStr)) {
            return "";
        }

        dateStr = dateStr.replace("Z", " UTC");
        SimpleDateFormat sdfDateFormat = new SimpleDateFormat(
                "yyyy-MM-dd'T'HH:mm:ssZ");
        try {
            Date date = sdfDateFormat.parse(dateStr);
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyy-MM-dd");
            String result = sf1.format(date);
            return result;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dateStr;
    }
}
