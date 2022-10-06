package easezhi.study.async;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class ConcurrentControl {

    public static <E> void eachLimit(List<E> dataList, int limit, Consumer<E> iteratee) throws InterruptedException {
        new MapLimit<>(dataList, limit, iteratee).startTask();
    }

    public static <E, R> ArrayList<R> mapLimit(List<E> coll, int limit, Function<E, R> iteratee) throws InterruptedException {
        return new MapLimit<>(coll, limit, iteratee).startMap();
    }
}
