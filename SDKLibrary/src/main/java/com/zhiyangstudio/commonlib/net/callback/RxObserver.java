package com.zhiyangstudio.commonlib.net.callback;

import com.zhiyangstudio.commonlib.CommonConst;
import com.zhiyangstudio.commonlib.bean.BaseBean;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;

/**
 * Created by example on 2018/4/17.
 * 通用Observer回调
 */

public abstract class RxObserver<T> extends RxBaseObserver<T> {
    public RxObserver(BasePresenter presenter) {
        this(presenter, null);
    }

    public RxObserver(BasePresenter presenter, String tag) {
        super(presenter, tag);
    }

    @Override
    public void onNext(BaseBean<T> tBaseBean) {
        if (tBaseBean.errorCode == CommonConst.NET_CONFIG.REQUEST_SUCCESS) {
            // 请求成功
            onSucess(tBaseBean.data);
        } else {
            // 请求失败
            onFailure(tBaseBean.errorCode, tBaseBean.errorMsg);
        }
    }

    protected abstract void onSucess(T data);

    protected abstract void onFailure(int errorCode, String errorMsg);
}
