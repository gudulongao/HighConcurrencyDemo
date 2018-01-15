package demo.concurrency.reentranlock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @descripte condition 样例 conditon的await和siginal与wait与notify类似
 */
public class ConditionDemo {
    private static ReentrantLock lock = new ReentrantLock();
    private static Condition condition = lock.newCondition();

    static class ThreadDemo extends Thread {
        @Override
        public void run() {
            try {
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " get lock ");
                lock.lock();
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " await ");
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " release lock ");
                condition.await();

                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " go on ");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                if (lock.isHeldByCurrentThread()) {
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
