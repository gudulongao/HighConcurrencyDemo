package demo.concurrency.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @descripte 循环栅栏样例
 * <p>
 *     循环栅栏，包含对多线程并发任务计数，暂停所有线程运行，执行特别栅栏任务，再恢复多线程任务执行的功能
 * </p>
 */
public class CyclicBarrierDemo {
    /**
     * 多线程任务
     */
    class MultiThreadTask implements Runnable {
        /**
         * 多线程任务共享一个循环栅栏
         */
        private CyclicBarrier cyclic = null;

        public MultiThreadTask(CyclicBarrier cyclic) {
            this.cyclic = cyclic;
        }

        @Override
        public void run() {
            try {
                /**
                 * 由于所有多线程任务都共享一个栅栏，即每个线程运行到此，
                 * 都会将当前线程添加到同一个栅栏的等待线程队列中。同时栅栏还有一个计数器功能。
                 * 每当一个线程进入栅栏的线程等待队列之后，计数器减1，等阻挡的线程数足够之后，会执行栅栏任务。
                 * 即多线程运行到了栅栏处被阻挡被迫停止运行了。等到阻挡的足够多了之后，执行栅栏任务。
                 */
                cyclic.await();
                //当栅栏任务执行完毕之后，所有暂停的线程恢复执行。
                Thread.sleep(1000);
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " done ");
                //循环栅栏再次调用await，会继续阻塞所有线程，再次执行栅栏中的代码
                cyclic.await();
                cyclic.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 栅栏任务
     */
    class BarriderRunnable implements Runnable {
        private boolean flag;
        private int num;

        public BarriderRunnable(boolean flag, int num) {
            this.flag = flag;
            this.num = num;
        }

        /**
         * 当阻挡的线程达到计数的预期数，之后会执行到此的栅栏任务。栅栏任务执行完毕会恢复所有栅栏等待线程队列中的所有线程
         */
        @Override
        public void run() {
            if (flag) {
                System.out.println(System.currentTimeMillis() + " [" + num + "] start ");
                flag = false;
            } else {
                System.out.println(System.currentTimeMillis() + " [" + num + "]  finished ");
                flag = true;
            }
        }
    }

    public void test() {
        /**
         * 构造栅栏，10代码栅栏可以拦截的线程数，后一个是当达到拦截线程数之后执行的栅栏任务
         */
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10, new BarriderRunnable(true, 10));
        MultiThreadTask soldierRunnable = new MultiThreadTask(cyclicBarrier);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 15; i++) {
            new Thread(soldierRunnable).start();
        }

        executorService.shutdown();
    }

    public static void main(String[] args) {
        CyclicBarrierDemo demo = new CyclicBarrierDemo();
        demo.test();
    }
}
