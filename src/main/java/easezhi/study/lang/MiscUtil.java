package easezhi.study.lang;

public class MiscUtil {
    // 线程等待毫秒。捕获检查异常，抛出运行时异常
    public static void waitMillis(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void waitSecond(double s) {
        var ms = Math.round(s * 1000);
        waitMillis(ms);
    }
}
