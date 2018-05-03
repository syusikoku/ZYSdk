package com.zhiyangstudio.commonlib.net.callback;

import com.blankj.utilcode.util.ToastUtils;
import com.google.gson.JsonParseException;
import com.zhiyangstudio.commonlib.CommonConst;
import com.zhiyangstudio.commonlib.R;
import com.zhiyangstudio.commonlib.mvp.inter.IView;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;
import com.zhiyangstudio.commonlib.utils.LoggerUtils;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by zzg on 2018/4/23.
 * 因为考虑到一些后台返回的数据不为标准的json的情况所以产生了此类,由用户自己实现onNext操作
 */

public abstract class AbsBaseObserver<T> implements Observer<T> {

    protected IView mView;
    private String mTag;
    private BasePresenter mPresenter;

    public AbsBaseObserver(BasePresenter presenter, String tag) {
        this.mPresenter = presenter;
        mView = mPresenter.getView();
        this.mTag = tag;
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
        LoggerUtils.loge("AbsBaseObserver onError");
        hideLoading();
        dealException(e);
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
        } else if (t instanceof JsonParseException
                || t instanceof JSONException
                || t instanceof ParseException) {
            //解析错误
            onException(CommonConst.NET_CONFIG.PARSE_ERROR);
        } else if (t instanceof SocketTimeoutException) {
            //请求超时
            onException(CommonConst.NET_CONFIG.REQUEST_TIMEOUT);
        } else if (t instanceof UnknownError) {
            //未知错误
            onException(CommonConst.NET_CONFIG.UNKNOWN_ERROR);
        } else {
            //未知错误
            onException(CommonConst.NET_CONFIG.UNKNOWN_ERROR);
        }
    }

    private void onException(int errorCode) {
        LoggerUtils.loge("AbsBaseObserver onException errorCode = " + errorCode);
        switch (errorCode) {
            case CommonConst.NET_CONFIG.CONNECT_ERROR:
                ToastUtils.showShort(R.string.connect_error);
                break;
            case CommonConst.NET_CONFIG.CONNECT_TIMEOUT:
                ToastUtils.showShort(R.string.connect_timeout);
                break;
            case CommonConst.NET_CONFIG.PARSE_ERROR:
                ToastUtils.showShort(R.string.parse_error);
                break;
            case CommonConst.NET_CONFIG.REQUEST_TIMEOUT:
                ToastUtils.showShort(R.string.request_timeout);
                break;
            case CommonConst.NET_CONFIG.UNKNOWN_ERROR:
                ToastUtils.showShort(R.string.unknown_error);
                break;
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
