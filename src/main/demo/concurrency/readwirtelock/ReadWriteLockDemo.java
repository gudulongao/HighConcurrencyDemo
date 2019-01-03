package demo.concurrency.readwirtelock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author tangleic
 */
public class ReadWriteLockDemo {
    /**
     * 实例化 读写锁（实现ReadWriteLock接口）
     */
    private ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
    /**
     * 获取读锁
     */
    private Lock readLock = rwLock.readLock();
    /**
     * 获取写锁
     */
    private Lock writeLock = rwLock.writeLock();

    public static void log(String mess) {
        System.out.println("time: " + System.currentTimeMillis() + " id: " + Thread.currentThread().getId() + " " + mess);
    }

    /**
     * 读操作的执行因子
     */
    class ReadRunnable implements Runnable {
        @Override
        public void run() {
            try {
                //读操作之前对ReadLock加锁
                readLock.lock();
                //读操作
                read();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //释放读锁
                readLock.unlock();
            }
        }

        private void read() throws InterruptedException {
            log("begin read...");
            Thread.sleep(2000);
            log("read done!");
        }
    }

    /**
     * 写操作的执行因子
     */
    class WriteRunnable implements Runnable {
        @Override
        public void run() {
            try {
                //写操作之前对WriteLock加锁
                writeLock.lock();
                write();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //释放写锁
                writeLock.unlock();
            }
        }

        private void write() throws InterruptedException {
            log("begin write...");
            Thread.sleep(5000);
            log("write done!");
        }
    }

    public void test() {
        ReadRunnable readRunnable = new ReadRunnable();
        WriteRunnable writeRunnable = new WriteRunnable();

        new Thread(writeRunnable).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(readRunnable).start();
        new Thread(readRunnable).start();
        new Thread(readRunnable).start();
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(writeRunnable).start();
        new Thread(writeRunnable).start();
    }

    public static void main(String[] args) {
        ReadWriteLockDemo demo = new ReadWriteLockDemo();
        demo.test();
    }
}
