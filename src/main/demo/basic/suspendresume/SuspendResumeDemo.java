package demo.basic.suspendresume;

/**
 * suspend & resume 样例
 */
public class SuspendResumeDemo {
    public static final Object lock = new Object();

    static class DemoThread extends Thread {
        public DemoThread(String name) {
            super(name);
        }

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " start !");
            //线程竞争锁
            synchronized (lock) {
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " suspend !");
                //线程阻塞，与wait不同的是，wait是通过锁来调用，将线程添加到一个等待锁的队列中。suspend则是线程的一个自主行为，线程自己阻塞，直到线程对象调用resume方法来恢复
                Thread.currentThread().suspend();

                //恢复竞争到锁后继续执行
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " resume !");
            }
        }
    }

    public static void testSuspendResume() {
        DemoThread thread1 = new DemoThread("A");
        DemoThread thread2 = new DemoThread("B");
        thread1.start();
        thread2.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        thread1.resume();
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " resume " + thread1.getName() + " !");
        thread2.resume();
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " resume " + thread2.getName() + " !");

    }

    public static void main(String[] args) {
        testSuspendResume();
    }
}
