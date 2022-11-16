package com.utaoo.thread;


import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public abstract class ThreadUtils<T> {
    ExecutorService fixedThreadPool;
    int threadPoolSize;
    int threadSize;
    int dataSize = 0;
    boolean special = false;
    int threadNum = 0;
    Long currentTime = System.currentTimeMillis();
    boolean init = false;


    List<T> entityList;


    final public void setList(List<T> list) {
        this.entityList = list;
        this.dataSize = list.size();
        this.special = this.dataSize % this.threadSize == 0;
        this.threadNum = this.dataSize / this.threadSize + 1;
    }

    public ThreadUtils() {
        this.threadSize = 100;
        this.threadPoolSize = 64;
        this.fixedThreadPool = Executors.newFixedThreadPool(this.threadPoolSize);
    }

    public ThreadUtils(List<T> list) {
        this.threadSize = 100;
        this.threadPoolSize = 64;
        this.fixedThreadPool = Executors.newFixedThreadPool(this.threadPoolSize);
        setList(list);
    }

    public ThreadUtils(List<T> list, int threadSize, int threadPoolSize) {
        this.threadSize = threadSize;
        this.threadPoolSize = threadPoolSize;
        this.fixedThreadPool = Executors.newFixedThreadPool(this.threadPoolSize);
        setList(list);
    }

    public ThreadUtils(int threadSize, int threadPoolSize) {
        this.threadSize = threadSize;
        this.threadPoolSize = threadPoolSize;
        this.fixedThreadPool = Executors.newFixedThreadPool(this.threadPoolSize);
    }


    public abstract void excuteDB(List<T> tList);

    public String excute(List<T> tList) throws InterruptedException {
        setList(tList);
        return excute();
    }

    public String excute() throws InterruptedException {
        if (init) {
            throw new RuntimeException("No List was init！");
        }
        for (int i = 0; i < this.threadNum; i++) {
            List<T> cutList = null;
            if (i == this.threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = this.entityList.subList(this.threadSize * i, this.dataSize);
                System.out.println("Thread" + i + ":" + (this.threadSize * i) + "=>" + this.dataSize);
            } else {
                cutList = this.entityList.subList(this.threadSize * i, this.threadSize * (i + 1));
                System.out.println("Thread" + i + ":" + (this.threadSize * i) + "=>" + (this.threadSize * (i + 1)));
            }
            List<T> finalCutList = cutList;
            Thread thread = new Thread() {
                @Override
                public void run() {
                    try {
                        excuteDB(finalCutList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            fixedThreadPool.execute(thread);
        }
        fixedThreadPool.shutdown();
        fixedThreadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);
        System.out.println("success!Cost:" + (System.currentTimeMillis() - currentTime) + "ms");
        return "success!Cost:" + (System.currentTimeMillis() - currentTime) + "ms";
    }

}
