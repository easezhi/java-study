package study.lang.concurrent;

import org.junit.Test;

public class ConcurrentTest {
    @Test
    public void test() {

    }

    static void sleepRandom() {
        double rt = Math.random() * 2000 + 1000;
        long ms = ((Double)rt).longValue();
    }
}
