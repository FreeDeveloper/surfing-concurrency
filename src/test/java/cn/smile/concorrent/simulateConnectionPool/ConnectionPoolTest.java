package cn.smile.concorrent.simulateConnectionPool;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionPoolTest {
    static ConnectionPool pool = new ConnectionPool(10);
    //保证所有ConnectionRunner能够同时开始
    static CountDownLatch start = new CountDownLatch(1);
    //main变成将会等待所有ConnectionRunner结束收才继续执行
    static CountDownLatch end;

    @Test
    public void connectionPoolTest(){
        //线程数量
        int threadCount = 100;
        end = new CountDownLatch(threadCount);

        int count = 20;
        AtomicInteger got = new AtomicInteger();
        AtomicInteger notGot = new AtomicInteger();

        for(int i = 0;i < threadCount;i++){
            Thread thread = new Thread(new ConnectionRunner(count,got,notGot,start,end,pool));
            thread.start();
        }

        start.countDown();
        try {
            end.await();
        } catch (InterruptedException e) {

        }
        System.out.println("total invoke : " + (threadCount * count));
        System.out.println("got connection: " + got);
        System.out.println("not got connection : " + notGot);

    }
}
