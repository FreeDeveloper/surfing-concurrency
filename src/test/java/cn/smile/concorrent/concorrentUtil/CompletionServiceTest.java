package cn.smile.concorrent.concorrentUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

public class CompletionServiceTest {
    @Test
    public void notUserFirstTest(){
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 异步向电商 S1 询价
        Future<Integer> f1 = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return getPriceByS1();
            }
        });
        // 异步向电商 S2 询价
        Future<Integer> f2 =executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return getPriceByS2();
            }
        });
        // 异步向电商 S3 询价
        Future<Integer> f3 =executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return getPriceByS3();
            }
        });

        // 获取电商 S1 报价并保存
        try {
            final int r1 = f1.get();
            executor.execute(()->save(r1));

            // 获取电商 S2 报价并保存
            final int r2=f2.get();
            executor.execute(()->save(r2));

            // 获取电商 S3 报价并保存
            final int r3 = f3.get();
            executor.execute(()->save(r3));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void notUserSendQueueTest(){
        // 创建阻塞队列
        BlockingQueue<Integer> bq = new LinkedBlockingQueue<>();
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 异步向电商 S1 询价
        Future<Integer> f1 = executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return getPriceByS1();
            }
        });
        // 异步向电商 S2 询价
        Future<Integer> f2 =executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return getPriceByS2();
            }
        });
        // 异步向电商 S3 询价
        Future<Integer> f3 =executor.submit(new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return getPriceByS3();
            }
        });

        // 获取电商 S1 报价并保存
        try {
            // 电商 S1 报价异步进入阻塞队列
            executor.execute(()->
            {
                try {
                    bq.put(f1.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
            // 电商 S2 报价异步进入阻塞队列
            executor.execute(()->
            {
                try {
                    bq.put(f2.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
            // 电商 S3 报价异步进入阻塞队列
            executor.execute(()->
            {
                try {
                    bq.put(f3.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            });
            // 异步保存所有报价
            for (int i=0; i<3; i++) {
                Integer r = bq.take();
                executor.execute(()->save(r));
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void completionServiceTest() throws InterruptedException, ExecutionException {
        // 创建线程池
        ExecutorService executor =
                Executors.newFixedThreadPool(3);
        // 创建 CompletionService
        CompletionService<Integer> cs = new
                ExecutorCompletionService<>(executor);
        // 异步向电商 S1 询价
        cs.submit(()->getPriceByS1());
        // 异步向电商 S2 询价
        cs.submit(()->getPriceByS2());
        // 异步向电商 S3 询价
        cs.submit(()->getPriceByS3());
        // 将询价结果异步保存到数据库
        for (int i=0; i<3; i++) {
            Integer r = cs.take().get();
            executor.execute(()->save(r));
        }
    }

    @Test
    public void mutiResultChooseEarlestTest() throws InterruptedException, ExecutionException {
        // 创建线程池
        ExecutorService executor = Executors.newFixedThreadPool(3);
        // 创建 CompletionService
        CompletionService<String> cs = new ExecutorCompletionService<>(executor);
        // 用于保存 Future 对象
        List<Future<String>> futures = new ArrayList<>(3);
        // 提交异步任务，并保存 future 到 futures
        futures.add(cs.submit(()->geocoderByS1()));
        futures.add(cs.submit(()->geocoderByS2()));
        futures.add(cs.submit(()->geocoderByS3()));
        // 获取最快返回的任务执行结果
        String r = "";
        try {
            // 只要有一个成功返回，则 break
            for (int i = 0; i < 3; ++i) {
                r = cs.take().get();
                // 简单地通过判空来检查是否成功返回
                if (r != null) {
                    break;
                }
            }
        } finally {
            // 取消所有任务
            for(Future<String> f : futures)
                f.cancel(true);
        }
        // 返回结果
        System.out.println("返回结果是："+r);
    }

    private String geocoderByS1(){
        Random ran1 = new Random();
        int t = ran1.nextInt(3000);
        sleep(t, TimeUnit.MILLISECONDS);
        return String.valueOf("geocoderByS1:"+t);
    }

    private String geocoderByS2(){
        Random ran1 = new Random();
        int t = ran1.nextInt(3000);
        sleep(t, TimeUnit.MILLISECONDS);
        return String.valueOf("geocoderByS2:"+t);
    }

    private String geocoderByS3(){
        Random ran1 = new Random();
        int t = ran1.nextInt(3000);
        sleep(t, TimeUnit.MILLISECONDS);
        return String.valueOf("geocoderByS3:"+t);
    }

    private void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        }catch(InterruptedException e){}
    }



    private int getPriceByS1(){
        System.out.println("getPriceByS1");
        try {
            Thread.currentThread().sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 10;
    }

    private int getPriceByS2(){
        System.out.println("getPriceByS2");
        try {
            Thread.currentThread().sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 20;
    }

    private int getPriceByS3(){
        System.out.println("getPriceByS3");
        try {
            Thread.currentThread().sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return 30;
    }

    private void save(int a){
        System.out.println("save:"+a);
    }
}
