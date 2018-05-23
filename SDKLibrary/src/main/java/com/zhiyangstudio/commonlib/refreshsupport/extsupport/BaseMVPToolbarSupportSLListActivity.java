package com.zhiyangstudio.commonlib.refreshsupport.extsupport;

import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import com.zhiyangstudio.commonlib.R;
import com.zhiyangstudio.commonlib.mvp.inter.ISampleRefreshView;
import com.zhiyangstudio.commonlib.mvp.presenter.BasePresenter;
import com.zhiyangstudio.commonlib.refreshsupport.smartrefresh.BaseMVPSRLListActivity;
import com.zhiyangstudio.commonlib.utils.UiUtils;

/**
 * Created by example on 2018/5/23.
 */

public abstract class BaseMVPToolbarSupportSLListActivity<P extends BasePresenter<V>, V extends
        ISampleRefreshView, T> extends BaseMVPSRLListActivity<P, V, T> {

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

    /**
     * 默认支持分割线，灰色，如果不想要返回false即可
     *
     * @return
     */
    protected boolean hasSupportMenuDivider() {
        return true;
    }

    protected abstract String getCurrentTitle();

    protected void setBgColor(@ColorInt int bgColor) {
        containerLayout.setBackgroundColor(bgColor);
    }

    protected void setBgDrawable(int drawable) {
        containerLayout.setBackgroundDrawable(UiUtils.getDrawable(drawable));
    }

    protected void setBgRes(@DrawableRes int resid) {
        containerLayout.setBackgroundResource(resid);
    }

}
