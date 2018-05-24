package com.zhiyangstudio.commonlib.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.FieldNamingStrategy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zhi yang 2018/4/8.
 */

public class RetorfitUtils {

    /**
     * 创建api service
     *
     * @param url
     * @param service
     * @param <T>
     * @return
     */
    public static <T> T create(String url, final Class<T> service) {
        return createRetrofit(url).create(service);
    }

    /**
     * 创建retrofit
     *
     * @param url
     * @return
     */
    public static Retrofit createRetrofit(String url) {
        OkHttpClient okHttpClient = OkHttpUtils.getOkHttpClient();

        // 解析json
        Gson gson = getJson();

        //gson = new GsonBuilder().setLenient().create();
        //获取实例
        Retrofit mRetrofit = new Retrofit
                .Builder()
                //设置baseUrl
                .baseUrl(url)
                //添加Gson转换器
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                //设置OKHttpClient,如果不设置会提供一个默认的
                .client(okHttpClient)
                .build();
        return mRetrofit;
    }

    public static Gson getJson() {
        GsonBuilder builder = new GsonBuilder();
        builder.setLenient();
        builder.setFieldNamingStrategy(new AnnotateNaming());
        builder.serializeNulls();
        return builder.create();
    }


    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface ParamNames {
        String value();
    }

    private static class AnnotateNaming implements FieldNamingStrategy {
        @Override
        public String translateName(Field field) {
            ParamNames a = field.getAnnotation(ParamNames.class);
            return a != null ? a.value() : FieldNamingPolicy.IDENTITY.translateName(field);
        }
    }
}
