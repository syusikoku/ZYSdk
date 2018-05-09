package com.zhiyangstudio.commonlib.widget.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyangstudio.commonlib.helper.ViewHolderHelper;

/**
 * Created by zhiyang on 2018/2/27.
 */

public class CommonLGViewHolder {
    private final ViewHolderHelper viewHolderHelper;
    private Context mContext;
    private ViewGroup mParent;
    private int mResId;
    private int mPosition;
    private View mRootView;
    private int position;

    private CommonLGViewHolder(Context context, ViewGroup parent, int resId, int position) {
        this.mContext = context;
        this.mParent = parent;
        this.mResId = resId;
        this.mPosition = position;
        this.mRootView = LayoutInflater.from(context).inflate(resId, parent, false);
        mRootView.setTag(this);
        viewHolderHelper = new ViewHolderHelper(mRootView);
    }

    public static CommonLGViewHolder create(Context context, int position, View view, int resId,
                                            ViewGroup parent) {
        if (view == null) {
            return new CommonLGViewHolder(context, parent, resId, position);
        } else {
            CommonLGViewHolder viewHolder = (CommonLGViewHolder) view.getTag();
            viewHolder.position = position;
            return viewHolder;
        }
    }

    public int getPosition() {
        return mPosition;
    }

    public View getConvertView() {
        return mRootView;
    }

    public ViewHolderHelper getViewHolderHelper() {
        return viewHolderHelper;
    }

}
