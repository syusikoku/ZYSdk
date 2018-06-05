package com.zysdk.vulture.clib.net.interceptor;

import com.zysdk.vulture.clib.utils.LogListener;
import com.zysdk.vulture.clib.utils.LoggerUtils;
import com.zysdk.vulture.clib.utils.NetUtils;
import com.zysdk.vulture.clib.utils.UiUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by zhiyang on 2018/2/6.
 * 缓存拦截器
 */

public class CacheInterceptor implements Interceptor, LogListener {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        LoggerUtils.loge(this, "intercept");
        boolean netAvailable = NetUtils.isNetAvailable(UiUtils.getContext());
        if (netAvailable) {
            LoggerUtils.loge(this, "网络可用，强制从网络获取数据");
            // 网络可用，强制从网络获取数据
            request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
        } else {
            LoggerUtils.loge(this, "网络不用，从缓存获取");
            // 网络不用，从缓存获取
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
        }

        Response response = chain.proceed(request);
        if (netAvailable) {
            /*有网络时，设置缓存超时时间为1个小时*/
            LoggerUtils.loge(this, "有网络时，设置缓存超时时间为1个小时");
            response = response.newBuilder().removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + 60 * 60)
                    .build();
        } else {
            LoggerUtils.loge(this, "无网络时，设置超时为1周");
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    // 无网络时，设置超时为1周
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 7 * 24 * 60 * 60)
                    .build();
        }
        return response;
    }
}
