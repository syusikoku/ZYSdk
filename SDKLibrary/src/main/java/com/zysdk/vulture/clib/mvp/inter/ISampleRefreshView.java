package com.zysdk.vulture.clib.mvp.inter;

import java.util.List;

/**
 * Created by zhiyang on 2018/5/10.
 */

public interface ISampleRefreshView<T> extends IView {
    void steDataCount(int total);

    int getPage();

    void setData(List<T> data);
}
