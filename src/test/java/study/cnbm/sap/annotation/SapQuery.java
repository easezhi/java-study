package study.cnbm.sap.annotation;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SapQuery {

    String propName() default "";

    Type type() default Type.EQ;

    boolean wideChar() default false;

    @AllArgsConstructor
    @Getter
    enum Type {
        EQ("="),

        NE("<>"),

        GT(">"),

        GE(">="),

        LT("<"),

        LE("<="),

        IN("IN"),

        LIKE("LIKE"),

        IS_NULL("IS NULL"),

        NOT_NULL("IS NOT NULL"),
        ;

        private final String operator;
    }
}
