package demo.concurrency.cyclicbarrier;

import demo.basic.util.Log;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author tanglei
 * <p>
 * 循环栅栏，包含对多线程并发任务计数，暂停所有线程运行，执行特别栅栏任务，再恢复多线程任务执行的功能
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
        private CyclicBarrier cyclic;

        @Override
        public void run() {
            try {
                //开始
                begin();
                //当前线程阻塞在共享的栅栏对象，栅栏对象开始执行任务。
                // 当栅栏对象执行完毕后，线程再恢复执行
                cyclic.await();
                //业务处理
                doBuinsess();
                //当前线程再次阻塞，等待栅栏对象执行完毕后在恢复
                cyclic.await();
                //结束
                finished();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (BrokenBarrierException e) {
                e.printStackTrace();
            }
        }

        @SuppressWarnings("WeakerAccess")
        public MultiThreadTask(CyclicBarrier cyclic) {
            this.cyclic = cyclic;
        }

        private void begin() throws InterruptedException {
            Thread.sleep(100);
            Log.log("multi thread begin...");
        }

        private void doBuinsess() throws InterruptedException {
            Thread.sleep(1000);
            Log.log("multi thread work...");
        }

        private void finished() {
            Log.log("multi thread finished!");
        }
    }

    /**
     * 栅栏任务
     * 当阻挡的线程达到计数的预期数，之后会执行到此的栅栏任务。
     * 栅栏任务执行完毕会恢复所有栅栏等待线程队列中的所有线程
     */
    class BarriderTask implements Runnable {
        @Override
        public void run() {
            try {
                //业务处理
                doBusiness();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        private void doBusiness() throws InterruptedException {
            Thread.sleep(2000);
            Log.log("barrider work...");
        }
    }

    public void test() {

        //构造栅栏，5代表栅栏可以拦截的线程数，后一个是当达到拦截线程数之后执行的栅栏任务
        CyclicBarrier cyclicBarrier = new CyclicBarrier(5, new BarriderTask());
        //构建多线程任务
        MultiThreadTask multiThreadTask = new MultiThreadTask(cyclicBarrier);
        //采用线程池并发执行
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 5; i++) {
            //构建10个任务
            executorService.submit(multiThreadTask);
        }
    }

    public static void main(String[] args) {
        CyclicBarrierDemo demo = new CyclicBarrierDemo();
        demo.test();
    }
}
