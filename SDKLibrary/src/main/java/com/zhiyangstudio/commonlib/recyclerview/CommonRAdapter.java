package com.zhiyangstudio.commonlib.recyclerview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyangstudio.commonlib.utils.UiUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiyang on 2018/4/10.
 */

public abstract class CommonRAdapter<T> extends RecyclerView.Adapter<CommonRViewHolder> {
    private final Context mContext;
    private final LayoutInflater mLayoutInflater;
    private final int mLayoutId;
    private List<T> mList;

    public CommonRAdapter(int layoutId) {
        this(layoutId, null);
    }

    public CommonRAdapter(int layoutId, List<T> list) {
        this.mList = list == null ? new ArrayList<>() : list;
        this.mContext = UiUtils.getContext();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        this.mLayoutId = layoutId;
    }

    @Override
    public CommonRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(mLayoutId, parent, false);
        return new CommonRViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommonRViewHolder holder, int position) {
        convert(holder, mList.get(position), position);
    }

    protected abstract void convert(CommonRViewHolder holder, T t, int position);

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }

    public void setData(List<T> tList) {
        this.mList = tList;
    }
}
