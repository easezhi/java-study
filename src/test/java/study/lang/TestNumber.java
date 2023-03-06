package study.lang;

import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class TestNumber {
    @Test
    public void testScale() {
        // BigDecimal 截取精度
        double v = 23.346547;
        System.out.println(new BigDecimal(v));
        System.out.println(new BigDecimal(v).setScale(3, RoundingMode.HALF_DOWN));
    }

    @Test
    public void testNumToPercent() {
        System.out.println(new DecimalFormat("#.##%").format(1.123421));
        System.out.println(new DecimalFormat("#.##%").format(0.12));
        System.out.println(new DecimalFormat("0.0%").format(0.12));
        System.out.println(new DecimalFormat("0%").format(0.02));
        System.out.println(new DecimalFormat("#.##%").format(0.123));
        System.out.println(new DecimalFormat("#.##%").format(0.00234));

        System.out.println(new DecimalFormat("#.##%").format(BigDecimal.valueOf(0.01234)));
    }
}
