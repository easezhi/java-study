package easezhi.study.async;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class ConcurrentControl {

    public static ExecutorService getExecutor() {
        return Executors.newCachedThreadPool();
    }

    public static <T, C extends List<T>> void parallelLimit(List<T> dataList, Consumer<C> task, int limit) {
        var executor = getExecutor();
        var countor = new CountDownLatch(limit);
    }
}
