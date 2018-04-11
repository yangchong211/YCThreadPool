package cn.ycbjie.ycthreadpoollib.wrapper;


import java.util.concurrent.Callable;

import cn.ycbjie.ycthreadpoollib.CallbackDelegate;
import cn.ycbjie.ycthreadpoollib.ThreadTools;
import cn.ycbjie.ycthreadpoollib.callback.ThreadCallback;
import cn.ycbjie.ycthreadpoollib.config.ThreadConfigs;

/**
 * <pre>
 *     @author: yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : CallableWrapper
 *     revise:
 * </pre>
 */
public final class CallableWrapper<T> implements Callable<T> {

    private String name;
    private ThreadCallback callback;
    private Callable<T> proxy;

    public CallableWrapper(ThreadConfigs configs, Callable<T> proxy) {
        this.name = configs.name;
        this.proxy = proxy;
        this.callback = new CallbackDelegate(configs.callback, configs.deliver, configs.asyncCallback);
    }

    @Override
    public T call() throws Exception {
        ThreadTools.resetThread(Thread.currentThread(),name,callback);
        if (callback != null) {
            callback.onStart(name);
        }
        T t = proxy == null ? null : proxy.call();
        if (callback != null)  {
            callback.onCompleted(name);
        }
        return t;
    }
}
