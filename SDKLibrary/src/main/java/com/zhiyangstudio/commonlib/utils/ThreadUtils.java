package com.zhiyangstudio.commonlib.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by zhiyang on 2018/2/14.
 */

public class ThreadUtils {
    private static ExecutorService threadPool = Executors.newSingleThreadExecutor();

    private static ExecutorService fixedPool = Executors.newFixedThreadPool(5);

    private static ExecutorService cachedPool = Executors.newCachedThreadPool();

    /**
     * 执行定时任务
     */
    private static ScheduledExecutorService schedulePool = Executors.newScheduledThreadPool(5);
    private static ExecutorService currentThreadPool;

    /**
     * 执行任务
     *
     * @param runnable
     */
    public static void executeBySingleThread(Runnable runnable) {
        threadPool.execute(runnable);
        currentThreadPool = threadPool;
    }

    /**
     * 执行任务
     *
     * @param runnable
     */
    public static void executeByFixedThread(Runnable runnable) {
        fixedPool.execute(runnable);
        currentThreadPool = fixedPool;
    }

    /**
     * 执行任务
     *
     * @param runnable
     */
    public static void executeByCachedPoolThread(Runnable runnable) {
        cachedPool.execute(runnable);
        currentThreadPool = cachedPool;
    }

    /**
     * 执行定时任务
     *
     * @param command
     * @param initialDelay
     * @param period
     * @param unit
     */
    public static void scheduleAtFixedRate(Runnable command, long initialDelay, long period, TimeUnit unit) {
        schedulePool.scheduleAtFixedRate(command, initialDelay, period, unit);
        currentThreadPool = schedulePool;
    }

    /**
     * 延迟执行定时任务
     *
     * @param command
     * @param initialDelay
     * @param delay
     * @param unit
     */
    public static void scheduleWithFixedDelay(Runnable command, long initialDelay, long delay, TimeUnit unit) {
        schedulePool.scheduleWithFixedDelay(command, initialDelay, delay, unit);
        currentThreadPool = schedulePool;
    }

    /**
     * 停止当前的线程池
     */
    public static void stopCurrentThreadPool() {
        if (currentThreadPool != null && !currentThreadPool.isShutdown()) {
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            currentThreadPool.shutdownNow();
        }
    }

    /**
     * 执行休眠操作
     *
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
