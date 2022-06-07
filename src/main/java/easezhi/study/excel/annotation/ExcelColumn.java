package easezhi.study.excel.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelColumn {
    String value();

    String format() default "";

    String column() default "";

    boolean required() default false; // 是否必填

    int precision() default 0; // 参考数据库精确度概念，整数位和小数位的位数总和。非正是无效值，即不检查精确度
    int scale() default 0; // 小数位数。非正是无效值

    int max() default 0; // 字符：最大字符个数。

    String polyfill() default "";

    String align() default ""; // 文本对齐

    int width() default 0; // 单元格宽度，字符数。0 使用工作表默认宽度。负值强制擦除工作表的默认宽度，使用office软件的默认宽度
}
