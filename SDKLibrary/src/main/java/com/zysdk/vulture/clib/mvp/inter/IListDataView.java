package com.zysdk.vulture.clib.mvp.inter;

import java.util.List;

/**
 * Created by zhiyang on 2018/4/11.
 */

public interface IListDataView<T> extends IView {
    int getPage();

    List<T> getData();

    void setData(List<T> data);

    void showContent();

    /**
     * 自动加载
     */
    void autoLoadMore();

    /**
     * 清空所有数据
     */
    void refresh();

    /**
     * 没有更多数据
     */
    void showNoMore();
}


