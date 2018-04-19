package com.zhiyangstudio.commonlib.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by example on 2018/4/9.
 */

public class PreUtils {

    private static Context context;
    private static String mSpName;

    /**
     * 取数据
     *
     * @param key
     * @param value
     * @return
     */
    public static void put(String key, Object value) {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        }
        editor.commit();
    }

    private static SharedPreferences getSp() {
        return context.getSharedPreferences(mSpName, Context.MODE_PRIVATE);
    }

    /**
     * 取数据
     *
     * @param key
     * @param defValue
     * @return
     */
    public static Object get(String key, Object defValue) {
        SharedPreferences sp = getSp();
        if (defValue instanceof String) {
            return sp.getString(key, (String) defValue);
        } else if (defValue instanceof Integer) {
            return sp.getInt(key, (Integer) defValue);
        } else if (defValue instanceof Float) {
            return sp.getFloat(key, (Float) defValue);
        } else if (defValue instanceof Long) {
            return sp.getLong(key, (Long) defValue);
        } else if (defValue instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defValue);
        }
        return null;
    }

    public static void init(String spName) {
        context = UiUtils.getContext();
        mSpName = spName;
    }

    /**
     * 移除指定的数据
     *
     * @param key
     */
    public static void remove(String key) {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    /**
     * 清除数据
     */
    public static void clearAll() {
        SharedPreferences sp = getSp();
        SharedPreferences.Editor edit = sp.edit();
        edit.clear();
        edit.commit();
    }
}
