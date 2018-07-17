package com.zysdk.vulture.clib.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

public class NoScrollGridLayoutManager extends GridLayoutManager {
    private boolean hasScrolled;

    public NoScrollGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int
            defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public NoScrollGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public NoScrollGridLayoutManager(Context context, int spanCount, int orientation, boolean
            reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }


    public void setScrollEnabled(boolean flag) {
        this.hasScrolled = flag;
    }

    @Override
    public boolean canScrollVertically() {
        return hasScrolled && super.canScrollVertically();
    }
}
