package study.lang;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.*;

public class DateTest {
    @Test
    public void testFormat() {
        // 周年 week-based-year 和年的区别，格式化是 Y 和 y 的区别
        // 周年，每年最后一周如果跨年，这周的所有日期输出时可能都是下一年。具体逻辑待考
//        System.out.println(LocalDate.of(2021, 12, 29).format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
        assertEquals(LocalDate.of(2021, 12, 29).format(DateTimeFormatter.ofPattern("YYYY-MM-dd")), "2022-12-29");

        // u: year 和 y: year-of-era 的区别
        // 区别在于对公元前纪年的表示法，具体未知。
    }

    @Test
    public void testCompare() {
        // 日期比较
        var d1 = LocalDate.parse("2022-10-12");
        var d2 = LocalDate.of(2022, 10, 12);
        System.out.println(d2.compareTo(d1));
        System.out.println(d2.equals(d1));
        System.out.println(d2.equals(null));
//        System.out.println(d2.compareTo(null)); // 不能是 null
    }
}
