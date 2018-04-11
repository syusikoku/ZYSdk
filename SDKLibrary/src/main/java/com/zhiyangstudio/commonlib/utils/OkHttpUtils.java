package com.zhiyangstudio.commonlib.utils;

import com.zhiyangstudio.commonlib.CommonConst;
import com.zhiyangstudio.commonlib.net.interceptor.CacheInterceptor;
import com.zhiyangstudio.commonlib.net.interceptor.DataInterceptor;

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
        // 默认的网络请求日志拦截器
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        oBuilder.addInterceptor(loggingInterceptor);
        oBuilder.addInterceptor(new DataInterceptor());
        // 错误重连
        oBuilder.retryOnConnectionFailure(true);

        /**
         * 缓存
         */
        String cacheDirName = CommonConst.NET_CACHE_DIR_NAME;
        if (EmptyUtils.isNotEmpty(cacheDirName)) {
            File cacheDir = new File(UiUtils.getAppInstance().getCacheDir(),
                    cacheDirName);
            if (!cacheDir.exists())
                cacheDir.mkdirs();
            Cache cache = new Cache(cacheDir, 1024 * 1024 * 10);
            oBuilder.cache(cache);
            oBuilder.addInterceptor(new CacheInterceptor());
        }
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
