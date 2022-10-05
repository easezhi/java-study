package study.async;

import easezhi.study.async.ConcurrentControl;
import easezhi.study.lang.MiscUtil;
import org.junit.Test;

import java.util.ArrayList;

public class TestConcurrentControl {
    @Test
    public void testParallelLimit() throws Exception {
        var total = 8;
        var limit = 5;
        var workers = new ArrayList<Worker>();
        for (var i = 0; i < total; i++) {
            workers.add(new Worker(i, Math.random() * 2 + 1));
        }
        var t = System.currentTimeMillis();
        ConcurrentControl.parallelLimit(workers, TestConcurrentControl::runSleep, limit);
        System.out.println("所有任务结束 " + (System.currentTimeMillis() - t));
    }

    static void runSleep(Worker e) {
        System.out.printf("task %d start %f\n", e.index, e.due);
        MiscUtil.waitSecond(e.due);
        System.out.printf("----task %d finish %f\n", e.index, e.due);
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
