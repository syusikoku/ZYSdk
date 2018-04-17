package com.zhiyangstudio.commonlib.mvp;

import android.content.Context;
import android.os.Bundle;

import com.zhiyangstudio.commonlib.corel.BaseFragment;
import com.zhiyangstudio.commonlib.mvp.inter.IView;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;

/**
 * Created by zhiyang on 2018/4/10.
 */

public abstract class BasePresenterFragment<P extends BasePresenter<V>, V extends IView> extends
        BaseFragment {
    protected P mPresenter;

    @Override
    protected void initArguments(Bundle bundle) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mPresenter = createPresenter();
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    protected abstract P createPresenter();

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
