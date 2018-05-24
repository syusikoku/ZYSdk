package com.zhiyangstudio.commonlib.adapter.lgrcommon;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiyang on 2018/4/17.
 * RecyclerView、ListView、GridView通用的适配器
 */

public abstract class QuickAdapter<T> extends BaseAdapter {

    private QuickMultiSupport<T> mSupport;
    private int mLayoutId;
    private Context mContext;
    protected List<T> mData;
    private int mPosition;
    private boolean isRecyclerView;
    private LayoutInflater mLayoutInflater;

    public QuickAdapter(Context context, List<T> tList, QuickMultiSupport<T> support) {
        this(context, tList, 0);
        this.mSupport = support;
    }

    public QuickAdapter(Context context, List<T> tList, int layoutId) {
        this.mContext = context;
        this.mData = tList == null ? new ArrayList<>() : new ArrayList<>(tList);
        this.mLayoutId = layoutId;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getItemViewType(int position) {
        mPosition = position;
        // 多条目
        if (mSupport != null) {
            return mSupport.getItemViewType(mData.get(position));
        }
        return super.getItemViewType(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (mSupport == null)
            return;
        int position = holder.getLayoutPosition();
        // 如果设置合并 单元格
        if (mSupport.isSpan(mData.get(position))) {
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null && lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) lp;
                layoutParams.setFullSpan(true);
            }
        }
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        if (mSupport == null || recyclerView == null)
            return;
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();
            // 如果设置合并 单元格占用spanCount那个多个位置
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mSupport.isSpan(mData.get(position))) {
                        return gridLayoutManager.getSpanCount();
                    } else if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        isRecyclerView = true;
        // 如果是多条目 ,viewType就是布局id
        View view = null;
        if (mSupport != null) {
            T data = mData.get(mPosition);
            int layoutId = mSupport.getLayoutId(data);
            view = mLayoutInflater.inflate(layoutId, parent, false);
        } else {
            // 非多条目 。使用的是传递进来的布局文件的id
            view = mLayoutInflater.inflate(mLayoutId, parent, false);
        }
        QuickViewHolder holder = new QuickViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof QuickViewHolder) {
            convert((QuickViewHolder) holder, mData.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QuickViewHolder holder;
        T data = mData.get(position);
        if (convertView == null) {
            int layoutId = mLayoutId;
            // 多条目
            if (mSupport != null) {
                layoutId = mSupport.getLayoutId(data);
            }
            // 创建viewholder
            holder = createListiewHolder(parent, layoutId);
        } else {
            holder = (QuickViewHolder) convertView.getTag();
            // 防止失误还要判断
            if (mSupport != null) {
                int layoutId = mSupport.getLayoutId(data);
                // 如果布局ID不一样，又重要创建
                if (layoutId != holder.getLayoutId()) {
                    holder = createListiewHolder(parent, layoutId);
                }
            }
        }
        // 绑定数据
        convert(holder, data, position);
        return holder.itemView;
    }

    @Override
    public int getViewTypeCount() {
        // 多条目
        if (mSupport != null) {
            return mSupport.getViewTypeCount() + super.getViewTypeCount();
        }
        return super.getViewTypeCount();
    }

    /**
     * 创建listview的holder
     *
     * @param parent
     * @param layoutId
     * @return
     */
    private QuickViewHolder createListiewHolder(ViewGroup parent, int layoutId) {
        QuickViewHolder holder = null;
        View view = mLayoutInflater.inflate(layoutId, parent, false);
        holder = new QuickViewHolder(view, layoutId);
        view.setTag(holder);
        return holder;
    }

    protected abstract void convert(QuickViewHolder holder, T data, int position);

    public void add(T elem) {
        mData.add(elem);
        notifyData();
    }

    public void notifyData() {
        if (isRecyclerView) {
            notifyDataSetChanged();
        } else {
            notifyListDataSetChanged();
        }
    }

    public void addAll(List<T> data) {
        mData.addAll(data);
        notifyData();
    }

    public void addFirst(T elem) {
        mData.add(0, elem);
        notifyData();
    }

    public void set(T oldEle, T newEle) {
        set(mData.indexOf(oldEle), newEle);
        notifyData();
    }

    public void set(int index, T elem) {
        mData.set(index, elem);
        //notify();
        notifyData();
    }

    public void remove(T elem) {
        mData.remove(elem);
        notifyData();
    }

    public void replaceAll(List<T> eList) {
        mData.clear();
        mData.addAll(eList);
        notifyData();
    }

    public void clear() {
        mData.clear();
        notifyData();
    }

    public List<T> getData() {
        return mData;
    }
}
