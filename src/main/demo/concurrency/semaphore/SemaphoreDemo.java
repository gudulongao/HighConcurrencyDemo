package demo.concurrency.semaphore;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

public class SemaphoreDemo {
    private boolean[] parks;//车位
    private Semaphore semaphore;//信号量
    private ReentrantLock lock = new ReentrantLock();//锁

    public void initParks() {
        parks = new boolean[10];
        for (int i = 0; i < parks.length; i++) {
            parks[i] = true;
        }
        semaphore = new Semaphore(10);
    }

    class Driver implements Runnable {
        @Override
        public void run() {
            try {
                semaphore.acquire();
                int lot = getPrkLot();
                System.out.println(Thread.currentThread().getName() + " use " + lot);

                Thread.sleep(5000);
                parks[lot] = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                semaphore.release();
            }
        }

        /**
         * 获取停车位
         *
         * @return
         */
        private int getPrkLot() {
            lock.lock();
            int lot = -1;
            try {
                for (int i = 0; i < parks.length; i++) {
                    if (parks[i]) {
                        lot = i;
                    }
                }
            } finally {
                if (lock.isHeldByCurrentThread()) {
                    lock.unlock();
                }
            }

            return lot;
        }
    }

    public void test() {

    }
}
