package com.zhiyangstudio.commonlib.refreshsupport.smartrefresh;

import android.widget.AbsListView;

import com.blankj.utilcode.util.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhiyangstudio.commonlib.R;
import com.zhiyangstudio.commonlib.adapter.BaseRecyclerAdapter;
import com.zhiyangstudio.commonlib.mvp.BaseMVPSupportActivivty;
import com.zhiyangstudio.commonlib.mvp.inter.ISampleRefreshView;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;
import com.zhiyangstudio.commonlib.utils.UiUtils;
import com.zhiyangstudio.commonlib.widget.recyclerview.LMRecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by zhiyang on 2018/4/11.
 * SmartRefreshLayout+AbsListView
 */

public abstract class BaseMVPSRLListActivity<P extends BasePresenter<V>, V extends
        ISampleRefreshView, T> extends BaseMVPSupportActivivty<P, V> implements LMRecyclerView
        .OnFooterAutoLoadMoreListener, ISampleRefreshView<T> {

    protected int mPage = 1;
    protected List<T> mList = new ArrayList<>();
    protected BaseRecyclerAdapter<T> mAdapter;
    protected int mDataCount;
    protected SmartRefreshLayout refreshLayout;
    protected AbsListView mListView;

    @Override
    public int getContentId() {
        return R.layout.layout_base_smart_refresh_list_view;
    }

    @Override
    public void initView() {
        refreshLayout = findViewById(R.id.refreshLayout);
        mListView = findViewById(R.id.listView);
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
                            loadRemoteData();
                        }
                    }
                }, 50);
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
            mListView.setAdapter(mAdapter);
        }
        UiUtils.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadRemoteData();
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
    public void setData(List<T> list) {
        if (mPage == 1) {
            mList.clear();
        }
        if (list == null || list.size() == 0) {
            if (mPage == 1) {
                // TODO: 2018/5/9 没有数据
            } else {
                // TODO: 2018/5/9 没有更多数据
            }
        }
        mList.addAll(list);

        if (mPage == 1) {
            mAdapter.refresh(mList);
            refreshLayout.finishRefresh();
            refreshLayout.setNoMoreData(false);
        } else {
            mAdapter.loadMore(mList);
            refreshLayout.finishLoadMore();
        }
    }
}