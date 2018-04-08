package com.zhiyangstudio.commonlib.interceptor;

import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DataInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = chain.proceed(chain.request());
        ResponseBody responseBody = response.body();
        String result = responseBody.string();
        Logger.e("result = " + result);
        MediaType contentType = response.body().contentType();
        /**
         * 解决okhttp 报java.lang.IllegalStateException: closed,
         * java.lang.IllegalStateException: closed，
         * 原因为OkHttp请求回调中response.body().string()只能有效调用一次
         */
        return response.newBuilder().body(ResponseBody.create(contentType, result)).build();
    }
}