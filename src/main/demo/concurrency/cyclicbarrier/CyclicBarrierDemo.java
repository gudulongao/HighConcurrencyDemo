package demo.concurrency.cyclicbarrier;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @descripte 循环栅栏样例
 */
public class CyclicBarrierDemo {
    class SoldierRunnable implements Runnable {
        private CyclicBarrier cyclic = null;

        public SoldierRunnable(CyclicBarrier cyclic) {
            this.cyclic = cyclic;
        }

        @Override
        public void run() {
            try {
                /**
                 * @see 个人感觉，循环栅栏其实就是控制使用了该栅栏的所有线程，当栅栏await时，阻塞所有线程，去执行栅栏任务，栅栏可以多次await来执行栅栏任务
                 */
                //循环栅栏调用await时，所有的线程都会阻塞，去执行循环栅栏中的栅栏代码
                cyclic.await();
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

        @Override
        public void run() {
            /**
             * 为了让多次执行栅栏任务有不同的效果用了flag来区分
             */
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
        //构造循环栅栏，参数有用于统计线程数的数据，还有在循环栅栏await需要执行的栅栏代码
        CyclicBarrier cyclicBarrier = new CyclicBarrier(10, new BarriderRunnable(true, 10));
        SoldierRunnable soldierRunnable = new SoldierRunnable(cyclicBarrier);
        ExecutorService executorService = Executors.newFixedThreadPool(20);
        for (int i = 0; i < 10; i++) {
            new Thread(soldierRunnable).start();
        }

        executorService.shutdown();
    }

    public static void main(String[] args) {
        CyclicBarrierDemo demo = new CyclicBarrierDemo();
        demo.test();
    }
}
