package cn.smile.spring.container.bean;

import cn.smile.spring.container.bean.factoryBean.AtomicObjectA;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceTest {
    private AtomicReference<AtomicObjectA> ar = new AtomicReference<AtomicObjectA>(new AtomicObjectA());
    AtomicObjectA b = new AtomicObjectA();

    @Test
    public void atomicReferenceTest(){
        List<Thread> ts = new ArrayList<Thread>(600);

        for(int j = 0; j < 100 ; j++){
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    safeSet();
                }
            });
            ts.add(t);
        }

        for (Thread t : ts) {
            t.start();
        }

        //等待所有线程执行完毕
        for (Thread t : ts) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(ar.get().getA());
    }

    /***
     * 使用CAS实现的线程安全计数器
     */
    private void safeSet(){
        AtomicObjectA a = ar.get();
        a.addOne();
        b.addOne();
        ar.compareAndSet(a,b);
    }

}
