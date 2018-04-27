package com.zhiyangstudio.commonlib.mvp;

import com.blankj.utilcode.util.ToastUtils;
import com.zhiyangstudio.commonlib.corel.BaseActivity;
import com.zhiyangstudio.commonlib.mvp.inter.IView;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;

/**
 * Created by example on 2018/4/9.
 * mvp模式支持的activity
 */

public abstract class BaseMVPSupportActivivty<P extends BasePresenter<V>, V extends IView> extends
        BaseActivity implements IView {

    protected P mPresenter;
    PermissionListener mPermissionListener = new PermissionListener() {
        @Override
        public void onGrant(int code) {
            onPermissonGrant(code);
        }

        @Override
        public void onDeny(int code) {
            onPermissionDeny(code);
        }
    };

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

    protected void onPermissonGrant(int code) {

    }

    protected void onPermissionDeny(int code) {

    }

    @Override
    protected PermissionListener getPermissonCallBack() {
        return mPermissionListener;
    }

    @Override
    public void beforeSubContentInit() {
        mPresenter = createPresenter();
        attachView();
    }

    protected abstract P createPresenter();

    protected void attachView() {
        if (mPresenter != null) {
            mPresenter.attachView((V) this);
        }
    }

    @Override
    public void release() {
        // view置空
        detachView();
        // 取消请求
        cancelRequest();
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
}