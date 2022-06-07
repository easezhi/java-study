package easezhi.study.excel.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelEntity {
    String sheet() default "";

    String[] title();

    int titleRow() default 1; // 标题所在的 Excel 行号，按Excel习惯，从1开始。
    int dataRow() default 0; // 数据行开始的 Excel 行号。如果不指定，默认是标题行的下一行

    String dateFormat() default ""; // 默认日期格式
    String dateTimeFormat() default ""; // 默认时间格式

    int precision() default -1; // 默认精确度。负数是无效值
    int scale() default -1; // 默认小数位数

    String align() default ""; // 文本类默认对齐方式

    int width() default 12; // 单元格宽度，字符数。设为0则不设置单元格宽度，用office软件的默认值

    boolean simplifyErrorMsg() default false;
}
