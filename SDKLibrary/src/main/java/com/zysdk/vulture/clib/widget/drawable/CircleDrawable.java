package com.zysdk.vulture.clib.widget.drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.zysdk.vulture.clib.utils.DisplayUtils;

/**
 * 圆形drawable 画的是圆
 *
 * @author zzg
 * @date 2018-12-9
 */
public class CircleDrawable extends Drawable {
    private final Paint paint;
    private final BitmapShader bitmapShader;
    private final int mWidth;
    private float mRadius = DisplayUtils.dip2px(75);

    public CircleDrawable(Bitmap bitmap) {
        bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        paint = new Paint();
        paint.setShader(bitmapShader);
        paint.setAntiAlias(true);
        paint.setDither(true);
        mWidth = Math.min(bitmap.getWidth(), bitmap.getHeight());
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        canvas.drawCircle(mWidth / 2, mWidth / 2, mRadius, paint);
    }

    @Override
    public void setAlpha(int i) {
        paint.setAlpha(i);
    }

    /**
     * 设置圆角的半径
     */
    public void setRadius(float radius) {
        this.mRadius = radius;
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

    /**
     * bitmap真实width
     */
    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    /**
     * bitmap真实height
     */
    @Override
    public int getIntrinsicHeight() {
        return mWidth;
    }
}
