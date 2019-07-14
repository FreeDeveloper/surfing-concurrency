package cn.smile.concorrent.SimulateAQS;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ReadAndWriteCache {
    static Map<String,Object> map = new HashMap<String,Object>();
    static ReentrantReadWriteLock rw1 = new ReentrantReadWriteLock();

    static Lock r = rw1.readLock();
    static Lock w = rw1.writeLock();

    //获取一个key对应的value
    public static final Object get(String key){
        r.lock();
        try {
            return map.get(key);
        }finally {
            r.unlock();
        }
    }

    //设置key对应的value，并返回就得value
    public static final Object put(String key,Object value){
        w.lock();
        try{
            return map.put(key,value);
        }finally {
            w.unlock();
        }
    }

    //设置key对应的value，并返回就得value
    public static final void clear(String key,Object value){
        w.lock();
        try{
            map.clear();
        }finally {
            w.unlock();
        }
    }



}
