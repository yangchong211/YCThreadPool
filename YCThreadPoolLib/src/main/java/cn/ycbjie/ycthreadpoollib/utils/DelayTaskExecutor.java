package cn.ycbjie.ycthreadpoollib.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * <pre>
 *     @author: yangchong
 *     blog  : www.pedaily.cn
 *     time  : 2017/08/22
 *     desc  : 使用核心线程池启动延迟任务的类
 *     revise:
 * </pre>
 */
@SuppressWarnings("ALL")
public final class DelayTaskExecutor {

    private ScheduledExecutorService dispatcher;

    private static DelayTaskExecutor instance = new DelayTaskExecutor();

    private DelayTaskExecutor() {
        dispatcher = Executors.newScheduledThreadPool(1, new ThreadFactory() {
            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = new Thread(runnable);
                thread.setName("YC_Delay-Task-Dispatcher");
                thread.setPriority(Thread.MAX_PRIORITY);
                return thread;
            }
        });
    }

    static DelayTaskExecutor get() {
        return instance;
    }

    /**
     * 启动
     * @param delay                     延迟执行的时间，注意默认单位是TimeUnit.MILLISECONDS
     * @param pool                      pool线程池
     * @param task                      runnable
     */
    void postDelay(long delay, final ExecutorService pool, final Runnable task) {
        if (delay == 0) {
            //如果时间是0，那么普通开启
            pool.execute(task);
            return;
        }

        //延时操作
        dispatcher.schedule(new Runnable() {
            @Override
            public void run() {
                //在将来的某个时间执行给定的命令。该命令可以在新线程、池线程或调用线程中执行
                pool.execute(task);
            }
        }, delay, TimeUnit.MILLISECONDS);
    }

}
