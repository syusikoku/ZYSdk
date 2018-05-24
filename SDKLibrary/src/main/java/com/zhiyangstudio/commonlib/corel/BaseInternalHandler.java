package com.zhiyangstudio.commonlib.corel;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Created by zhiyang on 2018/4/6.
 */

public abstract class BaseInternalHandler extends Handler {

    private final WeakReference<Activity> mActReference;

    public BaseInternalHandler(Activity pActivity) {
        mActReference = new WeakReference<>(pActivity);
    }

    @Override
    public void handleMessage(Message msg) {
        Activity lActivity = mActReference.get();
        if (lActivity != null && !lActivity.isFinishing()) {
            processMessage(msg);
        }
    }

    public void destory() {
        Activity lActivity = mActReference.get();
        if (lActivity != null) {
            lActivity = null;
        }
        mActReference.clear();
        removeCallbacksAndMessages(null);
    }

    protected abstract void processMessage(Message pMessage);
}
