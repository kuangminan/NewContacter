package com.future.wk.newcontacter.util;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by samsung on 2017/6/23.
 */

public class ExecutorCase {
    // 固定型线程池，和newCacheThreadPool()差不多，也能够实现resue(复用),
    // 但是这个池子规定了线程的最大数量，也就是说当池子有空闲时，那么新的
    // 任务将会在空闲线程中被执行，一旦线程池内的线程都在进行工作，那么新的
    // 任务就必须等待线程池有空闲的时候才能够进入线程池,其他的任务继续排队等待.
    // 这类池子没有规定其空闲的时间到底有多长.这一类的池子更适用于服务器.
    private static Executor executor = Executors.newFixedThreadPool(10);

    //缓存线程池，先查看线程池中是否有当前执行线程的缓存，如果有就resue(复用),如果没有,
    // 那么需要创建一个线程来完成当前的调用.并且这类线程池只能完成一些生存期很短的一些任务.
    // 并且这类线程池内部规定能resue(复用)的线程，空闲的时间不能超过60s,一旦超过了60s,就会被移出线程池
    private static Executor executor1 = Executors.newCachedThreadPool();

    //单例线程，表示在任意的时间段内，线程池中只有一个线程在工作
    private static Executor executor2 = Executors.newSingleThreadExecutor();

    // 调度型线程池,调度型线程池会根据Scheduled(任务列表)进行延迟执行，
    // 或者是进行周期性的执行.适用于一些周期性的工作.
    private static Executor executor3 = Executors.newScheduledThreadPool(10);

    private static ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void runThread(){
        for(int i = 0; i < 20; i++){
            executorService.submit(new callTask());
            executor.execute(new Task());
        }


    }

    //无返回值
    static class Task implements Runnable{
        @Override
        public void run(){
                System.out.print(Thread.currentThread().getName());
        }
    }

    //有返回值
    static class callTask implements Callable<String>{
        @Override
        public String call(){
            return (Thread.currentThread().getName());
        }
    }
}
