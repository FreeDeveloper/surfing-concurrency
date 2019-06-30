package cn.smile.spring.container.bean;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcorrentCounterTest {
    private AtomicInteger atomicInteger = new AtomicInteger();

    private int unSafeI = 0;

    @Test
    public void concorrentCounterTest(){
        List<Thread> ts = new ArrayList<Thread>(600);
        Long start = System.currentTimeMillis();
        for(int j = 0; j < 100 ; j++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int i = 0;i<10000;i++){
                        count();
                        safeCount();
                    }
                }
            });
            ts.add(t);
        }

        for (Thread t : ts) {
            t.start();
        }

        //等待所有线程执行完毕
        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(unSafeI);
        System.out.println(atomicInteger.get());
        System.out.println(System.currentTimeMillis() - start);
    }

    /***
     * 非线程安全计数器
     */
    private void count(){
        unSafeI ++;
    }

    /***
     * 使用CAS实现的线程安全计数器
     */
    private void safeCount(){
//        for(;;){
//            int i = atomicInteger.get();
//            boolean success = atomicInteger.compareAndSet(i,++i);
//            if(success){
//                break;
//            }
//        }
        atomicInteger.incrementAndGet();
    }

}
