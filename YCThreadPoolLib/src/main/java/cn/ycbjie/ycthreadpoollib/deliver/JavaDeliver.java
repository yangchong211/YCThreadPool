package cn.ycbjie.ycthreadpoollib.deliver;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  :
 *     desc  : 默认情况下，用于Java平台的交付。
 *     revise:
 * </pre>
 */
public final class JavaDeliver implements Executor {

    private static JavaDeliver instance = new JavaDeliver();

    public static JavaDeliver getInstance() {
        return instance;
    }

    /**
     * 注意增加非空判断
     * @param runnable              runnable
     */
    @Override
    public void execute(@NonNull Runnable runnable) {
        runnable.run();
    }


}
