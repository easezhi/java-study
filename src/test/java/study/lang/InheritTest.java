package study.lang;

import org.junit.Before;
import org.junit.Test;
import study.bean.*;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.function.Function;

public class InheritTest {
    Father fa;
    Son son;

    @Before
    public void initBean() {
        fa = new Father();
        son = new Son();
    }

    @Test
    public void testInherit() {
        Father fap = son;
        Son sonp = son;

        System.out.println("基类引用，调用实际子类的方法");
        System.out.println(fap.who());

        System.out.println("静态方法可以被继承");
        Grandson.whoAmI();
        System.out.println("通过实例对象调用静态方法，根据引用类型(非实际类型)调用");
        fap.whoAmI();
        sonp.whoAmI();
    }

    @Test
    public void testMethodReference() {
        Function<Father, String> who = Father::who;
        // 方法引用应该是保存着方法签名(方法表偏移)等信息，而不是直接保存函数指针
        System.out.println("用子类实例调用基类方法引用，执行的是子类重写的方法");
        System.out.println(who.apply(fa));
        System.out.println(who.apply(son));
    }

    @Test
    public void testMethodReflect() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method who = Father.class.getDeclaredMethod("who");
        System.out.println("方法反射，执行的是子类重写的方法");
        System.out.println(who.invoke(fa));
        System.out.println(who.invoke(son));
    }

    @Test
    public void testFieldInherit() {
        // 子类重写了基类的字段
        Father fap = son;
        fap.age = 2;
        son.age = 3;
        System.out.println("基类引用读取基类字段，子类引用读取重写的字段");
        System.out.println(fap.age); // 2
        System.out.println(son.age); // 3
        // 所以才提倡使用getter方法，而不直接使用字段
        System.out.println("方法执行哪个类定义的版本，就读取哪个类定义的字段");
        System.out.println(fap.howOld()); // 2
        System.out.println(son.howOld()); // 2
        System.out.println(son.getAge()); // 3
    }
}
