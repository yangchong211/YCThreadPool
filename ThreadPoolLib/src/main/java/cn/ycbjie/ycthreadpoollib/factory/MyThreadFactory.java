/*
Copyright 2017 yangchong211（github.com/yangchong211）

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package cn.ycbjie.ycthreadpoollib.factory;


import androidx.annotation.NonNull;

import java.util.concurrent.ThreadFactory;


/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : 默认Thread工厂
 *     revise:
 * </pre>
 */
public class MyThreadFactory implements ThreadFactory {

    /**
     * ThreadFactory
     * ThreadFactory是一个接口，里面只有一个newThread方法
     * 线程工厂，为线程池提供新线程的创建
     */

    private int priority;

    /**
     * 构造方法，默认为优先级是：Thread.NORM_PRIORITY
     */
    public MyThreadFactory() {
        this.priority = Thread.NORM_PRIORITY;
    }

    /**
     * 构造方法
     * @param priority                  优先级
     */
    public MyThreadFactory(int priority) {
        this.priority = priority;
    }


    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        // 创建线程
        Thread thread = new Thread(runnable);
        // 设置线程优先级
        thread.setPriority(priority);
        return thread;
    }

}
