package cn.ycbjie.ycthreadpoollib.config;

import java.util.concurrent.Executor;
import cn.ycbjie.ycthreadpoollib.callback.AsyncCallback;
import cn.ycbjie.ycthreadpoollib.callback.ThreadCallback;


/**
 * <pre>
 *     @author: yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : 存储当前任务的某些配置
 *     revise:
 * </pre>
 */

public final class ThreadConfigs {
    /**
     * thread name
     */
    public String name;
    /**
     * thread callback
     */
    public ThreadCallback callback;
    /**
     * delay time
     */
    public long delay;
    /**
     * thread deliver
     */
    public Executor deliver;
    /**
     * asyncCallback
     */
    public AsyncCallback asyncCallback;
}
