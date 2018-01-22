package demo.concurrency.threadpool;

import java.util.concurrent.*;

/**
 * @descripte 线程工厂样例
 */
public class ThreadFactoryDemo {
    /**
     * 自定义线程工厂，实现ThreadFactory接口，newThread在构建新线程时调用
     */
    class DemoFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            System.out.println(System.currentTimeMillis() + " create new thread !");
            return t;
        }
    }

    class DemoRunnable implements Runnable {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " run ! ");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void test() {
        DemoFactory factory = new DemoFactory();
        ExecutorService executorService = new ThreadPoolExecutor(5, 5, 0L, TimeUnit.MILLISECONDS, new SynchronousQueue<Runnable>(), factory);
        DemoRunnable demo = new DemoRunnable();
        for (int i = 0; i < 5; i++) {
            executorService.submit(demo);
        }
        /**
         * @see shutdown 不会暴力停止线程的运行，类似发送了一个指令，并不会等待所有的线程都执行完毕，但自此以后不会再接受新的任务
         */
        executorService.shutdown();
    }

    public static void main(String[] args) {
        ThreadFactoryDemo demo = new ThreadFactoryDemo();
        demo.test();
    }
}
