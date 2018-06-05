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
    protected void onFinishInflate() {
        super.onFinishInflate();
        mLoadingView = mInflater.inflate(R.layout.loading_layout, this, false);
        mLoadTips = mLoadingView.findViewById(R.id.tv_tips);
        mErrorView = mInflater.inflate(R.layout.error_layout, this, false);
        mEmptyView = mInflater.inflate(R.layout.empty_layout, this, false);
        addView(mLoadingView);
        addView(mErrorView);
        addView(mEmptyView);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
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
        hasDismiss = false;
        if (mLoadingView != null && mLoadingView.getVisibility() != VISIBLE)
            mLoadingView.setVisibility(VISIBLE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
        if (mContentView != null && mContentView.getVisibility() != GONE)
            mContentView.setVisibility(GONE);
    }

    public interface OnRetryListner {
        void onRetry();
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
        hasDismiss = true;
        if (mErrorView != null && mErrorView.getVisibility() != VISIBLE) {
            mErrorView.setVisibility(VISIBLE);
            mErrorView.setOnClickListener(v -> {
                if (mRetryListener != null) {
                    mRetryListener.onRetry();
                }
            });
        }
        mLoadingView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
        if (mContentView != null && mContentView.getVisibility() != GONE)
            mContentView.setVisibility(GONE);
    }

    //empty
    public void showEmpty() {
        hasDismiss = true;
        if (mEmptyView != null && mEmptyView.getVisibility() != VISIBLE)
            mEmptyView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        if (mContentView != null && mContentView.getVisibility() != GONE)
            mContentView.setVisibility(GONE);
    }

    //content
    public void showContent() {
        hasDismiss = true;
        if (mContentView != null && mContentView.getVisibility() != VISIBLE)
            mContentView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
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
}
