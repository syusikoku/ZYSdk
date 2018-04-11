package com.zhiyangstudio.commonlib.inter;

/**
 * Created by syusikoku on 2018/1/18.
 */

public interface ILifecycle {
    void preProcess();

    int getContentId();

    void initView();

    void addListener();

    void initData();

    void refreshUi();

    void release();
}