package cn.smile.concorrent.concorrentUtil;

/**
 * 线程A调用writer，线程B调用reader 根据happens-before规则：
 * 1、根据程序次序规则：1 happens-before 2;3 happens-before 4
 * 2、根据volatile规则：2 happens-before 3
 * 3、根据happens-before传递规则：1 happens-before 4
 *
 * 可以理解为读线程B读一个volatile变量时，写线程A在写volatile之前所有可见的共享变量的值都将立即变得对B可见
 * */
public class VolatileExample {
    int a = 0;
    volatile boolean flag = false;

    public void witer(){
        a = 1;                      //1
        flag = true;                //2
    }

    public void reader(){
        if(flag){                   //3
            int i = a;              //4
        }
    }
}
