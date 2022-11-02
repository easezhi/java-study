package study.cnbm.sap;

public @interface SapView {
    String name() default "";

    String alias() default "";
}
