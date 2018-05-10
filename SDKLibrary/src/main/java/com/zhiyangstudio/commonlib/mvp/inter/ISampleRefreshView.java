package com.zhiyangstudio.commonlib.mvp.inter;

import java.util.List;

/**
 * Created by example on 2018/5/10.
 */

public interface ISampleRefreshView<T> extends IView {
    void steDataCount(int total);

    int getPage();

    void setData(List<T> data);
}
