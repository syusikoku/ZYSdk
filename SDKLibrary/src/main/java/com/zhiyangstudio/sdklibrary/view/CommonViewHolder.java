package com.zhiyangstudio.sdklibrary.view;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by zhiyang on 2018/2/27.
 */

public class CommonViewHolder {
    private Context mContext;
    private ViewGroup mParent;
    private int mResId;
    private int mPosition;
    private View mRootView;
    private int position;
    private SparseArray<View> mViews;

    private CommonViewHolder(Context context, ViewGroup parent, int resId, int position) {
        this.mContext = context;
        this.mParent = parent;
        this.mResId = resId;
        this.mPosition = position;
        this.mRootView = LayoutInflater.from(context).inflate(resId, parent, false);
        this.mViews = new SparseArray<>();
        mRootView.setTag(this);
    }

    public static CommonViewHolder create(Context context, int position, View view, int resId,
                                          ViewGroup parent) {
        if (view == null) {
            return new CommonViewHolder(context, parent, resId, position);
        } else {
            CommonViewHolder viewHolder = (CommonViewHolder) view.getTag();
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

    public <T extends View> T findView(int viewId) {
        View view = null;
        if (mViews.get(viewId) != null) {
            view = mViews.get(viewId);
        } else {
            view = mRootView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public void setText(int viewId, String str) {
        TextView textView = findView(viewId);
        textView.setText(str);
    }
}
