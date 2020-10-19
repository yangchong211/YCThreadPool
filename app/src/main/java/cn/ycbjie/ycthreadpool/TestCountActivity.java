package cn.ycbjie.ycthreadpool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestCountActivity extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_thread_count);

        findViewById(R.id.tv_0).setOnClickListener(this);
        findViewById(R.id.tv_1).setOnClickListener(this);
        findViewById(R.id.tv_2).setOnClickListener(this);
        findViewById(R.id.tv_3).setOnClickListener(this);
        findViewById(R.id.tv_4).setOnClickListener(this);
        findViewById(R.id.tv_5).setOnClickListener(this);
        findViewById(R.id.tv_6).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_0:
                try {
                    test1(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_1:
                try {
                    test1(2);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tv_2:
                break;
            case R.id.tv_3:
                break;
            case R.id.tv_4:
                break;
            case R.id.tv_5:
                break;
            case R.id.tv_6:
                break;
            default:
                break;
        }
    }

    // 初始化线程池
    private static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(
            8, 8, 10,
            TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(1000),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public void test1(int type) throws InterruptedException, ExecutionException, TimeoutException{

        int cores = Runtime.getRuntime().availableProcessors();

        int requestNum = 100;
        System.out.println("CPU核数 " + cores);

        List<Future<?>> futureList = new ArrayList<Future<?>>();
        Vector<Long> wholeTimeList = new Vector<Long>();
        Vector<Long> runTimeList = new Vector<Long>();

        for (int i = 0; i < requestNum; i++) {
            if (type==1){
                Future<?> future = threadPool.submit(new CPUTypeTest(runTimeList, wholeTimeList));
                futureList.add(future);
            } else {
                Future<?> future = threadPool.submit(new IOTypeTest(runTimeList, wholeTimeList));
                futureList.add(future);
            }
        }

        for (Future<?> future : futureList) {
            //获取线程执行结果
            future.get(requestNum, TimeUnit.SECONDS);
        }

        long wholeTime = 0;
        for (int i = 0; i < wholeTimeList.size(); i++) {
            wholeTime = wholeTimeList.get(i) + wholeTime;
        }

        long runTime = 0;
        for (int i = 0; i < runTimeList.size(); i++) {
            runTime = runTimeList.get(i) + runTime;
        }

        System.out.println("平均每个线程整体花费时间： " +wholeTime/wholeTimeList.size());
        System.out.println("平均每个线程执行花费时间： " +runTime/runTimeList.size());
    }

    public class CPUTypeTest implements Runnable {

        //整体执行时间，包括在队列中等待的时间
        List<Long> wholeTimeList;
        //真正执行时间
        List<Long> runTimeList;

        private long initStartTime = 0;

        /**
         * 构造函数
         * @param runTimeList
         * @param wholeTimeList
         */
        public CPUTypeTest(List<Long> runTimeList, List<Long> wholeTimeList) {
            initStartTime = System.currentTimeMillis();
            this.runTimeList = runTimeList;
            this.wholeTimeList = wholeTimeList;
        }

        /**
         * 判断素数
         * @param number
         * @return
         */
        public boolean isPrime(final int number) {
            if (number <= 1)
                return false;

            for (int i = 2; i <= Math.sqrt(number); i++) {
                if (number % i == 0)
                    return false;
            }
            return true;
        }

        public int countPrimes(final int lower, final int upper) {
            int total = 0;
            for (int i = lower; i <= upper; i++) {
                if (isPrime(i))
                    total++;
            }
            return total;
        }

        public void run() {
            long start = System.currentTimeMillis();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            countPrimes(1, 1000000);
            long end = System.currentTimeMillis();

            long wholeTime = end - initStartTime;
            long runTime = end - start;
            wholeTimeList.add(wholeTime);
            runTimeList.add(runTime);
            System.out.println("单个线程花费时间：" + (end - start));
        }
    }


    public class IOTypeTest implements Runnable {


        //整体执行时间，包括在队列中等待的时间
        Vector<Long> wholeTimeList;
        //真正执行时间
        Vector<Long> runTimeList;

        private long initStartTime = 0;

        /**
         * 构造函数
         * @param runTimeList
         * @param wholeTimeList
         */
        public IOTypeTest(Vector<Long> runTimeList, Vector<Long> wholeTimeList) {
            initStartTime = System.currentTimeMillis();
            this.runTimeList = runTimeList;
            this.wholeTimeList = wholeTimeList;
        }

        /**
         *IO操作
         */
        public void readAndWrite() throws IOException {
            InputStream in = TestCountActivity.this.getAssets().open("evaluation.config");
            int size = in.available();
            byte[] buffer = new byte[size];
            in.read(buffer);
            in.close();


//            File sourceFile = new File("");
//            //创建输入流
//            BufferedReader input = new BufferedReader(new FileReader(sourceFile));
//            //读取源文件,写入到新的文件
//            String line = null;
//            while((line = input.readLine()) != null){
//                System.out.println(line);
//            }
//            //关闭输入输出流
//            input.close();
        }

        public void run() {
            long start = System.currentTimeMillis();
            try {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                readAndWrite();
            } catch (IOException e) {
                e.printStackTrace();
            }
            long end = System.currentTimeMillis();

            long wholeTime = end - initStartTime;
            long runTime = end - start;
            wholeTimeList.add(wholeTime);
            runTimeList.add(runTime);
            System.out.println("单个线程花费时间：" + (end - start));
        }
    }

}
