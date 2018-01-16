package demo.concurrency.locksupport;

import java.util.concurrent.locks.LockSupport;

/**
 * @descripte LockSupport样例 与suspend和resume不同，不会将线程卡死
 */
public class LockSupportDemo {
    private Object lock = new Object();


    class DemoThread extends Thread {
        public DemoThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " wait lock ");
            synchronized (lock) {
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " get lock ");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " park ");
                /**
                 * @see park方法等同于有一个信号量为1的机制，如果这个许可本身可用，则park会立即返回，不会阻塞，如果许可不可用，则会阻塞到LockSupport.unPork方法。
                 * 当第一次调用park方法时会阻塞，直到调用unpark方法，如果先执行了unpark方法，则后面的park方法会立即返回，即unpark方法后面一次的park方法会立即返回不会阻塞
                 */
                LockSupport.park();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " go on ");
            }
        }
    }

    public void test() throws InterruptedException {
        DemoThread t1 = new DemoThread("A");
        DemoThread t2 = new DemoThread("B");
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " start A");

        t1.start();
        Thread.sleep(2000);
        t2.start();
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " start B");

        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " unpark A ");
        /**
         * @see 将许可由不可用变为可用，后面一次的park方法会立即返回不阻塞
         */
        LockSupport.unpark(t1);
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getId() + " unpark B 4");
        LockSupport.unpark(t2);
        t1.join();
        t2.join();
    }

    public static void main(String[] args) throws InterruptedException {
        LockSupportDemo demo = new LockSupportDemo();
        demo.test();
    }
}
