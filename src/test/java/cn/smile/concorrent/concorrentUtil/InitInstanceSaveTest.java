package cn.smile.concorrent.concorrentUtil;

public class InitInstanceSaveTest {
    private static InitInstance initInstance;

    /**
     * 假设A线程执行代码1同时，B执行代码2.此时线程A可能看到initInstance引用的对象还未初始化完成，将再次初始化
     * */
    public static InitInstance unSafeLazyInitialization(){
        if(initInstance == null){                                       //1：线程A执行
            initInstance = new InitInstance();                          //2：线程B执行
        }
        return  initInstance;
    }

    /**
     * 对getInstance方法进行了同步处理，导致性能开销，如果被多个线程频繁调用，导致性能下降
     * */
    public synchronized static InitInstance safeLazyInitialization(){
        if(initInstance == null){                                       //1：线程A执行
            initInstance = new InitInstance();                          //2：线程B执行
        }
        return  initInstance;
    }

    /**
     * 第四行创建对象，代码可能会被分解为以下三行伪代码：
     * memory = allocate();         1：分配对象的内存空间
     * ctorInstance(memory);        2：初始化对象
     * instance = memory            3：设置instance指向刚分配的内存地址
     *
     * 以上代码可能会被重排序：
     * memory = allocate();         1：分配对象的内存空间
     * instance = memory            3：设置instance指向刚分配的内存地址
     *                                 此时对象还没有被初始化
     * ctorInstance(memory);        2：初始化对象
     *
     * 如发生重排序，另一个并发线程可能在第四行判断instance不为null，接下来访问instance对引用的对象，可能对象还未初始化
     *
     * 解决方案：1、不允许2和3重排序
     *           2、允许2和3重排序，不允许其他线程‘看到’重排序
     *
     * 1、只需要将initInstance用volatile修饰，就能解决此问题
     *
     * */
    public static InitInstance doubleCheckLazyInitialization(){
        if(initInstance == null){                                       //1:第一次检查
            synchronized (InitInstanceSaveTest.class){                  //2:加锁，锁定class对象，保证只有一个线程拿到class
                if(initInstance == null){                               //3:第二次检查
                    initInstance = new InitInstance();                  //4：问题出在这儿
                }
            }
        }
        return  initInstance;
    }


    /**
     * 基于类初始化的解决方案
     *
     * JVM在初始化时（在class被加载后，被线程使用之前），获取类的锁
     * 以下情况，一个类或者接口类型T将被初始化
     *  1、T是一个类，T中 的实力被创建
     *  2、T是一个类，T中声明的一个静态方法被调用
     *  3、T中声明的一个静态字段被赋值
     *  4、T中声明的一个静态字段被使用，且不是常量字段
     *  5、T是一个顶级类，而且在一个断言语句嵌套在T内部调用
     * */

    private static class InstanceHolder{
        public static InitInstance initInstance1 = new InitInstance();
    }

    public static InitInstance getInitInstance(){
        return InstanceHolder.initInstance1;
    }

}
