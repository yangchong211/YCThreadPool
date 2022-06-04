package com.yc.ycthreadpool;

import android.app.Application;
import android.util.Log;

import com.yc.apploglib.config.AppLogConfig;
import com.yc.apploglib.config.AppLogFactory;
import com.yc.toolutils.file.AppFileUtils;
import com.yc.ycthreadpoollib.PoolThread;
import com.yc.ycthreadpoollib.ScheduleTask;

public class App extends Application{


    private static App instance;
    private PoolThread executor;

    public static synchronized App getInstance() {
        if (null == instance) {
            instance = new App();
        }
        return instance;
    }


    public App(){}


    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        //初始化线程池管理器
        initThreadPool();
        ScheduleTask.getInstance().schedule(new Runnable() {
            @Override
            public void run() {
                //做一些耗时任务

            }
        });
        initLog();
    }

    private void initLog() {
        String ycLogPath = AppFileUtils.getCacheFilePath(this, "ycLog");
        AppLogConfig config = new AppLogConfig.Builder()
                //设置日志tag总的标签
                .setLogTag("yc")
                //是否将log日志写到文件
                .isWriteFile(true)
                //是否是debug
                .enableDbgLog(true)
                //设置日志最小级别
                .minLogLevel(Log.VERBOSE)
                //设置输出日志到file文件的路径。前提是将log日志写入到文件设置成true
                .setFilePath(ycLogPath)
                .build();
        //配置
        AppLogFactory.init(config);
    }


    /**
     * 初始化线程池管理器
     */
    private void initThreadPool() {
        // 创建一个独立的实例进行使用
        executor = PoolThread.ThreadBuilder
                .createFixed(5)
                .setPriority(Thread.MAX_PRIORITY)
                .setCallback(new LogCallback())
                .build();
    }

    /**
     * 获取线程池管理器对象，统一的管理器维护所有的线程池
     * @return                      executor对象
     */
    public PoolThread getExecutor(){
        if(executor ==null){
            executor = PoolThread.ThreadBuilder
                    .createFixed(5)
                    .setPriority(Thread.MAX_PRIORITY)
                    .setCallback(new LogCallback())
                    .build();
        }
        return executor;
    }



}
