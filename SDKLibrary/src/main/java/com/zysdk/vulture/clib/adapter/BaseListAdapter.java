package com.zysdk.vulture.clib.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.zysdk.vulture.clib.adapter.lgrcommon.QuickViewHolder;
import com.zysdk.vulture.clib.utils.LogListener;
import com.zysdk.vulture.clib.utils.UiUtils;
import com.zysdk.vulture.clib.widget.recyclerview.LMRecyclerView;

import java.util.List;

/**
 * Created by zhiyang on 2018/4/10.
 */

public abstract class BaseListAdapter<T> extends RecyclerView.Adapter<QuickViewHolder> implements
        LogListener {
    private List<T> mList;

    //刷新所有数据
    public void notifyAllDatas(List<T> mList, LMRecyclerView recyclerView) {
        this.mList = mList;
        recyclerView.notifyDataSetChanged();
    }

    //刷新单条数据
    public void notifyItemDataChanged(int position, LMRecyclerView recyclerView) {
        recyclerView.notifyItemChanged(position);
    }

    //移除单条数据
    public void notifyItemDataRemove(int position, LMRecyclerView recyclerView) {
        recyclerView.notifyItemRemoved(position);
    }

    @Override
    public QuickViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layoutId = getLayoutId(viewType);
        return new QuickViewHolder(UiUtils.inflateView(layoutId, parent), layoutId);
    }

    protected abstract int getLayoutId(int viewType);

    @Override
    public void onBindViewHolder(QuickViewHolder holder, int position) {
        //初始化View
        T bean = mList.get(position);
        //绑定数据
        bindDatas(holder, bean, holder.getItemViewType(), position);
    }

    protected abstract void bindDatas(QuickViewHolder holder, T bean, int itemViewType, int position);

    @Override
    public int getItemCount() {
        return mList != null ? mList.size() : 0;
    }
}
