package com.zysdk.vulture.clib.refreshsupport.lmr;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.zysdk.vulture.clib.CommonConst;
import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.adapter.BaseListAdapter;
import com.zysdk.vulture.clib.mvp.BaseMVPSupportFragment;
import com.zysdk.vulture.clib.mvp.inter.IListDataView;
import com.zysdk.vulture.clib.mvp.inter.IView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.widget.recyclerview.LMRecyclerView;
import com.zysdk.vulture.clib.widget.recyclerview.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiyang on 2018/4/10.
 * 列表fragment:loadingpager+RecyclerView+SwipeRefreshLayout
 */
@Deprecated
public abstract class BaseAbsListFragment<P extends BasePresenter<V>, V extends IView, T> extends
        BaseMVPSupportFragment<P, V> implements LMRecyclerView.OnFooterAutoLoadMoreListener,
        IListDataView<T> {

    public int state;
    protected List<T> mListData = new ArrayList<>();
    protected LMRecyclerView recyclerView;
    protected SwipeRefreshLayout refreshLayout;
    protected LoadingLayout loadingView;
    protected BaseListAdapter mListAdapter;
    protected int page;
    protected boolean isAutoLoadMore;

    @Override
    public int getContentId() {
        return R.layout.layout_base_recycler_list;
    }

    @Override
    public void initView() {
        refreshLayout = mRootView.findViewById(R.id.base_swiperefrsh);
        loadingView = mRootView.findViewById(R.id.base_loadinglayout);
        recyclerView = mRootView.findViewById(R.id.base_recyclerview);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        refreshLayout.setOnRefreshListener(() -> {
            // 下拉刷新
            state = CommonConst.PAGE_STATE.STATE_REFRESH;
            isAutoLoadMore = true;
            page = 0;
            loadDatas();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration
                .VERTICAL));
        recyclerView.setCanLoadMore(isCanLoadMore());
        recyclerView.addFooterAutoLoadMoreListener(this);
        mListAdapter = getListAdapter();
        if (mListAdapter != null) {
            recyclerView.addHeaderView(initHeaderView());
            recyclerView.setAdapter(mListAdapter);
            loadingView.showLoding();
            loadDatas();
        }
    }

    /**
     * 加载数据
     */
    public abstract void loadDatas();

    /**
     * 是否能够自动加载更多
     */
    protected abstract boolean isCanLoadMore();

    protected abstract BaseListAdapter getListAdapter();

    protected abstract View initHeaderView();

    @Override
    public int getPage() {
        return page;
    }

    @Override
    public List<T> getData() {
        return mListData;
    }

    @Override
    public void showContent() {
        loadingView.showContent();
        mListAdapter.notifyAllDatas(mListData, recyclerView);
    }

    /**
     * 自动加载更多
     */
    @Override
    public void autoLoadMore() {
        recyclerView.showLoadMore();
        page++;
        isAutoLoadMore = true;
    }

    /**
     * 当前page=1,为刷新状态
     */
    @Override
    public void refresh() {
        // 清空列表
        mListData.clear();
    }

    /**
     * 没有更多数据了
     */
    @Override
    public void showNoMore() {
        recyclerView.showNoMoreData();
        isAutoLoadMore = false;
    }

    @Override
    public void showLoading(String msg) {
        if (state == CommonConst.PAGE_STATE.STATE_REFRESH) {
            setRefreshing(true);
        }
    }

    private void setRefreshing(boolean isRefreshing) {
        refreshLayout.postDelayed(() -> {
            refreshLayout.setRefreshing(isRefreshing);
        }, 100);
    }

    @Override
    public void hideLoading() {
        setRefreshing(false);
    }

    @Override
    public void showFail(String msg) {
        ToastUtils.showShort(msg);
    }

    /**
     * 加载出错
     */
    @Override
    public void showError() {
        // 如果是加载更多，那么底部就显示点击重新加载
        // 否则，就清空数据，显示没有数据
        isAutoLoadMore = false;
        if (state == CommonConst.PAGE_STATE.STATE_LOAD_MORE) {
            recyclerView.showLoadMoreError();
            mListAdapter.notifyAllDatas(mListData, recyclerView);
        } else {
            loadingView.showError();
        }
    }

    @Override
    public void showEmpty() {
        loadingView.showEmpty();
    }

    /**
     * 滑动到底部自动加载
     */
    @Override
    public void loadMore() {
        if (!isAutoLoadMore) {
            return;
        }
        state = CommonConst.PAGE_STATE.STATE_LOAD_MORE;
        loadDatas();
    }


    /**
     * 点击重新加载
     */
    @Override
    public void reLoadMore() {
        isAutoLoadMore = false;
        loadMore();
    }


}
