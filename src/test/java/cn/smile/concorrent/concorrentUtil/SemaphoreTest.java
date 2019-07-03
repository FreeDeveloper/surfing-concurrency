package cn.smile.concorrent.concorrentUtil;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {

    @Test
    public void semaphoreTest(){
        int N = 8;            //工人数
        Semaphore semaphore = new Semaphore(5); //机器数目
        List<Thread> threads = new ArrayList<>();
        for(int i=0;i<N;i++){
            final int num = i;
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                       semaphore.acquire();
                       System.out.println("工人"+num+"占用一个机器在生产...");
                       Thread.sleep(2000);
                       System.out.println("工人"+num+"释放出机器");
                       semaphore.release();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            threads.add(thread);
        }

        for (Thread thread:threads) {
            thread.start();
        }

        for (Thread thread:threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
