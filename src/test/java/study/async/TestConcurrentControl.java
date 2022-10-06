package study.async;

import easezhi.study.async.ConcurrentControl;
import easezhi.study.async.MapLimit;
import easezhi.study.lang.MiscUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class TestConcurrentControl {
    @Test
    public void testEachLimit() throws Exception {
        var total = 8;
        var limit = 5;
        var workers = new ArrayList<Worker>();
        for (var i = 0; i < total; i++) {
            workers.add(new Worker(i, Math.random() * 2 + 1));
        }
        var t = System.currentTimeMillis();
        ConcurrentControl.eachLimit(workers, limit, TestConcurrentControl::runMap);
        System.out.println("所有任务结束 " + (System.currentTimeMillis() - t));
    }

    @Test
    public void testMapLimit() throws Exception {
        var total = 7;
        var limit = 5;
        var workers = new ArrayList<Worker>();
        for (var i = 0; i < total; i++) {
            workers.add(new Worker(i, Math.random() * 2 + 1));
        }
        var t = System.currentTimeMillis();
        var rst = ConcurrentControl.mapLimit(workers, limit, TestConcurrentControl::runMap);
        System.out.println("所有任务结束 " + (System.currentTimeMillis() - t));
        System.out.println(rst.stream().map(e -> e.due).collect(Collectors.toList()));
    }

    static Worker runMap(Worker e) {
        System.out.printf("task %d start %f\n", e.index, e.due);
        MiscUtil.waitSecond(e.due);
        System.out.printf("----task %d finish %f\n", e.index, e.due);
        return e;
    }
}

class Worker {
    int index;

    double due;

    Worker(int index, double due) {
        this.index = index;
        this.due = due;
    }
}
