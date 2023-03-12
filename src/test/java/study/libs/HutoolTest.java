package study.libs;

import cn.hutool.core.util.TypeUtil;
import org.junit.Test;
import study.bean.GenericBean;

public class HutoolTest {
    @Test
    public void testType() {
        var clazz = GenericBean.class;
        Class gClazz = (Class) TypeUtil.getTypeArgument(clazz);
        System.out.println(gClazz);
    }
}
