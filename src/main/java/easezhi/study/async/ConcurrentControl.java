package easezhi.study.async;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class ConcurrentControl {

    public static ExecutorService getExecutor() {
        return Executors.newCachedThreadPool();
    }

    public static <T> void parallelLimit(List<T> dataList, Consumer<T> task, int limit) throws InterruptedException {
        var executor = getExecutor();
        new ParallelLimiter<>(executor, dataList, task, limit).start();
    }
}

class ParallelLimiter<E> {
    ExecutorService executor;

    Consumer<E> task;

    List<E> eles;

    final int total;

    final int limit;

    CountDownLatch counter;

    AtomicInteger cursor;

    ParallelLimiter(ExecutorService executor, List<E> eles, Consumer<E> task, int limit) {
        this.executor = executor;
        this.eles = eles;
        this.task = task;
        this.limit = limit;
        total = eles.size();
        counter = new CountDownLatch(limit);
        cursor = new AtomicInteger(limit);
    }

    void start() throws InterruptedException {
        for (var i = 0; i < limit; i++) {
            runTask(i);
        }
        counter.await();
    }

    void runTask(int i) {
        var e = eles.get(i);
        executor.submit(() -> {
            task.accept(e);

            var current = cursor.getAndIncrement();
            if (current < total) {
                runTask(current);
            } else {
                counter.countDown();
            }
        });
    }
}
