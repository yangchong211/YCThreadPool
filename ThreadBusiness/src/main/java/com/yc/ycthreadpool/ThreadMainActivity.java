package com.yc.ycthreadpool;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;

import com.yc.ycthreadpoollib.PoolThread;
import com.yc.ycthreadpoollib.callback.AsyncCallback;
import com.yc.ycthreadpoollib.callback.ThreadCallback;
import com.yc.ycthreadpoollib.deliver.AndroidDeliver;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;


public class ThreadMainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread_main);
        findViewById(R.id.tv_easy).setOnClickListener(this);
        findViewById(R.id.tv_poll).setOnClickListener(this);
        findViewById(R.id.tv_executors).setOnClickListener(this);
        findViewById(R.id.tv_thread).setOnClickListener(this);


        // 计算可使用的最大内存
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // 取四分之一的可用内存作为缓存
        final int cacheSize = maxMemory / 4;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_easy:
                EasyExecutorActivity.startActivity(this);
                break;
            case R.id.tv_poll:
                ThreadPollActivity.startActivity(this);
                break;
            case R.id.tv_executors:
                startActivity(new Intent(this, ExecutorsTestActivity.class));
                break;
            case R.id.tv_thread:
                startActivity(new Intent(this,ThreadActivity.class));
                break;
            default:
                break;
        }
    }

}
