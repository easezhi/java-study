package study.datastructure;

import easezhi.study.datastructure.ListUtil;
import org.junit.Test;

import java.util.Arrays;

public class TestList {
    @Test
    public void testPartition() {
        var ss = Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h");
        var sb = ListUtil.partition(ss, 4);
        sb.forEach(s -> {
            System.out.println(s);
        });
    }
}
