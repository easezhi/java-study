package study.lang;

import easezhi.study.lang.SFunction;
import easezhi.study.lang.bean.BeanUtil;
import org.junit.Test;
import study.bean.Father;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class LambdaTest {
    @Test
    public void test() {
        Consumer<String> cf = (s) -> {
//            return s; // 返回值必须是void
        };
    }

    @Test
    public void testLambdaObject() {
        // 同一个方法的多次引用，不是同一个对象
        Function<Father, String> f1 = Father::getName;
        Function<Father, String> f2 = Father::getName;
        System.out.println(f1 == f2); // false
        System.out.println(f1.equals(f2)); // false
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
        var N = 1000000000;
        var clazz = InnerClass.class;
        var inst = new InnerClass();

        var pckMethod = clazz.getDeclaredMethod("getPackageMsg");
        pckMethod.setAccessible(true);
        Function<InnerClass, String> fun = InnerClass::getPackageMsg;

        // 预热？
        for (var i = 0; i < 1000; i++) {
            inst.getPackageMsg();
            pckMethod.invoke(inst);
            fun.apply(inst);
        }

        var t1 = System.currentTimeMillis();
        for (var i = 0; i < N; i++) {
            inst.getPackageMsg();
        }
        System.out.println("直接调用实例方法：" + (System.currentTimeMillis() - t1));

        var t2 = System.currentTimeMillis();
        for (var i = 0; i < N; i++) {
            pckMethod.invoke(inst);
        }
        System.out.println("调用反射方法：" + (System.currentTimeMillis() - t2));

        var t3 = System.currentTimeMillis();
        for (var i = 0; i < N; i++) {
            fun.apply(inst);
        }
        System.out.println("调用方法引用：" + (System.currentTimeMillis() - t3));
    }

    @Test
    public void testLambdaName() {
        // 方法引用相当于函数式接口（Function等）的实现类，SerializedLambda 中保存了实现类、方法等信息
        System.out.println(BeanUtil.getPropertyName(Father::getName));

        // 匿名 lambda 表达式，实现类是当前上下文的类，实现方法是虚拟方法，名字包含当前上下文的类、函数名和随机字符
        SFunction<Father, String> fun = fa -> fa.getName();
        System.out.println(BeanUtil.getPropertyName(fun));
    }
}
