### 目录介绍
- **1.遇到的问题和需求**
- 1.1 遇到的问题有哪些
- 1.2 遇到的需求
- 1.3 多线程通过实现Runnable弊端
- 1.4 为什么要用线程池
- 1.5 线程，多线程，线程池问题答疑
- **2.封装库具有的功能**
- 2.1 常用的功能
- **3.封装库的具体使用**
- 3.1 一键集成
- 3.2 在application中初始化库
- 3.3 最简单的runnable线程调用方式
- 3.4 最简单的异步回调
- 3.5 设置线程其他属性
- **4.关于版本更新情况**
- 4.0.1 V1.0.0 更新于2016年3月9日
- 4.0.2 V1.1.0 更新于2017年6月5日
- 4.0.3 V1.2.0 更新于2017年11月8日
- 4.0.4 V1.3.0 更新于2018年5月3日
- 4.0.5 V1.3.2 更新于2018年8月25日
- **5其他说明**
- 5.1 关于LICENSE说明
- 5.2 关于推荐说明


### 0.前言介绍
#### 0.1 基础介绍
- 轻量级线程池封装库，支持线程执行过程中状态回调监测(包含成功，失败，异常等多种状态)；支持创建异步任务，并且可以设置线程的名称，延迟执行时间，线程优先级，回调callback等；可以根据自己需要创建自己需要的线程池，一共有四种；线程异常时，可以打印异常日志，避免崩溃。
- 关于线程池，对于开发来说是十分重要，但是又有点难以理解或者运用。关于写线程池的博客网上已经有很多了，但是一般很少有看到的实际案例或者封装的库，许多博客也仅仅是介绍了线程池的概念，方法，或者部分源码分析，那么为了方便管理线程任务操作，所以才想结合实际案例是不是更容易理解线程池，更多可以参考代码。
- 非常感谢aofeng，Alibaba，OpenHFT，yjfnypeu等开源工作者的奉献精神，文末有链接。查找了大量的博客，以及开源的项目，因此最后才会有了这个案例。会慢慢完善的……
- 代码位置：https://github.com/yangchong211/YCThreadPool


#### 0.2 其他相关
- **关于线程，多线程，线程池关联博客有：**
- [01.线程基础知识](http://www.jcodecraeer.com/plus/view.php?aid=9597)
- [02.线程池深入理解](http://www.jcodecraeer.com/plus/view.php?aid=9604)
- [03.线程池封装库详细文档](https://github.com/yangchong211/YCBlogs/blob/master/java/Java%E5%A4%9A%E7%BA%BF%E7%A8%8B/03.%E7%BA%BF%E7%A8%8B%E6%B1%A0%E5%B0%81%E8%A3%85%E5%BA%93.md)
- [04.线程Thread关闭方法](https://github.com/yangchong211/YCBlogs/blob/master/java/Java%E5%A4%9A%E7%BA%BF%E7%A8%8B/05.Thread%E5%85%B3%E9%97%AD%E6%96%B9%E6%B3%95.md)
- [05.AsyncTask异步任务类](https://github.com/yangchong211/YCBlogs/blob/master/android/%E5%A4%9A%E7%BA%BF%E7%A8%8B/03.AsyncTask%E5%BC%82%E6%AD%A5%E4%BB%BB%E5%8A%A1%E7%B1%BB.md)
- [06.IntentService源码分析](https://github.com/yangchong211/YCBlogs/blob/master/android/%E5%A4%9A%E7%BA%BF%E7%A8%8B/04.IntentService%E6%BA%90%E7%A0%81%E5%88%86%E6%9E%90.md)
- 如果觉得前两篇线程知识太基础，可以直接忽略……主要是回顾基础知识点！


#### 0.3 其他相关开源介绍
- 技术博客大汇总：https://www.jianshu.com/p/53017c3fc75d
- 生活博客大汇总：https://www.jianshu.com/p/ed6a5d3ef3e8
- 开源项目库大汇总：https://github.com/yangchong211



### 1.遇到的问题和需求
#### 1.1 遇到的问题有哪些？
- 继承Thread，或者实现接口Runnable来开启一个子线程，无法准确地知道线程什么时候执行完成并获得到线程执行完成后返回的结果
- 当线程出现异常的时候，如何避免导致崩溃问题



#### 1.2 遇到的需求
- 如何在实际开发中配置线程的优先级
- 开启一个线程，是否可以监听Runnable接口中run方法操作的过程，比如监听线程的状态开始，成功，异常，完成等多种状态。
- 开启一个线程，是否可以监听Callable<T>接口中call()方法操作的过程，比如监听线程的状态开始，错误异常，完成等多种状态。


#### 1.3 多线程通过实现Runnable弊端
- **1.3.1 一般开启线程的操作如下所示**

```
new Thread(new Runnable() {
    @Override
    public void run() {
        //做一些任务
    }
}).start();
```


- 创建了一个线程并执行，它在任务结束后GC会自动回收该线程。
- 在线程并发不多的程序中确实不错，而假如这个程序有很多地方需要开启大量线程来处理任务，那么如果还是用上述的方式去创建线程处理的话，那么将导致系统的性能表现的非常糟糕。



- **1.3.2 主要的弊端有这些，可能总结并不全面**
- 大量的线程创建、执行和销毁是非常耗cpu和内存的，这样将直接影响系统的吞吐量，导致性能急剧下降，如果内存资源占用的比较多，还很可能造成OOM
- 大量的线程的创建和销毁很容易导致GC频繁的执行，从而发生内存抖动现象，而发生了内存抖动，对于移动端来说，最大的影响就是造成界面卡顿
- 线程的创建和销毁都需要时间，当有大量的线程创建和销毁时，那么这些时间的消耗则比较明显，将导致性能上的缺失



#### 1.4 为什么要用线程池
- 重用线程池中的线程，避免频繁地创建和销毁线程带来的性能消耗；
- 有效控制线程的最大并发数量，防止线程过大导致抢占资源造成系统阻塞；
- 可以对线程进行一定地管理。



### 2.封装库具有的功能
#### 2.1 常用的功能
- 支持线程执行过程中状态回调监测(包含成功，失败，异常等多种状态)
- 支持线程异常检测，并且可以打印异常日志
- 支持设置线程属性，比如名称，延时时长，优先级，callback
- 支持异步开启线程任务，支持监听异步回调监听
- 方便集成，方便使用，可以灵活选择创建不同的线程池



### 3.封装库的具体使用
#### 3.1 一键集成
- compile 'cn.yc:YCThreadPoolLib:1.3.2'


#### 3.2 在application中初始化库

```
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
        return executor;
    }
}


//自定义回调监听callback，可以全局设置，也可以单独设置。都行
public class LogCallback implements ThreadCallback {

    private final String TAG = "LogCallback";

    @Override
    public void onError(String name, Throwable t) {
        Log.e(TAG, "LogCallback"+"------onError"+"-----"+name+"----"+Thread.currentThread()+"----"+t.getMessage());
    }

    @Override
    public void onCompleted(String name) {
        Log.e(TAG, "LogCallback"+"------onCompleted"+"-----"+name+"----"+Thread.currentThread());
    }

    @Override
    public void onStart(String name) {
        Log.e(TAG, "LogCallback"+"------onStart"+"-----"+name+"----"+Thread.currentThread());
    }
}
```

#### 3.3 最简单的runnable线程调用方式
- 关于设置callback回调监听，我这里在app初始化的时候设置了全局的logCallBack，所以这里没有添加，对于每个单独的执行任务，可以添加独立callback。

```
PoolThread executor = App.getInstance().getExecutor();
        executor.setName("最简单的线程调用方式");
        executor.setDeliver(new AndroidDeliver());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                Log.e("MainActivity","最简单的线程调用方式");
            }
        });
```


#### 3.4 最简单的异步回调


```
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
                Log.e("AsyncCallback","成功");
            }

            @Override
            public void onFailed(Throwable t) {
                Log.e("AsyncCallback","失败");
            }

            @Override
            public void onStart(String threadName) {
                Log.e("AsyncCallback","开始");
            }
        });
```
