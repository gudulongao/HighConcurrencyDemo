package demo.concurrency.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolDemo {
    private class DemoRunnable implements Runnable {
        private boolean flag = false;

        @Override
        public void run() {
            try {
                if (flag) {
                    //模拟任务执行的时间，8秒
                    Thread.sleep(8000);
                    flag = false;
                } else {
                    Thread.sleep(4000);
                    flag = true;
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("run at " + System.currentTimeMillis() / 1000);
        }
    }

    public void testFixedRate() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
        DemoRunnable demoRunnable = new DemoRunnable();
        /**
         * @see 设定执行周期，初始延期0秒，调用任务的周期为2秒。
         * @see atFixedRate的调用模式，调度器会以period的时长频率来调用任务。如果任务的执行时间超过了调度周期，即到点该再次执行任务的时候发现上一次的任务还没执行完，调度器会等待上一次任务执行完毕后直接执行新的任务。故调度周期会受到任务实际时间的影响
         */
        executorService.scheduleAtFixedRate(demoRunnable, 0, 2, TimeUnit.SECONDS);
    }

    public void testWithFixedDelay() {
        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(10);
        DemoRunnable demo = new DemoRunnable();
        /**
         * @see 设定执行周期，初始延期0秒，调度任务周期2秒
         * @see withFiexDelay的调度模式，调度器会等上一次的任务执行完毕后，等待delay的时长，再次执行新的任务
         */
        executorService.scheduleWithFixedDelay(demo, 0, 2, TimeUnit.SECONDS);
    }

    public static void main(String[] args) {
        ScheduledThreadPoolDemo demo = new ScheduledThreadPoolDemo();
//        demo.testFixedRate();
        demo.testWithFixedDelay();
    }
}
