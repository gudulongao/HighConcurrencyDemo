package demo.concurrency.countdownlatch;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @descripte 计数器样例
 */
public class CountDownLatchDemo {
    class DemoRunnable implements Runnable {
        private CountDownLatch count;

        public DemoRunnable(CountDownLatch count) {
            this.count = count;
        }

        @Override
        public void run() {
            try {
                Thread.sleep(1000);
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " done ");
                //coutDown表示计数器中的一个线程已结束任务，计数减一
                count.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void test() throws InterruptedException {
        CountDownLatch count = new CountDownLatch(10);
        DemoRunnable demoRunnable = new DemoRunnable(count);

        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 10; i++) {
            //构建10个任务
            executorService.submit(demoRunnable);
        }

        //计数器await将主线程阻塞，等待所有计数器中的计数减为0
        count.await();
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " finished! ");
    }

    public static void main(String[] args) throws InterruptedException {
        CountDownLatchDemo demo = new CountDownLatchDemo();
        demo.test();
    }
}
