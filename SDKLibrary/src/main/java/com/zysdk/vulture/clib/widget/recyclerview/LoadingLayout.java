package com.zysdk.vulture.clib.widget.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.utils.EmptyUtils;
import com.zysdk.vulture.clib.utils.LoggerUtils;

/**
 * Created by zhiyang on 2018/4/10.
 */

public class LoadingLayout extends FrameLayout {
    private Context mContext;
    private LayoutInflater mInflater;
    private View mLoadingView;
    private View mErrorView;
    private View mEmptyView;
    private View mContentView;
    // 是否被关闭
    private boolean hasDismiss;
    private TextView mLoadTips;
    private OnRetryListner mRetryListener;

    public LoadingLayout(@NonNull Context context) {
        this(context, null);
    }

    public LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        LoggerUtils.loge("onMeasure");
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LoggerUtils.loge("onFinishInflate");
        mLoadingView = mInflater.inflate(R.layout.loading_layout, this, false);
        mLoadTips = mLoadingView.findViewById(R.id.tv_tips);
        mErrorView = mInflater.inflate(R.layout.error_layout, this, false);
        mEmptyView = mInflater.inflate(R.layout.empty_layout, this, false);
        addView(mLoadingView, new FrameLayout.LayoutParams(-1, -1));
        addView(mErrorView, new FrameLayout.LayoutParams(-1, -1));
        addView(mEmptyView, new FrameLayout.LayoutParams(-1, -1));
        mLoadingView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LoggerUtils.loge("onLayout");
        if (changed) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                mContentView = getChildAt(i);
                if (mContentView != null && mContentView instanceof RecyclerView)
                    break;
            }
        }
    }

    //loading
    public void showLoding() {
        LoggerUtils.loge("showLoding");
        hasDismiss = false;
        if (mLoadingView != null && mLoadingView.getVisibility() != View.VISIBLE)
            mLoadingView.setVisibility(View.VISIBLE);
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        if (mContentView != null && mContentView.getVisibility() != View.GONE)
            mContentView.setVisibility(View.GONE);
    }

    public void setOnRetryListener(OnRetryListner retryListener) {
        this.mRetryListener = retryListener;
    }

    public void setEmptyTextColor(int color) {
        if (mEmptyView.findViewById(R.id.tv_empty) != null) {
            TextView textView = mEmptyView.findViewById(R.id.tv_empty);
            if (textView != null) {
                textView.setTextColor(color);
            }
        }
    }

    public void setLoadingTextColor(int color) {
        if (mLoadingView.findViewById(R.id.tv_tips) != null) {
            TextView textView = mLoadingView.findViewById(R.id.tv_tips);
            if (textView != null) {
                textView.setTextColor(color);
            }
        }
    }

    public void setErrorTextColor(int color) {
        if (mErrorView.findViewById(R.id.tv_error) != null) {
            TextView textView = mErrorView.findViewById(R.id.tv_error);
            if (textView != null) {
                textView.setTextColor(color);
            }
        }
        if (mErrorView.findViewById(R.id.tv_retry) != null) {
            TextView textView = mErrorView.findViewById(R.id.tv_retry);
            if (textView != null) {
                textView.setTextColor(color);
            }
        }
    }

    //error
    public void showError() {
        LoggerUtils.loge("showError");
        hasDismiss = true;
        if (mErrorView != null && mErrorView.getVisibility() != View.VISIBLE) {
            mErrorView.setVisibility(View.VISIBLE);
            mErrorView.setOnClickListener(v -> {
                if (mRetryListener != null) {
                    mRetryListener.onRetry();
                }
            });
        }
        mLoadingView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
        if (mContentView != null && mContentView.getVisibility() != View.GONE)
            mContentView.setVisibility(View.GONE);
    }

    //empty
    public void showEmpty() {
        LoggerUtils.loge("showEmpty");
        LoggerUtils.loge("mEmptyView w = " + mEmptyView.getMeasuredWidth() + " , h = " +
                mEmptyView.getMeasuredHeight());
        if (mEmptyView.getVisibility() != View.VISIBLE) {
            mEmptyView.setVisibility(View.VISIBLE);
        }
        View tv = mEmptyView.findViewById(R.id.tv_empty);

        LoggerUtils.loge("tv w = " + tv.getMeasuredWidth() + " , h = " +
                tv.getMeasuredHeight());
        hasDismiss = true;
        mLoadingView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mContentView.setVisibility(View.GONE);
    }

    //content
    public void showContent() {
        LoggerUtils.loge("showContent");
        hasDismiss = true;
        if (mContentView != null && mContentView.getVisibility() != View.VISIBLE)
            mContentView.setVisibility(View.VISIBLE);
        mLoadingView.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mEmptyView.setVisibility(View.GONE);
    }

    public void setTips(String str) {
        if (!hasDismiss) {
            if (mLoadTips != null) {
                if (EmptyUtils.isNotEmpty(str)) {
                    mLoadTips.setText(str);
                }
            }
        }
    }

    public interface OnRetryListner {
        void onRetry();
    }
}
