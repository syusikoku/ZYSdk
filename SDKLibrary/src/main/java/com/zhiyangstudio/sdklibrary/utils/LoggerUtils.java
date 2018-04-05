package com.zhiyangstudio.sdklibrary.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by example on 2018/3/2.
 */

public class LoggerUtils {
    public static void loge(LogListener listener, String msg) {
        Logger.e(listener.getClass().getSimpleName() + " -> " + msg);
    }
}
