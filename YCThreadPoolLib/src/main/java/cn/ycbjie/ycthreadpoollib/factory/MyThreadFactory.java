package cn.ycbjie.ycthreadpoollib.factory;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;


/**
 * <pre>
 *     @author: yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : 默认Thread工厂
 *     revise:
 * </pre>
 */
public class MyThreadFactory implements ThreadFactory {

    private int priority;
    public MyThreadFactory(int priority) {
        this.priority = priority;
    }


    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        Thread thread = new Thread(runnable);
        thread.setPriority(priority);
        return thread;
    }

}
