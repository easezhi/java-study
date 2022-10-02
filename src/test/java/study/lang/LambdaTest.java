package study.lang;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class LambdaTest {
    @Test
    public void test() {
        Consumer<String> cf = (s) -> {
//            return s; // 返回值必须是void
        };
    }

    @Test
    public void testReference() {
        var list = new ArrayList<InnerClass>();
        var obj = new InnerClass();
        list.add(obj);

        System.out.println("引用包方法");
        Function<InnerClass, String> fr = InnerClass::getPackageMsg;
        System.out.println(fr.apply(obj));

        System.out.println("引用私有方法");
        Function<InnerClass, String> pfr = obj.getPrivateMapper();
        System.out.println(pfr.apply(obj));

        System.out.println("通过类引用方法 VS 通过实例引用方法");
        Function<InnerClass, String> f1 = InnerClass::getPackageMsg;
        System.out.println(f1.apply(obj));
        Supplier<String> f2 = obj::getPublicMsg;
        System.out.println(f2.get());
    }

    @Test
    public void testPerformance() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        var N = 100000000;
        var clazz = InnerClass.class;
        var inst = new InnerClass();

        var t1 = System.currentTimeMillis();
        for (var i = 0; i < N; i++) {
            inst.getPackageMsg();
        }
        System.out.println("直接调用实例方法：" + (System.currentTimeMillis() - t1));

        var pckMethod = clazz.getDeclaredMethod("getPackageMsg");
        pckMethod.setAccessible(true);
        var t2 = System.currentTimeMillis();
        for (var i = 0; i < N; i++) {
            pckMethod.invoke(inst);
        }
        System.out.println("调用反射方法：" + (System.currentTimeMillis() - t2));

        Function<InnerClass, String> fun = InnerClass::getPackageMsg;
        var t3 = System.currentTimeMillis();
        for (var i = 0; i < N; i++) {
            fun.apply(inst);
        }
        System.out.println("调用方法引用：" + (System.currentTimeMillis() - t3));
    }
}
