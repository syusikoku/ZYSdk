package com.zysdk.vulture.clib.corel;

import com.zysdk.vulture.clib.inter.IPermissionListener;
import com.zysdk.vulture.clib.utils.LoggerUtils;

/**
 * 简单的Activity基类，不想使用ToolBar和MVP仅想要一个单纯的界面可以使用这个类,需要某些操作，直接覆盖这些方法即可
 */
public abstract class BaseSampleActivity extends BaseActivity {
    @Override
    protected IPermissionListener getPermissonCallBack() {
        return null;
    }


    @Override
    public void initView() {
        LoggerUtils.loge("initView");
    }

    @Override
    public void addListener() {
        LoggerUtils.loge("addListener");
    }

    @Override
    public void initData() {
        LoggerUtils.loge("initData");
    }

    @Override
    public void refreshUi() {
        LoggerUtils.loge("refreshUi");
    }

    @Override
    public void release() {
        LoggerUtils.loge("release");
    }
}
