package cn.smile.concorrent;

public class ThreadCount {
    public static void main(String [] args){
        System.out.println("Hello World");
        ThreadGroup group = Thread.currentThread().getThreadGroup();
        ThreadGroup top = group;

        while(group != null){
            top = group;
            group = group.getParent();
        }

        int nowTheads = top.activeCount();

        Thread [] lstThreads = new Thread[nowTheads];
        top.enumerate(lstThreads);

        for(int i = 0;i < nowTheads; i++){
            System.out.println("线程number:"+i+"="+lstThreads[i].getName());
        }

    }
}
