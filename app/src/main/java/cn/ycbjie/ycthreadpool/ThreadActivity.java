package cn.ycbjie.ycthreadpool;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


public class ThreadActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);
//        test2();
        test3();
    }


    private void test1(){
        @SuppressLint("StaticFieldLeak")
        AsyncTask asyncTask = new AsyncTask<Void, Void, ArrayList<String>>() {
            /**
             * 开始执行后台任务
             * @param voids                 void
             * @return                      集合
             */
            @Override
            protected ArrayList<String> doInBackground(Void... voids) {
                for (int i=0 ; i<10000 ; i++){

                }
                return null;
            }

            /**
             * 开始执行后台任务之后
             * @param list                  list
             */
            @Override
            protected void onPostExecute(ArrayList<String> list) {
                super.onPostExecute(list);
            }

            /**
             * 开始执行后台任务之前
             */
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            /**
             * 进度更新中，会被多次调用
             * @param values                values
             */
            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }
        }.execute();
    }

    /**
     * 现在有T1、T2、T3三个线程，你怎样保证T2在T1执行完后执行，T3在T2执行完后执行？
     */
    private void test2(){
        Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("线程执行","Thread1");
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("线程执行","Thread2");
            }
        });
        Thread t3 = new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d("线程执行","Thread3");
            }
        });

        t1.start();
        t2.start();
        t3.start();


        try {
            t1.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void test3(){
        final ShareThread sh = new ShareThread();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    sh.Test01();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "T1").start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    sh.Test02();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "T2").start();
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    sh.Test03();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "T3").start();
    }


    class ShareThread {

        // flag作为标记
        private int flag = 1;
        private Lock lock = new ReentrantLock();
        private Condition c1 = lock.newCondition();
        private Condition c2 = lock.newCondition();
        private Condition c3 = lock.newCondition();

        public void Test01() throws InterruptedException {
            lock.lock();
            try {
                while (flag != 1) {
                    c1.await();
                }
                System.out.println("正在执行的是:" + Thread.currentThread().getName());
                flag = 2;
                c2.signal();// 通知一个线程来执行
            } finally {
                lock.unlock();
            }
        }

        public void Test02() throws InterruptedException {
            lock.lock();
            try {
                while (flag != 2) {
                    c2.await();
                }
                System.out.println("正在执行的是:" + Thread.currentThread().getName());
                flag = 3;
                c3.signal();// 通知一个线程来执行
            } finally {
                lock.unlock();
            }
        }

        public void Test03() throws InterruptedException {
            lock.lock();
            try {
                while (flag != 3) {
                    c3.await();
                }
                System.out.println("正在执行的是:" + Thread.currentThread().getName());
                flag = 1;
                c1.signal();// 通知一个线程来执行
            } finally {
                lock.unlock();
            }
        }
    }

}
