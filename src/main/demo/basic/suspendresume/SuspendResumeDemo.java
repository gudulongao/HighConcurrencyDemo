package demo.basic.suspendresume;

/**
 * suspend & resume 样例
 */
public class SuspendResumeDemo {
    public static final Object lock = new Object();

    static class Thread1 extends Thread {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " start !");
            //线程竞争锁
            synchronized (lock) {
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " suspend !");
                //线程阻塞，与wait不同的是，wait是通过锁来调用，将线程添加到一个等待锁的队列中。suspend则是线程的一个自主行为，线程自己阻塞，直到线程对象调用resume方法来恢复
                suspend();

                //恢复竞争到锁后继续执行
                System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " resume !");
            }
        }
    }

    public static void testSuspendResume() {
        Thread1 thread1 = new Thread1();
        thread1.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(System.currentTimeMillis() + " " + Thread.currentThread().getName() + " resume " + thread1.getName() + " !");
        thread1.resume();
    }

    public static void main(String[] args) {
        testSuspendResume();
    }
}
