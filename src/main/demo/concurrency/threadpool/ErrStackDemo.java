package demo.concurrency.threadpool;

import java.util.concurrent.*;

/**
 * @descripte 自定义线程池对异常的处理
 */
public class ErrStackDemo {
    private ExecutorService executorService = null;

    class TaskDemo implements Runnable {

        @Override
        public void run() {
            System.out.println(10 / 0);
        }
    }

    /**
     * 使用submit执行任务的时候，线程池会吃掉异常
     */
    public void testWithSubmit() {
        System.out.println("test with submit begin ");
        executorService = new ThreadPoolExecutor(0, 5, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
//        for (int i = 0; i < 5; i++) {
        executorService.submit(new TaskDemo());
//        }

        executorService.shutdown();
    }

    /**
     * 使用execute执行任务时，线程池会抛出异常
     */
    public void testWithExecute() {
        System.out.println("test with execute begin ");
        executorService = new ThreadPoolExecutor(0, 5, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
        executorService.execute(new TaskDemo());

        executorService.shutdown();
    }

    /**
     * 使用Future组件来执行任务，会抛出异常堆栈。
     *
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public void testWithFuture() throws ExecutionException, InterruptedException {
        System.out.println("test with future begin ");
        executorService = new ThreadPoolExecutor(0, 5, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
        Future future = null;
        future = executorService.submit(new TaskDemo());
        future.get();

        executorService.shutdown();
    }

    /**
     * 自定义线程池，复写submit和execute方法，通过warp来代理执行任务，执行过程中有异常则抛出打印异常堆栈， 从而能够看到任务执行过程中的异常信息
     */
    class MyThreadPool extends ThreadPoolExecutor {
        public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }



        @Override
        public void execute(Runnable command) {
            super.execute(warp(command, Thread.currentThread().getId()));
        }

        @Override
        public Future<?> submit(Runnable task) {
            return super.submit(warp(task, Thread.currentThread().getId()));
        }

        private Runnable warp(final Runnable runnable, final long threadID){
            return new Runnable() {
                @Override
                public void run() {
                    try {
                        runnable.run();
                    } catch (Exception e) {
                        new Exception("MyThreadPool occured a exception at " + threadID).printStackTrace();
                    }
                }
            };
        }
    }

    public void testWithMyThreadPool() {
        MyThreadPool pool = new MyThreadPool(0, 5, 0L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(10));
        pool.submit(new TaskDemo());

        pool.shutdown();
    }

    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ErrStackDemo demo = new ErrStackDemo();
//        demo.testWithSubmit();
//        demo.testWithExecute();
//        demo.testWithFuture();
        demo.testWithMyThreadPool();
    }
}
