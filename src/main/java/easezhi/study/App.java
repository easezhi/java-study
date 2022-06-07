package easezhi.study;

import easezhi.study.resource.pojo.Person;


import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

public class App
{
    public static void main( String[] args )
    {
        try {
            var formatter = new DateTimeFormatterBuilder()
                .append(DateTimeFormatter.ofPattern("yyyy-M-d"))
//                .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
//                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
//            .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
//            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter();
            System.out.println(LocalDateTime.parse("2022-12-32", formatter));
//            System.out.println(i);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
