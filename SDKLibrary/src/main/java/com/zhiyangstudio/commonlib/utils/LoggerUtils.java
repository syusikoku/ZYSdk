package com.zhiyangstudio.commonlib.utils;

import com.orhanobut.logger.Logger;
import com.zhiyangstudio.commonlib.CommonConst;

/**
 * Created by zhiyang on 2018/3/2.
 */

public class LoggerUtils {
    public static void loge(LogListener listener, String msg) {
        if (listener != null && EmptyUtils.isNotEmpty(listener.getClass().getSimpleName())) {
            Logger.e(listener.getClass().getSimpleName() + " -> " + msg);
        } else {
            loge(msg);
        }
    }

    public static void loge(String msg) {
        Logger.e(CommonConst.LOG_TAG + " -> " + msg);
    }
}
