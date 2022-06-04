package com.yc.easyexecutor;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : 抽象task任务类
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCThreadPool
 * </pre>
 */
public abstract class AbsTaskExecutor {

    /**
     * 核心任务的线程池，执行任务
     *
     * @param runnable 任务
     */
    public abstract void executeOnCore(@NonNull Runnable runnable);

    /**
     * IO 密集型任务的线程池，执行任务
     *
     * @param runnable 任务
     */
    public abstract void executeOnDiskIO(@NonNull Runnable runnable);

    /**
     * CPU 密集型任务的线程池，执行任务
     *
     * @param runnable 任务
     */
    public abstract void executeOnCpu(@NonNull Runnable runnable);

    /**
     * UI主线程共有handler对象，执行任务
     *
     * @param runnable 任务
     */
    public abstract void postToMainThread(@NonNull Runnable runnable);

    /**
     * 获取UI主线程共有handler对象
     *
     * @return handler对象
     */
    public abstract Handler getMainHandler();

    /**
     * 配合HandlerThread使用的handler【handlerThread具有自己的looper】，一般用来执行大量任务，执行任务
     * 一般用于在一个后台线程执行同一种任务，避免线程安全问题。如数据库，文件操作，轮训操作等
     * @param runnable 任务
     */
    public abstract void postIoHandler(@NonNull Runnable runnable);

    /**
     * UI主线程共有handler对象，执行任务
     *
     * @param runnable 任务
     */
    public void executeOnMainThread(@NonNull Runnable runnable) {
        if (isMainThread()) {
            runnable.run();
        } else {
            postToMainThread(runnable);
        }
    }

    /**
     * 判断是否是主线程
     *
     * @return true
     */
    public abstract boolean isMainThread();
}
