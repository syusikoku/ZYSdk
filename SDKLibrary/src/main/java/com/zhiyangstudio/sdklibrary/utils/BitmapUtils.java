package com.zhiyangstudio.sdklibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.support.annotation.NonNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by example on 2018/4/3.
 */

public class BitmapUtils {

    /**
     * 图片中心截取，生成的是希望值大小的正文形图片
     *
     * @param bitmap
     * @param reqSize
     * @return
     */
    public static Bitmap cropCenter(Bitmap bitmap, int reqSize) {
        int h = bitmap.getHeight() / 2;
        int w = bitmap.getWidth() / 2;
        bitmap = Bitmap.createBitmap(bitmap, w - (reqSize / 2), h - (reqSize / 2), reqSize, reqSize);
        return bitmap;
    }

    /**
     * 图片区域截取
     *
     * @param filePath
     * @param reqSize
     * @return
     */
    public static Bitmap cropRegoinDecoder(String filePath, int reqSize) {
        Bitmap bitmap = BitmapFactory.decodeFile(filePath);
        try {
            BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(filePath, true);
            bitmap = getRegionBitmap(reqSize, bitmap, regionDecoder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 图片区域截取
     *
     * @param data
     * @param reqSize
     * @return
     */
    public static Bitmap cropRegoinDecoder(byte[] data, int reqSize) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        try {
            BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(data, 0, data.length, true);
            bitmap = getRegionBitmap(reqSize, bitmap, regionDecoder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 图片区域截取
     *
     * @param is
     * @param reqSize
     * @return
     */
    public static Bitmap cropRegoinDecoder(InputStream is, int reqSize) {
        Bitmap bitmap = BitmapFactory.decodeStream(is);
        try {
            BitmapRegionDecoder regionDecoder = BitmapRegionDecoder.newInstance(is, true);
            bitmap = getRegionBitmap(reqSize, bitmap, regionDecoder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 区域截取图片
     *
     * @param reqSize
     * @param bitmap
     * @param regionDecoder
     * @return
     */
    private static Bitmap getRegionBitmap(int reqSize, Bitmap bitmap, BitmapRegionDecoder regionDecoder) {
        int h = bitmap.getHeight() / 2;
        int w = bitmap.getWidth() / 2;
        Rect react = new Rect();
        int baseLine = reqSize / 2;
        react.left = w - baseLine;
        react.top = h - baseLine;
        react.right = w + baseLine;
        react.bottom = h + baseLine;
        bitmap = regionDecoder.decodeRegion(react, null);
        return bitmap;
    }

    /**
     * 缩放bitmap到希望的宽高
     *
     * @param bitmap
     * @param reqW
     * @param reqH
     * @return
     */
    public static Bitmap scale(Bitmap bitmap, int reqW, int reqH) {
        return Bitmap.createScaledBitmap(bitmap, reqW, reqH, true);
    }

    public static Bitmap decodeFile(String filePath, int reqW, int reqH) {
        BitmapFactory.Options opts = getOptions();
        BitmapFactory.decodeFile(filePath, opts);
        opts.inSampleSize = calcInSampleSize(opts, reqW, reqH);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, opts);
    }

    @NonNull
    private static BitmapFactory.Options getOptions() {
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inJustDecodeBounds = true;
        return opts;
    }

    public static Bitmap decodeByteArray(byte[] data, int reqW, int reqH) {
        BitmapFactory.Options opts = getOptions();
        BitmapFactory.decodeByteArray(data, 0, data.length, opts);
        opts.inSampleSize = calcInSampleSize(opts, reqW, reqH);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, opts);
    }

    public static Bitmap decodeResource(Context context, int resId, int reqW, int
            reqH) {
        BitmapFactory.Options opts = getOptions();
        BitmapFactory.decodeResource(UiUtils.getResources(context), resId, opts);
        opts.inSampleSize = calcInSampleSize(opts, reqW, reqH);
        opts.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(UiUtils.getResources(context), resId, opts);
    }

    public static Bitmap decodeStream(InputStream inputStream, int reqW, int reqH) {
        return decodeStream(inputStream, null, reqW, reqH);
    }

    public static Bitmap decodeStream(InputStream inputStream, Rect rect, int reqW, int reqH) {
        if (rect == null) {
            return BitmapFactory.decodeStream(inputStream);
        } else {
            BitmapFactory.Options opts = getOptions();
            BitmapFactory.decodeStream(inputStream, rect, opts);
            opts.inSampleSize = calcInSampleSize(opts, reqW, reqH);
            opts.inJustDecodeBounds = false;
            return BitmapFactory.decodeStream(inputStream, rect, opts);
        }
    }

    /**
     * 按比例压缩
     *
     * @param opts
     * @param reqW
     * @param reqH
     * @return
     */
    private static int calcInSampleSize(BitmapFactory.Options opts, int reqW, int reqH) {
        int w = opts.outWidth;
        int h = opts.outHeight;
        int ratio = 1;
        while ((h / ratio) > reqW || (w / ratio) > reqH) {
            // ratio *= 2;
            ratio += 2;
        }

        /*while ((outHeight / inSampleSize > 1280.f) || (outWidth / inSampleSize > 960.f)) {
            inSampleSize *= 2;
        }*/
        if (ratio <= 1) {
            ratio = 1;
        }
        return ratio;
    }

    /**
     * 按比例压缩
     *
     * @param opts
     * @param reqW
     * @param reqH
     * @return
     */
    private static int calcInSampleSize2(BitmapFactory.Options opts, int reqW, int reqH) {
        int w = opts.outWidth;
        int h = opts.outHeight;
        // 缩放比:由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        // 1表示不缩放
        int ratio = 1;
        if (w > h && w > reqW) {
            //如果宽度大的话根据宽度固定大小缩放
            ratio = w / reqW;
        } else if (w < h && h > reqH) {
            //如果高度高的话根据宽度固定大小缩放
            ratio = h / reqH;
        }
        if (ratio <= 1) {
            ratio = 1;
        }
        return ratio;
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }

    public static byte[] WeChatBitmapToByteArray(Bitmap bmp, boolean needRecycle) {

        // 首先进行一次大范围的压缩

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, output);
        float zoom = (float) Math.sqrt(32 * 1024 / (float) output.toByteArray().length); //获取缩放比例

        // 设置矩阵数据
        Matrix matrix = new Matrix();
        matrix.setScale(zoom, zoom);

        // 根据矩阵数据进行新bitmap的创建
        Bitmap resultBitmap = Bitmap.createBitmap(bmp, 0, 0, bmp.getWidth(), bmp.getHeight(), matrix, true);

        output.reset();

        resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);

        // 如果进行了上面的压缩后，依旧大于32K，就进行小范围的微调压缩
        while (output.toByteArray().length > 32 * 1024) {
            matrix.setScale(0.9f, 0.9f);//每次缩小 1/10

            resultBitmap = Bitmap.createBitmap(
                    resultBitmap, 0, 0,
                    resultBitmap.getWidth(), resultBitmap.getHeight(), matrix, true);

            output.reset();
            resultBitmap.compress(Bitmap.CompressFormat.JPEG, 100, output);
        }

        return output.toByteArray();
    }

    /**
     * 按质量压缩
     *
     * @param bitmap
     * @param format
     * @param maxSize
     * @return
     */
    public static int calcInSampleSizeQuality(Bitmap bitmap, Bitmap.CompressFormat format, int maxSize) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        // scale
        int options = 100;
        // Store the bitmap into output stream(no compress)
        bitmap.compress(format, options, os);
        // Compress by loop
        while (os.toByteArray().length / 1024 > maxSize) {
            // Clean up os
            os.reset();
            // interval 10
            options -= 10;
        }
        try {
            os.close();
            os = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return options;
    }

    /**
     * 保存图片
     *
     * @param bitmap
     * @param outPath
     * @param maxSize
     */
    public static void compressBitmap(Bitmap bitmap, String outPath, int maxSize) {
        compressBitmap(bitmap, Bitmap.CompressFormat.JPEG, true, outPath, maxSize);
    }

    /**
     * 保存图片
     *
     * @param bitmap
     * @param outPath
     * @param maxSize
     */
    public static void compressBitmap(Bitmap bitmap, Bitmap.CompressFormat compressFormat, boolean
            hasProcessQuality, String outPath, int maxSize) {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        Bitmap.CompressFormat cFormat = compressFormat == null ? Bitmap.CompressFormat.PNG : compressFormat;

        boolean compressSucess = bitmap.compress(
                cFormat,
                hasProcessQuality ? calcInSampleSizeQuality(bitmap, cFormat, maxSize) : 100,
                os);
        if (compressSucess) {
            // bitmap.compress(cFormat, 80, os);
            bitmap.compress(cFormat, calcInSampleSizeQuality(bitmap, cFormat, maxSize), os);
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outPath);
            fos.write(os.toByteArray());
            fos.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
