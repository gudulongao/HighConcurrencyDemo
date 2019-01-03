package demo.concurrency.reentranlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tangleic
 */
public class ConditionDemo {
    /**
     * 实例重入锁
     */
    private static ReentrantLock lock = new ReentrantLock();
    /**
     * 基于重入锁获取Condition实例
     */
    private static Condition condition = lock.newCondition();

    static class ThreadDemo extends Thread {
        @Override
        public void run() {
            try {
                //加锁
                lock.lock();
                //通过Condition使当前线程等待
                condition.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    //释放锁
                    lock.unlock();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadDemo threadDemo = new ThreadDemo();
        threadDemo.start();

        Thread.sleep(5000);

        /**
         * @see 通过condition进行signal的时候也必须实现拿到锁
         */
        lock.lock();
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " get lock ");
        condition.signal();
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " singnal ");
        lock.unlock();
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " release lock ");
    }
}
