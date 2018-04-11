package cn.ycbjie.ycthreadpoollib.wrapper;


import java.util.concurrent.Callable;

import cn.ycbjie.ycthreadpoollib.CallbackDelegate;
import cn.ycbjie.ycthreadpoollib.ThreadTools;
import cn.ycbjie.ycthreadpoollib.config.ThreadConfigs;

/**
 * <pre>
 *     @author: yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : RunnableWrapper
 *     revise:
 * </pre>
 */
public final class RunnableWrapper implements Runnable {

    private String name;
    private CallbackDelegate delegate;
    private Runnable runnable;
    private Callable callable;

    public RunnableWrapper(ThreadConfigs configs) {
        this.name = configs.name;
        this.delegate = new CallbackDelegate(configs.callback, configs.deliver, configs.asyncCallback);
    }

    public RunnableWrapper setRunnable(Runnable runnable) {
        this.runnable = runnable;
        return this;
    }

    public RunnableWrapper setCallable(Callable callable) {
        this.callable = callable;
        return this;
    }

    @Override
    public void run() {
        Thread current = Thread.currentThread();
        ThreadTools.resetThread(current, name, delegate);
        delegate.onStart(name);
        // avoid NullPointException
        if (runnable != null) {
            runnable.run();
        } else if (callable != null) {
            try {
                Object result = callable.call();
                delegate.onSuccess(result);
            } catch (Exception e) {
                delegate.onError(name, e);
            }
        }
        delegate.onCompleted(name);
    }
}
