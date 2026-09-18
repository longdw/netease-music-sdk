package com.netease.httpdns.configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private static ScheduledExecutorService THREAD_POOL = Executors.newScheduledThreadPool(10);

    public static void setThreadPool(ScheduledExecutorService threadPool) {
        if (threadPool != null) {
            THREAD_POOL = threadPool;
        }

    }

    public static void submit(Runnable runnable) {
        THREAD_POOL.submit(runnable);
    }

    public static void schedule(Runnable runnable, long delay) {
        THREAD_POOL.schedule(runnable, delay, TimeUnit.MILLISECONDS);
    }
}