package com.zhiyangstudio.commonlib.mvp.refreshsupport.haorefresh;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.zhiyangstudio.commonlib.R;
import com.zhiyangstudio.commonlib.corel.BaseActivity;
import com.zhiyangstudio.commonlib.mvp.inter.IView;
import com.zhiyangstudio.commonlib.utils.LoggerUtils;
import com.zhiyangstudio.commonlib.utils.UiUtils;
import com.zhiyangstudio.commonlib.widget.recyclerview.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import me.fangx.haorefresh.HaoRecyclerView;

/**
 * Created by zhiyang on 2018/4/11.
 * 列表activity:loadingpager+RecyclerView+SwipeRefreshLayout
 * 实现的接口的view让用户自行选择，该框架未经过测试可先不使用
 * maxDataCount这个是通过服务器请求获取到的数据的总个数
 */

public abstract class BaseAbsHaoRefreshListActivity<T> extends BaseActivity implements IView {

    protected List<T> mListData = new ArrayList<>();
    protected SwipeRefreshLayout refreshLayout;
    protected LoadingLayout loadingView;
    protected int mPage = 0;
    protected int maxDataCount;
    protected BaseQuickAdapter<T, BaseViewHolder> mAdapter;
    private HaoRecyclerView mHaoRecyclerView;

    @Override
    public int getContentId() {
        return R.layout.layout_base_harorefresh_list;
    }

    @Override
    public void initView() {
        refreshLayout = findViewById(R.id.swiperefresh);
        mHaoRecyclerView = findViewById(R.id.hao_recycleview);

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
            UiUtils.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // 重置
                    mPage = 1;
                    loadData();
                }
            }, 500);
        });
        mHaoRecyclerView.setLoadMoreListener(() -> {
            UiUtils.postDelayed(new Runnable() {
                @Override
                public void run() {
                    int size = mListData.size();
                    mPage++;
                    LoggerUtils.loge("listData.size() = " + size);
                    if (size >= maxDataCount) {
                        mHaoRecyclerView.loadMoreEnd();
                        loadingView.showContent();
                        return;
                    }

                    loadMore();
                }
            }, 800);
        });
    }

    protected abstract void loadData();

    protected abstract void loadMore();

    @Override
    public void initData() {
        mAdapter = getListAdapter(mListData);
        if (mAdapter != null) {
            mHaoRecyclerView.setAdapter(mAdapter);
        }
    }

    protected abstract BaseQuickAdapter<T, BaseViewHolder> getListAdapter(List<T> listData);

    @Override
    public void showLoading(String msg) {
        loadingView.showLoding();
    }

    @Override
    public void hideLoading() {
        loadingView.showContent();
    }

    @Override
    public void showError() {
        loadingView.showError();
    }

    @Override
    public void showEmpty() {
        loadingView.showEmpty();
    }
}
