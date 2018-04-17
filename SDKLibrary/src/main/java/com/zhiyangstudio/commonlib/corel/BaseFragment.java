package com.zhiyangstudio.commonlib.corel;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zhiyangstudio.commonlib.inter.ILifecycle;
import com.zhiyangstudio.commonlib.utils.LogListener;
import com.zhiyangstudio.commonlib.utils.LoggerUtils;
import com.zhiyangstudio.commonlib.utils.UiUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * Created by zhiyang on 2018/4/10.
 */

public abstract class BaseFragment extends SupportFragment implements ILifecycle, LogListener {
    protected Context mContext;
    protected Activity mActivity;
    protected LayoutInflater layoutInflater;
    protected View mRootView;
    /*标识视图已经初始化完毕*/
    private boolean isViewPrepared;
    /*标识已经触发过懒加载数据*/
    private boolean hasFetchData;
    private Unbinder unbinder;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LoggerUtils.loge(this, "onCreate");
        Bundle bundle = getArguments();
        if (bundle != null) {
            initArguments(bundle);
        }
    }

    protected abstract void initArguments(Bundle bundle);

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LoggerUtils.loge(this, " onDestroyView");
        /**
         * view被销毁后，将可以重新触发数据懒加载，因为在viewpager下，
         * fragment不会再次新建并走onCreate的生命周期流程，将从onCreateView开始
         */
        hasFetchData = false;
        isViewPrepared = false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LoggerUtils.loge(this, " onDestroy");
        release();
        if (unbinder != null) {
            unbinder.unbind();
        }
        mContext = null;
        mActivity = null;
        mRootView = null;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LoggerUtils.loge(this, " setUserVisibleHint");
        if (isVisibleToUser) {
            lazyFetchDataIfPrepared();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        LoggerUtils.loge(this, "onAttach");
        this.mContext = context;
        this.mActivity = getActivity();
        layoutInflater = LayoutInflater.from(mContext);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LoggerUtils.loge(this, "onCreateView");
        mRootView = null;
        int layoutId = getContentId();
        if (layoutId != 0) {
            mRootView = inflater.inflate(layoutId, container, false);
        }
        UiUtils.removeSelfByParent(mRootView);
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LoggerUtils.loge(this, "onViewCreated");
        initView();
        addListener();
        initData();
        refreshUi();
        isViewPrepared = true;
        lazyFetchDataIfPrepared();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LoggerUtils.loge(this, "------>onDetach");
    }

    private void lazyFetchDataIfPrepared() {
        LoggerUtils.loge(this, "------>lazyFetchDataIfPrepared");
        LoggerUtils.loge(this, "------>isViewPrepared = " + isViewPrepared);
        LoggerUtils.loge(this, "------>hasFetchData = " + hasFetchData);
        /**
         * 用户可见fragment&&视图初始化完毕&&没有加载过数据
         */
        if (getUserVisibleHint() && isViewPrepared && !hasFetchData) {
            hasFetchData = true;
            lazyFetchData();
        }
    }

    /**
     * 懒加载的方式获取数据，仅在满足fragment可见和视图已经准备好的时候调用一次
     */
    protected void lazyFetchData() {
        LoggerUtils.loge(this, "------>lazyFetchData");
    }
}
