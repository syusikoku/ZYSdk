package com.zhiyangstudio.commonlib.refreshsupport.smartrefresh;

import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

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

import java.util.ArrayList;
import java.util.List;

import ezy.ui.layout.LoadingLayout;
import io.reactivex.annotations.NonNull;

/**
 * Created by zhiyang on 2018/4/10.
 * SmartRefreshLayout+RecyclerView
 */

public abstract class BaseMVPSRRListFragment<P extends BasePresenter<V>, V extends
        ISampleRefreshView, T> extends BaseMVPSupportFragment<P, V> implements LMRecyclerView
        .OnFooterAutoLoadMoreListener, ISampleRefreshView<T> {

    protected int mPage = 1;
    protected List<T> mList = new ArrayList<>();
    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    protected int mDataCount;
    protected SmartRefreshLayout refreshLayout;
    protected RecyclerView mRecyclerView;
    protected LoadingLayout mLoadingLayout;

    @Override
    public int getContentId() {
        return R.layout.layout_base_smart_refresh_recycler_view;
    }

    @Override
    public void initView() {
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        mRecyclerView = mRootView.findViewById(R.id.recyclerView);
        mLoadingLayout = mRootView.findViewById(R.id.loading);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL));
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
                        loadRemoteData();
                    }
                }, 300);
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
                            loadRemoteData();
                        }
                    }
                }, 300);
            }
        });
    }

    protected abstract void loadRemoteData();


    @Override
    public void initData() {
        initPageNumb();
        mAdapter = getListAdapter();
        // TODO: 2018/5/10 可用上面的这个也可以用自己写的这个
        if (mAdapter != null) {
            mRecyclerView.setAdapter(mAdapter);
        }
        UiUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadRemoteData();
            }
        }, 500);
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

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showFail(String msg) {
        ToastUtils.showShort(msg);
    }

    @Override
    public void showError() {

    }

    @Override
    public void showEmpty() {

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
        mList.addAll(list);

        mAdapter.setNewData(mList);
        if (mPage == 1) {
            mLoadingLayout.showContent();
            refreshLayout.finishRefresh();
            refreshLayout.setNoMoreData(false);

        } else {
            refreshLayout.finishLoadMore();
        }
    }

    @Override
    protected void initArguments(Bundle bundle) {

    }
}