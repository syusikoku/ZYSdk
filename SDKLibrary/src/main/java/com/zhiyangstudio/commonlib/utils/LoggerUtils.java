package com.zhiyangstudio.commonlib.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by zhiyang on 2018/3/2.
 */

public class LoggerUtils {
    public static void loge(LogListener listener, String msg) {
        Logger.e(listener.getClass().getSimpleName() + " -> " + msg);
    }
}
