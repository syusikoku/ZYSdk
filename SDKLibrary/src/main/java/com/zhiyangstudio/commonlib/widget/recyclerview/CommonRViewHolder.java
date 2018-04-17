package com.zhiyangstudio.commonlib.widget.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyangstudio.commonlib.helper.ViewHolderHelper;

/**
 * Created by zhiyang on 2018/4/10.
 */

public class CommonRViewHolder extends RecyclerView.ViewHolder {

    private ViewHolderHelper mHolderHelper;

    public CommonRViewHolder(ViewGroup parent, int layoutId) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    public CommonRViewHolder(View itemView) {
        super(itemView);
        mHolderHelper = new ViewHolderHelper(itemView);
    }

    public ViewHolderHelper getHolderHelper() {
        return mHolderHelper;
    }
}
