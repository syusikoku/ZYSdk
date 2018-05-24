package com.zhiyangstudio.commonlib.utils;

/**
 * Created by zhiyang on 2018/4/6.
 */

public class InternalUtils {
    /**
     * 获取指定服务
     */
    public static Object getTargetService(String serviceName) {
        return UiUtils.getContext().getSystemService(serviceName);
    }

}
