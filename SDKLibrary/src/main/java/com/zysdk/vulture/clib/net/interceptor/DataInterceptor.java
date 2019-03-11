package com.zysdk.vulture.clib.net.interceptor;

import com.zysdk.vulture.clib.utils.LogListener;
import com.zysdk.vulture.clib.utils.LoggerUtils;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class DataInterceptor implements Interceptor, LogListener {
    @Override
    public Response intercept(Chain chain) throws IOException {
        LoggerUtils.loge(this, "intercept");
        Request request = chain.request();
        String reqUrl = request.url().url().toString();
        LoggerUtils.loge(this, "reqUrl = " + reqUrl);
        Response response = chain.proceed(request);
        ResponseBody responseBody = response.body();
        MediaType contentType = response.body().contentType();
        String result = "";
        result = responseBody.string();
        LoggerUtils.loge(this, "result = " + result);
        /**
         * 解决okhttp 报java.lang.IllegalStateException: closed,
         * java.lang.IllegalStateException: closed，
         * 原因为OkHttp请求回调中response.body().string()只能有效调用一次
         */
        return response.newBuilder().body(ResponseBody.create(contentType, result)).build();
    }
}