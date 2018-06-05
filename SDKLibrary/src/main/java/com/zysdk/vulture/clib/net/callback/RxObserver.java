package com.zysdk.vulture.clib.net.callback;

import com.zysdk.vulture.clib.CommonConst;
import com.zysdk.vulture.clib.bean.BaseBean;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.utils.LoggerUtils;

/**
 * Created by zhiyang on 2018/4/17.
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
            LoggerUtils.loge("RxObserver onNext onSucess");
            onSucess(tBaseBean.data);
        } else {
            LoggerUtils.loge("RxObserver onNext onFailure errorcode = " + tBaseBean.errorCode + " , errorMsg = " + tBaseBean.errorMsg);
            // 请求失败
            onFailure(tBaseBean.errorCode, tBaseBean.errorMsg);
        }
    }

    protected abstract void onSucess(T data);

    protected abstract void onFailure(int errorCode, String errorMsg);
}
