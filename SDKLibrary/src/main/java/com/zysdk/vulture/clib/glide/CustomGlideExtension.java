package com.zysdk.vulture.clib.glide;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.annotation.GlideExtension;
import com.bumptech.glide.annotation.GlideOption;
import com.bumptech.glide.annotation.GlideType;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestOptions;

/**
 * 该类主要是用于Glide的扩展属性支持
 * 为了添加新的方法，修改已有的方法或者添加对其他类型格式的支持
 */
@GlideExtension
public class CustomGlideExtension {

    //缩略图的最小尺寸，单位：px
    private static final int MINI_THUMB_SIZE = 30;
    private static final RequestOptions DECODE_TYPE_GIF = GlideOptions.decodeTypeOf(GifDrawable.class).lock();

    /**
     * 将构造方法设为私有，作为工具类使用
     */
    private CustomGlideExtension() {
    }

    /**
     * 1.自己新增的方法的第一个参数必须是RequestOptions options
     * 2.方法必须是静态的
     *
     * //     GlideApp.with(fragment)
     * //     .load(url)
     * //     .miniThumb(thumbnailSize)
     * //     .into(imageView);
     */
    @GlideOption
    public static void miniThumb(RequestOptions options) {
        options.fitCenter()
                .override(MINI_THUMB_SIZE);
    }

    /**
     * //     GlideApp.with(fragment)
     * //     .asGIF()
     * //     .load(url)
     * //     .into(imageView);
     */
    @GlideType(GifDrawable.class)
    public static void asGIF(RequestBuilder<GifDrawable> requestBuilder) {
        requestBuilder
                .transition(new DrawableTransitionOptions())
                .apply(DECODE_TYPE_GIF);
    }
}