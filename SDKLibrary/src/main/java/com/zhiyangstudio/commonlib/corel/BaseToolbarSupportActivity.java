package com.zhiyangstudio.commonlib.corel;

import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.zhiyangstudio.commonlib.R;

import butterknife.ButterKnife;

/**
 * Created by example on 2018/5/4.
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
            toolbar.setBackgroundResource(getToolbarBgColor());
            if (hasShowHome()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(v -> {
                    onNavigationClick();
                });
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

    protected abstract boolean initToolBar();

    protected abstract int getToolbarBgColor();

    protected boolean hasShowHome() {
        return false;
    }

    protected void onNavigationClick() {
        finish();
        release();
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
