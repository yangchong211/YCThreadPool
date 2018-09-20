package cn.ycbjie.ycthreadpoollib.callback;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : 异步callback回调接口
 *     revise:
 * </pre>
 */
public interface AsyncCallback<T> {

    /**
     * 成功时调用
     * @param t         泛型
     */
    void onSuccess(T t);

    /**
     * 异常时调用
     * @param t         异常
     */
    void onFailed(Throwable t);


    /**
     * 通知用户任务开始运行
     * @param threadName            正在运行线程的名字
     */
    void onStart(String threadName);


}
