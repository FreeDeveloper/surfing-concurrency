package cn.smile.concorrent.concorrentUtil;

import java.util.concurrent.RecursiveTask;

public class Fibonacci extends RecursiveTask<Integer> {
        final int n;

        Fibonacci(int n){
            this.n = n;
        }

        protected Integer compute(){
            if (n <= 1)
                return n;
            Fibonacci f1 = new Fibonacci(n - 1);
            // 创建子任务
            f1.fork();
            Fibonacci f2 = new Fibonacci(n - 2);
            // 等待子任务结果，并合并结果
            return f2.compute() + f1.join();
        }
}
