package com.zhiyangstudio.commonlib.utils;

import com.zhiyangstudio.commonlib.interceptor.CacheInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by sample 2018/4/8.
 */

public class OkHttpUtils {

    @NonNull
    public static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder oBuilder = new OkHttpClient.Builder();
        oBuilder.connectTimeout(30, TimeUnit.SECONDS);
        oBuilder.readTimeout(30, TimeUnit.SECONDS);
        oBuilder.writeTimeout(30, TimeUnit.SECONDS);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        oBuilder.addInterceptor(loggingInterceptor);
        // 错误重连
        oBuilder.retryOnConnectionFailure(true);

        /**
         * 缓存
         */
        File cacheDir = new File(UiUtils.getAppInstance().getCacheDir(), "response");
        Cache cache = new Cache(cacheDir, 1024 * 1024 * 10);
        oBuilder.cache(cache);
        oBuilder.addInterceptor(new CacheInterceptor());
        return oBuilder.build();
    }

    /**
     //     builder = new OkHttpClient.Builder();
     //
     //     拦截日志，依赖
     //      builder.addInterceptor(InterceptorUtils.getHttpLoggingInterceptor(true));
     //      OkHttpClient build = builder.build();
     //
     //     拦截日志，自定义拦截日志
     //     builder.addInterceptor(new LogInterceptor("YC"));
     //
     //     添加请求头拦截器
     //     builder.addInterceptor(InterceptorUtils.getRequestHeader());
     //
     //     添加统一请求拦截器
     //     builder.addInterceptor(InterceptorUtils.commonParamsInterceptor());
     //
     //     添加缓存拦截器
     //     创建Cache
     //     File httpCacheDirectory = new File("OkHttpCache");
     //     Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
     //     builder.cache(cache);
     //
     //     设置缓存
     //     builder.addNetworkInterceptor(InterceptorUtils.getCacheInterceptor());
     //     builder.addInterceptor(InterceptorUtils.getCacheInterceptor());
     //
     //     添加自定义CookieJar
     //     InterceptorUtils.addCookie(builder);
     */
}
