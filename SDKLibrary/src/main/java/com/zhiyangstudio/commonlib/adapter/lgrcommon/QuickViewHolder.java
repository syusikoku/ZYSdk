package com.zhiyangstudio.commonlib.adapter.lgrcommon;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhiyangstudio.commonlib.utils.EmptyUtils;

import java.lang.ref.WeakReference;

/**
 * Created by zzg on 2018/4/17.
 */

public class QuickViewHolder extends RecyclerView.ViewHolder {

    private int mLayoutId;
    private SparseArray<WeakReference<View>> mViews;

    public QuickViewHolder(View itemView) {
        this(itemView, -1);
    }

    public QuickViewHolder(View itemView, int layoutId) {
        super(itemView);
        mViews = new SparseArray<>();
        this.mLayoutId = layoutId;
    }

    public int getLayoutId() {
        return mLayoutId;
    }

    public QuickViewHolder setOnClickListener(View.OnClickListener listener) {
        itemView.setOnClickListener(listener);
        return this;
    }

    public QuickViewHolder setOnLongClickListener(View.OnLongClickListener listener) {
        itemView.setOnLongClickListener(listener);
        return this;
    }

    public QuickViewHolder setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnLongClickListener(listener);
        }
        return this;
    }

    public QuickViewHolder setOnClickListener(int viewId, View.OnClickListener listener) {
        View view = getView(viewId);
        if (view != null) {
            view.setOnClickListener(listener);
        }
        return this;
    }

    public <T extends View> T getView(int viewId) {
        WeakReference<View> vwr = mViews.get(viewId);
        View view = null;
        if (vwr == null) {
            view = itemView.findViewById(viewId);
            if (view != null) {
                mViews.put(viewId, new WeakReference<View>(view));
            }
        } else {
            view = vwr.get();
        }
        return (T) view;
    }

    public QuickViewHolder setText(int viewId, CharSequence txt) {
        TextView textView = getView(viewId);
        if (textView != null && EmptyUtils.isNotEmpty(txt)) {
            textView.setText(txt);
        }
        return this;
    }

    public QuickViewHolder setTextColor(int viewId, int color) {
        TextView textView = getView(viewId);
        if (textView != null) {
            textView.setTextColor(color);
        }
        return this;
    }

    public QuickViewHolder setVisible(int viewId, int visible) {
        View view = getView(viewId);
        if (view != null) {
            view.setVisibility(visible);
        }
        return this;
    }

    public QuickViewHolder setChecked(int viewId, boolean checked) {
        Checkable checkable = getView(viewId);
        if (checkable != null) {
            checkable.setChecked(checked);
        }
        return this;
    }

    public QuickViewHolder setBackgroundRes(int viewId, int backgroundRes) {
        View view = getView(viewId);
        if (view != null) {
            view.setBackgroundResource(backgroundRes);
        }
        return this;
    }

    public QuickViewHolder setImageRes(int viewId, int imgRes) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageResource(imgRes);
        }
        return this;
    }

    public QuickViewHolder setImageRes(int viewId, Bitmap bm) {
        ImageView imageView = getView(viewId);
        if (imageView != null) {
            imageView.setImageBitmap(bm);
        }
        return this;
    }
}
