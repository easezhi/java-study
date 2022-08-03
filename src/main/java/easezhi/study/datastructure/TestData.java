package easezhi.study.datastructure;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestData {
    public static void main(String[] args) {
        testCUD();
    }

    static void testCUD() {
        List<A> sl = new ArrayList<>();
        sl.add(new A("a"));
        sl.add(new A("b"));
        sl.add(new A("e"));
        List<A> tl = new ArrayList<>();
        tl.add(new A("b"));
        tl.add(new A("c"));
        tl.add(new A("d"));
        tl.add(new A("d"));
        var rst = ListUtil.pickCUD(sl, tl, A::get);
        var nl = rst.get(0);
        var ul = rst.get(1);
        var dl = rst.get(2);
        System.out.println(joina(nl));
        System.out.println(joina(ul));
        System.out.println(joina(dl));
    }

    static String joina(List<A> l) {
        return l.stream().map(A::get).collect(Collectors.joining(","));
    }
}

class A {
    String name;
    A(String n) {
        this.name = n;
    }
    String get() {
        return this.name;
    }
}
