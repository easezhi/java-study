package easezhi.study.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

public class MapLimit<E, R> {

    ExecutorService executor;

    Function<E, R> iteratee;

    Consumer<E> consumer;

    final List<E> coll;

    Object[] result;

    final int total;

    final int limit;

    final CountDownLatch counter;

    final AtomicInteger cursor;

    MapLimit(List<E> coll, int limit, Function<E, R> iteratee) {
        this(ConcurrentUtil.defaultExecutor(), coll, limit, iteratee);
    }

    MapLimit(ExecutorService executor, List<E> coll, int limit, Function<E, R> iteratee) {
        this(executor, coll, limit);
        this.iteratee = iteratee;
        result = new Object[total];
    }

    MapLimit(List<E> coll, int limit, Consumer<E> iteratee) {
        this(ConcurrentUtil.defaultExecutor(), coll, limit, iteratee);
    }

    MapLimit(ExecutorService executor, List<E> coll, int limit, Consumer<E> iteratee) {
        this(executor, coll, limit);
        consumer = iteratee;
    }

    MapLimit(ExecutorService executor, List<E> coll, int limit) {
        this.executor = executor;
        this.coll = coll;
        total = coll.size();
        this.limit = Math.min(total, limit);
        counter = new CountDownLatch(this.limit);
        cursor = new AtomicInteger(this.limit);
    }

    void startTask() throws InterruptedException {
        for (var i = 0; i < limit; i++) {
            execListIt(i);
        }

        counter.await();
    }

    ArrayList<R> startMap() throws InterruptedException {
        startTask();

        var resultT = new ArrayList<R>(total);
        for (var data: result) {
            resultT.add((R)data);
        }
        return resultT;
    }

    void execListIt(int i) {
        var e = coll.get(i);
        executor.submit(() -> {
            if (iteratee != null) {
                result[i] = iteratee.apply(e);
            } else {
                consumer.accept(e);
            }

            var current = cursor.getAndIncrement();
            if (current < total) {
                execListIt(current);
            } else {
                counter.countDown();
            }
        });
    }
}
