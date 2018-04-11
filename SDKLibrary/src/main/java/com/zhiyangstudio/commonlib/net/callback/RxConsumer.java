package com.zhiyangstudio.commonlib.net.callback;

import com.zhiyangstudio.commonlib.CommonConst;
import com.zhiyangstudio.commonlib.bean.BaseBean;
import com.zhiyangstudio.commonlib.utils.LogListener;
import com.zhiyangstudio.commonlib.utils.LoggerUtils;

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
