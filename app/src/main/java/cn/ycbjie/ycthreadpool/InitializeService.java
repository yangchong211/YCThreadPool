package cn.ycbjie.ycthreadpool;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/01/22
 *     desc  : 初始化工作，子线程，处理耗时操作和避免在application做过多初始化工作，比如初始化数据库等等
 *     revise:
 * </pre>
 */
@SuppressLint("Registered")
public class InitializeService extends IntentService {

    private static final String ACTION_INIT = "initApplication";

    public static void start(Context context , String name) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        intent.putExtra("name",name);
        context.startService(intent);
    }

    /**
     * 在构造函数中传入线程名字
     **/
    public InitializeService(){
        //调用父类的构造函数
        //构造函数参数=工作线程的名字
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            String name = intent.getStringExtra("name");
            Log.e("IntentService初始化","onHandleIntent----"+name);
            if (ACTION_INIT.equals(action)) {
                initApplication();
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("IntentService初始化","onCreate");
    }

    private void initApplication() {
        //处理耗时操作和避免在application做过多初始化工作，比如初始化数据库等等
        Log.e("IntentService初始化","initApplication");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        Log.e("IntentService初始化","onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("IntentService销毁","onDestroy");
    }
}
