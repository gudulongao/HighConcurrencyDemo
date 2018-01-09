package demo.basic.waitnotify;

/**
 * wait & notify 样例
 */
public class WaitNotifyDemo {
    public static Object lock = new Object();

    static class Thread1 extends Thread {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + ":" + Thread.currentThread().getName() + " start !");
            //线程竞争锁
            synchronized (lock) {
                System.out.println(System.currentTimeMillis() + ":" + Thread.currentThread().getName() + " get lock !");
                try {
                    System.out.println(System.currentTimeMillis() + ":" + Thread.currentThread().getName() + " wait !");
                    /**
                     * @see 通过锁lock的wait方法，是的当前 线程进入了一个竞争锁lock的队列。只有当锁再次调用notify方法时会队列中随机一个线程唤醒
                     */
                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                //唤醒的线程如果还能拿到锁，则还会继续执行
                System.out.println(System.currentTimeMillis() + ":" + Thread.currentThread().getName() + " be  notify !");
            }
        }
    }

    static class Thread2 extends Thread {
        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + ":" + Thread.currentThread().getName() + " start !");
            //线程竞争锁
            synchronized (lock) {
                System.out.println(System.currentTimeMillis() + ":" + Thread.currentThread().getName() + " notify !");
                //从等待锁lock的队列中随机唤醒一个线程
                lock.notify();
            }
        }
    }

    public static void testWaitNotify() {
        Thread1 thread1 = new Thread1();
        Thread2 thread2 = new Thread2();
        thread1.start();
//        thread2.start();
        //主线程sleep一秒后再启动线程2保证线程1先执行避免线程1执行后wait就再没有线程来唤醒
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        thread2.start();
//        thread1.start();
    }

    public static void main(String[] args) {
        testWaitNotify();
    }
}
