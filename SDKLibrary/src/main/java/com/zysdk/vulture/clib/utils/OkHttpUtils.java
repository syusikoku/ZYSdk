package com.zysdk.vulture.clib.utils;

import com.zysdk.vulture.clib.CommonConst;
import com.zysdk.vulture.clib.net.interceptor.CacheInterceptor;
import com.zysdk.vulture.clib.net.interceptor.DataInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import okhttp3.Cache;
import okhttp3.CookieJar;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by zhi yang 2018/4/8.
 */

public class OkHttpUtils {

    private static boolean isSupportDataInterceptor;
    private static CookieJar mCookieJar;

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
        if (isSupportDataInterceptor) {
            oBuilder.addInterceptor(new DataInterceptor());
        }
        // 设置cookie
        if (mCookieJar != null) {
            oBuilder.cookieJar(mCookieJar);
        }
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
     * 是否支持数据拦截器
     */
    public static void isSupportDataInterceptor(boolean hasSupport) {
        isSupportDataInterceptor = hasSupport;
    }

    /**
     * 设置cookiejar
     */
    public static void getCookieJar(CookieJar cookieJar) {
        mCookieJar = cookieJar;
    }
}
