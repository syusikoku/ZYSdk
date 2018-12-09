package com.zysdk.vulture.clib.mvp;

import android.content.Context;

import com.zysdk.vulture.clib.corel.BaseFragment;
import com.zysdk.vulture.clib.mvp.inter.IView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.utils.LoggerUtils;

/**
 * Created by zhiyang on 2018/4/10.
 */

public abstract class BaseMVPSupportFragment<P extends BasePresenter<V>, V extends IView> extends
        BaseFragment {
    protected P mPresenter;

    @Override
    public void onAttach(Context context) {
        LoggerUtils.loge(this, " onAttach");
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
        LoggerUtils.loge(this, " onDetach");
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
