package com.zysdk.vulture.clib.widget;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import java.util.List;

/**
 * Created by zhiyang on 2018/2/28.
 */

public abstract class BasePopupWindow<T> extends PopupWindow {
    private float mRadio;
    private int mWidth;
    private int mHeight;
    protected LayoutInflater layoutInflater;
    protected View contentView;
    protected Context mContext;
    protected List<T> mList;

    public BasePopupWindow(Context context, int layoutId, float radio) {
        this(context, layoutId, 0, 0, radio);
    }

    public BasePopupWindow(Context context, int layoutId, int width, int height, float radio) {
        this.mContext = context;
        this.mRadio = radio;
        layoutInflater = LayoutInflater.from(context);
        contentView = layoutInflater.inflate(layoutId, null, false);

        setContentView(contentView);
        this.mWidth = width;
        this.mHeight = height;
        calcWidthAndHeight();
        setWidth(mWidth);
        setHeight(mHeight);

        setFocusable(true);
        setTouchable(true);
        setOutsideTouchable(true);
        setBackgroundDrawable(new BitmapDrawable());

        setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    dismiss();
                    return true;
                }
                return false;
            }
        });

        initView();
    }

    public void setData(List<T> list) {
        this.mList = list;
        refreshUI();
    }

    protected abstract void initView();

    protected abstract void refreshUI();

    public void calcWidthAndHeight() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        this.mWidth = metrics.widthPixels;
        this.mHeight = (int) (metrics.heightPixels * mRadio);
    }

}
