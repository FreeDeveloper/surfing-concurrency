package cn.smile.spring.container.bean;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;

public class DeadLockTest {
    private String A = "a";
    private String B = "b";

    @Test
    public void deadLockTest(){
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                synchronized (A){
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (B){
                        System.out.println("1");
                    }
                }
                countDownLatch.countDown();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                synchronized (B){
                    synchronized (A){
                        System.out.println("1");
                    }
                }
                countDownLatch.countDown();
            }
        });
        t1.start();
        t2.start();

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
