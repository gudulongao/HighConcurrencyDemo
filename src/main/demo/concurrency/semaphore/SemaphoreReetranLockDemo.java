package demo.concurrency.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @descripte 信号量Semaphore和重入锁ReetranLock的混合使用
 */
public class SemaphoreReetranLockDemo {
    //构建信号量，临界区同时可以允许3个线程访问
    private static Semaphore semaphore = new Semaphore(3);
    private static ReentrantLock lock = new ReentrantLock();
    private static int num = 0;

    static class TestRunable implements Runnable {
        @Override
        public void run() {
            try {
                //先申请信号量
                semaphore.acquire();
                count();
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }

        /**
         * 计数
         */
        private void count() {
            /**
             * @see 利用重入锁保证计数功能的线程安全
             */
            lock.lock();
            //计数
            num++;
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " " + num);
            //释放锁
            lock.unlock();
        }
    }

    public static void test() {
        //构造线程池
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        //待处理任务
        TestRunable testRunable = new TestRunable();
        /**
         * 循环12次执行任务
         */
        for (int i = 0; i < 12; i++) {
            executorService.submit(testRunable);
        }
        //关闭线程池
        executorService.shutdown();
    }

    public static void main(String[] args) {
        test();
    }
}
