package com.zysdk.vulture.clib.net.callback;

import com.google.gson.JsonParseException;

import com.blankj.utilcode.util.ToastUtils;
import com.zysdk.vulture.clib.CommonConst;
import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.mvp.inter.IView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.utils.LoggerUtils;
import com.zysdk.vulture.clib.utils.UiUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.ProtocolException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zhiyang on 2018/4/23. 因为考虑到一些后台返回的数据不为标准的json的情况所以产生了此类,由用户自己实现onNext操作
 */

public abstract class AbsBaseObserver<T> implements Observer<T> {
    /**
     * 由客户端决定是否要显示错误提示，如果不需要，则不回调onError，仅回调onEmpty(不区分异常)
     * @moidfy by zzg
     * @modify date : 2018-12-17
     */
    private boolean mHasShowNofity;
    protected IView mView;
    private String mTag;
    private BasePresenter mPresenter;

    public AbsBaseObserver(BasePresenter presenter, String tag) {
        this(presenter, tag, false);
    }

    public AbsBaseObserver(BasePresenter presenter, String tag, boolean hasShowNofity) {
        this.mPresenter = presenter;
        mView = mPresenter.getView();
        this.mTag = tag;
        this.mHasShowNofity = hasShowNofity;
    }

    @Override
    public void onSubscribe(Disposable d) {
        LoggerUtils.loge("AbsBaseObserver onSubscribe");
        if (mTag != null) {
            mPresenter.addRequestTag(mTag, d);
        }
    }

    @Override
    public void onError(Throwable e) {
        LoggerUtils.loge("AbsBaseObserver onError msg : " + e.toString() + " , threadId = " + Thread.currentThread().getId());
        UiUtils.runInMainThread(() -> {
            hideLoading();
            if (mHasShowNofity) {
                dealException(e);
                mView.showError();
            } else {
                mView.showEmpty();
            }
        });

    }

    protected void hideLoading() {
        if (mView != null) {
            mView.hideLoading();
        }
    }

    private void dealException(Throwable t) {
        LoggerUtils.loge("AbsBaseObserver dealException");
        if (t instanceof ConnectException || t instanceof UnknownHostException) {
            //连接错误
            onException(CommonConst.NET_CONFIG.CONNECT_ERROR);
        } else if (t instanceof InterruptedException) {
            //连接超时
            onException(CommonConst.NET_CONFIG.CONNECT_TIMEOUT);
        } else if (t instanceof JsonParseException || t instanceof JSONException || t instanceof ParseException) {
            //解析错误
            onException(CommonConst.NET_CONFIG.PARSE_ERROR);
        } else if (t instanceof SocketTimeoutException) {
            //请求超时
            onException(CommonConst.NET_CONFIG.REQUEST_TIMEOUT);
        } else if (t instanceof UnknownError) {
            //未知错误
            onException(CommonConst.NET_CONFIG.UNKNOWN_ERROR);
        } else if (t instanceof ProtocolException) {
            //网络不稳定
            onException(CommonConst.NET_CONFIG.REQUEST_TIMEOUT);
        } else {
            //未知错误
            onException(CommonConst.NET_CONFIG.UNKNOWN_ERROR);
        }
    }

    private void onException(int errorCode) {
        LoggerUtils.loge("AbsBaseObserver onException errorCode = " + errorCode);
        if (mHasShowNofity) {
            switch (errorCode) {
                case CommonConst.NET_CONFIG.CONNECT_ERROR:
                    ToastUtils.showShort(R.string.connect_error);
                    break;
                case CommonConst.NET_CONFIG.CONNECT_TIMEOUT:
                    ToastUtils.showShort(R.string.connect_timeout);
                    break;
                case CommonConst.NET_CONFIG.PARSE_ERROR:
                    break;
                case CommonConst.NET_CONFIG.REQUEST_TIMEOUT:
                    ToastUtils.showShort(R.string.request_timeout);
                    break;
                case CommonConst.NET_CONFIG.UNKNOWN_ERROR:
                    ToastUtils.showShort(R.string.unknown_error);
                    break;
                case CommonConst.NET_CONFIG.NETWORK_INSTABILITY:
                    ToastUtils.showShort(R.string.network_instability);
                    break;
            }
        }
    }

    @Override
    public void onComplete() {
        LoggerUtils.loge("AbsBaseObserver onComplete");
        hideLoading();
    }

    protected void showLoading() {
        mView.showLoading("");
    }

    protected void showLoading(String msg) {
        mView.showLoading(msg);
    }
}
