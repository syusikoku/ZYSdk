package com.zhiyangstudio.commonlib.interceptor;

import com.orhanobut.logger.Logger;
import com.zhiyangstudio.commonlib.utils.NetUtils;
import com.zhiyangstudio.commonlib.utils.UiUtils;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by example on 2018/2/6.
 * 缓存拦截器
 */

public class CacheInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        boolean netAvailable = NetUtils.isNetAvailable(UiUtils.getContext());
        if (netAvailable) {
            Logger.e("网络可用，强制从网络获取数据");
            // 网络可用，强制从网络获取数据
            request = request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK).build();
        } else {
            Logger.e("网络不用，从缓存获取");
            // 网络不用，从缓存获取
            request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
        }

        Response response = chain.proceed(request);
        if (netAvailable) {
            /*有网络时，设置缓存超时时间为1个小时*/
            Logger.e("有网络时，设置缓存超时时间为1个小时");
            response = response.newBuilder().removeHeader("Pragma")
                    .header("Cache-Control", "public, max-age=" + 60 * 60)
                    .build();
        } else {
            Logger.e("无网络时，设置超时为1周");
            response = response.newBuilder()
                    .removeHeader("Pragma")
                    // 无网络时，设置超时为1周
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + 7 * 24 * 60 * 60)
                    .build();
        }
        return response;
    }
}
