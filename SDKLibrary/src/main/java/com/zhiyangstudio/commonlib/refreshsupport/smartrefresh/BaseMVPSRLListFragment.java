package com.zhiyangstudio.commonlib.refreshsupport.smartrefresh;

import android.os.Bundle;
import android.widget.ListView;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhiyangstudio.commonlib.R;
import com.zhiyangstudio.commonlib.adapter.BaseRecyclerAdapter;
import com.zhiyangstudio.commonlib.mvp.BaseMVPSupportFragment;
import com.zhiyangstudio.commonlib.mvp.inter.ISampleRefreshView;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;
import com.zhiyangstudio.commonlib.utils.UiUtils;
import com.zhiyangstudio.commonlib.widget.recyclerview.LMRecyclerView;
import com.zhiyangstudio.commonlib.widget.recyclerview.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by zhiyang on 2018/4/10.
 * SmartRefreshLayout + AbsListView
 */

public abstract class BaseMVPSRLListFragment<P extends BasePresenter<V>, V extends
        ISampleRefreshView, T> extends BaseMVPSupportFragment<P, V> implements LMRecyclerView
        .OnFooterAutoLoadMoreListener, ISampleRefreshView<T> {

    protected int mPage = 1;
    protected List<T> mList = new ArrayList<>();
    protected BaseRecyclerAdapter<T> mAdapter;
    protected int mDataCount;
    protected SmartRefreshLayout refreshLayout;
    protected LoadingLayout mLoadingLayout;
    // 是否是来自其它界面的action
    public boolean isFromOtherAction = false;
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

        initOtherProperty();
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

    protected void showLoading() {
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
        }
        mLoadingLayout.showError();
    }

    @Override
    public void showError() {
        if (mPage == 1) {
            refreshLayout.finishRefresh();
        }
        mLoadingLayout.showError();
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