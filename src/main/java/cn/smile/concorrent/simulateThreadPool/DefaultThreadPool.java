package cn.smile.concorrent.simulateThreadPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class DefaultThreadPool<Job extends Runnable> implements ThreadPool<Job> {
    //线程池最大限制数
    private static final int  MAX_WORKER_NUMBERS = 10;
    //线程池默认的数量
    private static final int  DEFAULT_WORKER_NUMBERS = 5;
    //线程池最小的数量
    private static final int MIN_WORKER_NUMBERS = 1;
    //这是一个工作列表，将会向里面插入任务
    private final LinkedList<Job> jobs = new LinkedList<Job>();
    //工作者列表
    private List<Worker> workers = Collections.synchronizedList(new ArrayList<Worker>());
    //工作者线程的数量
    private int workerNum = DEFAULT_WORKER_NUMBERS;
    //线程编号生成
    private AtomicLong threadNum = new AtomicLong();

    public DefaultThreadPool(){
        initializeWorkers(workerNum);
    }

    public DefaultThreadPool(int num){
        workerNum = num > MAX_WORKER_NUMBERS ? MAX_WORKER_NUMBERS : num < MIN_WORKER_NUMBERS ? MIN_WORKER_NUMBERS : num;
        initializeWorkers(workerNum);
    }


    @Override
    public void execute(Job job) {
        if(job != null){
            //添加一个工作，然后通知
            synchronized (jobs){
                jobs.addLast(job);
                jobs.notify();
            }
        }
    }

    @Override
    public void shutdown() {
        for(Worker worker : workers){
            worker.shutdown();
        }
    }

    @Override
    public void addWorkers(int num) {
        synchronized (jobs){
            //限制新增的worker数量不能超过最大值
            if(num + this.workerNum > MAX_WORKER_NUMBERS){
                num = MAX_WORKER_NUMBERS - this.workerNum;
            }

            initializeWorkers(num);
            this.workerNum += num;

        }
    }

    @Override
    public void removeWorkers(int num) {
        synchronized (jobs){
            if(num > this.workerNum){
                throw new IllegalArgumentException("超过工作任务数");
            }

            int count = 0;
            while(count < num){
                Worker worker = workers.get(count);
                if(workers.remove(worker)){
                    worker.shutdown();
                    count ++;
                }
            }

            this.workerNum -= count;
        }
    }

    @Override
    public int getJobSize() {
        return jobs.size();
    }

    private void initializeWorkers(int num){
       for(int i = 0; i < num; i++){
           Worker worker = new Worker();
           workers.add(worker);
           Thread thread = new Thread(worker,"ThreadPool-Worker-"+threadNum);
           threadNum.incrementAndGet();
           thread.start();
       }
    }

    class Worker implements Runnable {
        //是否工作
        private volatile boolean running = true;

        @Override
        public void run() {
            while(running){
                Job job = null;
                synchronized (jobs){
                    //如果工作列表是空的，就等待
                    while(jobs.isEmpty()){
                        try {
                            jobs.wait();
                        } catch (InterruptedException e) {
                            //赶回到外部对WorkerThread的中断操作，返回
                            Thread.currentThread().interrupt();
                            e.printStackTrace();
                        }
                    }

                    //取出一个job
                    job = jobs.removeFirst();
                }

                if(job != null){
                    try{
                        job.run();
                    }catch (Exception e){
                        //实际任务执行中的异常，可以打日志，不影响正常的Worker线程工作
                    }
                }
            }
        }

        public void shutdown(){
            running = false;
        }
    }
}
