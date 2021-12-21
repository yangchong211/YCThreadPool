package cn.ycbjie.ycthreadpool;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import cn.ycbjie.ycthreadpoollib.PoolThread;
import cn.ycbjie.ycthreadpoollib.callback.AsyncCallback;
import cn.ycbjie.ycthreadpoollib.callback.ThreadCallback;
import cn.ycbjie.ycthreadpoollib.deliver.AndroidDeliver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int i = 0;
    private PoolThread executor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_0).setOnClickListener(this);
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2_1).setOnClickListener(this);
        findViewById(R.id.tv_2_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_4_2).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
        findViewById(R.id.tv_6).setOnClickListener(this);


        // 计算可使用的最大内存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 取四分之一的可用内存作为缓存
        final int cacheSize = maxMemory / 4;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_0:
                i++;
                InitializeService.start(this,"yc"+i);
                break;
            case R.id.tv_1:
                startThread1();
                break;
            case R.id.tv_2_1:
                startThread2();
                break;
            case R.id.tv_2_2:
                executor.stop();
                break;
            case R.id.tv_3:
                startThread3();
                break;
            case R.id.tv_4:
                startThread4();
                break;
            case R.id.tv_4_2:
                startThread5();
                break;
            case R.id.tv_5:
                startActivity(new Intent(this, ExecutorsTestActivity.class));
                break;
            case R.id.tv_6:
                startActivity(new Intent(this,ThreadActivity.class));
                break;
            default:
                break;
        }
    }

    private void startThread1() {
        PoolThread executor = App.getInstance().getExecutor();
        executor.setName("最简单的线程调用方式");
        executor.setDeliver(new AndroidDeliver());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("PoolThreadMainActivity","最简单的线程调用方式");
            }
        });
    }


    private void startThread2() {
        executor = App.getInstance().getExecutor();
        executor.setName("异步回调");
        executor.setDelay(2,TimeUnit.SECONDS);
        // 启动异步任务
        executor.async(new Callable<Login>(){
            @Override
            public Login call() throws Exception {
                Log.e("PoolThreadAsyncCallback","耗时操作");
                Thread.sleep(5000);
                // 做一些操作
                return null;
            }
        }, new AsyncCallback<Login>() {
            @Override
            public void onSuccess(Login user) {
                Log.e("PoolThreadAsyncCallback","成功");
            }

            @Override
            public void onFailed(Throwable t) {
                Log.e("PoolThreadAsyncCallback","失败");
            }

            @Override
            public void onStart(String threadName) {
                Log.e("PoolThreadAsyncCallback","开始");
            }
        });
    }


    private void startThread3() {
        PoolThread executor = App.getInstance().getExecutor();
        executor.setName("延迟时间执行任务");
        executor.setDelay(2, TimeUnit.SECONDS);
        executor.setDeliver(new AndroidDeliver());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("PoolThreadMainActivity","延迟时间执行任务");
            }
        });
    }


    private void startThread4() {
        PoolThread executor = App.getInstance().getExecutor();
        //设置为当前的任务设置线程名
        executor.setName("延迟时间执行任务");
        //设置当前任务的延迟时间
        executor.setDelay(2, TimeUnit.SECONDS);
        //设置当前任务的线程传递
        executor.setDeliver(new AndroidDeliver());
        executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.d("PoolThreadstartThread4","startThread4---call");
                Thread.sleep(2000);
                String str = "小杨逗比";
                return str;
            }
        });
        executor.setCallback(new ThreadCallback() {
            @Override
            public void onError(String threadName, Throwable t) {
                Log.d("PoolThreadstartThread4","startThread4---onError");
            }

            @Override
            public void onCompleted(String threadName) {
                Log.d("PoolThreadstartThread4","startThread4---onCompleted");
            }

            @Override
            public void onStart(String threadName) {
                Log.d("PoolThreadstartThread4","startThread4---onStart");
            }
        });
    }



    private void startThread5() {
        PoolThread executor = App.getInstance().getExecutor();
        //设置为当前的任务设置线程名
        executor.setName("延迟时间执行任务");
        //设置当前任务的延迟时间
        executor.setDelay(2, TimeUnit.SECONDS);
        //设置当前任务的线程传递
        executor.setDeliver(new AndroidDeliver());
        Future<String> submit = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.d("PoolThreadstartThread5","startThread5---call");
                Thread.sleep(2000);
                String str = "小杨逗比";
                return str;
            }
        });
        try {
            String result = submit.get();
            Log.d("PoolThreadstartThread5","startThread5-----"+result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
