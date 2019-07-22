package cn.smile.concorrent.simulateConnectionPool;

import java.sql.Connection;
import java.util.LinkedList;

/***
 * 模拟数据库连接池，并设置超时时间
 */

public class ConnectionPool {
    private LinkedList<Connection> pool = new LinkedList<Connection>();

    public  ConnectionPool(int initialSize){
        if(initialSize > 0){
            for(int i = 0; i < initialSize; i++){
                pool.addLast(ConnectionDriver.createConnection());
            }
        }
    }

    public void releaseConnection(Connection connection){
        if(connection != null){
            synchronized (pool){

                //链接释放后需要进行通知，这样阻塞在pool的条件等待消费者能够被激活
                pool.addLast(connection);
                System.out.println("线程"+Thread.currentThread().getName()+"释放链接，"+"连接池连接数："+pool.size());
                pool.notify();
            }
        }
    }

    public Connection fetchConnection(long mills) throws InterruptedException {
        synchronized (pool){
            //未设置少时时间，一直等待直至返回
            if(mills <= 0){
                while(pool.isEmpty()){
                    pool.wait();
                }
                return pool.removeFirst();
            }else{
                long future = System.currentTimeMillis() + mills;
                long remaining = mills;
                boolean printFlag = false;
                while(pool.isEmpty() && remaining > 0){
                    System.out.println("连接池为空，线程"+Thread.currentThread().getName()+"开始等待，等待时间"+remaining+"ms");
                    pool.wait(remaining);
                    remaining = future - System.currentTimeMillis();
                    printFlag = true;
                }

                if(printFlag){
                    System.out.println("线程"+Thread.currentThread().getName()+"等待结束，连接池连接数："+pool.size());
                }

                Connection result = null;
                if(!pool.isEmpty()){
                    result = pool.removeFirst();
                    System.out.println("线程"+Thread.currentThread().getName()+"获取到链接，链接名："+result.getClass().getName()+"连接池连接数："+pool.size());
                }
                return result;
            }
        }
    }
}
