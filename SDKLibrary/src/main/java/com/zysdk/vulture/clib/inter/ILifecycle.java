package com.zysdk.vulture.clib.inter;

/**
 * Created by syusikoku on 2018/1/18.
 */

public interface ILifecycle {
    int getContentId();

    void initView();

    void addListener();

    void initData();

    void refreshUi();

    void release();
}
