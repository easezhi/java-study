package easezhi.study.async;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConcurrentControl {

    // 对集合中的每个元素执行遍历函数。如果某次执行发生异常，终止任务
    public static <E> void eachLimit(List<E> coll, int limit, Consumer<E> iteratee) {
        eachLimit(coll, limit, iteratee, false);
    }

    // 遍历元素时发生的异常会被忽略，继续遍历下一个元素
    public static <E> void eachLimit(List<E> coll, int limit, Consumer<E> iteratee, boolean tryBest) {
        new MapLimit<>(coll, limit, iteratee).startTask(tryBest);
    }

    // 对集合中的每个元素执行遍历函数，并收集执行结果导列表中，结果的顺序与原始集合对应。
    public static <E, R> ArrayList<R> mapLimit(List<E> coll, int limit, Function<E, R> iteratee) {
        return mapLimit(coll, limit, iteratee, false);
    }
    public static <E, R> ArrayList<R> mapLimit(List<E> coll, int limit, Function<E, R> iteratee, boolean tryBest) {
        return new MapLimit<>(coll, limit, iteratee).startMap(tryBest);
    }
}
