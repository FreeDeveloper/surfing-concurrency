package cn.smile.concorrent.reorder;

import lombok.Getter;

public class ReorderSet {

    @Getter
    private int a = 0;
    boolean flag = false;
    @Getter
    private int i = 0;

    public void writerTrue(){
        a = 1;
        flag = true;
    }

    public void reader(){
        if(flag){
            i = a * a;
        }
    }
}
