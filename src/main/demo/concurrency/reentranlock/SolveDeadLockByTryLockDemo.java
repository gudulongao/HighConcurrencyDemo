package demo.concurrency.reentranlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @escripte 通过重入锁的tryLock方法来加锁解决死锁问题
 */
public class SolveDeadLockByTryLockDemo {
    private static LockObject lock1 = new LockObject("A");
    private static LockObject lock2 = new LockObject("B");

    static class FirstThread extends Thread {
        private boolean finishedFlag = false;

        public FirstThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (!finishedFlag) {
                if (lock1.tryLock()) {
                    try {
                        Thread.sleep(5000);
                        if (lock2.tryLock()) {
                            finishedFlag = true;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock1.unLock();
                        lock2.unLock();
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " finished job!");
        }
    }

    static class AfterThread extends Thread {
        private boolean finishedFlag = false;

        public AfterThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            while (!finishedFlag) {
                if (lock2.tryLock()) {
                    try {
                        Thread.sleep(3000);
                        if (lock1.tryLock()) {
                            finishedFlag = true;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        lock1.unLock();
                        lock2.unLock();
                    }
                }
            }
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " finished job!");
        }
    }

    /**
     * 封装的锁对象
     */
    static class LockObject {
        private ReentrantLock lock = new ReentrantLock();
        private String name;

        public LockObject(String name) {
            this.name = name;
        }

        public void unLock() {
            if (lock.isHeldByCurrentThread()) {
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " unlock " + name);
                lock.unlock();
            }
        }

        public boolean tryLock() {
            /**
             * @see tryLock无参调用的时候，线程会立即获取锁，如果拿到则返回true，否之则反，tryLock可以有参调用，意义为当无法立即拿到锁会等待长时间
             */
            boolean flag = lock.tryLock();
            if (flag) {
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " lock " + name + " success ");
            } else {
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " lock " + name + " failed ");
            }
            return flag;
        }
    }

    public static void testTryLockSolveDeadLock() {
        FirstThread firstThread = new FirstThread("1");
        AfterThread afterThread = new AfterThread("2");
        firstThread.start();
        afterThread.start();
    }

    public static void main(String[] args) {
        testTryLockSolveDeadLock();
    }
}
