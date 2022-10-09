package study.lang;

import org.junit.Test;
import study.bean.TestEnum;

public class LangTest {
    @Test
    public void test() {}

    @Test
    public void testEnum() {
        System.out.println(TestEnum.CREATE);
        System.out.println(TestEnum.CREATE.name());
        System.out.println(TestEnum.CREATE.ordinal());
    }
}
