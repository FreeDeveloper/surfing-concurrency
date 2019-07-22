package cn.smile.concorrent.rateLimit;

import com.google.common.util.concurrent.RateLimiter;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SimpleLimiterOneTest {
    //创建限流器 5个请求每秒
    SimpleLimiterCapOne limiter = new SimpleLimiterCapOne();

    //执行任务的线程池
    ExecutorService es = Executors.newFixedThreadPool(1);

    //记录上一次的执行时间
    volatile long prev = System.nanoTime();

    @Test
    public void rateLimiterTest(){

        //测试执行20次
        for(int i = 0; i< 20 ; i++){
            //限流器限流
            limiter.acquire();
            es.execute(new Runnable() {
                public void run() {
                    long cur = System.nanoTime();
                    // 打印时间间隔：毫秒
                    System.out.println((cur-prev)/1000_000);
                    prev = cur;
                }
            });

        }
    }
}
