package cn.smile.concorrent;

import org.junit.Test;

public class ConcorrencyTest {
    private final long count = 1000000000l;

    @Test
    public void concurrencyTest() throws InterruptedException {
        long start = System.currentTimeMillis();
        Thread thread = new Thread(new Runnable() {
            public void run() {
                int a = 0;
                for(long i = 0;i < count; i++){
                    a += 5;
                }
            }
        });

        thread.start();

        int b = 0;
        for (long i = 0; i < count; i++){
            b--;
        }

        thread.join();

        long time = System.currentTimeMillis() - start;
        System.out.println("concurrency:"+time+"ms,b="+b);
    }

    @Test
    public void serialTest() throws InterruptedException {
        long start = System.currentTimeMillis();
        int a = 0;
        for(long i = 0;i < count; i++){
             a += 5;
         }

        int b = 0;
        for (long i = 0; i < count; i++){
            b--;
        }

        long time = System.currentTimeMillis() - start;
        System.out.println("concurrency:"+time+"ms,b="+b+",a="+a);
    }
}
