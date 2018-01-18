package demo.concurrency.threadpool;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @descripte fiexedThreadPool样例
 */
public class FiexThreadPoolDemo {
    class DemorRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId());
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void test() {
        //构建有5个线程数的线程池，当有空闲线程则立即执行，如果没有空闲线程会将任务存入任务队列中，等到有空闲线程再执行任务
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        DemorRunnable demorRunnable = new DemorRunnable();
        for (int i = 0; i < 10; i++) {
            executorService.submit(demorRunnable);
        }
        executorService.shutdown();
    }

    public static void main(String[] args) {
        FiexThreadPoolDemo demo = new FiexThreadPoolDemo();
        demo.test();
    }
}
