package com.zhiyangstudio.commonlib.recyclerview;

import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;

import com.zhiyangstudio.commonlib.helper.ViewHolderHelper;
import com.zhiyangstudio.commonlib.inter.IViewHolder;

/**
 * Created by zhiyang on 2018/4/10.
 */

public class CommonRViewHolder extends RecyclerView.ViewHolder implements IViewHolder {

    private ViewHolderHelper viewHolderHelper;

    public CommonRViewHolder(ViewGroup parent, int layoutId) {
        this(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    public CommonRViewHolder(View itemView) {
        super(itemView);
        viewHolderHelper = new ViewHolderHelper(itemView);
    }

    @Override
    public <T> T findView(int viewID) {
        return viewHolderHelper.findView(viewID);
    }

    @Override
    public void setText(int viewID, String txt) {
        viewHolderHelper.setText(viewID, txt);
    }

    @Override
    public void setText(int viewId, int strId) {
        viewHolderHelper.setText(viewId, strId);
    }

    @Override
    public void setImageResource(int viewId, int imageResId) {
        viewHolderHelper.setImageResource(viewId, imageResId);
    }

    @Override
    public void setBackgroundColor(int viewId, int color) {
        viewHolderHelper.setBackgroundColor(viewId, color);
    }

    @Override
    public void setBackgroundRes(int viewId, int backgroundRes) {
        viewHolderHelper.setBackgroundRes(viewId, backgroundRes);
    }

    @Override
    public void setTextColor(int viewId, int textColor) {
        viewHolderHelper.setTextColor(viewId, textColor);
    }

    @Override
    public void setImageDrawable(int viewId, Drawable drawable) {
        viewHolderHelper.setImageDrawable(viewId, drawable);
    }

    @Override
    public void setImageBitmap(int viewId, Bitmap bitmap) {
        viewHolderHelper.setImageBitmap(viewId, bitmap);
    }

    @Override
    public void setAlpha(int viewId, float value) {
        viewHolderHelper.setAlpha(viewId, value);
    }

    @Override
    public void setVisible(int viewId, boolean visible) {
        viewHolderHelper.setVisible(viewId, visible);
    }

    @Override
    public void linkify(int viewId) {
        viewHolderHelper.linkify(viewId);
    }

    @Override
    public void setTypeface(int viewId, Typeface typeface) {
        viewHolderHelper.setTypeface(viewId, typeface);
    }

    @Override
    public void setTypeface(Typeface typeface, int... viewIds) {
        viewHolderHelper.setTypeface(typeface, viewIds);
    }

    @Override
    public void setProgress(int viewId, int progress) {
        viewHolderHelper.setProgress(viewId, progress);
    }

    @Override
    public void setProgress(int viewId, int progress, int max) {
        viewHolderHelper.setProgress(viewId, progress, max);
    }

    @Override
    public void setMax(int viewId, int max) {
        viewHolderHelper.setMax(viewId, max);
    }

    @Override
    public void setRating(int viewId, float rating) {
        viewHolderHelper.setRating(viewId, rating);
    }

    @Override
    public void setRating(int viewId, float rating, int max) {
        viewHolderHelper.setRating(viewId, rating, max);
    }

    @Override
    public void setOnItemClickListener(int viewId, View.OnClickListener listener) {
        viewHolderHelper.setOnItemClickListener(viewId, listener);
    }

    @Override
    public void setOnTouchListener(int viewId, View.OnTouchListener listener) {
        viewHolderHelper.setOnTouchListener(viewId, listener);
    }

    @Override
    public void setOnLongClickListener(int viewId, View.OnLongClickListener listener) {
        viewHolderHelper.setOnLongClickListener(viewId, listener);
    }

    @Override
    public void setOnItemClickListener(int viewId, AdapterView.OnItemClickListener listener) {
        viewHolderHelper.setOnItemClickListener(viewId, listener);
    }

    @Override
    public void setOnItemLongClickListener(int viewId, AdapterView.OnItemLongClickListener listener) {
        viewHolderHelper.setOnItemLongClickListener(viewId, listener);
    }

    @Override
    public void setOnItemSelectedClickListener(int viewId, AdapterView.OnItemSelectedListener listener) {
        viewHolderHelper.setOnItemSelectedClickListener(viewId, listener);
    }

    @Override
    public void setOnCheckedChangeListener(int viewId, CompoundButton.OnCheckedChangeListener listener) {
        viewHolderHelper.setOnCheckedChangeListener(viewId, listener);
    }

    @Override
    public void setTag(int viewId, Object tag) {
        viewHolderHelper.setTag(viewId, tag);
    }

    @Override
    public void setTag(int viewId, int key, Object tag) {
        viewHolderHelper.setTag(viewId, key, tag);
    }

    @Override
    public void setChecked(int viewId, boolean checked) {
        viewHolderHelper.setChecked(viewId, checked);
    }

}
