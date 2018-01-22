package demo.concurrency.threadpool;

import java.util.concurrent.*;

/**
 * @descripte 拓展线程池样例
 */
public class ThreadPoolPluginDemo {

    class TaskRunnbale implements Runnable {
        private String name = null;

        public String getName() {
            return name;
        }

        @Override
        public void run() {
            System.out.println(System.currentTimeMillis() + " " + name + " run ");
        }

        public TaskRunnbale(String name) {
            this.name = name;
        }
    }

    /**
     * 自定义线程池
     */
    class MyThreadPool extends ThreadPoolExecutor {
        public MyThreadPool(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue) {
            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        }

        @Override
        protected void beforeExecute(Thread t, Runnable r) {
            System.out.println(System.currentTimeMillis() + " " + ((TaskRunnbale) r).getName() + " before run ");
        }

        @Override
        protected void afterExecute(Runnable r, Throwable t) {
            System.out.println(System.currentTimeMillis() + " " + ((TaskRunnbale) r).getName() + " before run ");
        }

        @Override
        protected void terminated() {
            super.terminated();
            System.out.println(System.currentTimeMillis() + " exist ");
        }
    }

    public void test() {
        ExecutorService executorService = new MyThreadPool(5, 5, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingDeque<Runnable>(5));
        TaskRunnbale task = null;
        for (int i = 0; i < 10; i++) {
            task = new TaskRunnbale("Task " + i);
            /**
             * @see 这里使用sumbit会报错，再beforeExecute里将r转换成TaskRunnable的时候报类型转换错误，debug的时候可以看到r的类型确实不是TaskRunnable类型。貌似和后面的Fork/Join有关
             */
            executorService.execute(task);
        }
        executorService.shutdown();
    }

    public static void main(String[] args) {
        ThreadPoolPluginDemo demo = new ThreadPoolPluginDemo();
        demo.test();
    }
}
