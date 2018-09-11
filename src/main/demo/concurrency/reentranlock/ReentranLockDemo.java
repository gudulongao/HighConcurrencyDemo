package demo.concurrency.reentranlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ReetranLock 样例
 */
public class ReentranLockDemo implements Runnable {
    public static ReentrantLock lock = new ReentrantLock();
    public static int i = 0;

    @Override
    public void run() {
        for (int j = 0; j < 10000; j++) {
            //手动加锁
            lock.lock();
            try {
                i++;
            } finally {
                //必须手动释放锁
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ReentranLockDemo demo = new ReentranLockDemo();
        Thread t1 = new Thread(demo);
        Thread t2 = new Thread(demo);
        t1.start();
        t1.join();
        t2.start();
        t2.join();
        System.out.println(ReentranLockDemo.i);
    }
}
