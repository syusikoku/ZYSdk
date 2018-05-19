package com.zhiyangstudio.commonlib.widget.recyclerview.divider;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by zhi yang on 2018/3/29.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private final int mSpace;
    private final int mColumnCount;

    public SpacesItemDecoration(int space, int columnCount) {
        this.mSpace = space;
        this.mColumnCount = columnCount;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = mSpace;
        if (parent.getChildLayoutPosition(view) == mColumnCount - 1) {
            outRect.right = 0;
        } else {
            outRect.right = mSpace / 2;
        }
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = 0;
        } else {
            outRect.left = mSpace / 2;
        }
    }
}
