package demo.basic.util;

public class Log {
    public static void log(String mess) {
        System.out.println("time: " + System.currentTimeMillis() + " name: " + Thread.currentThread().getName() + " mess: " + mess);
    }
}
