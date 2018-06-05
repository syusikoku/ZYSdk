package com.zysdk.vulture.clib.corel;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.utils.ResourceUtils;

import butterknife.ButterKnife;

/**
 * Created by zhiyang on 2018/5/4.
 */

public abstract class BaseToolbarSupportActivity extends BaseActivity {

    protected Toolbar toolbar;
    private LinearLayout containerLayout;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // TODO: 2018/4/10 在这里处理注解无法使用的问题
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        containerLayout = (LinearLayout) findViewById(R.id.frameLayout);
        if (initToolBar()) {
            setSupportActionBar(toolbar);
            int toolbarBgColor = getToolbarBgColor();
            if (toolbarBgColor != 0)
                toolbar.setBackgroundColor(toolbarBgColor);
            if (hasShowHome()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(v -> {
                    onNavigationClick();
                });
            }
            if (hasSupportMenuDivider()) {
                toolbar.setPopupTheme(R.style.SampleAppToolbarPopupTheme);
            }
        } else {
            toolbar.setVisibility(View.GONE);
        }

        if (getContentLayoutId() != 0) {
            View contentView = LayoutInflater.from(mContext).inflate(getContentLayoutId(),
                    containerLayout,
                    false);
            containerLayout.addView(contentView);
            // TODO: 2018/4/10 重新绑定view,不重新绑定会无法使用
            unbinder = ButterKnife.bind(this);
        }
    }

    protected boolean initToolBar() {
        return true;
    }

    protected int getToolbarBgColor() {
        return ResourceUtils.getColor(R.color.sr_color_primary);
    }

    protected boolean hasShowHome() {
        return false;
    }

    protected void onNavigationClick() {
        finish();
        release();
    }

    /**
     * 默认支持分割线，灰色，如果不想要返回false即可
     */
    protected boolean hasSupportMenuDivider() {
        return true;
    }

    protected abstract int getContentLayoutId();


    @Override
    public int getContentId() {
        return R.layout.layout_toolbar_support_root;
    }

    @Override
    public void initView() {

    }

    @Override
    public void addListener() {

    }

    @Override
    public void initData() {

    }

    @Override
    public void refreshUi() {

    }

    @Override
    public void release() {

    }

    @Override
    protected PermissionListener getPermissonCallBack() {
        return null;
    }
}
