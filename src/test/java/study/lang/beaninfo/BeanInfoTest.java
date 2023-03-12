package study.lang.beaninfo;

import org.junit.Test;
import study.lang.beaninfo.bean.MyBean;

import java.beans.Introspector;

public class BeanInfoTest {

    @Test
    public void testProperty() throws Exception {
        var beanInfo = Introspector.getBeanInfo(MyBean.class, Object.class);
        var propertyList = beanInfo.getPropertyDescriptors();
        for (var p: propertyList) {
            System.out.printf("%s %s %s\n", p.getName(), p.getReadMethod(), p.getWriteMethod());
        }
    }
}
