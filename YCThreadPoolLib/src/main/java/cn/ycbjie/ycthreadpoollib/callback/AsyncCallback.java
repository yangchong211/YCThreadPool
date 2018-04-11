package cn.ycbjie.ycthreadpoollib.callback;


/**
 * <pre>
 *     @author      杨充
 *     blog         https://www.jianshu.com/p/53017c3fc75d
 *     time         2017/08/22
 *     desc         异步callback回调接口
 *     revise
 *     GitHub       https://github.com/yangchong211
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
}
