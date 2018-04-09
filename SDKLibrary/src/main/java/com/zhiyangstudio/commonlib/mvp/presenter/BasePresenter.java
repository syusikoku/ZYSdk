package com.zhiyangstudio.commonlib.mvp.presenter;

import com.zhiyangstudio.commonlib.mvp.inter.IPresenter;
import com.zhiyangstudio.commonlib.mvp.inter.IView;
import com.zhiyangstudio.commonlib.utils.LogListener;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by example on 2018/4/9.
 */

public class BasePresenter<V extends IView> implements IPresenter<V>, LogListener {
    private V mView;
    private HashMap<String, Disposable> mDisposableMaps;

    /**
     * 绑定
     *
     * @param view
     */
    @Override
    public void attachView(V view) {
        this.mView = view;
    }


    /**
     * 解除绑定
     */
    @Override
    public void detachView() {
        this.mView = null;
    }


    /**
     * 检验是否已绑定此view
     */
    @Override
    public void checkAttachView() {
        if (mView == null) {
            throw new RuntimeException("you have no binding this view");
        }
    }


    /**
     * 获取目标view
     */
    @Override
    public V getView() {
        checkAttachView();
        return mView;
    }

    @Override
    public void addRequestTag(String tag, Disposable disposable) {
        if (mDisposableMaps == null) {
            mDisposableMaps = new HashMap<>();
        }
        mDisposableMaps.put(tag, disposable);
    }

    @Override
    public void cancelRequestTags() {
        if (mDisposableMaps == null)
            return;

        Iterator<Map.Entry<String, Disposable>> iterator = mDisposableMaps.entrySet().iterator();
        Disposable disposable;
        while (iterator.hasNext()) {
            disposable = iterator.next().getValue();
            if (!disposable.isDisposed())
                disposable.dispose();
        }
        mDisposableMaps.clear();
        mDisposableMaps = null;
    }
}
