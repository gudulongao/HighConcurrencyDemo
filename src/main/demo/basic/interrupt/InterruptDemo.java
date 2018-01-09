package demo.basic.interrupt;

/**
 * 中断
 */
public class InterruptDemo {
    public static void testInterruped() throws InterruptedException {
        Thread thread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    //判断中断标志来判断线程是否中断
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println(Thread.currentThread().getName() + " interrupted !");
                        break;
                    }

                    /**
                     * 当线程在sleep时被中断，会抛出InterruptedException。同时会清空中断标志
                     */
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread().getName() + " interrupted when sleep !");
                        //中断异常会清空中断标志，故再次加上中断标志
                        Thread.currentThread().interrupt();
                    }

                    //线程调用yield方法表示要释放CPU时间片，重新抢占CPU，有可能再次抢上有可能抢不上
                    yield();
                }
            }
        };

        thread.start();
        Thread.sleep(1000);
        thread.interrupt();//中断线程
    }

    public static void main(String[] args) throws InterruptedException {
        testInterruped();
    }
}
