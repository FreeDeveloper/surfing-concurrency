package cn.smile.concorrent;

import org.junit.Test;

import java.util.Vector;

public class VectorTest {
    private static Vector<Integer> vector = new Vector<Integer>();

    @Test
    public void normalVectorTest(){
        while(true){
            for(int i = 0;i < 10 ;i++){
                vector.add(i);
            }

            Thread removeThread = new Thread(new Runnable() {
                public void run() {
                    for(int i = 0;i < vector.size() ;i++){
                        vector.remove(i);
                    }
                }
            });

            Thread getThread = new Thread(new Runnable() {
                public void run() {
                    for(int i = 0;i < vector.size() ;i++){
                        System.out.println(vector.get(i));
                    }
                }
            });

            removeThread.start();
            getThread.start();

            while(Thread.activeCount() > 20);
        }
    }

    @Test
    public void concorrentVectorTest(){
        while(true){
            for(int i = 0;i < 10 ;i++){
                vector.add(i);
            }

            Thread removeThread = new Thread(new Runnable() {
                public void run() {
                    synchronized (vector){
                        for(int i = 0;i < vector.size() ;i++){
                            vector.remove(i);
                        }
                    }
                }
            });

            Thread getThread = new Thread(new Runnable() {
                public void run() {
                    synchronized (vector) {
                        for (int i = 0; i < vector.size(); i++) {
                            System.out.println(vector.get(i));
                        }
                    }
                }
            });

            removeThread.start();
            getThread.start();

           while(Thread.activeCount() > 20);
        }
    }
}
