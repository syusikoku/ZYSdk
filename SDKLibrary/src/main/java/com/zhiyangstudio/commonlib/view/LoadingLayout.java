package com.zhiyangstudio.commonlib.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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


    public LoadingLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
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
        if (changed) {
            int count = getChildCount();
            for (int i = 0; i < count; i++) {
                mContentView = getChildAt(i);
                if (mContentView instanceof RecyclerView)
                    break;
            }
        }
    }

    //loading
    public void showLoding() {
        mLoadingView.setVisibility(VISIBLE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
        if (mContentView != null)
            mContentView.setVisibility(GONE);
    }

    //error
    public void showError() {
        mErrorView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
        if (mContentView != null)
            mContentView.setVisibility(GONE);
    }

    //empty
    public void showEmpty() {
        mEmptyView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        if (mContentView != null)
            mContentView.setVisibility(GONE);
    }

    //content
    public void showContent() {
        if (mContentView != null)
            mContentView.setVisibility(VISIBLE);
        mLoadingView.setVisibility(GONE);
        mErrorView.setVisibility(GONE);
        mEmptyView.setVisibility(GONE);
    }
}
