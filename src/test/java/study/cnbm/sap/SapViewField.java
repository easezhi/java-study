package study.cnbm.sap;

public @interface SapViewField {
    String fieldName() default "";

    String fieldType() default "";
}
