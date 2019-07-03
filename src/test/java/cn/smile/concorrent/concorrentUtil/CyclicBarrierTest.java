package cn.smile.concorrent.concorrentUtil;

import org.junit.Test;

import java.util.Vector;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CyclicBarrierTest {
    Vector pos = new Vector();
    Vector dos = new Vector();

    @Test
    public void cyclicBarrierTest() {

        OrderCheck orderCheck = new OrderCheck();

        // 执行回调的线程池
        Executor executor =
                Executors.newFixedThreadPool(1);
        final CyclicBarrier barrier =
                new CyclicBarrier(2, ()->{
                    System.out.println("当前线程"+Thread.currentThread().getName());
                    executor.execute(()->orderCheck.check(pos,dos));
                });


            Thread T1 = new Thread(()->{
                System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
                // 查询订单库
                pos.add(orderCheck.getPOrders());
                // 等待
                try {
                    System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("所有线程写入完毕，继续处理其他任务...");
            });


            // 循环查询运单库
            Thread T2 = new Thread(()->{
                System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 查询运单库
                dos.add(orderCheck.getDOrders());
                // 等待
                try {
                    System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                    barrier.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (BrokenBarrierException e) {
                    e.printStackTrace();
                }
                System.out.println("所有线程写入完毕，继续处理其他任务...");
            });
            T1.start();
            T2.start();

            try {
                T1.join();
                T2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Test
        public void easyTestBarrier(){
            int N = 4;
            final CyclicBarrier barrier  = new CyclicBarrier(N,new Runnable() {
                @Override
                public void run() {
                    System.out.println("当前线程"+Thread.currentThread().getName());
                }
            });

            for(int i=0;i<N-1;i++){
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println("线程"+Thread.currentThread().getName()+"正在写入数据...");
                        try {
                            Thread.sleep(5000);      //以睡眠来模拟写入数据操作
                            System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                            barrier.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }catch(BrokenBarrierException e){
                            e.printStackTrace();
                        }
                        System.out.println("所有线程写入完毕，继续处理其他任务...");
                    }
                }).start();
            }

            try {
                Thread.sleep(10000);
                System.out.println("线程"+Thread.currentThread().getName()+"写入数据完毕，等待其他线程写入完毕");
                barrier.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }

        }
}
