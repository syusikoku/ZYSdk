package com.zysdk.vulture.clib.refreshsupport.extsupport;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.mvp.inter.ISampleRefreshView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.refreshsupport.smartrefresh.BaseMVPSRRListActivity;
import com.zysdk.vulture.clib.utils.ResourceUtils;
import com.zysdk.vulture.clib.utils.UiUtils;

/**
 * Created by zhiyang on 2018/5/23.
 */

public abstract class BaseMVPToolbarSupportSRListActivity<P extends BasePresenter<V>, V extends
        ISampleRefreshView, T> extends BaseMVPSRRListActivity<P, V, T> {

    protected Toolbar toolbar;
    protected LinearLayout containerLayout;

    @Override
    public int getContentId() {
        return R.layout.layout_toolbar_support_root;
    }

    @Override
    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        containerLayout = (LinearLayout) findViewById(R.id.frameLayout);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getCurrentTitle());
        // TODO: 2018/4/17 toolbar返回键点击时的操作
        toolbar.setNavigationOnClickListener(v -> {
            finish();
        });

        if (hasSupportMenuDivider()) {
            toolbar.setPopupTheme(R.style.SampleAppToolbarPopupTheme);
        }
        View listView = UiUtils.inflateView(R.layout.layout_base_smart_refresh_recycler_view,
                containerLayout);
        containerLayout.addView(listView);
        super.initView();
    }

    protected abstract String getCurrentTitle();

    /**
     * 默认支持分割线，灰色，如果不想要返回false即可
     *
     * @return
     */
    protected boolean hasSupportMenuDivider() {
        return true;
    }

    protected void setBgColor(@ColorInt int bgColor) {
        containerLayout.setBackgroundColor(bgColor);
    }

    protected void setBgDrawable(int drawable) {
        containerLayout.setBackgroundDrawable(ResourceUtils.getDrawable(drawable));
    }

    protected void setBgRes(@DrawableRes int resid) {
        containerLayout.setBackgroundResource(resid);
    }

}
