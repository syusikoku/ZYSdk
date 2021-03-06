package com.zysdk.vulture.clib.mvp.rx;

import com.blankj.utilcode.util.ToastUtils;
import com.zysdk.vulture.clib.mvp.inter.IView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.corel.rx.RxBaseActivity;
import com.zysdk.vulture.clib.widget.dialog.LoadingDialog;

/**
 * Created by zhiyang on 2018/4/9.
 * mvp模式支持的activity
 */

public abstract class RxBaseMVPSupportActivivty<P extends BasePresenter<V>, V extends IView> extends
        RxBaseActivity implements IView {

    protected P mPresenter;
    protected PermissionListener mPermissionListener = new PermissionListener() {
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
        LoadingDialog.show(this, msg, true, null);
    }

    @Override
    public void hideLoading() {
        LoadingDialog.hideDialog();
    }

    @Override
    public void showFail(String msg) {
        LoadingDialog.hideDialog();
        ToastUtils.showShort(msg);
    }

    @Override
    public void showError() {
        LoadingDialog.hideDialog();
    }

    @Override
    public void showEmpty() {
        LoadingDialog.hideDialog();
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
