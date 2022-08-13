package study.lang;

import org.junit.Test;
import study.bean.Grandson;
import study.bean.Son;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectTest {
    @Test
    public void testField() throws NoSuchFieldException {
        Field[] decFields = Son.class.getDeclaredFields();
        Field[] fields = Grandson.class.getFields();
        System.out.println("getDeclaredFields：类自己声明的字段，包括私有。不包括继承的字段");
        System.out.println(Arrays.stream(decFields).map(Field::getName).collect(Collectors.joining(",")));
        System.out.println("getFields：类所有公共字段，包括继承的。先子类后基类");
        System.out.println(Arrays.stream(fields).map(Field::getName).collect(Collectors.joining(",")));
        System.out.println(Arrays.stream(fields).map(Field::getDeclaringClass).map(Class::getSimpleName)
            .collect(Collectors.joining(",")));

        Field decName = Son.class.getDeclaredField("name");
        Field name = Son.class.getField("name");
        System.out.println(decName.getDeclaringClass().getSimpleName());
        System.out.println(name.getDeclaringClass().getSimpleName());
        System.out.println("同一个字段多次获取的 Field 实例相同");
        System.out.println(decName.equals(name));
    }
}
