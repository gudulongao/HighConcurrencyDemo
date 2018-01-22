package demo.concurrency.threadpool;

import java.util.concurrent.*;

/**
 * @descripte 线程池策略样例
 */
public class RejectedDemo {
    /**
     * 自定义任务
     */
    class DemoRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " run ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自定义拒绝策略
     */
    class RejectedHandler implements RejectedExecutionHandler {
        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " rejected !");
        }
    }

    public void test() {
        /**
         * 自定义线程池
         */
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(10), Executors.defaultThreadFactory(), new RejectedHandler());
        DemoRunnable demoRunnable = new DemoRunnable();
        for (int i = 0; i < Integer.MAX_VALUE; i++) {
            executorService.submit(demoRunnable);
        }
    }

    public static void main(String[] args) {
        RejectedDemo demo = new RejectedDemo();
        demo.test();
    }
}
