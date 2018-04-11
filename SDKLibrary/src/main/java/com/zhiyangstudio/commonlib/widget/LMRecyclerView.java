package com.zhiyangstudio.commonlib.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.zhiyangstudio.commonlib.R;
import com.zhiyangstudio.commonlib.widget.recyclerview.CommonRViewHolder;
import com.zhiyangstudio.commonlib.utils.UiUtils;

/**
 * Created by zhiyang on 2018/4/10.
 * 滑动到底部自动加载更多
 */

public class LMRecyclerView extends RecyclerView {
    private static final int VIEW_TYPE_NOMAL = 0;//nomal
    private static final int VIEW_TYPE_HEADER = 100;//header
    private static final int VIEW_TYPE_FOOTER = 200;//footer
    private OnFooterAutoLoadMoreListener listener;
    //是否允许加载更多
    private boolean isCanLoadMore;
    private final OnScrollListener mOnScrollListener = new OnScrollListener() {
        /**
         * 滑动状态 监听
         * @param recyclerView
         * @param newState
         */
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        /**
         * 滑动监听
         * @param recyclerView
         * @param dx
         * @param dy
         */
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;
                int lastChildPos = linearLayoutManager.findLastVisibleItemPosition();
                int childCount = linearLayoutManager.getItemCount();
                // 最后一个条目
                View lastChildView = linearLayoutManager.getChildAt(linearLayoutManager.getChildCount() - 1);
                int lastChildBottom = lastChildView.getBottom();
                int recyclerBottom = getBottom();
                if (lastChildPos == childCount - 1 && lastChildBottom == recyclerBottom) {
                    if (isCanLoadMore && listener != null) {
                        listener.loadMore();
                    }
                }
            }
        }
    };
    // 头部
    private View mHeaderView;
    private int footerResId;
    //是否可以点击重新加载
    private boolean isReClickLoadMore;
    private BaseAdapter mBaseAdapter;

    public LMRecyclerView(Context context) {
        this(context, null);
    }

    public LMRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LMRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(mOnScrollListener);
    }

    /**
     * 添加headerView
     *
     * @param header
     */
    public void addHeaderView(View header) {
        this.mHeaderView = header;
    }

    /**
     * 移除header
     */
    public void removeHeaderView() {
        this.mHeaderView = null;
    }

    /**
     * 是否允许加载更多
     *
     * @param canLoadMore
     */
    public void setCanLoadMore(boolean canLoadMore) {
        this.isCanLoadMore = canLoadMore;
    }

    public void addFooterAutoLoadMoreListener(OnFooterAutoLoadMoreListener listener) {
        this.listener = listener;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (adapter != null) {
            mBaseAdapter = new BaseAdapter(adapter);
        }
        super.swapAdapter(adapter, true);
    }

    /**
     * 显示footer
     *
     * @param holder
     */
    private void showFooterView(CommonRViewHolder holder) {
        FrameLayout rootView = holder.findView(R.id.root_footer);
        rootView.removeAllViews();

        if (footerResId != 0) {
            View footerView = UiUtils.inflateView(footerResId, rootView);
            rootView.addView(footerView);
            // 只有加载更多失败，才能点击重新加载数据
            rootView.setOnClickListener(v -> {
                if (!isReClickLoadMore)
                    return;

                if (listener != null) {
                    showLoadMore();
                    notifyDataSetChanged();
                    listener.reLoadMore();
                }
            });
        }
    }

    /**
     * 显示底部加载更多
     */
    public void showLoadMore() {
        showFooterStatus(R.layout.item_root_loading_more);
        setIsReClickLoadMore(false);
    }

    public void notifyDataSetChanged() {
        getAdapter().notifyDataSetChanged();
    }

    public void showFooterStatus(int footerResId) {
        this.footerResId = footerResId;
    }

    /**
     * 底部是否可以重新加载更多
     *
     * @param isReClickLoadMore
     */
    public void setIsReClickLoadMore(boolean isReClickLoadMore) {
        this.isReClickLoadMore = isReClickLoadMore;
    }

    /**
     * /加载更多,没有更多的数据了
     */
    public void showNoMoreData() {
        showFooterStatus(R.layout.item_footer_nomore);
        setIsReClickLoadMore(false);
    }

    /**
     * 加载更多失败
     */
    public void showLoadMoreError() {
        showFooterStatus(R.layout.item_footer_load_error);
        setIsReClickLoadMore(false);
    }

    public void notifyItemChanged(int position) {
        getAdapter().notifyItemChanged(position);

    }

    public void notifyItemRemoved(int position) {
        getAdapter().notifyItemRemoved(position);
        getAdapter().notifyItemRangeChanged(position, getAdapter().getItemCount());
    }

    public interface OnFooterAutoLoadMoreListener {
        // 自动加载更多
        void loadMore();

        // 加载出错，点击重新加载
        void reLoadMore();
    }

    /**
     * 内置的adapter支持头和脚布局
     */
    private class BaseAdapter extends Adapter<CommonRViewHolder> {
        private final Adapter mAdapter;

        public BaseAdapter(Adapter adapter) {
            this.mAdapter = adapter;
        }

        @Override
        public CommonRViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == VIEW_TYPE_HEADER) {
                return new CommonRViewHolder(mHeaderView);
            }

            if (viewType == VIEW_TYPE_FOOTER) {
                return new CommonRViewHolder(parent, R.layout.item_root_footer);
            }
            return (CommonRViewHolder) mAdapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(CommonRViewHolder holder, int position) {
            int itemViewType = getItemViewType(position);
            if (itemViewType == VIEW_TYPE_NOMAL) {
                if (mHeaderView != null) {
                    position--;
                }
                mAdapter.onBindViewHolder(holder, position);
            } else if (itemViewType == VIEW_TYPE_FOOTER) {
                showFooterView(holder);
            } else if (itemViewType == VIEW_TYPE_HEADER) {

            }
        }

        @Override
        public int getItemViewType(int position) {
            if (mHeaderView != null && position == 0) {
                return VIEW_TYPE_HEADER;
            } else if (isCanLoadMore && position == getItemCount() - 1) {
                return VIEW_TYPE_FOOTER;
            }
            return VIEW_TYPE_NOMAL;
        }

        @Override
        public int getItemCount() {
            int itemCount = mAdapter.getItemCount();
            if (mHeaderView != null)
                itemCount++;
            if (isCanLoadMore)
                itemCount++;
            return itemCount;
        }
    }
}
