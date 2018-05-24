package com.zhiyangstudio.commonlib.adapter.lgrcommon;

/**
 * Created by zhiyang on 2018/4/17.
 */

public interface QuickMultiSupport<T> {
    /**
     * 获取多条目view类型的数量
     *
     * @return
     */
    int getViewTypeCount();

    /**
     * 根据数据，获取多条目布局id
     *
     * @param data
     * @return
     */
    int getLayoutId(T data);

    /**
     * 根据数据，获取多条目的itemViewType
     *
     * @param data
     * @return
     */
    int getItemViewType(T data);

    /**
     * 是否合并条目 ->使用RecyclerView时，如果无效，则使用原生的RecyclerView
     *
     * @param data
     * @return
     */
    boolean isSpan(T data);
}
