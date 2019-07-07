package cn.smile.concorrent.concorrentUtil;

/***
 * 线程A调用writer，线程B调用reader 根据happens-before规则：
 * 1、根据程序次序规则：1 happens-before 2,2 happens-before 3;4 happens-before 5,5 happens-before 6
 * 2、根据监视器锁规则：3 happens-before 4
 * 3、根据传递规则：2 happens-before 5
 */

public class MonitorExample {
    int a = 0;

    public synchronized void writer(){      //1 加锁
        a ++;                               //2
    }                                       //3 解锁

    public synchronized  void reader(){     //4 加锁
        int i = a;                          //5
    }                                       //6 解锁
}
