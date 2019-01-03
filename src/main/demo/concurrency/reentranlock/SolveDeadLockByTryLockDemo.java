package demo.concurrency.reentranlock;

import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author tanglei
 */
public class SolveDeadLockByTryLockDemo {
    static enum Order {
        A, B
    }

    static class WorkThread extends Thread {
        private LockObject resA;
        private LockObject resB;

        private Order order;

        public WorkThread(String name, Order order, LockObject resA, LockObject resB) {
            super.setName(name);
            this.order = order;
            this.resA = resA;
            this.resB = resB;
        }

        @Override
        public void run() {
            while (true) {
                if (order == Order.A) {
                    if (doBusinessWork(resA, resB)) {
                        break;
                    }
                } else {
                    if (doBusinessWork(resB, resA)) {
                        break;
                    }
                }
            }
        }

        /**
         * 业务处理 先获取先手资源，再获取后手资源，如果资源获取齐全，业务处理完毕
         */
        private boolean doBusinessWork(LockObject first, LockObject last) {
            boolean flag = false;
            try {
                if (first.tryLock()) {
                    workOnFirstRes();
                    if (last.tryLock()) {
                        workOnLastRes();
                        flag = true;
                        finish();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                first.unLock();
                last.unLock();
            }
            return flag;
        }

        private void workOnFirstRes() throws InterruptedException {
            Thread.sleep(1500);
        }

        private void workOnLastRes() throws InterruptedException {
            Thread.sleep(1000);
        }

        private void finish() throws InterruptedException {
            System.out.println(Thread.currentThread().getName() + " 开始绘画... ");
            Thread.sleep(500);
            System.out.println(Thread.currentThread().getName() + " 绘画完成! ");
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

        public String getName() {
            return name;
        }

        public void unLock() {
            if (lock.isHeldByCurrentThread()) {
                releaseRes();
                lock.unlock();
            }
        }

        public boolean tryLock() {
            waitRes();
            boolean flag = lock.tryLock();
            if (flag) {
                getRes();
            }
            return flag;
        }

        private void waitRes() {
            System.out.println(Thread.currentThread().getName() + " 等着用 " + name);
        }

        private void getRes() {
            System.out.println(Thread.currentThread().getName() + " 得到 " + name);
        }

        private void releaseRes() {
            System.out.println(Thread.currentThread().getName() + " 用完 " + name);
        }
    }

    public static void testTryLockSolveDeadLock() {
        LockObject resA = new LockObject("纸");
        LockObject resB = new LockObject("笔");
        WorkThread workA = new WorkThread("张三", Order.A, resA, resB);
        WorkThread workB = new WorkThread("李四", Order.B, resA, resB);
        workA.start();
        workB.start();

        String[] arr = new String[]{"a"};
        Collections.addAll(Arrays.asList(arr),new String[]{"v"});
    }


    public static void main(String[] args) {
        testTryLockSolveDeadLock();
    }
}
