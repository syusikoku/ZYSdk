package com.zysdk.vulture.clib.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.util.SparseIntArray;
import android.util.SparseLongArray;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;

/**
 * Created by zhiyang on 2018/4/6.
 */

public class EmptyUtils {

    /**
     * 判断对象是否为空
     *
     * @param obj 对象
     * @return {@code true}: 为空<br>{@code false}: 不为空
     */
    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        }
        if (obj instanceof String) {
            String str = obj.toString();
            if (TextUtils.isEmpty(str) ||
                    str.equalsIgnoreCase("null") ||
                    str.length() == 0) {
                return true;
            }
        }

        if (obj.getClass().isArray() && Array.getLength(obj) == 0) {
            return true;
        }

        if (obj instanceof Collection && ((Collection) obj).isEmpty()) {
            return true;
        }

        if (obj instanceof Map && ((Map) obj).isEmpty()) {
            return true;
        }

        if (obj instanceof SparseArray && ((SparseArray) obj).size() == 0) {
            return true;
        }

        if (obj instanceof SparseBooleanArray && ((SparseBooleanArray) obj).size() == 0) {
            return true;
        }

        if (obj instanceof SparseIntArray && ((SparseIntArray) obj).size() == 0) {
            return true;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            if (obj instanceof SparseLongArray && ((SparseLongArray) obj).size() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断对象是否非空
     *
     * @param obj 对象
     * @return {@code true}: 非空<br>{@code false}: 空
     */
    public static boolean isNotEmpty(Object obj) {
        return !isEmpty(obj);
    }
}
