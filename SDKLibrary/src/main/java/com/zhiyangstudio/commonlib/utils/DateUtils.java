package com.zhiyangstudio.commonlib.utils;

import android.support.annotation.IntRange;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by syusikoku on 2018/1/19.
 */

public class DateUtils {

    public static final int SECOND = 1000;
    public static final int MINUTE = 60 * SECOND;
    public static final int HOUR = 60 * MINUTE;
    public static final int DAY = 24 * HOUR;

    private static final long ONE_MINUTE = 60000L;
    private static final long ONE_HOUR = 3600000L;
    private static final long ONE_DAY = 86400000L;
    private static final long ONE_WEEK = 604800000L;

    private static final String ONE_SECOND_AGO = "秒前";
    private static final String ONE_MINUTE_AGO = "分钟前";
    private static final String ONE_HOUR_AGO = "小时前";
    private static final String ONE_DAY_AGO = "天前";
    private static final String ONE_MONTH_AGO = "月前";
    private static final String ONE_YEAR_AGO = "年前";

    static long getFirstSundayTimeMillisOfWeek() {
        Calendar calendar = Calendar.getInstance();
        int daysFromSunday = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        long secondOfToday = calendar.get(Calendar.HOUR_OF_DAY) * 60 * 60 +
                calendar.get(Calendar.MINUTE) * 60 + calendar.get(Calendar.SECOND);
        long millisFromSunday = (daysFromSunday * 24 * 60 * 60 + secondOfToday) * 1000;
        return System.currentTimeMillis() - millisFromSunday;
    }

    public static int calNextDayDayOfWeek(int currDayOfWeek) {
        int i = (currDayOfWeek + 1) % 8;
        if (i == 0) i = 1;
        return i;
    }

    public static synchronized String formatDateTime(long time) {
        if (time <= 0) throw new IllegalArgumentException("time shouldn't <= 0");
        FormatDateTimeHolder.mDate.setTime(time);
        return FormatDateTimeHolder.mSimpleDateTimeFormat.format(FormatDateTimeHolder.mDate);
    }

    public static synchronized String formatDate(long time) {
        if (time <= 0) throw new IllegalArgumentException("time shouldn't <= 0");
        FormatDateTimeHolder.mDate.setTime(time);
        return FormatDateTimeHolder.mSimpleDateFormat.format(FormatDateTimeHolder.mDate);
    }

    public static synchronized String formatDateWeek(long time) {
        if (time <= 0) throw new IllegalArgumentException("time shouldn't <= 0");
        FormatDateTimeHolder.mDate.setTime(time);
        return FormatDateTimeHolder.mSimpleDateWeekFormat.format(FormatDateTimeHolder.mDate);
    }

    public static String weekNumberToChinese(@IntRange(from = 1, to = 7) int i) {
        switch (i) {
            case 1:
                return "日";
            case 2:
                return "一";
            case 3:
                return "二";
            case 4:
                return "三";
            case 5:
                return "四";
            case 6:
                return "五";
            case 7:
                return "六";
            default:
                return "";
        }
    }

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


    /**
     * 根据Date获取描述性时间，如3分钟前，1天前
     *
     * @param date
     * @return
     */
    public static String getDescriptionTimeFromDate(Date date) {
        long delta = new Date().getTime() - date.getTime();
        if (delta < 1L * ONE_MINUTE) {
            long seconds = toSeconds(delta);
            return (seconds <= 0 ? 1 : seconds) + ONE_SECOND_AGO;
        }
        if (delta < 45L * ONE_MINUTE) {
            long minutes = toMinutes(delta);
            return (minutes <= 0 ? 1 : minutes) + ONE_MINUTE_AGO;
        }
        if (delta < 24L * ONE_HOUR) {
            long hours = toHours(delta);
            return (hours <= 0 ? 1 : hours) + ONE_HOUR_AGO;
        }
        if (delta < 48L * ONE_HOUR) {
            return "昨天";
        }
        if (delta < 30L * ONE_DAY) {
            long days = toDays(delta);
            return (days <= 0 ? 1 : days) + ONE_DAY_AGO;
        }
        if (delta < 12L * 4L * ONE_WEEK) {
            long months = toMonths(delta);
            return (months <= 0 ? 1 : months) + ONE_MONTH_AGO;
        } else {
            long years = toYears(delta);
            return (years <= 0 ? 1 : years) + ONE_YEAR_AGO;
        }
    }

    private static long toSeconds(long date) {
        return date / 1000L;
    }

    private static long toMinutes(long date) {
        return toSeconds(date) / 60L;
    }

    private static long toHours(long date) {
        return toMinutes(date) / 60L;
    }

    private static long toDays(long date) {
        return toHours(date) / 24L;
    }

    private static long toMonths(long date) {
        return toDays(date) / 30L;
    }

    private static long toYears(long date) {
        return toMonths(date) / 365L;
    }

    /**
     * 将时间戳转换为时间
     *
     * @param time 时间戳
     * @return yyyy-MM-dd
     */
    public static String yyyyMMdd(long time) {
        if (time == 0)
            return "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date(time);
        return format.format(date);
    }

    /***
     * 时间戳转换为时间
     *
     * @param time 时间戳
     * @return HH:mm:ss
     */
    public static String HHmmss(long time) {
        if (time == 0)
            return "";
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(time);
        return format.format(date);
    }

    /***
     * 时间戳转换为时间
     *
     * @param time 时间戳
     * @return yyyy-MM-dd HH:mm
     */
    public static String yyyyMMddHHmm(long time) {
        if (time == 0)
            return "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(time);
        return format.format(date);
    }

    /***
     * 时间戳转换为时间
     *
     * @param time 时间戳
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String yyyyMMddHHmmss(long time) {
        if (time == 0)
            return "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(time);
        return format.format(date);
    }

    /***
     * 时间戳转换为时间
     *
     * @param time 时间戳
     * @return MM-dd HH:mm
     */
    public static String MMddHHmm(long time) {
        if (time == 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm");
        Date date = new Date(time);
        return format.format(date);
    }

    /***
     * 时间戳转换为时间
     *
     * @param time 时间戳
     * @return MM-dd HH:mm:ss
     */
    public static String MMddHHmmss(long time) {
        if (time == 0) {
            return "";
        }
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss");
        Date date = new Date(time);
        return format.format(date);
    }

    /***
     * 时间转换为时间戳
     *
     * @param time 时间
     * @param format 格式 例如：yyyy-MM-dd HH:mm:ss
     * @return 时间戳
     */
    public static long toTimeStamp(String time, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0L;
    }


    /**
     * 格式化时间
     *
     * @param time
     * @return
     */
    public static String parseTime(long time) {
        long tmpTime;
        tmpTime = (System.currentTimeMillis() - time) / 1000;
        int day, hour, minute, second;

        // 将时间换算成天
        day = (int) (tmpTime / 3600 / 24);
        if (day == 1)
            return "1天前";
        if (day > 1) {
            return new SimpleDateFormat("yyyy-MM-dd").format(time);
        }

        // 小时
        hour = (int) (tmpTime / 3600);
        if (hour > 0)
            return String.valueOf(hour) + "小时前";
        minute = (int) (tmpTime / 60);
        if (minute > 0)
            return String.valueOf(minute) + "分钟前";
        second = (int) tmpTime;
        if (second >= 0)
            return "刚刚";
        return "";
    }

    /**
     * 毫秒转化时分秒毫秒
     *
     * @param ms
     * @return
     */
    public static String formatTime(Long ms) {
        Integer ss = 1000;
        Integer mi = ss * 60;
        Integer hh = mi * 60;
        Integer dd = hh * 24;

        Long day = ms / dd;
        Long hour = (ms - day * dd) / hh;
        Long minute = (ms - day * dd - hour * hh) / mi;
        Long second = (ms - day * dd - hour * hh - minute * mi) / ss;
        Long milliSecond = ms - day * dd - hour * hh - minute * mi - second * ss;

        StringBuffer sb = new StringBuffer();
        if (day > 0) {
            sb.append(day + "天");
        }
        if (hour > 0) {
            sb.append(hour + "小时");
        }
        if (minute > 0) {
            sb.append(minute + "分");
        }
        if (second > 0) {
            sb.append(second + "秒");
        }
        if (milliSecond > 0) {
            sb.append(milliSecond + "毫秒");
        }
        return sb.toString();
    }

    /**
     * 获取当前年
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.YEAR);
    }

    /**
     * 获取当前月
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.MONTH);
    }

    /**
     * 获取当前日
     *
     * @return
     */
    public static int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        return cal.get(Calendar.DATE);
    }

    private static class FormatDateTimeHolder {
        static SimpleDateFormat mSimpleDateTimeFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.CHINA);
        static SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.CHINA);
        static SimpleDateFormat mSimpleDateWeekFormat = new SimpleDateFormat("yyyy年M月d日 EEEE", Locale.CHINA);
        static Date mDate = new Date();
    }
}
