package com.zysdk.vulture.clib.corel;

/**
 * 简单的Activity基类，不想使用ToolBar和MVP仅想要一个单纯的界面可以使用这个类,需要某些操作，直接覆盖这些方法即可
 */
public abstract class BaseSampleActivity extends BaseActivity {
    @Override
    protected PermissionListener getPermissonCallBack() {
        return null;
    }


    @Override
    public void initView() {

    }

    @Override
    public void addListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void refreshUi() {

    }

    @Override
    public void release() {

    }
}
