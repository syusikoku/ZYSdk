package com.zysdk.vulture.clib.inter;

/**
 * Created by zhiyang on 2018/4/17.
 */

public interface IActivityLifeCycle extends ILifecycle {
    void beforeCreate();

    void beforeSetContentView();

    void beforeSubContentInit();
}
