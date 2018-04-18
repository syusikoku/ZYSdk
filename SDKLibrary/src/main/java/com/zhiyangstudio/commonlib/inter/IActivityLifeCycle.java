package com.zhiyangstudio.commonlib.inter;

/**
 * Created by example on 2018/4/17.
 */

public interface IActivityLifeCycle extends ILifecycle {
    void beforeCreate();

    void beforeSetContentView();

    void beforeSubContentInit();
}
