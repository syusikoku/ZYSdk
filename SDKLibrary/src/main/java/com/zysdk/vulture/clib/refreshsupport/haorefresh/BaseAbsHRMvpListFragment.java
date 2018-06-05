package com.zysdk.vulture.clib.refreshsupport.haorefresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.mvp.BaseMVPSupportFragment;
import com.zysdk.vulture.clib.mvp.inter.IView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.utils.LoggerUtils;
import com.zysdk.vulture.clib.utils.UiUtils;
import com.zysdk.vulture.clib.widget.recyclerview.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import me.fangx.haorefresh.HaoRecyclerView;

/**
 * Created by zhiyang on 2018/4/10.
 * 列表fragment:loadingpager+SwipeRefreshLayout+HaoRecyclerView
 */

public abstract class BaseAbsHRMvpListFragment<P extends BasePresenter<V>, V extends IView,
        T> extends BaseMVPSupportFragment<P, V> implements IView {

    protected List<T> mListData = new ArrayList<>();
    protected SwipeRefreshLayout refreshLayout;
    protected LoadingLayout loadingView;
    protected int mPage = 0;
    protected int maxDataCount;
    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    protected HaoRecyclerView mHaoRecyclerView;

    @Override
    public int getContentId() {
        return R.layout.layout_base_harorefresh_list;
    }

    @Override
    public void initView() {
        refreshLayout = mRootView.findViewById(R.id.swiperefresh);
        mHaoRecyclerView = mRootView.findViewById(R.id.hao_recycleview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mHaoRecyclerView.setLayoutManager(layoutManager);

        mHaoRecyclerView.addItemDecoration(new DividerItemDecoration(mContext,
                DividerItemDecoration.VERTICAL));

        View footerView = UiUtils.inflateView(R.layout.item_root_loading_more);
        mHaoRecyclerView.setFootLoadingView(footerView);

        View noMoreDataView = UiUtils.inflateView(R.layout.item_footer_nomore);
        mHaoRecyclerView.setFootEndView(noMoreDataView);
    }

    @Override
    public void addListener() {
        refreshLayout.setOnRefreshListener(() -> {
            // 重置
            mPage = 1;
            loadData();
        });
        mHaoRecyclerView.setLoadMoreListener(() -> {
            int size = mListData.size();
            mPage++;
            LoggerUtils.loge("listData.size() = " + size);
            if (size >= maxDataCount) {
                mHaoRecyclerView.loadMoreEnd();
                if (loadingView != null) {
                    loadingView.showContent();
                }
                return;
            }
            loadMore();
        });
    }

    protected abstract void loadData();

    protected abstract void loadMore();

    @Override
    public void initData() {
        loadData();
        mAdapter = getListAdapter(mListData);
        if (mAdapter != null) {
            mHaoRecyclerView.setAdapter(mAdapter);
        }
    }

    protected abstract BaseQuickAdapter<T, BaseViewHolder> getListAdapter(List<T> list);

    @Override
    public void showLoading(String msg) {
        if (loadingView != null)
            loadingView.showLoding();
    }

    @Override
    public void hideLoading() {
        if (loadingView != null)
            loadingView.showContent();
    }

    @Override
    public void showError() {
        if (loadingView != null)
            loadingView.showError();
    }

    @Override
    public void showEmpty() {
        if (loadingView != null)
            loadingView.showEmpty();
    }
}
