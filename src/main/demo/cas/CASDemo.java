package demo.cas;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * CAS策略样例
 *
 * @author tangleic
 */
public class CASDemo {
    private AtomicInteger i = new AtomicInteger(0);

    private void test() {
        DemoRunner demoRunner = new DemoRunner();
        Thread thread = null;
        for (int num = 0; num < 1000; num++) {
            thread = new Thread(demoRunner);
            thread.start();
        }
        System.out.println(i.get());
    }

    class DemoRunner implements Runnable {
        @Override
        public void run() {
            //自增
            i.getAndIncrement();
        }
    }

    public static void main(String[] args) {
        CASDemo demo = new CASDemo();
        demo.test();
    }
}
