package study.lang;

import org.junit.Test;

import java.util.function.Consumer;

public class LambdaTest {
    @Test
    public void test() {
        Consumer<String> cf = (s) -> {
//            return s; // 返回值必须是void
        };
    }
}
