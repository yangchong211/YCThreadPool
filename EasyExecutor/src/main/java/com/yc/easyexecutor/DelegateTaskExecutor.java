package com.yc.easyexecutor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.concurrent.Executor;

/**
 * <pre>
 *     @author yangchong
 *     email  : yangchong211@163.com
 *     time   : 2019/5/11
 *     desc   : task代理类，简单xia。具体用这个
 *     revise :
 *     GitHub : https://github.com/yangchong211/YCThreadPool
 * </pre>
 */
public class DelegateTaskExecutor extends AbsTaskExecutor {

    private static volatile DelegateTaskExecutor sInstance;

    @NonNull
    private AbsTaskExecutor mDelegate;

    @NonNull
    private final AbsTaskExecutor mDefaultTaskExecutor;

    @NonNull
    private static final Executor sMainThreadExecutor = new Executor() {
        @Override
        public void execute(Runnable command) {
            if (command != null) {
                getInstance().postToMainThread(command);
            }
        }
    };

    @NonNull
    private static final Executor sIOThreadExecutor = new Executor() {
        @Override
        public void execute(Runnable command) {
            if (command != null) {
                getInstance().executeOnDiskIO(command);
            }
        }
    };

    @NonNull
    private static final Executor sCpuThreadExecutor = new Executor() {
        @Override
        public void execute(Runnable command) {
            if (command != null) {
                getInstance().executeOnCpu(command);
            }
        }
    };

    private DelegateTaskExecutor() {
        mDefaultTaskExecutor = new DefaultTaskExecutor();
        mDelegate = mDefaultTaskExecutor;
    }

    @NonNull
    public static DelegateTaskExecutor getInstance() {
        if (sInstance != null) {
            return sInstance;
        }
        synchronized (DelegateTaskExecutor.class) {
            if (sInstance == null) {
                sInstance = new DelegateTaskExecutor();
            }
        }
        return sInstance;
    }

    public void setDelegate(@Nullable AbsTaskExecutor taskExecutor) {
        mDelegate = taskExecutor == null ? mDefaultTaskExecutor : taskExecutor;
    }

    @Override
    public void executeOnDiskIO(@Nullable Runnable runnable) {
        if (runnable != null) {
            mDelegate.executeOnDiskIO(runnable);
        }
    }

    @Override
    public void executeOnCpu(@NonNull Runnable runnable) {
        if (runnable != null) {
            mDelegate.executeOnCpu(runnable);
        }
    }

    @Override
    public void postToMainThread(@Nullable Runnable runnable) {
        if (runnable != null) {
            mDelegate.postToMainThread(runnable);
        }
    }

    @Override
    public void executeOnMainThread(@NonNull Runnable runnable) {
        super.executeOnMainThread(runnable);
    }

    /**
     * 使用HandlerThread和handler处理消息
     *
     * @return MainThreadExecutor
     */
    @NonNull
    public Executor getMainThreadExecutor() {
        return sMainThreadExecutor;
    }

    /**
     * 获得io密集型线程池，有好多任务其实占用的CPU time非常少，所以使用缓存线程池,基本上来着不拒
     *
     * @return IOThreadPoolExecutor
     */
    @NonNull
    public Executor getIOThreadExecutor() {
        return sIOThreadExecutor;
    }

    /**
     * 获得cpu密集型线程池,因为占据CPU的时间片过多的话会影响性能，所以这里控制了最大并发，防止主线程的时间片减少
     *
     * @return CPUThreadPoolExecutor
     */
    @NonNull
    public Executor getCpuThreadExecutor() {
        return sCpuThreadExecutor;
    }

    /**
     * 判断是否是主线程
     *
     * @return true表示主线成
     */
    @Override
    public boolean isMainThread() {
        return mDelegate.isMainThread();
    }
}
