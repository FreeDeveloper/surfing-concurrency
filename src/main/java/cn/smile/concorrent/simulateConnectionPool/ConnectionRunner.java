package cn.smile.concorrent.simulateConnectionPool;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

public class ConnectionRunner implements Runnable {
    int count ;
    AtomicInteger got;
    AtomicInteger notGot;
    CountDownLatch start;
    CountDownLatch end;
    ConnectionPool pool;

    public ConnectionRunner(int count,AtomicInteger got,AtomicInteger notGot,CountDownLatch start,CountDownLatch end,ConnectionPool pool){
        this.count = count;
        this.got = got;
        this.notGot = notGot;
        this.start = start;
        this.end = end;
        this.pool = pool;
    }

    @Override
    public void run() {
        try{
            start.await();
        }catch (Exception ex){

        }

        while(count > 0){
            try {
                //从线程池中获取连接，如果1000ms内无法获取到，将会返回null
                //分别统计连接获取的数量got和为获取到的数量notGot
                Connection connection = pool.fetchConnection(100);
                if(connection != null){
                    try {
                        connection.createStatement();
                        connection.commit();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }finally {
                        pool.releaseConnection(connection);
                        got.incrementAndGet();
                    }
                }else{
                    notGot.incrementAndGet();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {
                count --;
            }

        }
        end.countDown();
    }
}
