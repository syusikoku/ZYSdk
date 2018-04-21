package com.zhiyangstudio.commonlib.net.callback;


import com.zhiyangstudio.commonlib.CommonConst;
import com.zhiyangstudio.commonlib.bean.BaseBean;
import com.zhiyangstudio.commonlib.bean.PageListDataBean;
import com.zhiyangstudio.commonlib.mvp.inter.IListDataView;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;
import com.zhiyangstudio.commonlib.utils.LoggerUtils;

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
