package com.zysdk.vulture.clib.widget;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;

/**
 * Created by zhiyang on 2018/4/11.
 */

public class BannerViewPager extends ViewPager {
    private final BannerViewPager mInstance;
    private Handler mH;
    // 是否正在执行
    private boolean mIsRunning;
    private TaskRunnable mTaskRunnable;
    private OnPageChangeListener mOnPageChangeListener = new OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            switch (state) {
                case ViewPager.SCROLL_STATE_IDLE:
                    // 空闲状态
                    start();
                    break;
                case ViewPager.SCROLL_STATE_DRAGGING:
                    // 手指触摸滑动
                    stop();
                    break;
                case ViewPager.SCROLL_STATE_SETTLING:
                    break;
            }
        }
    };

    public BannerViewPager(Context context) {
        this(context, null);
    }

    public BannerViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInstance = this;

        // 滚动监听
        addOnPageChangeListener(mOnPageChangeListener);

        // 设置viewpager的滚动速度
        setViewPagerDuration();
    }

    private void setViewPagerDuration() {
        try {
            Class<?> aClass = Class.forName("android.support.v4.view.ViewPager");
            Field field = aClass.getDeclaredField("mScroller");
            FixedSppedScroll sppedScroll = new FixedSppedScroll(getContext());
            field.setAccessible(true);
            field.set(this, sppedScroll);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        removeOnPageChangeListener(mOnPageChangeListener);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //防止ViewPager可见时第一次切换无动画效果
        //滚动监听
        addOnPageChangeListener(mOnPageChangeListener);
        setFirstLayout(false);
    }

    private void setFirstLayout(boolean isFirstLayout) {
        try {
            Class<?> aClass = Class.forName("android.support.v4.view.ViewPager");
            Field field = aClass.getDeclaredField("mFirstLayout");
            field.setAccessible(true);
            field.setBoolean(this, isFirstLayout);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        stopTimingTask();
    }

    /**
     * 结束任务
     */
    private void stopTimingTask() {
        if (mH != null && mIsRunning) {
            mH.removeCallbacks(mTaskRunnable);
            mIsRunning = false;
            mH = null;
        }
    }

    public void start() {
        startTimingTask();
    }

    /**
     * 开启任务
     */
    private void startTimingTask() {
        if (mH == null && !mIsRunning) {
            mH = new Handler();
            mTaskRunnable = new TaskRunnable(mInstance);
            mH.postDelayed(mTaskRunnable, 6000);
            mIsRunning = true;
        }
    }

    private void setCurrentItem() {
        setCurrentItem(getCurrentItem() + 1, true);
        mH.postDelayed(mTaskRunnable, 6000);
    }

    private class TaskRunnable implements Runnable {

        private final WeakReference<BannerViewPager> weakReference;

        public TaskRunnable(BannerViewPager viewPager) {
            weakReference = new WeakReference<>(viewPager);
        }

        @Override
        public void run() {
            BannerViewPager viewPager = weakReference.get();
            if (viewPager == null)
                return;
            viewPager.setCurrentItem();

        }
    }

    private class FixedSppedScroll extends Scroller {
        private int mDuration = 750;

        public FixedSppedScroll(Context context) {
            super(context);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, mDuration);
        }
    }
}
