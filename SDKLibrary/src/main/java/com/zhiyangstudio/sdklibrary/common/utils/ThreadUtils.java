package com.zhiyangstudio.sdklibrary.common.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by example on 2018/2/14.
 */

public class ThreadUtils {
    private static ExecutorService threadPool = Executors.newSingleThreadExecutor();

    /**
     * 执行任务
     *
     * @param runnable
     */
    public static void execute(Runnable runnable) {
        threadPool.execute(runnable);
    }

}
