package study.cnbm.sap.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 声明视图中字段(FIELDNAME FIELDTYPE)的方式：
 *   SapViewField 注解。视图字段必须有这个注解。fieldName 声明字段名（可选）。fieldType 声明字段类型，一般需要按照 SAP 给的文档声明字段类型，
 *     如果没有声明，则使用SAP的字段名。
 *   JSONField 注解。一般需要借助 fastjson 对查询结果反序列号为 Java Bean，可以复用 JSONField.name 声明的字段名。
 *   Bean 字段名。
 * 字段名优先级：SapViewField > JSONField > 字段名
 * 推荐做法：Bean 的字段按 Java 规范起名，用 fastjson 声明 SAP 的字段名，用 SapViewField 声明字段类型。
 * 注：使用该注解的类，最好不要用于业务，只用于 SAP 字段声明，和接口交互的数据传输。
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SapView {
    // SAP 视图名。视图信息用这个名称进行缓存，需要保持唯一性。
    String value();
}
