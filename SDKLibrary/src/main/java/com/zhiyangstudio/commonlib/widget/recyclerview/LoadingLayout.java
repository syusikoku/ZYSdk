package com.zhiyangstudio.commonlib.widget.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.zhiyangstudio.commonlib.R;

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
//        if (changed) {
//            int count = getChildCount();
//            for (int i = 0; i < count; i++) {
//                mContentView = getChildAt(i);
//                if (mContentView instanceof RecyclerView)
//                    break;
//            }
//        }
    }

    //loading
    public void showLoding() {
        if (mLoadingView != null && mLoadingView.getVisibility() != VISIBLE)
            mLoadingView.setVisibility(VISIBLE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
        if (mContentView != null && mContentView.getVisibility() != GONE)
            mContentView.setVisibility(GONE);
    }

    //error
    public void showError() {
        if (mErrorView != null && mErrorView.getVisibility() != VISIBLE)
            mErrorView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
        if (mContentView != null && mContentView.getVisibility() != GONE)
            mContentView.setVisibility(GONE);
    }

    //empty
    public void showEmpty() {
        if (mEmptyView != null && mEmptyView.getVisibility() != VISIBLE)
            mEmptyView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        if (mContentView != null && mContentView.getVisibility() != GONE)
            mContentView.setVisibility(GONE);
    }

    //content
    public void showContent() {
        if (mContentView != null && mContentView.getVisibility() != VISIBLE)
            mContentView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
    }
}
