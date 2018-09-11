package demo.concurrency.readwirtelock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @descripte 读写锁样例
 */
public class ReadWriteLockDemo {
    //读写锁
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    //获取读操作的锁
    private Lock readLock = rwLock.readLock();
    //获取写操作的锁
    private Lock writeLock = rwLock.writeLock();

    class ReadRunnable implements Runnable {
        @Override
        public void run() {
            try {
                readLock.lock();
                Thread.sleep(1000);
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " read done ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                readLock.unlock();
            }
        }
    }

    class WriteRunnable implements Runnable {
        @Override
        public void run() {

            try {
                writeLock.lock();
                Thread.sleep(1000);
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " write done ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                writeLock.unlock();
            }
        }
    }

    public void test() {
        ReadRunnable readRunnable = new ReadRunnable();
        WriteRunnable writeRunnable = new WriteRunnable();

        for (int i = 0; i < 18; i++) {
            new Thread(readRunnable).start();
        }

        for (int i = 0; i < 18; i++) {
            new Thread(writeRunnable).start();
        }




    }

    public static void main(String[] args) {
        ReadWriteLockDemo demo = new ReadWriteLockDemo();
        demo.test();
    }
}
