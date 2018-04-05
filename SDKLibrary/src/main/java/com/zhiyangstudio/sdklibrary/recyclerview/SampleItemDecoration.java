package com.zhiyangstudio.sdklibrary.recyclerview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;

/**
 * Created by zhiyang on 2018/3/2.
 */

public class SampleItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(Rect outRect, int itemPosition, RecyclerView parent) {
        outRect.set(0, 0, 0, 10);
    }
}
