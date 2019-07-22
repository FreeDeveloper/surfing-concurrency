package cn.smile.concorrent.concorrentUtil;

import org.junit.Test;

import java.util.concurrent.*;

public class ThreadPoolExecutorTest {

    @Test
    public void futureTaskAtExecutorTest() throws ExecutionException, InterruptedException {
        // 创建 FutureTask
        FutureTask<Integer> futureTask = new FutureTask<>(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 1+2;
            }
        });
        // 创建线程池
        ExecutorService es = Executors.newCachedThreadPool();
        // 提交 FutureTask
        es.submit(futureTask);
        // 获取计算结果
        Integer result = futureTask.get();
        System.out.println(result);
    }

    @Test
    public void futureTaskAtThreadTest() throws ExecutionException, InterruptedException {
        // 创建 FutureTask
        FutureTask<Integer> futureTask
                = new FutureTask<>(()-> 1+2);
        // 创建并启动线程
        Thread T1 = new Thread(futureTask);
        T1.start();
        // 获取计算结果
        Integer result = futureTask.get();
        System.out.println(result);
    }

    @Test
    public void futureTaskMutiTest() throws ExecutionException, InterruptedException {
        // 创建任务 T2 的 FutureTask
        FutureTask<String> ft2 = new FutureTask<>(new T2Task());
        // 创建任务 T1 的 FutureTask
        FutureTask<String> ft1 = new FutureTask<>(new T1Task(ft2));
        // 线程 T1 执行任务 ft1
        Thread T1 = new Thread(ft1);
        T1.start();
        // 线程 T2 执行任务 ft2
        Thread T2 = new Thread(ft2);
        T2.start();
        // 等待线程 T1 执行结果
        System.out.println(ft1.get());
    }
}
