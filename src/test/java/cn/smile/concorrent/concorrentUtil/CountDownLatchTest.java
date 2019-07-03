package cn.smile.concorrent.concorrentUtil;

import org.junit.Test;

import java.util.Vector;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CountDownLatchTest {
    Vector pos;
    Vector dos;

    @Test
    public void countDownLatchTest(){

        OrderCheck orderCheck = new OrderCheck();

        // 创建 2 个线程的线程池
        Executor executor =
                Executors.newFixedThreadPool(2);
            // 计数器初始化为 2
            CountDownLatch latch =
                    new CountDownLatch(2);
            // 查询未对账订单
            executor.execute(()-> {
                pos = orderCheck.getPOrders();
                latch.countDown();
            });
            // 查询派送单
            executor.execute(()-> {
                dos = orderCheck.getDOrders();
                latch.countDown();
            });

            // 等待两个查询操作结束
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // 执行对账操作
            Vector diff = orderCheck.check(pos, dos);
            // 差异写入差异库
            orderCheck.save(diff);
    }

    @Test
    public void easyTest(){
        final CountDownLatch latch = new CountDownLatch(2);

        new Thread(){
            public void run() {
                try {
                    System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                    Thread.sleep(3000);
                    System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        }.start();

        new Thread(){
            public void run() {
                try {
                    System.out.println("子线程"+Thread.currentThread().getName()+"正在执行");
                    Thread.sleep(3000);
                    System.out.println("子线程"+Thread.currentThread().getName()+"执行完毕");
                    latch.countDown();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            };
        }.start();

        try {
            System.out.println("等待2个子线程执行完毕...");
            latch.await();
            System.out.println("2个子线程已经执行完毕");
            System.out.println("继续执行主线程");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
