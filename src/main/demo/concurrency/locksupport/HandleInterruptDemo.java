package demo.concurrency.locksupport;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;

/**
 * @descripte LockSupport 处理中断
 */
public class HandleInterruptDemo {
    private Object obj = new Object();

    class DemoThread extends Thread {
        public DemoThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " wait lock ");
            synchronized (obj) {
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " get lock ");
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " park ");
                /**
                 * @see park方法没有中断异常InterruptException。直接 通过Thread.isInterrupted方法来判断是否中断进行中断处理即可
                 */
                LockSupport.park();
                if (Thread.interrupted()) {
                    System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " interrupt ");
                }

                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " done ");
            }
        }

    }

    public void test() throws InterruptedException {
        DemoThread t1 = new DemoThread("A");
        DemoThread t2 = new DemoThread("B");

        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " start A");
        t1.start();
//        LockSupport.unpark(t1);

        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " start B");
        t2.start();

        Thread.sleep(2000);
        //中断t1线程
        t1.interrupt();
        LockSupport.unpark(t2);
        t1.join();
        t2.join();
    }

    public static void main(String[] args) throws InterruptedException {
        HandleInterruptDemo demo = new HandleInterruptDemo();
        demo.test();
    }
}
