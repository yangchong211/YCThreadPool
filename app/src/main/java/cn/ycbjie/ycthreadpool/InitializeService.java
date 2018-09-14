package cn.ycbjie.ycthreadpool;

import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;


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

    public static void start(Context context) {
        Intent intent = new Intent(context, InitializeService.class);
        intent.setAction(ACTION_INIT);
        context.startService(intent);
    }


    public InitializeService(){
        super("InitializeService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_INIT.equals(action)) {
                initApplication();
            }
        }
    }

    private void initApplication() {
        //处理耗时操作和避免在application做过多初始化工作，比如初始化数据库等等
    }


}
