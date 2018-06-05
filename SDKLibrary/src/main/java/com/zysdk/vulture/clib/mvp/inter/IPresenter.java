package com.zysdk.vulture.clib.mvp.inter;

import io.reactivex.disposables.Disposable;

/**
 * Created by zhiyang on 2018/4/9.
 */

public interface IPresenter<V extends IView> {
    /**
     * bind view
     *
     * @param view
     */
    void attachView(V view);

    /**
     * unbind view
     */
    void detachView();

    /**
     * check view
     */
    void checkAttachView();

    V getView();

    void addRequestTag(String tag, Disposable disposable);

    /**
     * cancel request
     */
    void cancelRequestTags();
}
