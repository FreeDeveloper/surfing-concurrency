package cn.smile.concorrent.concorrentUtil;

/**
 * 1、在构造函数对一个final域的写入，与随后把这个被构造对象的引用赋值给另一个引用比变量之间不能重排序
 * 2、初次读一个包含final域的对象的引用，与随后初次读取这个final域，这两个操作不能重排序
 * 3、在构造函数内对一个final引用对象的成员域的写入，与随后在构造函数外把这个被构造对象的引用赋值给一个引用变变量之间不能重排序
 *
 *  JMM可以确保读线程C至少能看到写线程A在构造函数中 对final引用对象的成员域写入，对于线程B的写入，可能看到可能看不到
 *
 *  需要确保在构造函数返回之前，被构造对象的引用不能被其他线程所见，容易出现对象‘逸出’
 *
 */
public class FinalExample {
    int i;                          //普通变量
    final int j;                    //final变量
    final int[] intArray;

    static FinalExample obj;

    public FinalExample(){          //构造函数
        i = 1;                      //写普通域
        j = 1;                      //写final域
        intArray = new int[1];
        intArray[0] = 1;
        //obj = this;               //此句会造成对象逸出
    }

    public static void writer(){    //写线程A执行
        obj = new FinalExample();
    }

    public static void writerTwo(){    //写线程B执行
        obj.intArray[0] = 2;
    }

    public static void reader(){    //读线程C执行
        FinalExample object = obj;  //读对象引用
        int a = object.i;           //读普通域
        int b = object.j;           //读final域

        if(obj != null){
            int temp1 = obj.intArray[0];
        }
    }
}
