package study.lang;

import org.junit.Test;
import study.bean.Grandson;
import study.bean.Son;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.stream.Collectors;

public class ReflectTest {
    @Test
    public void testField() throws NoSuchFieldException {
        Field[] decFields = Son.class.getDeclaredFields();
        Field[] fields = Son.class.getFields();
        System.out.println("getDeclaredFields：类自己声明的字段，包括私有。不包括继承的字段");
        System.out.println(Arrays.stream(decFields).map(Field::getName).collect(Collectors.joining(",")));
        System.out.println("getFields：类所有公共字段，包括继承的。先子类后基类");
        System.out.println(Arrays.stream(fields).map(Field::getName).collect(Collectors.joining(",")));
        System.out.println(Arrays.stream(fields).map(Field::getDeclaringClass).map(Class::getSimpleName)
            .collect(Collectors.joining(",")));

//        System.out.println(decFields[2].equals(fields[0]));
//        System.out.println(fields[4].equals(fields[0]));

        Field decName = Son.class.getDeclaredField("covered");
        Field name = Son.class.getField("covered");
        System.out.println(decName.getDeclaringClass().getSimpleName());
        System.out.println(name.getDeclaringClass().getSimpleName());
        System.out.println("同一个字段多次获取的 Field 不是同一个对象实例");
        System.out.println(decName == name);
    }

    @Test
    public void testAccess() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var clazz = InnerClass.class;
        var inst = new InnerClass();
        var pckMethod = clazz.getDeclaredMethod("getPackageMsg");
        System.out.println(pckMethod.invoke(inst));

        var prvMethod = clazz.getDeclaredMethod("getPrivateMsg");
        var prvMethod2 = clazz.getDeclaredMethod("getPrivateMsg");
        prvMethod.setAccessible(true); // 必需禁用访问控制
        System.out.println(prvMethod.invoke(inst));
        System.out.println("修改访问控制，只会影响到具体的反射对象，而不是修改原类成员的访问级别");
        System.out.println(prvMethod2.invoke(inst));
    }

    @Test
    public void testPerformance() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var N = 500000;
        var clazz = InnerClass.class;
        var inst = new InnerClass();
        System.out.println("禁用访问控制检查，可以提高反射方法调用的性能");
        
        var pckMethod = clazz.getDeclaredMethod("getPackageMsg");
        var t1 = System.currentTimeMillis();
        for (var i = 0; i < N; i++) {
            pckMethod.invoke(inst);
        }
        System.out.println("直接调用包方法：" + (System.currentTimeMillis() - t1));

        pckMethod.setAccessible(true);
        var t2 = System.currentTimeMillis();
        for (var i = 0; i < N; i++) {
            pckMethod.invoke(inst);
        }
        System.out.println("禁用访问控制调用包方法：" + (System.currentTimeMillis() - t2));
    }
}
