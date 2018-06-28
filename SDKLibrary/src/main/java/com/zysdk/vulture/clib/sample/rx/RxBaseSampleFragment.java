package com.zysdk.vulture.clib.sample.rx;

import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.zysdk.vulture.clib.mvp.inter.IView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.mvp.rx.RxBaseMVPSupportFragment;

public abstract class RxBaseSampleFragment<P extends BasePresenter<V>, V extends IView> extends
        RxBaseMVPSupportFragment<P, V> implements IView {

    @Override
    protected void initArguments(Bundle bundle) {

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
    public void showLoading(String msg) {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showFail(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void showError() {

    }

    @Override
    public void showEmpty() {

    }
}
