package easezhi.study.lang.date;

import java.time.LocalDateTime;

public class DateCalcu {
    public static boolean timeGt(LocalDateTime t1, LocalDateTime t2) {
        if (t1 == null || t2 == null) {
            return false;
        }
        return t1.compareTo(t2) > 0;
    }
}
