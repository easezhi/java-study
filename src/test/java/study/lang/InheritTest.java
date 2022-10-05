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
        System.out.println("通过实例对象调用静态方法，静态绑定，根据引用类型(非实际类型)调用");
        fap.whoAmI();
        sonp.whoAmI();
    }

    @Test
    public void testOverride() {
        Father s1 = new Son();
        Son s2 = new Son();
        // 子类重写父类同名变量，但是扩展了变量类型（子类），不能简单视为“重写”，getter setter 函数也不是简单的重写，而已经被重载了
        s1.setSibling(s1.genSibling()); // 虽然生成的 sibling 是 Son 类型，但是引用类型是 Father，所以调用的基类的 setSibling 版本
        s2.setSibling(s2.genSibling());
        System.out.println(s1.getSibling()); // 子类“重写”的 sibling 没有被赋上值
        System.out.println(s1.getMySibling().who()); // 值被赋到了父类的 sibling 字段上
        System.out.println(s2.getSibling().who());
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
    public void testPerformance() {
        var N = 500000000;
        var son = new Son();
        Father fa = son;
        Who who = son;
        fa.who();
        who.who();
        // 理论上用接口调用方法比用类调用方法要慢。但是不明显
        var t1 = System.currentTimeMillis();
        for (var i = 0; i < N; i++) {
            fa.who();
        }
        var d1 = System.currentTimeMillis() - t1;
        System.out.println("父类引用动态分配：" + d1);

        var t2 = System.currentTimeMillis();
        for (var i = 0; i < N; i++) {
            who.who();
        }
        var d2 = System.currentTimeMillis() - t2;
        System.out.println("接口引用动态分配：" + d2);
    }

    @Test
    public void testFieldInherit() {
        // 子类重写了基类的字段，字段也是静态绑定
        Father fap = son;
        fap.covered = "f";
        son.covered = "s";
        System.out.println("基类引用读取基类字段，子类引用读取重写的字段");
        System.out.println(fap.covered); // f
        System.out.println(son.covered); // s
        // 所以才提倡使用getter方法，而不直接使用字段
        System.out.println("方法执行哪个类定义的版本，就读取哪个类定义的字段");
        System.out.println(fap.getCovered()); // s。基类引用执行子类重写的方法
        System.out.println(son.getCovered()); // s
        System.out.println("子类调用继承来的方法，读取基类定义的字段");
        System.out.println(son.getCoveredByFather()); // f
    }
}
