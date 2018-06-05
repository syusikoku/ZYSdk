package com.zysdk.vulture.clib.net.callback;

import com.zysdk.vulture.clib.CommonConst;
import com.zysdk.vulture.clib.bean.BaseBean;
import com.zysdk.vulture.clib.utils.LogListener;
import com.zysdk.vulture.clib.utils.LoggerUtils;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.functions.Function;

/**
 * Created by zhiyang on 2018/4/11.
 * 用来处理嵌套请求的操作
 */

public abstract class RxFunction<T, R> implements Function<BaseBean<T>,
        ObservableSource<BaseBean<R>>>, LogListener {
    @Override
    public ObservableSource<BaseBean<R>> apply(BaseBean<T> tBaseBean) throws Exception {
        LoggerUtils.loge(this, "apply tBaseBean.errorCode = " + tBaseBean.errorCode);
        if (tBaseBean.errorCode == CommonConst.NET_CONFIG.REQUEST_SUCCESS) {
            return doOnNextRequest();
        }
        return null;
    }

    protected abstract Observable<BaseBean<R>> doOnNextRequest();
}
