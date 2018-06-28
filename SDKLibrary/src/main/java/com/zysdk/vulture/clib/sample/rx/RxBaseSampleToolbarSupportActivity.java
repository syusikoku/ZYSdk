package com.zysdk.vulture.clib.sample.rx;

import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zysdk.vulture.clib.R;
import com.zysdk.vulture.clib.corel.BaseInternalHandler;
import com.zysdk.vulture.clib.corel.rx.RxBaseActivity;

import butterknife.ButterKnife;

/**
 * Created by zzg on 2018/5/2.
 */

public abstract class RxBaseSampleToolbarSupportActivity extends RxBaseActivity {

    protected BaseInternalHandler mH = new BaseInternalHandler(this) {
        @Override
        protected void processMessage(Message pMessage) {

        }
    };

    protected Toolbar toolbar;

    private LinearLayout containerLayout;
    private TextView mTvTitle;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        // TODO: 2018/4/10 在这里处理注解无法使用的问题
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mTvTitle = (TextView) findViewById(R.id.tv_title_main);
        containerLayout = (LinearLayout) findViewById(R.id.frameLayout);
        if (initToolBar()) {
            setSupportActionBar(toolbar);
            if (hasShowBackIcon()) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                toolbar.setNavigationOnClickListener(v -> {
                    onNavigationClick();
                });
            }
        } else {
            toolbar.setVisibility(View.GONE);
        }

        if (getContentLayoutId() != 0) {
            View contentView = LayoutInflater.from(mContext).inflate(getContentLayoutId(), containerLayout,
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
    protected boolean hasSupportTransStatusBar() {
        return true;
    }

    @Override
    protected int getStatusbarColor() {
        return R.color.sr_color_primary;
    }

    @Override
    protected void onDestroy() {
        mH.destory();
        super.onDestroy();
    }

    @Override
    public void addListener() {

    }

    @Override
    protected PermissionListener getPermissonCallBack() {
        return null;
    }
}
