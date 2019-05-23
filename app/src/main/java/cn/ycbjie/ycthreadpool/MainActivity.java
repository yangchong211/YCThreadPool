package cn.ycbjie.ycthreadpool;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.ScriptGroup.Future;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import cn.ycbjie.ycthreadpoollib.PoolThread;
import cn.ycbjie.ycthreadpoollib.callback.AsyncCallback;
import cn.ycbjie.ycthreadpoollib.deliver.AndroidDeliver;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tv_0).setOnClickListener(this);
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
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
            case R.id.tv_2:
                startThread2();
                break;
            case R.id.tv_3:
                startThread3();
                break;
            case R.id.tv_4:
                startThread4();
                break;
            case R.id.tv_5:
                startActivity(new Intent(this,TestActivity.class));
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
                Log.e("PoolThread   MainActivity","最简单的线程调用方式");
            }
        });
    }


    private void startThread2() {
        PoolThread executor = App.getInstance().getExecutor();
        executor.setName("异步回调");
        executor.setDelay(2,TimeUnit.MILLISECONDS);
        // 启动异步任务
        executor.async(new Callable<Login>(){
            @Override
            public Login call() throws Exception {
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
        executor.setName("延迟时间执行任务");
        executor.setDelay(2, TimeUnit.SECONDS);
        executor.setDeliver(new AndroidDeliver());
        java.util.concurrent.Future<String> submit = executor.submit(new Callable<String>() {
            @Override
            public String call() throws Exception {
                Log.d("PoolThreadstartThread4","startThread4");
                return null;
            }
        });
//        submit.cancel(true);
        boolean cancelled = submit.isCancelled();
        /*try {
            submit.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
