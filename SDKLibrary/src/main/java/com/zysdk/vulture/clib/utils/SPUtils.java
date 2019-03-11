package com.zysdk.vulture.clib.utils;

import android.content.Context;
import android.content.SharedPreferences;


/**
 * @author zzg
 * @date 2019-3-3 sp工具类
 */
public class SPUtils {
    private static Context appContext;
    /**
     * 保存在手机里面的文件名
     */
    private static String FILE_NAME = "local_config";

    private static SPUtils instance;

    private SPUtils() {
        this.appContext = UiUtils.getContext();
    }

    /**
     * 初始化文件名
     */
    public void initFileName(String fileName) {
        FILE_NAME = fileName;
    }

    public static SPUtils getInstance() {
        if (instance == null) {
            synchronized (SPUtils.class) {
                if (instance == null) {
                    instance = new SPUtils();
                }
            }
        }
        return instance;
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     */
    public void setParam(String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = appContext.getSharedPreferences(FILE_NAME,
                Context.MODE_MULTI_PROCESS);
        SharedPreferences.Editor editor = sp.edit();
        if ("String".equals(type)) {
            editor.putString(key, object.toString());
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    public Object getParam(String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = appContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);

        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     */
    // Delete
    public void remove(String key) {
        SharedPreferences sp = appContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    public void clear() {
        SharedPreferences sp = appContext.getSharedPreferences(FILE_NAME,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.clear();
        editor.commit();
    }
}
