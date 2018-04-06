package com.zhiyangstudio.sdklibrary.common.utils;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

/**
 * Created by zzg on 2018/4/6.
 */

public class GsonUtils {

    private static Gson sGson;

    static {
        sGson = new Gson();
    }

    /**
     * list转换成json
     */
    public static String toJsonStr(Object src) {
        return sGson.toJson(src);
    }

    /**
     * 字符串转换成对象
     */
    public static <T> T toObject(String json, Class<T> pClass) {
        return sGson.fromJson(json, pClass);
    }

    /**
     * json转换成list
     */
    public static <T> T toObjectListSample(String jsonStr, TypeToken<T> pToken) {
        return new Gson().fromJson(jsonStr, pToken.getType());
    }

    /**
     * json转换成list
     */
    public static <T> List<T> toObjectList(String jsonStr, TypeToken<List<T>> pToken) {
        return new Gson().fromJson(jsonStr, pToken.getType());
    }
}
