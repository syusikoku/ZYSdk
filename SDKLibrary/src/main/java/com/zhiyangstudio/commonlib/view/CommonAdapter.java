package com.zhiyangstudio.commonlib.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by zhiyang on 2018/2/27.
 */

public abstract class CommonAdapter<T> extends BaseAdapter {
    protected int resId;
    protected Context mContext;
    protected List<T> mList;

    public CommonAdapter(Context context, int resId) {
        this(context, resId, null);
    }

    public CommonAdapter(Context context, int resId, List<T> list) {
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
        CommonViewHolder commonViewHolder = CommonViewHolder.create(mContext, position, convertView,
                resId, parent);
        bindView(commonViewHolder, mList == null ? null : mList.get(position));
        return commonViewHolder.getConvertView();
    }

    public abstract void bindView(CommonViewHolder holder, T data);

    public void setData(List<T> list) {
        this.mList = list;
    }

}
