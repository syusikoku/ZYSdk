package com.zhiyangstudio.commonlib.mvp;

import com.blankj.utilcode.util.ToastUtils;
import com.zhiyangstudio.commonlib.corel.BaseActivity;
import com.zhiyangstudio.commonlib.mvp.inter.IView;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;

/**
 * Created by example on 2018/4/9.
 */

public abstract class BasePresenterActivivty<P extends BasePresenter<V>, V extends IView> extends
        BaseActivity implements IView {

    protected P mPresenter;

    @Override
    protected void doExtOpts() {
        mPresenter = createPresenter();
        attachView();
    }

    @Override
    protected void onDestroy() {
        // view置空
        detachView();
        // 取消请求
        cancelRequest();
        super.onDestroy();
    }

    private void detachView() {
        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    private void cancelRequest() {
        if (mPresenter != null) {
            mPresenter.cancelRequestTags();
        }
    }

    protected abstract P createPresenter();

    private void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
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