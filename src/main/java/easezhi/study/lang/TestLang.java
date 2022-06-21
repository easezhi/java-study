package easezhi.study.lang;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.stream.Collectors;

public class TestLang {
    public static void main( String[] args ) {
        getField();
    }

    static void getField() {
        var fa = A.class.getFields();
        var dfa = A.class.getDeclaredFields();
        var fb = B.class.getFields();
        var b = new B();
        var dfb = b.getClass().getSuperclass().getDeclaredFields();
        System.out.println(Arrays.stream(fa).map(Field::getName).collect(Collectors.toList()));
        System.out.println(Arrays.stream(dfa).map(Field::getName).collect(Collectors.toList()));
        System.out.println(Arrays.stream(fb).map(Field::getName).collect(Collectors.toList()));
        System.out.println(Arrays.stream(dfb).map(Field::getName).collect(Collectors.toList()));
    }
}

class A {
    private String a;
    public String pa;
}

class B extends A {
    private String b;
    public String pb;
}
