package easezhi.study.lang.date;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class DateFormatter {
    public static String formatDate(Date date, String format) {
        var formatter = new SimpleDateFormat("yyyy-MM-dd");
        return formatter.format(date);
    }
}
