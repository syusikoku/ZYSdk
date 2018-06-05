package com.zysdk.vulture.clib.net.callback;

import com.zysdk.vulture.clib.CommonConst;
import com.zysdk.vulture.clib.bean.BaseBean;
import com.zysdk.vulture.clib.utils.LogListener;
import com.zysdk.vulture.clib.utils.LoggerUtils;

import io.reactivex.functions.Consumer;

/**
 * Created by zhiyang on 2018/4/11.
 */

public abstract class RxConsumer<T> implements Consumer<BaseBean<T>>, LogListener {
    @Override
    public void accept(BaseBean<T> tBaseBean) throws Exception {
        LoggerUtils.loge(this,"accept");
        if (tBaseBean.errorCode == CommonConst.NET_CONFIG.REQUEST_SUCCESS) {
            onSucess(tBaseBean.data);
        } else {
            onFail(tBaseBean.errorMsg);
        }
    }

    protected abstract void onSucess(T data);

    protected abstract void onFail(String errorMsg);
}
