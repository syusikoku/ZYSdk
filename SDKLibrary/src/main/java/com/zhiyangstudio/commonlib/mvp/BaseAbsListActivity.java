package com.zhiyangstudio.commonlib.mvp;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.zhiyangstudio.commonlib.CommonConst;
import com.zhiyangstudio.commonlib.R;
import com.zhiyangstudio.commonlib.adapter.BaseListAdapter;
import com.zhiyangstudio.commonlib.mvp.inter.IListDataView;
import com.zhiyangstudio.commonlib.mvp.inter.IView;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;
import com.zhiyangstudio.commonlib.widget.LMRecyclerView;
import com.zhiyangstudio.commonlib.widget.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhiyang on 2018/4/11.
 * 列表activity:loadingpager+RecyclerView+SwipeRefreshLayout
 */

public abstract class BaseAbsListActivity<P extends BasePresenter<V>, V extends IView, T> extends
        BasePresenterActivivty<P, V> implements LMRecyclerView.OnFooterAutoLoadMoreListener,
        IListDataView<T> {

    protected List<T> mListData = new ArrayList<>();
    SwipeRefreshLayout refreshLayout;
    LoadingLayout loadingView;
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
        refreshLayout = findViewById(R.id.base_swiperefrsh);
        loadingView = findViewById(R.id.base_loadinglayout);
        recyclerView = findViewById(R.id.base_recyclerview);

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
            recyclerView.setCanLoadMore(isCanLoadMore());
        }

        mListAdapter = getListAdapter();
        if (mListAdapter != null) {
            recyclerView.addHeaderView(initHeaderView());
            recyclerView.setAdapter(mListAdapter);
            loadDatas();
        }
    }

    protected abstract boolean isCanLoadMore();

    protected abstract BaseListAdapter getListAdapter();

    protected abstract View initHeaderView();

    protected abstract void loadDatas();

    @Override
    public void addListener() {
        if (refreshLayout != null) {
            refreshLayout.setOnRefreshListener(() -> {
                // 下拉刷新
                state = CommonConst.PAGE_STATE.STATE_REFRESH;
                isAutoLoadMore = true;
                page = 0;
                loadDatas();
            });
        }

        if (recyclerView != null) {
            recyclerView.addFooterAutoLoadMoreListener(this);
        }
    }

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
