package cn.ycbjie.ycthreadpoollib.deliver;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  :
 *     revise:
 * </pre>
 */
public final class AndroidDeliver implements Executor {

    private static AndroidDeliver instance = new AndroidDeliver();
    private Handler main = new Handler(Looper.getMainLooper());

    public static AndroidDeliver getInstance() {
        return instance;
    }

    @Override
    public void execute(final Runnable runnable) {
        //返回应用程序的looper，它位于应用程序的主线程中。
        Looper mainLooper = Looper.getMainLooper();
        //如果当前looper就是当前主线程，那么调用run后不再执行下面的语句
        if (Looper.myLooper() == mainLooper) {
            runnable.run();
            return;
        }

        //开启子线程
        main.post(new Runnable() {
            @Override
            public void run() {
                //注意：这里需要增加非空判断
                if(runnable!=null){
                    runnable.run();
                }
            }
        });
    }
}
