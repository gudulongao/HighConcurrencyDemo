package demo.concurrency.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * @author tangleic
 */
public class SemaphoreReetranLockDemo {
    /**
     * 构建信号量，指定令牌数，表示临界区同时可以允许3个线程访问
     */
    private static Semaphore semaphore = new Semaphore(3);

    /**
     * 测试线程执行因子
     */
    static class TestRunable implements Runnable {
        @Override
        public void run() {
            try {
                //先申请令牌
                semaphore.acquire();
                //业务处理
                doBusinessWork();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //归还令牌
                semaphore.release();
            }
        }

        /**
         * 计数
         */
        private void doBusinessWork() throws InterruptedException {
            System.out.println("time: " + System.currentTimeMillis() + " id: " + Thread.currentThread().getId() + " enter block area!");
            Thread.sleep(2000);
            System.out.println("time: " + System.currentTimeMillis() + " id: " + Thread.currentThread().getId() + " left!");
        }
    }

    public static void test() {
        //构造线程池
        //noinspection AlibabaThreadPoolCreation
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        //待处理任务
        TestRunable testRunable = new TestRunable();
        /**
         * 循环12次执行任务
         */
        for (int i = 0; i < 6; i++) {
            executorService.submit(testRunable);
        }
        //关闭线程池
        executorService.shutdown();
    }

    public static void main(String[] args) {
        test();
    }
}
