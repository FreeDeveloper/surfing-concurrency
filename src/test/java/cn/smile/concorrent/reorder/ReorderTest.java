package cn.smile.concorrent.reorder;

import org.junit.Test;

public class ReorderTest {
    private ReorderSet reorderSet = new ReorderSet();

    @Test
    public void reorderTest(){
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                reorderSet.writerTrue();
            }
        });

        Thread t2 = new Thread(new Runnable() {
            public void run() {
                reorderSet.reader();
            }
        });
        t1.start();
        t2.start();


        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(reorderSet.getI());

    }
}
