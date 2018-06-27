package com.zysdk.vulture.clib.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * 禁止左右滑动的viewpager
 */
public class NoScrollViewPager extends ViewPager {
    private boolean isPagingEnabled = false;

    public NoScrollViewPager(@NonNull Context context) {
        super(context);
    }

    public NoScrollViewPager(@NonNull Context context, boolean isPaging) {
        super(context);
        this.isPagingEnabled = isPaging;
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(@NonNull Context context, @Nullable AttributeSet attrs, boolean
            isPaging) {
        super(context, attrs);
        this.isPagingEnabled = isPaging;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return isPagingEnabled && super.onTouchEvent(ev);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return isPagingEnabled && super.onInterceptTouchEvent(ev);
    }

    public void setPagingEnabled(boolean pagingEnabled) {
        isPagingEnabled = pagingEnabled;
    }
}
