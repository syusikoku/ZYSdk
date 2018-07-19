package com.zysdk.vulture.clib.widget;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.zysdk.vulture.clib.adapter.BannerAdapter;
import com.zysdk.vulture.clib.bean.BannerData;
import com.zysdk.vulture.clib.utils.LogListener;
import com.zysdk.vulture.clib.utils.LoggerUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * recyclerview实现的轮播图
 *
 * @author zzg
 * @date 2018-7-19
 */
public class BannerRecyclerView extends FrameLayout {
    private LinearLayout linearLayout;
    private int size;
    private GradientDrawable defGD;
    private GradientDrawable selectedGD;
    private List<BannerData> mDatas = new ArrayList<>();
    private BannerAdapter bannerAdapter;
    private int currentIndex;
    private boolean isPlaying;

    private static Handler mH = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            return false;
        }
    });
    private RecyclerView recyclerView;
    private int startX;
    private int startY;

    public BannerRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public BannerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int
            defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context);
    }

    private void initAttr(Context context) {
        size = (int) (6 * context.getResources().getDisplayMetrics().density + 0.5f);
        initDots();
        initRV(context);
        initTipArea(context);
    }

    private void initDots() {
        defGD = new GradientDrawable();
        defGD.setSize(size, size);
        defGD.setCornerRadius(size);
        defGD.setColor(0xffffffff);
        selectedGD = new GradientDrawable();
        selectedGD.setSize(size, size);
        selectedGD.setCornerRadius(size);
        selectedGD.setColor(0xff0094ff);
    }

    private void initRV(Context context) {
        recyclerView = new RecyclerView(context);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager
                .HORIZONTAL, false));
        LayoutParams vpLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        MyPagerSnapHelper pagerSnapHelper = new MyPagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(recyclerView);

        bannerAdapter = new BannerAdapter(mDatas);
        recyclerView.setAdapter(bannerAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // TODO: 2018/7/19 找到第一个和最后一个条目
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView
                        .getLayoutManager();

                int firstItem = layoutManager.findFirstVisibleItemPosition();
                int lastItem = layoutManager.findLastVisibleItemPosition();

                if (currentIndex != (firstItem + lastItem) / 2) {
                    // 如果当前的条目不在中间，就将其移动到屏幕中间
                    currentIndex = (firstItem + lastItem) / 2;

                    // TODO: 2018/7/19 处理提示的小圆点
                    changePoint();
                }
            }
        });

        addView(recyclerView, vpLayoutParams);
    }

    private void changePoint() {
        if (linearLayout != null && linearLayout.getChildCount() > 0) {
            for (int i = 0; i < linearLayout.getChildCount(); i++) {
                ((ImageView) linearLayout.getChildAt(i)).setImageDrawable(i == currentIndex %
                        mDatas.size() ? selectedGD : defGD);
            }
        }
    }

    public void setSelectDotColor(int color) {
        selectedGD.setColor(color);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        setPlaying(true);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        setPlaying(false);
    }

    private Runnable playTask = new Runnable() {

        @Override
        public void run() {
            recyclerView.smoothScrollToPosition(++currentIndex);
            changePoint();
            mH.postDelayed(this, 3000);
        }
    };

    private void setPlaying(boolean playing) {
        if (!isPlaying && playing && bannerAdapter != null && bannerAdapter.getItemCount() > 2) {
            mH.postDelayed(playTask, 3000);
            isPlaying = true;
        } else if (isPlaying && !playing) {
            mH.removeCallbacksAndMessages(null);
            isPlaying = false;
        }
    }

    private void initTipArea(Context context) {
        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        LayoutParams linearLayoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setPadding(size * 2, size * 2, size * 2, size * 2);
        linearLayoutParams.gravity = Gravity.BOTTOM;
        addView(linearLayout, linearLayoutParams);
    }

    /**
     * 设置点击事件
     */
    public void setOnItemClick(BannerAdapter.OnItemClickListener onItemClick) {
        bannerAdapter.setOnItemClickListener(onItemClick);
    }

    /**
     * 设置数据并刷新界面
     */
    public void setData(List<BannerData> list) {
        setPlaying(false);

        mDatas.clear();
        mDatas.addAll(list);

        linearLayout.removeAllViews();
        if (this.mDatas.size() > 1) {
            currentIndex = this.mDatas.size() * 10000;
            recyclerView.scrollToPosition(currentIndex);
            ImageView img = null;
            for (int i = 0; i < this.mDatas.size(); i++) {
                img = new ImageView(getContext());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup
                        .LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                lp.leftMargin = size / 2;
                lp.rightMargin = size / 2;
                img.setImageDrawable(i == 0 ? selectedGD : defGD);
                linearLayout.addView(img, lp);
            }
            setPlaying(true);
        } else {
            currentIndex = 0;
        }
        bannerAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = (int) ev.getX();
                startY = (int) ev.getY();
                getParent().requestDisallowInterceptTouchEvent(true);
                setPlaying(false);
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getX();
                int moveY = (int) ev.getY();
                int dX = moveX - startX;
                int dY = moveY - startY;
                getParent().requestDisallowInterceptTouchEvent(2 * Math.abs(dX) > Math.abs(dY));
                setPlaying(false);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                setPlaying(true);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    class MyPagerSnapHelper extends LinearSnapHelper implements LogListener {
        @Override
        public int findTargetSnapPosition(RecyclerView.LayoutManager layoutManager, int
                velocityX, int velocityY) {
            int targetSnapPosition = super.findTargetSnapPosition(layoutManager, velocityX,
                    velocityY);

            LoggerUtils.loge(this, "findTargetSnapPosition velocityX = " + velocityX + " , " +
                    "velocityY = " + velocityY + " , targetSnapPosition = " + targetSnapPosition);

            View currentView = findSnapView(layoutManager);
            if (targetSnapPosition != RecyclerView.NO_POSITION && currentView != null) {
                // 获取当前view在屏幕上的位置
                int currentPosition = layoutManager.getPosition(currentView);
                LinearLayoutManager llm = (LinearLayoutManager) layoutManager;
                int firstItemPos = llm.findFirstVisibleItemPosition();
                int lastItemPos = llm.findLastVisibleItemPosition();

                LoggerUtils.loge(this, "currentPosition = " + currentPosition + " , " +
                        "firstItemPos = " + firstItemPos + " , lastItemPos = " + lastItemPos);
                currentPosition = targetSnapPosition < currentPosition ? lastItemPos :
                        (targetSnapPosition > currentPosition ? firstItemPos : currentPosition);
                LoggerUtils.loge(this, "currentPosition = " + currentPosition);
                targetSnapPosition = targetSnapPosition < currentPosition ? currentPosition - 1 :
                        (targetSnapPosition > currentPosition ? currentPosition + 1 :
                                currentPosition);
                LoggerUtils.loge(this, "targetSnapPosition = " + targetSnapPosition);
            }
            return targetSnapPosition;
        }
    }
}
