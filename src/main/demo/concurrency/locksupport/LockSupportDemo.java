package demo.concurrency.locksupport;

import demo.basic.util.Log;

import java.util.concurrent.locks.LockSupport;

/**
 * @author tanglei
 */
public class LockSupportDemo {
    private final Object lock = new Object();


    class DemoThread extends Thread {
        DemoThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            Log.log("wait lock...");
            synchronized (lock) {
                Log.log("get lock!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Log.log("suspend...");
                /**
                 * @see park方法等同于有一个信号量为1的机制，如果这个许可本身可用，则park会立即返回，不会阻塞，如果许可不可用，则会阻塞直到LockSupport.unPork方法。
                 * 当第一次调用park方法时会阻塞，直到调用unpark方法，如果先执行了unpark方法，则后面的park方法会立即返回，即unpark方法后面一次的park方法会立即返回不会阻塞
                 */
                LockSupport.park();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.log("continue !");
            }
        }
    }

    public void test() throws InterruptedException {
        DemoThread t1 = new DemoThread("A");
        DemoThread t2 = new DemoThread("B");
        Log.log("start A");
        t1.start();
        Thread.sleep(2000);
        t2.start();
        Log.log("start B");

        Log.log("resume A");
        /**
         * @see 将许可由不可用变为可用，后面一次的park方法会立即返回不阻塞
         */
        LockSupport.unpark(t1);

        Log.log("resume B");
        LockSupport.unpark(t2);
        t1.join();
        t2.join();
    }

    public static void main(String[] args) throws InterruptedException {
        LockSupportDemo demo = new LockSupportDemo();
        demo.test();
    }
}
