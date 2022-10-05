package easezhi.study.async;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ConcurrentUtil {
    private static ExecutorService executor;

    public static ExecutorService defaultExecutor() {
        if (executor == null) {
            executor = Executors.newCachedThreadPool();
        }
        return executor;
    }
}
