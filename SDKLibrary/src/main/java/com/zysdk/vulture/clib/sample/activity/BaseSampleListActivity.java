package com.zysdk.vulture.clib.sample.activity;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.mvp.inter.IView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.refreshsupport.lmr.BaseAbsListActivity;
import com.zysdk.vulture.clib.utils.UiUtils;

/**
 * Created by zzg on 2018/4/21.
 */

public abstract class BaseSampleListActivity<P extends BasePresenter<V>, V extends IView, T> extends
        BaseAbsListActivity<P, V, T> {

    protected Toolbar toolbar;
    protected LinearLayout containerLayout;

    @Override
    public int getContentId() {
        return R.layout.internal_activity_base;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        containerLayout = (LinearLayout) findViewById(R.id.frameLayout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // TODO: 2018/4/17 toolbar返回键点击时的操作
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        View listView = UiUtils.inflateView(R.layout.layout_base_recycler_list, containerLayout);
        containerLayout.addView(listView);
        super.initView();
    }

    @Override
    protected boolean isCanLoadMore() {
        return true;
    }

    @Override
    protected boolean hasSupportTransStatusBar() {
        return true;
    }

    @Override
    protected int getStatusbarColor() {
        return R.color.sr_color_primary;
    }

    @Override
    public void initData() {
        setTitle(getCurrentTitle());
    }

    protected abstract String getCurrentTitle();

    @Override
    public void refreshUi() {

    }

    @Override
    public void release() {

    }
}
