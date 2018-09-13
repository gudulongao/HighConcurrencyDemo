package demo.concurrency.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPoolDemo {
    public static void testArrayBlockingQueueThreadPool() {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(0, 2, 50000, TimeUnit.HOURS, new ArrayBlockingQueue<Runnable>(1)){
            @Override
            protected void afterExecute(Runnable r, Throwable t) {
                System.out.println("pool size: "+this.getPoolSize()+" active: "+this.getActiveCount());
                super.afterExecute(r, t);
            }
        };
        Runnable longTimeTask = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("long time " + Thread.currentThread().getName() + " start ...");
                    Thread.sleep(6000);
                    System.out.println("long time " + Thread.currentThread().getName() + " done!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        Runnable quickTask = new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println("short time " + Thread.currentThread().getName() + " start ...");
                    Thread.sleep(3000);
                    System.out.println("short time " + Thread.currentThread().getName() + " done!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };

        threadPoolExecutor.submit(longTimeTask);
        threadPoolExecutor.submit(longTimeTask);
        threadPoolExecutor.submit(quickTask);
        threadPoolExecutor.shutdown();
    }

    public static void main(String[] args) {
        testArrayBlockingQueueThreadPool();
    }
}
