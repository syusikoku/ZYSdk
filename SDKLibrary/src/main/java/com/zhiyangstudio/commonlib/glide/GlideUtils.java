package com.zhiyangstudio.commonlib.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zhiyangstudio.commonlib.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by zhiyang on 2018/4/4.
 * glide 工具类
 */

public class GlideUtils {
    public static void displayCircle(Context context, Bitmap bitmap, ImageView imageView) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();
        GlideCircleTransform circleTransform = new GlideCircleTransform(context);
        GlideApp.with(context).load(bytes).transform(circleTransform).into
                (imageView);
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static void displayCircle(Context context, String str, ImageView imageView) {
        GlideCircleTransform circleTransform = new GlideCircleTransform(context);
        GlideApp.with(context).load(str).transform(circleTransform).into
                (imageView);
    }

    public static void displayCircle(Context context, File file, ImageView imageView) {
        GlideCircleTransform circleTransform = new GlideCircleTransform(context);
        GlideApp.with(context).load(file).transform(circleTransform).into
                (imageView);

    }

    public static void loadPic(Context context, String url, ImageView imageView) {
        loadPic(context, url, 0, 0, imageView, false);
    }

    /**
     * 加载图片
     *
     * @param url           图片url
     * @param displayCircle 是否支持圆角
     */
    public static void loadPic(Context context, String url, int placeImgRes, int errorImgRes, ImageView imageView,
                               boolean displayCircle) {
        GlideRequests glideRequests = GlideApp.with(context);
        if (placeImgRes == 0) {
            placeImgRes = R.drawable.iv_img_default;
        }

        if (errorImgRes == 0) {
            placeImgRes = R.drawable.iv_img_default;
        }
        GlideCircleTransform circleTransform = new GlideCircleTransform(context, displayCircle);
        glideRequests.load(url)
                .centerCrop()
                //禁用磁盘缓存
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                //跳过内存缓存,这里不要跳过内存缓存，会导致列表的时候滑动的时候图片会闪烁
//                .skipMemoryCache(true)
                .dontAnimate()  // 取消加载动画
                .placeholder(placeImgRes)
                .transform(circleTransform)
                .error(errorImgRes)
                .into(imageView);
    }

    public static void loadPic(Context context, String url, ImageView imageView, int placeImgRes, int errorImgRes) {
        loadPic(context, url, placeImgRes, errorImgRes, imageView, false);
    }

    public static void pauseLoadPic(Context context) {
        GlideApp.with(context).pauseRequests();
    }

    public static void reLoadPic(Context context) {
        GlideApp.with(context).resumeRequests();
    }
}
