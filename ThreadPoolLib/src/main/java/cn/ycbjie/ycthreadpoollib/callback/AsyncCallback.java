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
package cn.ycbjie.ycthreadpoollib.callback;



/**
 * <pre>
 *     @author yangchong
 *     blog  : https://github.com/yangchong211
 *     time  : 2017/08/22
 *     desc  : 异步callback回调接口
 *     revise:
 * </pre>
 */
public interface AsyncCallback<T> {

    /**
     * 成功时调用
     * @param t         泛型
     */
    void onSuccess(T t);

    /**
     * 异常时调用
     * @param t         异常
     */
    void onFailed(Throwable t);


    /**
     * 通知用户任务开始运行
     * @param threadName            正在运行线程的名字
     */
    void onStart(String threadName);


}
