package cn.ycbjie.ycthreadpool;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/22
 *     desc  : 初始化工作，子线程，处理耗时操作和避免在application做过多初始化工作，比如初始化数据库等等
 *     revise:
 * </pre>
 */
public abstract class IntentService extends Service {

    //子线程中的Looper
    private volatile Looper mServiceLooper;
    //内部持有的一个mServiceHandler对象
    private volatile ServiceHandler mServiceHandler;
    //内部创建的线程名字
    private String mName;
    //服务被异常终止后重新创建调用onStartCommand是否回传Intent
    private boolean mRedelivery;

    /**
     * 内部创建了一个ServiceHandler，然后将传递过来的Intent封装成一个Message，
     * 然后再将Message封装成一个Intent，回调onHandleIntent，其实转换的目的就是
     * 将主线程的Intent切换到子线程中去执行了而已。
     */
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            //处理发送过来的消息,在子线程
            onHandleIntent((Intent)msg.obj);
            //处理完消息之后停止Service
            stopSelf(msg.arg1);
        }
    }

    /**
     * 工作线程的名字
     * @param name                      name
     */
    public IntentService(String name) {
        super();
        mName = name;
    }

    public void setIntentRedelivery(boolean enabled) {
        mRedelivery = enabled;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //创建HandlerThread
        HandlerThread thread = new HandlerThread("IntentService[" + mName + "]");
        //开启线程创建子线程Looper
        thread.start();
        //获取子线程Looper
        mServiceLooper = thread.getLooper();
        //创建子线程Handler
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public void onStart(@Nullable Intent intent, int startId) {
        //创建一个Message
        Message msg = mServiceHandler.obtainMessage();
        //消息标志，作为当前Service的标志
        msg.arg1 = startId;
        //携带Intent
        msg.obj = intent;
        //发送消息，此时将线程切换到子线程
        mServiceHandler.sendMessage(msg);
    }


    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        //调用onStart方法
        onStart(intent, startId);
        //根据mRedelivery的值来确定返回重传Intent的黏性广播还是非黏性广播
        return mRedelivery ? START_REDELIVER_INTENT : START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        //退出Looper
        mServiceLooper.quit();
    }


    @Override
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    /*子类必须实现的抽象方法*/
    @WorkerThread
    protected abstract void onHandleIntent(@Nullable Intent intent);
}

