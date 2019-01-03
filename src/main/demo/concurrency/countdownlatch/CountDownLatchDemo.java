package demo.concurrency.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tangleic
 */
public class CountDownLatchDemo {
    /**
     * 实例计数器，指定计数10，表示可以支持10个子线程的并发执行
     */
    private static CountDownLatch count = new CountDownLatch(10);

    private static void log(String mess) {
        System.out.println("time: " + System.currentTimeMillis() + " name: " + Thread.currentThread().getName() + " mess: " + mess);
    }

    static class DemoRunnable implements Runnable {
        /**
         * 计数器
         */
        private CountDownLatch count;

        public DemoRunnable(CountDownLatch count) {
            this.count = count;
        }

        @Override
        public void run() {
            try {
                doBusiness();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //计数器减1，
                // 在finally处执行是为了避免因为异常导致计数器没有变化导致主线程一致卡死
                count.countDown();
            }
        }

        private void doBusiness() throws InterruptedException {
            Thread.sleep(1000);
            log("finished!");
        }
    }

    public static void test() throws InterruptedException {
        DemoRunnable demoRunnable = new DemoRunnable(count);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 10; i++) {
            //构建10个任务
            executorService.submit(demoRunnable);
        }

        log("wait...");
        //计数器await将主线程阻塞，等待所有计数器中的计数减为0
        count.await();
        log(" finished!");
        //关闭线程池
        executorService.shutdown();
    }

    public static void main(String[] args) throws InterruptedException {
        test();
    }
}
