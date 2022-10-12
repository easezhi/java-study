package study.lang;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TestNumber {
    @Test
    public void testScale() {
        // BigDecimal 截取精度
        double v = 23.346547;
        System.out.println(new BigDecimal(v));
        System.out.println(new BigDecimal(v).setScale(3, RoundingMode.HALF_DOWN));
    }
}
