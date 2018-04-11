package com.zhiyangstudio.commonlib.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.zhiyangstudio.commonlib.widget.CommonLGViewHolder;

import java.util.List;

/**
 * Created by zhiyang on 2018/2/27.
 * listview & gridview使用
 */

public abstract class CommonLGAdapter<T> extends BaseAdapter {
    protected int resId;
    protected Context mContext;
    protected List<T> mList;

    public CommonLGAdapter(Context context, int resId) {
        this(context, resId, null);
    }

    public CommonLGAdapter(Context context, int resId, List<T> list) {
        this.mContext = context;
        this.resId = resId;
        this.mList = list;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CommonLGViewHolder commonLGViewHolder = CommonLGViewHolder.create(mContext, position, convertView,
                resId, parent);
        bindView(commonLGViewHolder, mList == null ? null : mList.get(position));
        return commonLGViewHolder.getConvertView();
    }

    public abstract void bindView(CommonLGViewHolder holder, T data);

    public void setData(List<T> list) {
        this.mList = list;
    }

}
