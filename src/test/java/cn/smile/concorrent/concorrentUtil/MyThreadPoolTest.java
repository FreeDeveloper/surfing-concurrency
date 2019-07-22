package cn.smile.concorrent.concorrentUtil;

import org.junit.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class MyThreadPoolTest {

    @Test
    public void myThreadPoolTest() throws InterruptedException{
        /** 下面是使用示例 **/
        // 创建有界阻塞队列
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>(2);
        // 创建线程池
        MyThreadPool pool = new MyThreadPool(10, workQueue);
        // 提交任务
        pool.execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("你好~");
            }
        });
    }
}
