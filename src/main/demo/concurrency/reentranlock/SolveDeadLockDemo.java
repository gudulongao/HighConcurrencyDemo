package demo.concurrency.reentranlock;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @descripte 通过重入锁来解决死锁问题
 */
public class SolveDeadLockDemo {
    private static LockObject readLock = new LockObject("readlock");
    private static LockObject writeLock = new LockObject("writeLock");

    /**
     * 先执行的线程
     */
    static class FirstThread extends Thread {
        @Override
        public void run() {
            try {
                //首先获取readLock
                readLock.lockWithInterrupt();

                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " sleep 5 s");
                //线程sleep5秒，给另一个线程获取writelock留时间
                Thread.sleep(5000);

                //再获取writeLock的锁
                writeLock.lockWithInterrupt();
            } catch (InterruptedException e) {
                //处理中断响应
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " interrupt ");
            } finally {
                //释放锁
                readLock.unLock();
                writeLock.unLock();
            }
        }
    }

    /**
     * 后执行的线程
     */
    static class AfterThread extends Thread {
        @Override
        public void run() {
            try {
                //先获取writeLock的锁
                writeLock.lockWithInterrupt();

                //再获取readLock的锁
                readLock.lockWithInterrupt();
            } catch (InterruptedException e) {
                //处理中断
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " interrupt ");
            } finally {
                //释放锁
                readLock.unLock();
                writeLock.unLock();
            }
        }
    }

    /**
     * 封装锁对象
     */
    static class LockObject {
        //重入锁
        private ReentrantLock lock = new ReentrantLock();
        //锁的名称
        private String name = null;

        /**
         * 释放锁
         */
        public void unLock() {
            //通过isHeldByCurrentThread方法来判断当前线程是否拥有该锁，拥有再释放
            if (lock.isHeldByCurrentThread()) {
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " unlock " + name);
                lock.unlock();
            }
        }

        /**
         * 可以处理中断事件的加锁，即当有线程在持有该锁的过程中遇到了中断事件，会抛出InterruptedException来进行中断响应
         *
         * @throws InterruptedException
         */
        public void lockWithInterrupt() throws InterruptedException {
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " wait " + name);
            lock.lockInterruptibly();
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " locked " + name);
        }

        public String getName() {
            return name;
        }

        public LockObject() {
        }

        public LockObject(String name) {
            this.name = name;
        }
    }

    public static void testSolveDeadLock() {
        FirstThread firstThread = new FirstThread();
        AfterThread afterThread = new AfterThread();
        //先启动线程FirstThread
        firstThread.start();
        try {
            //主线程等待2秒方便线程FirstThread获取readLock
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " sleep 2 s");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //线程AfterThread启动
        afterThread.start();

        try {
            //主线程等待死锁情况发生，FirstThread先拿到了readLock，等待writeLock。AfterThread先拿到了writeLock，等待redLock。两个线程构成死锁
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " sleep 8 s");
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //主线程将线程AfterThread中断。当线程AfterThread中断后会最终再finally里释放自己锁持有的锁，即被中断线程无法完成任务，但可以成全别的线程。
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " interrupted ");
        afterThread.interrupt();
    }

    public static void main(String[] args) {
        testSolveDeadLock();
    }
}
