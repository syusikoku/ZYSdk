package com.zysdk.vulture.clib.refreshsupport.smartrefresh;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.AbsListView;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.adapter.BaseRecyclerAdapter;
import com.zysdk.vulture.clib.glide.GlideUtils;
import com.zysdk.vulture.clib.mvp.BaseMVPSupportFragment;
import com.zysdk.vulture.clib.mvp.inter.ISampleRefreshView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.utils.UiUtils;
import com.zysdk.vulture.clib.widget.recyclerview.LMRecyclerView;
import com.zysdk.vulture.clib.widget.recyclerview.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by zhiyang on 2018/4/10.
 * SmartRefreshLayout + AbsListView+LoadingLayout
 */

public abstract class BaseMVPSRLListFragment<P extends BasePresenter<V>, V extends
        ISampleRefreshView, T> extends BaseMVPSupportFragment<P, V> implements LMRecyclerView
        .OnFooterAutoLoadMoreListener, ISampleRefreshView<T> {

    // 是否是来自其它界面的action
    public boolean isFromOtherAction = false;
    protected int mPage = 1;
    protected List<T> mList = new ArrayList<>();
    protected BaseRecyclerAdapter<T> mAdapter;
    protected int mDataCount;
    protected SmartRefreshLayout refreshLayout;
    protected LoadingLayout mLoadingLayout;
    private ListView mListView;

    @Override
    public int getContentId() {
        return R.layout.layout_base_smart_refresh_list_view;
    }

    @Override
    public void initView() {
        refreshLayout = mRootView.findViewById(R.id.refreshLayout);
        mListView = mRootView.findViewById(R.id.listView);
        mLoadingLayout = mRootView.findViewById(R.id.loading);

        initLoadingView();

        initOtherProperty();
    }

    private void initLoadingView() {
        mLoadingLayout.setEmptyTextColor(getLoadingTipColor());
        mLoadingLayout.setErrorTextColor(getLoadingTipColor());
        mLoadingLayout.setLoadingTextColor(getLoadingTipColor());

        mLoadingLayout.setOnRetryListener(() -> {
            mPage = 1;
            loadRemoteData();
        });
    }

    protected int getLoadingTipColor() {
        return ResourceUtils.getColor(R.color.white);
    }

    protected void initOtherProperty() {

    }

    @Override
    public void addListener() {
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState != RecyclerView.SCROLL_STATE_IDLE) {
                    GlideUtils.pauseLoadPic(view.getContext());
                } else {
                    GlideUtils.reLoadPic(view.getContext());
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
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
        mLoadingLayout.showLoding();
        initPageNumb();
        mAdapter = getListAdapter();
        // TODO: 2018/5/10 可用上面的这个也可以用自己写的这个
        if (mAdapter != null) {
            mListView.setAdapter(mAdapter);
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

    protected abstract BaseRecyclerAdapter<T> getListAdapter();


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
        if (mPage == 1) {
            mAdapter.refresh(mList);
            if (list == null || list.size() == 0) {
                mLoadingLayout.showEmpty();
            } else {
                mLoadingLayout.showContent();
            }
            refreshLayout.finishRefresh();
            refreshLayout.setNoMoreData(false);
        } else {
            mAdapter.loadMore(mList);
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