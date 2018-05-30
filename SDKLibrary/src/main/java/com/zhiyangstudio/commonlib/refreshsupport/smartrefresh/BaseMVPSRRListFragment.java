package com.zhiyangstudio.commonlib.refreshsupport.smartrefresh;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhiyangstudio.commonlib.R;
import com.zhiyangstudio.commonlib.mvp.BaseMVPSupportFragment;
import com.zhiyangstudio.commonlib.mvp.inter.ISampleRefreshView;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;
import com.zhiyangstudio.commonlib.utils.UiUtils;
import com.zhiyangstudio.commonlib.widget.recyclerview.LMRecyclerView;
import com.zhiyangstudio.commonlib.widget.recyclerview.LoadingLayout;
import com.zhiyangstudio.commonlib.widget.recyclerview.divider.GridDivider;
import com.zhiyangstudio.commonlib.widget.recyclerview.divider.LinearDivider;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by zhiyang on 2018/4/10.
 * SmartRefreshLayout+RecyclerView+LoadingLayout
 */

public abstract class BaseMVPSRRListFragment<P extends BasePresenter<V>, V extends
        ISampleRefreshView, T> extends BaseMVPSupportFragment<P, V> implements LMRecyclerView
        .OnFooterAutoLoadMoreListener, ISampleRefreshView<T> {

    // 是否是来自其它界面的action
    public boolean isFromOtherAction = false;
    protected int mPage = 1;
    protected List<T> mList = new ArrayList<>();
    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    protected int mDataCount;
    protected SmartRefreshLayout refreshLayout;
    protected RecyclerView mRecyclerView;
    protected LoadingLayout mLoadingLayout;
    protected LinearLayout mExtRoot;
    protected FrameLayout mRootContainer;
    private DividerItemDecoration mDividerItemDecoration;

    @Override
    public int getContentId() {
        return R.layout.layout_base_smart_refresh_recycler_view;
    }

    @Override
    public void initView() {
        mRootContainer = mRootView.findViewById(R.id.fl_root_container);
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        mLoadingLayout = mRootView.findViewById(R.id.loading);
        mExtRoot = mRootView.findViewById(R.id.ll_ext_root);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        RecyclerView.LayoutManager layoutManager = getLayoutManager();
        if (layoutManager != null) {
            mRecyclerView.setLayoutManager(layoutManager);
            if (hasSupportItemDivider()) {
                if (layoutManager instanceof GridLayoutManager) {
                    GridDivider gridDivider = new GridDivider(mContext, getDividerHight(),
                            getDividerColor());
                    mRecyclerView.addItemDecoration(gridDivider);
                } else if (layoutManager instanceof LinearLayoutManager) {
                    LinearDivider itemDecoration = getItemDecoration();
                    if (itemDecoration != null) {
                        mRecyclerView.addItemDecoration(itemDecoration);
                    }
                }
            }
        }
        refreshLayout.setEnabled(hasEnableRereshAndLoadMore());
        initOtherProperty();
    }

    /**
     * 可覆盖重写,重写的时候要调整分割线
     */
    protected RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(mContext, LinearLayoutManager
                .VERTICAL, false);
    }

    /**
     * 是否支持条目分割线
     */
    protected boolean hasSupportItemDivider() {
        return true;
    }

    /**
     * 分割线高度
     */
    protected int getDividerHight() {
        return 1;
    }

    /**
     * 分割线颜色
     */
    protected int getDividerColor() {
        return getResources().getColor(R
                .color.gray);
    }

    protected LinearDivider getItemDecoration() {
        return new LinearDivider(mContext, LinearLayoutManager.VERTICAL,
                getDividerHight(), getDividerColor());
    }

    /**
     * 是否允许加载更多或下拉刷新
     */
    protected boolean hasEnableRereshAndLoadMore() {
        return true;
    }

    protected void initOtherProperty() {

    }

    @Override
    public void addListener() {
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPage = 1;
                        if (isFromOtherAction) {
                            loadExtRemoteData();
                        } else {
                            loadRemoteData();
                        }
                    }
                }, 50);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull final RefreshLayout refreshLayout) {
                refreshLayout.getLayout().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mList.size() >= mDataCount) {
                            ToastUtils.showShort("数据全部加载完毕");
                            refreshLayout.finishLoadMoreWithNoMoreData();//将不会再次触发加载更多事件
                        } else {
                            mPage++;
                            if (isFromOtherAction) {
                                loadExtRemoteData();
                            } else {
                                loadRemoteData();
                            }
                        }
                    }
                }, 50);
            }
        });
    }

    protected void loadExtRemoteData() {

    }

    protected abstract void loadRemoteData();


    @Override
    public void initData() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mLoadingLayout.showLoding();
        initPageNumb();
        mAdapter = getListAdapter();
        // TODO: 2018/5/10 可用上面的这个也可以用自己写的这个
        if (mAdapter != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
        UiUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFromOtherAction) {
                    loadExtRemoteData();
                } else {
                    loadRemoteData();
                }
            }
        }, 50);
    }

    protected abstract void initPageNumb();

    protected abstract BaseQuickAdapter<T, BaseViewHolder> getListAdapter();


    @Override
    public void refreshUi() {

    }

    @Override
    public void loadMore() {

    }

    @Override
    public void reLoadMore() {

    }

    @Override
    public void showLoading(String msg) {
        mLoadingLayout.showLoding();
    }

    @Override
    public void hideLoading() {
        mLoadingLayout.showContent();
    }

    @Override
    public void showFail(String msg) {
        if (mPage == 1) {
            refreshLayout.finishRefresh();
            mLoadingLayout.showError();
        }
    }

    @Override
    public void showError() {
        if (mPage == 1) {
            refreshLayout.finishRefresh();
            mLoadingLayout.showError();
        }
    }

    @Override
    public void showEmpty() {
        UiUtils.runInMainThread(new Runnable() {
            @Override
            public void run() {
                mLoadingLayout.showEmpty();
            }
        });
    }

    protected void showLoading() {
        mLoadingLayout.showLoding();
    }

    @Override
    public void steDataCount(int total) {
        this.mDataCount = total;
    }

    @Override
    public int getPage() {
        return mPage;
    }

    @Override
    public void setData(List<T> list) {
        if (mPage == 1) {
            mList.clear();
        }
        if (list == null || list.size() == 0) {
            if (mPage == 1) {
                // TODO: 2018/5/9 没有数据
                mLoadingLayout.showEmpty();
            } else {
                // TODO: 2018/5/9 没有更多数据
            }
        }

        preProcessData(list);
        mList.addAll(list);
        mAdapter.setNewData(mList);
        if (mPage == 1) {
            if (list == null || list.size() == 0) {
                mLoadingLayout.showEmpty();
            } else {
                mLoadingLayout.showContent();
                if (mRecyclerView.getVisibility() != View.VISIBLE) {
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
            refreshLayout.finishRefresh();
            refreshLayout.setNoMoreData(false);
        } else {
            refreshLayout.finishLoadMore();
        }
    }

    /**
     * 允许用户对数据进行处理，然后 再设置到界面上
     */
    protected void preProcessData(List<T> list) {

    }

    @Override
    protected void initArguments(Bundle bundle) {
    }
}