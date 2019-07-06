package cn.smile.concorrent.concorrentUtil;

import org.junit.Test;

import java.util.Map;
import java.util.concurrent.ForkJoinPool;

public class ForkJoinTest {

    @Test
    public void forkJoinFibonacciTest(){
            // 创建分治任务线程池
            ForkJoinPool fjp = new ForkJoinPool(4);
            // 创建分治任务
            Fibonacci fib = new Fibonacci(30);
            // 启动分治任务
            Integer result = fjp.invoke(fib);
            // 输出结果
            System.out.println(result);
    }

    @Test
    public void wordCountTest() {
        String[] fc = {"hello world",
                "hello me",
                "hello fork",
                "hello join",
                "fork join in world"};
        // 创建 ForkJoin 线程池
        ForkJoinPool fjp = new ForkJoinPool(3);
        // 创建任务
        MR mr = new MR(fc, 0, fc.length);
        // 启动任务
        Map<String, Long> result = fjp.invoke(mr);
        // 输出结果
        result.forEach((k, v) -> System.out.println(k + ":" + v));
    }
}
