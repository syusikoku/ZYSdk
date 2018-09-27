package com.zysdk.vulture.clib.sample.activity;

import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.corel.BaseInternalHandler;
import com.zysdk.vulture.clib.mvp.BaseMVPSupportActivivty;
import com.zysdk.vulture.clib.mvp.inter.IView;
import com.zysdk.vulture.clib.mvp.presenter.BasePresenter;
import com.zysdk.vulture.clib.utils.CommonUtils;

import butterknife.ButterKnife;

public abstract class BaseSampleMvpActivity<P extends BasePresenter<V>, V extends IView> extends
        BaseMVPSupportActivivty<P, V> {

    protected BaseInternalHandler mH = new BaseInternalHandler(this) {
        @Override
        protected void processMessage(Message pMessage) {

        }
    };
    protected Toolbar toolbar;

    private LinearLayout containerLayout;
    private TextView mTvTitle;

    @Override
    public void beforeCreate() {
        CommonUtils.requestedOrientation(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        // TODO: 2018/4/10 在这里处理注解无法使用的问题
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvTitle = (TextView) findViewById(R.id.tv_title_main);
        toolbar.setTitle("");

        toolbar.setNavigationIcon(R.drawable.ic_back);

        containerLayout = (LinearLayout) findViewById(R.id.frameLayout);
        if (initToolBar()) {
            setSupportActionBar(toolbar);
            CommonUtils.fixToolbar(toolbar);
            if (hasShowBackIcon()) {
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

    protected boolean hasShowBackIcon() {
        return true;
    }

    protected void onNavigationClick() {
        finish();
        release();
    }

    protected abstract int getContentLayoutId();

    @Override
    public void setTitle(CharSequence title) {
        if (mTvTitle != null) {
            mTvTitle.setText(title);
        }
    }

    @Override
    public int getContentId() {
        return R.layout.internal_activity_base;
    }

    @Override
    public void addListener() {

    }

    @Override
    protected void onDestroy() {
        mH.destory();
//        //必须调用该方法，防止内存泄漏
//        if (mImmersionBar != null) {
//            mImmersionBar.destroy();
//        }
        super.onDestroy();
    }

    @Override
    protected boolean hasSupportTransStatusBar() {
        return true;
    }

    @Override
    protected int getStatusbarColor() {
        return R.color.sr_color_primary;
    }
}
