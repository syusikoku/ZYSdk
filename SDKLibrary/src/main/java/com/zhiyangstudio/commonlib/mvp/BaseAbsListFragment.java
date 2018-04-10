package com.zhiyangstudio.commonlib.mvp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.zhiyangstudio.commonlib.CommonConst;
import com.zhiyangstudio.commonlib.R;
import com.zhiyangstudio.commonlib.mvp.inter.IView;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;
import com.zhiyangstudio.commonlib.view.BaseListAdapter;
import com.zhiyangstudio.commonlib.view.LMRecyclerView;
import com.zhiyangstudio.commonlib.view.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiyang on 2018/4/10.
 */

public abstract class BaseAbsListFragment<P extends BasePresenter<V>, V extends IView, T> extends
        BasePresenterFragment<P, V> implements LMRecyclerView.OnFooterAutoLoadMoreListener {

    protected List<T> mListData = new ArrayList<>();
    SwipeRefreshLayout swipeRefreshLayout;
    LoadingLayout loadingLayout;
    LMRecyclerView recyclerView;
    private int state;
    private boolean isAutoLoadMore;
    private int page;
    private BaseListAdapter mListAdapter;

    @Override
    public int getContentId() {
        return R.layout.layout_base_recycler_list;
    }

    @Override
    public void initView() {
        swipeRefreshLayout = mRootView.findViewById(R.id.base_swiperefrsh);
        loadingLayout = mRootView.findViewById(R.id.base_loadinglayout);
        recyclerView = mRootView.findViewById(R.id.base_recyclerview);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            // 下拉刷新
            state = CommonConst.PAGE_STATE.STATE_REFRESH;
            isAutoLoadMore = true;
            page = 0;
            loadDatas();
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setCanLoadMore(isCanLoadMore());
        recyclerView.addFooterAutoLoadMoreListener(this);
        mListAdapter = getListAdapter();
        if (mListAdapter != null) {
            recyclerView.addHeaderView(initHeaderView());
            recyclerView.setAdapter(mListAdapter);
            loadDatas();
        }
    }

    /**
     * 加载数据
     */
    protected abstract void loadDatas();

    /**
     * 是否能够自动加载更多
     *
     * @return
     */
    protected abstract boolean isCanLoadMore();

    protected abstract BaseListAdapter getListAdapter();

    protected abstract View initHeaderView();

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
