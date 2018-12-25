package demo.basic.interrupt;

import java.util.concurrent.locks.ReentrantLock;

/**
 * 中断
 */
public class InterruptDemo {

    /**
     * 测试线程的中断
     */
    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public static void testInterruped() throws InterruptedException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    //判断中断标志来判断线程是否中断
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println(Thread.currentThread().getName() + " interrupted !");
                        break;
                    }

                    /**
                     * 当线程在sleep时被中断，会抛出InterruptedException。同时会清空中断标志
                     */
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + " interrupted when sleep !");
                        //中断异常会清空中断标志，故再次加上中断标志
                        Thread.currentThread().interrupt();
                    }

                    //线程调用yield方法表示要释放CPU时间片，重新抢占CPU，有可能再次抢上有可能抢不上
                    yield();
                }
            }
        };

        thread.start();
        Thread.sleep(1000);
        thread.interrupt();//中断线程
    }

    /**
     * 测试同步锁与中断的关系
     */
    public static void testSynchronizedInterrupt() {
        final Object lock = new Object();
        SynchronizedThread target = new SynchronizedThread(lock);
        Thread th1 = new Thread(target);
        Thread th2 = new Thread(target);

        System.out.println(th1.getName() + " start ...");
        th1.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(th2.getName() + " start ...");
        th2.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("interrupt " + th2.getName());
        th2.interrupt();
    }

    /**
     * 测试重入锁与中断的关系
     */
    public static void testReentrantLockInterrupt() {
        final ReentrantLock lock = new ReentrantLock();
        ReentrantLockThread target = new ReentrantLockThread(lock);
        Thread th1 = new Thread(target);
        Thread th2 = new Thread(target);

        System.out.println(th1.getName() + " start ...");
        th1.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(th2.getName() + " start ...");
        th2.start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("interrupt " + th2.getName());
        th2.interrupt();
    }

    /**
     * 基于同步锁的线程执行因子
     */
    static class SynchronizedThread implements Runnable {
        private final Object lock;

        public SynchronizedThread(Object lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            System.out.println(name + " wait for lock...");
            //线程1先获取锁，之后sleep的作用是让线程1一直保有锁
            synchronized (lock) {
                System.out.println(name + " get lock!");
                try {
                    Thread.sleep(8000);
                } catch (InterruptedException e) {
                    System.out.println(name + " interrupted !");
                }
                System.out.println(name + " done!");
            }

        }
    }

    static class ReentrantLockThread implements Runnable {
        private ReentrantLock lock;

        public ReentrantLockThread(ReentrantLock lock) {
            this.lock = lock;
        }

        @Override
        public void run() {
            String name = Thread.currentThread().getName();
            try {
                System.out.println(name + " wait for lock...");
                lock.lockInterruptibly();
                System.out.println(name + " get lock!");
                Thread.sleep(8000);
            } catch (InterruptedException e) {
                System.out.println(name + " interrupted!");
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    System.out.println(name + " release lock!");
                    lock.unlock();
                }
            }
            System.out.println(name + " done!");
        }
    }

    public static void main(String[] args) throws InterruptedException {
//        testInterruped();
        testSynchronizedInterrupt();
//        testReentrantLockInterrupt();
    }
}
