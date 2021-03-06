package com.zysdk.vulture.clib.mvp.inter;

/**
 * Created by zhiyang on 2018/4/9.
 */

public interface IView {
    /**
     * 显示进度条
     *
     * @param msg
     */
    void showLoading(String msg);

    /**
     * 隐藏进度条
     */
    void hideLoading();

    /**
     * 显示失败
     */
    void showFail(String msg);

    /**
     * 显示错误
     */
    void showError();

    /**
     * 显示没有数据
     */
    void showEmpty();
}
