package com.zysdk.vulture.clib.mvp.rx;

import android.content.Context;

import com.zysdk.vulture.clib.mvp.inter.IView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.corel.rx.RxBaseFragment;

/**
 * Created by zhiyang on 2018/4/10.
 */

public abstract class RxBaseMVPSupportFragment<P extends BasePresenter<V>, V extends IView> extends
        RxBaseFragment {
    protected P mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (hasSupport()) {
            mPresenter = createPresenter();
            attachView();
        }
    }

    protected boolean hasSupport() {
        return true;
    }

    protected abstract P createPresenter();

    protected void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (mPresenter != null) {
            mPresenter.detachView();
            mPresenter = null;
        }
    }

    @Override
    public void release() {

    }

}
