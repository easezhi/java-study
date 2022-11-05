package study.lang;

import easezhi.study.lang.bean.BeanUtil;
import org.junit.Test;
import study.bean.Person;
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

    @Test
    public void cloneTest() {
        var p1 = new Person("qq", 12);
        System.out.println(p1);

        var p2 = BeanUtil.cloneBean(p1);
        System.out.println(p2);
    }
}
