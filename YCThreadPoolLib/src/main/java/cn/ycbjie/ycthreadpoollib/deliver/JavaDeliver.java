package cn.ycbjie.ycthreadpoollib.deliver;

import java.util.concurrent.Executor;


/**
 * <pre>
 *     @author: yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : 默认情况下，用于Java平台的交付。
 *     revise:
 * </pre>
 */
public final class JavaDeliver implements Executor {

    private static JavaDeliver instance = new JavaDeliver();

    public static JavaDeliver getInstance() {
        return instance;
    }

    @Override
    public void execute(Runnable runnable) {
        runnable.run();
    }
}
