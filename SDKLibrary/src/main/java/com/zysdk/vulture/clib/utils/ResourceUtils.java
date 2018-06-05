package com.zysdk.vulture.clib.utils;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
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

    public static String[] getStrArrs(int arrsResId) {
        return resources.getStringArray(arrsResId);
    }

    public static int[] getIntArrs(int arrsID) {
        return resources.getIntArray(arrsID);
    }

    public static float getDimension(int arrsResId) {
        return resources.getDimension(arrsResId);
    }

    public static int getColor(int arrsResId) {
        return resources.getColor(arrsResId);
    }

    public static ColorStateList getColorStateList(int arrsResId) {
        return resources.getColorStateList(arrsResId);
    }

    public static Drawable getDrawable(int resId) {
        return resources.getDrawable(resId);
    }

    @ColorInt
    public static int getColorAttr(int attr) {
        Resources.Theme theme = mContext.getTheme();
        TypedArray typedArray = theme.obtainStyledAttributes(new int[]{attr});
        int color = typedArray.getColor(0, Color.LTGRAY);
        typedArray.recycle();
        return color;
    }
}
