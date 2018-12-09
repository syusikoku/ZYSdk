package com.zysdk.vulture.clib.widget.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zysdk.vulture.clib.utils.DisplayUtils;

/**
 * 圆角drawable 画的是圆角矩形
 *
 * @author zzg
 * @date 2018-12-9
 */
public class RoundRectDrawable extends Drawable {
    private final Bitmap mBmp;
    private final Paint paint;
    private final BitmapShader bitmapShader;

    private float radius = DisplayUtils.dip2px(30);
    private RectF rect;

    public RoundRectDrawable(Bitmap bitmap) {
        this.mBmp = bitmap;
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);
        paint.setDither(true);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawRoundRect(rect, radius, radius, paint);
    }

    /**
     * 设置圆角的半径
     * @param radius
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        paint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        // 像窗体一样透明
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public void setBounds(int left, int top, int right, int bottom) {
        rect = new RectF(left, top, right, bottom);
    }


    /**
     * bitmap真实width
     */
    @Override
    public int getIntrinsicWidth() {
        return mBmp.getWidth();
    }

    /**
     * bitmap真实height
     */
    @Override
    public int getIntrinsicHeight() {
        return mBmp.getHeight();
    }
}
