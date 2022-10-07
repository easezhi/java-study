package easezhi.study.async;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;

public class MapLimit<E, R> {
    // 线程池执行器
    ExecutorService executor;
    // 需要收集执行结果的遍历函数
    Function<E, R> iteratee;
    // 忽略执行结果的遍历函数
    Consumer<E> consumer;
    // 待遍历的集合
    final List<E> coll;
    // 收集中间结果
    Object[] result;

    final int total;

    final int limit;

    final CountDownLatch counter;

    final AtomicInteger cursor;
    // 遍历中发生异常，是否终止任务
    boolean fastFail = true;

    Exception taskException;

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

    void startTask() {
        startTask(false);
    }

    void startTask(boolean tryBest) {
        if (tryBest) {
            fastFail = false;
        }

        for (var i = 0; i < limit; i++) {
            execListIt(i);
        }

        try {
            counter.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        if (taskException != null) {
            throw new RuntimeException(taskException);
        }
    }

    ArrayList<R> startMap() {
        return startMap(false);
    }

    ArrayList<R> startMap(boolean tryBest) {
        startTask(tryBest);

        var resultT = new ArrayList<R>(total);
        for (var data: result) {
            resultT.add((R)data);
        }
        return resultT;
    }

    void execListIt(int i) {
        var e = coll.get(i);
        executor.submit(() -> {
            try {
                if (iteratee != null) {
                    result[i] = iteratee.apply(e);
                } else {
                    consumer.accept(e);
                }

                listItForward();
            } catch (Exception err) {
                if (fastFail) {
                    taskException = err;
                    listItShutdown();
                } else {
                    listItForward();
                }
            }
        });
    }

    void listItForward() {
        var current = cursor.getAndIncrement();
        if (current < total) {
            execListIt(current);
        } else {
            counter.countDown();
        }
    }

    void listItShutdown() {
        // 不再开始新任务
        cursor.addAndGet(total);
        // 结束阻塞等待
        for (var i = 0; i < limit; i++) {
            counter.countDown();
        }
    }
}
