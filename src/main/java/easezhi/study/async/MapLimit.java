package easezhi.study.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public class MapLimit<E, R> {

    ExecutorService executor;

    Function<E, R> iteratee;

    List<E> coll;

    Object[] result;

    final int total;

    final int limit;

    CountDownLatch counter;

    AtomicInteger cursor;

    public MapLimit(List<E> coll, int limit, Function<E, R> iteratee) {
        this(ConcurrentUtil.defaultExecutor(), coll, limit, iteratee);
    }

    public MapLimit(ExecutorService executor, List<E> coll, int limit, Function<E, R> iteratee) {
        this.executor = executor;
        this.coll = coll;
        this.iteratee = iteratee;
        total = coll.size();
        this.limit = Math.min(total, limit);
        counter = new CountDownLatch(this.limit);
        cursor = new AtomicInteger(this.limit);
        result = new Object[total];
    }

    public ArrayList<R> start() throws InterruptedException {
        for (var i = 0; i < limit; i++) {
            runTask(i);
        }
        counter.await();

        var resultT = new ArrayList<R>(total);
        for (var data: result) {
            resultT.add((R)data);
        }
        return resultT;
    }

    void runTask(int i) {
        var e = coll.get(i);
        executor.submit(() -> {
            result[i] = iteratee.apply(e);

            var current = cursor.getAndIncrement();
            if (current < total) {
                runTask(current);
            } else {
                counter.countDown();
            }
        });
    }
}
