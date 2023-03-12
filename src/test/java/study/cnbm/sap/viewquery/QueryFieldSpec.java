package study.cnbm.sap.viewquery;

import lombok.Data;
import study.cnbm.sap.annotation.SapQuery;

import java.lang.reflect.Method;

/**
 * 在查询器类中声明的查询字段信息，从类型和注解解析获取。
 * 作用类似 PropertyDescriptor，只保留针对 SAP 查询需要的字段信息。
 */
@Data
public class QueryFieldSpec {
    // 对应实体类的属性名。默认是查询器的字段名，也可以显示指定
    private String propName;

    // 查询器中字段的 getter 函数
    private Method getter;

    // SQL 查询操作符
    private SapQuery.Type type;

    // SQL 中字符类型的值如果有汉字，需要加 N 前缀，where field = N'汉字'
    private Boolean wideChar;
}
