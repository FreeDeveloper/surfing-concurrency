package cn.smile.concorrent.concorrentUtil;

import org.junit.Test;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class CompletableFutureTest {

    @Test
    public void completableFutureCombineTest(){
        // 任务 1：洗水壶 -> 烧开水
        CompletableFuture<Void> f1 = CompletableFuture.runAsync(()->{
                    System.out.println("T1: 洗水壶...");
                    sleep(1, TimeUnit.SECONDS);

                    System.out.println("T1: 烧开水...");
                    sleep(15, TimeUnit.SECONDS);
                });
        // 任务 2：洗茶壶 -> 洗茶杯 -> 拿茶叶
        CompletableFuture<String> f2 = CompletableFuture.supplyAsync(()->{
                    System.out.println("T2: 洗茶壶...");
                    sleep(1, TimeUnit.SECONDS);

                    System.out.println("T2: 洗茶杯...");
                    sleep(2, TimeUnit.SECONDS);

                    System.out.println("T2: 拿茶叶...");
                    sleep(1, TimeUnit.SECONDS);
                    return " 龙井 ";
                });
        // 任务 3：任务 1 和任务 2 完成后执行：泡茶
        CompletableFuture<String> f3 = f1.thenCombine(f2, new BiFunction<Void, String, String>() {
            @Override
            public String apply(Void aVoid, String tf) {
                System.out.println("T3: 拿到茶叶:" + tf);
                System.out.println("T3: 泡茶...");
                return " 上茶:" + tf;
            }
        });
        // 等待任务 3 执行结果
        System.out.println(f3.join());
    }

    private void sleep(int t, TimeUnit u) {
        try {
            u.sleep(t);
        }catch(InterruptedException e){}
    }

    @Test
    public void completableFutureThenApplyTest(){
        CompletableFuture<String> f0 =
                CompletableFuture.supplyAsync(new Supplier<String>() {
                    @Override
                    public String get() {
                        return "Hello World";
                    }
                }).thenApply(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        return s + " QQ";
                    }
                }).thenApply(new Function<String, String>() {
                    @Override
                    public String apply(String s) {
                        return s.toUpperCase();
                    }
                });
        System.out.println(f0.join());
    }

    @Test
    public void completableFutureApplyToEitherTest(){
        CompletableFuture<String> f1 =
                CompletableFuture.supplyAsync(()->{
                    Random ran1 = new Random();
                    int t = ran1.nextInt(10);
                    sleep(t, TimeUnit.SECONDS);
                    return String.valueOf("f1:"+t);
                });

        CompletableFuture<String> f2 =
                CompletableFuture.supplyAsync(()->{
                    Random ran1 = new Random();
                    int t = ran1.nextInt(10);
                    return String.valueOf("f2:"+t);
                });

        CompletableFuture<String> f3 =
                f1.applyToEither(f2,s -> s);

        System.out.println(f3.join());
    }

    @Test
    public void completableFutureExceptionTest(){
        CompletableFuture<Integer> f0 = CompletableFuture
                .supplyAsync(new Supplier<Integer>() {
                    @Override
                    public Integer get() {
                        System.out.println("7/0");
                        return 7/0;
                    }
                }).thenApply(new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer r) {
                        System.out.println("r*10");
                        return r * 10;
                    }
                }).exceptionally(new Function<Throwable, Integer>() {
                    @Override
                    public Integer apply(Throwable throwable) {
                        System.out.println("捕获异常:"+throwable.getMessage());
                        return 0;
                    }
                }).whenComplete(new BiConsumer<Integer, Throwable>() {
                    @Override
                    public void accept(Integer integer, Throwable throwable) {
                        System.out.println("whenComplete:"+throwable.getMessage());
                        integer++;
                    }
                }).handle(new BiFunction<Integer, Throwable, Integer>() {
                    @Override
                    public Integer apply(Integer integer, Throwable throwable) {
                        System.out.println("handle:"+throwable.getMessage());
                        integer++;
                        return integer;
                    }
                });
                System.out.println(f0.join());
    }

}
