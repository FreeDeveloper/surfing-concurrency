package cn.smile.concorrent.reorder;

import lombok.Getter;

public class ReorderSet {

    @Getter
    private int a = 0;
    boolean flag = false;

    public void writerTrue(){
        a = 1;
        flag = true;
    }

    public void reader(){
        if(flag){
            int i = a * a;
            System.out.println("i的值为"+i);
        }
    }
}
