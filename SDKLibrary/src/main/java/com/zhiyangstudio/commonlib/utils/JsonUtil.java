package com.zhiyangstudio.commonlib.utils;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by example on 2018/5/29.
 */

public class JsonUtil {

    public static int optInt(JSONObject jsonObject, String key) {
        if (jsonObject != null && jsonObject.has(key))
            return jsonObject.optInt(key);
        return 0;
    }

    public static boolean optBool(JSONObject jsonObject, String key) {
        if (jsonObject != null && jsonObject.has(key))
            return jsonObject.optBoolean(key);
        return false;
    }

    public static String optStr(JSONObject jsonObject, String key) {
        if (jsonObject != null && jsonObject.has(key))
            return jsonObject.optString(key);
        return "";
    }

    public static double optDouble(JSONObject jsonObject, String key) {
        if (jsonObject != null && jsonObject.has(key))
            return jsonObject.optDouble(key);
        return 0;
    }

    public static long optLong(JSONObject jsonObject, String key) {
        if (jsonObject != null && jsonObject.has(key))
            return jsonObject.optLong(key);
        return 0L;
    }

    public static JSONObject optJsonObj(JSONObject jsonObject, String key) {
        if (jsonObject != null && jsonObject.has(key))
            return jsonObject.optJSONObject(key);
        return null;
    }

    public static JSONArray optJsonArr(JSONObject jsonObject, String key) {
        if (jsonObject != null && jsonObject.has(key))
            return jsonObject.optJSONArray(key);
        return null;
    }


}
