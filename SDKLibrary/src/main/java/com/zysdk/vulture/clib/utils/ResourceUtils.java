package com.zysdk.vulture.clib.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;

public class ResourceUtils {

    private static Context mContext;
    private static Resources resources;

    static {
        mContext = UiUtils.getContext();
        resources = UiUtils.getResources(mContext);
    }

    public static String getStr(int arrsResId) {
        if (arrsResId == 0)
            return null;
        return resources.getString(arrsResId);
    }

    public static String[] getStrArrs(int arrsResId) {
        if (arrsResId == 0)
            return null;
        return resources.getStringArray(arrsResId);
    }

    public static int getInt(int arrsResId) {
        if (arrsResId == 0)
            return 0;
        return resources.getInteger(arrsResId);
    }

    public static int getDimensionPixelOffset(int arrsResId) {
        if (arrsResId == 0)
            return 0;
        return resources.getDimensionPixelOffset(arrsResId);
    }

    public static XmlResourceParser getLayout(int arrsResId) {
        if (arrsResId == 0)
            return null;
        return resources.getLayout(arrsResId);
    }

    public static int[] getIntArrs(int arrsID) {
        if (arrsID == 0)
            return null;
        return resources.getIntArray(arrsID);
    }

    public static float getDimension(int arrsResId) {
        if (arrsResId == 0)
            return 0f;
        return resources.getDimension(arrsResId);
    }

    public static int getColor(int arrsResId) {
        if (arrsResId == 0)
            return 0;
        return resources.getColor(arrsResId);
    }

    public static ColorStateList getColorStateList(int arrsResId) {
        if (arrsResId == 0)
            return null;
        return resources.getColorStateList(arrsResId);
    }

    public static Drawable getDrawable(int resId) {
        if (resId == 0)
            return null;
        return resources.getDrawable(resId);
    }

    @ColorInt
    public static int getColorAttr(int attr) {
        if (attr == 0)
            return 0;
        Resources.Theme theme = mContext.getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{attr});
        int color = typedArray.getColor(0, Color.LTGRAY);
        typedArray.recycle();
        return color;
    }
}
