package study.lang;

import easezhi.study.lang.date.DateFormatter;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

import static org.junit.Assert.*;

public class DateTest {

    @Test
    public void testParse() {
        // pattern 必需与待解析的文本匹配，也不能有多余的字符
        // LocalDate 对象年月日必需有值，如果待解析文本中缺某段，需要通过 parseDefaulting 指定默认值
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
            .append(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter();

        System.out.println(LocalDate.parse("2022-01-12 12:23", formatter));
//        System.out.println(LocalDate.parse("2023-02-23").format(formatter));
//        System.out.println(LocalDate.parse("2023-02-23 10:12").format(formatter)); // 抛异常
    }

    @Test
    public void testFormat() {
        // 周年 week-based-year 和年的区别，格式化是 Y 和 y 的区别
        // 周年，每年最后一周如果跨年，这周的所有日期输出时可能都是下一年。具体逻辑待考
//        System.out.println(LocalDate.of(2021, 12, 29).format(DateTimeFormatter.ofPattern("YYYY-MM-dd")));
        assertEquals(LocalDate.of(2021, 12, 29).format(DateTimeFormatter.ofPattern("YYYY-MM")), "2022-12-29");

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

    @Test
    public void testCalcu() {
        var now = LocalDate.now();
        var first = now.withDayOfMonth(1);
        System.out.println(now);
        System.out.println(first);
        System.out.println("日期只读，修改某段会生成新的日期对象");
    }
}
