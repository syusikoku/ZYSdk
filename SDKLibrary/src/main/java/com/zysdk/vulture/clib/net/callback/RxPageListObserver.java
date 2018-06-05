package com.zysdk.vulture.clib.net.callback;


import com.zysdk.vulture.clib.CommonConst;
import com.zysdk.vulture.clib.bean.BaseBean;
import com.zysdk.vulture.clib.bean.PageListDataBean;
import com.zysdk.vulture.clib.mvp.inter.IListDataView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.utils.LoggerUtils;

import java.util.List;

/**
 * 分页列表加载通用Observer
 * Created by zhiyang on 2018/4/11.
 */

public abstract class RxPageListObserver<T> extends RxBaseObserver<PageListDataBean<T>> {
    private IListDataView<T> mListDataView;

    public RxPageListObserver(BasePresenter presenter) {
        this(presenter, null);
    }

    public RxPageListObserver(BasePresenter presenter, String tag) {
        super(presenter, tag);
        this.mListDataView = (IListDataView<T>) presenter.getView();
    }

    @Override
    public void onNext(BaseBean<PageListDataBean<T>> bean) {
        LoggerUtils.loge(" RxPageListObserver onNext");
        if (bean.errorCode == CommonConst.NET_CONFIG.REQUEST_SUCCESS) {
            PageListDataBean<T> listDataBean = bean.data;
            if (mListDataView.getPage() == 0) {
                mListDataView.refresh();
            }

            if (listDataBean.isOver()) {
                mListDataView.showNoMore();
            } else {
                mListDataView.autoLoadMore();
            }
            onSucess(listDataBean.getDatas());
        } else {
            onFailure(bean.errorCode, bean.errorMsg);
        }
    }

    protected abstract void onSucess(List<T> list);

    protected abstract void onFailure(int errorCode, String errorMsg);

    @Override
    public void onError(Throwable e) {
        super.onError(e);
        LoggerUtils.loge(" RxPageListObserver onError");
        mListDataView.showError();
    }
}
