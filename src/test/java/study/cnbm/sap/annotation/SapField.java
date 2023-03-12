package study.cnbm.sap.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SapField {
    // 对应SAP的 FIELDNAME。详见 SapView 注释
    String fieldName() default "";

    // 对应SAP的 FIELDTYPE
    String fieldType();
}
