package com.zhiyangstudio.commonlib.net.callback;

import com.zhiyangstudio.commonlib.CommonConst;
import com.zhiyangstudio.commonlib.bean.BaseBean;
import com.zhiyangstudio.commonlib.utils.LogListener;
import com.zhiyangstudio.commonlib.utils.LoggerUtils;

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
