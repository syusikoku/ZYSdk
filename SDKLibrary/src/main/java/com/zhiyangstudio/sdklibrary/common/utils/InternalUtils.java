package com.zhiyangstudio.sdklibrary.common.utils;

import com.zhiyangstudio.sdklibrary.utils.UiUtils;

/**
 * Created by zzg on 2018/4/6.
 */

public class InternalUtils {
    /**
     * 获取指定服务
     */
    public static Object getTargetService(String serviceName) {
        return UiUtils.getContext().getSystemService(serviceName);
    }

    /**
     * 执行休眠操作
     * @param timeMill
     */
    public static void doSleep(int timeMill) {
        try {
            Thread.sleep(timeMill);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
