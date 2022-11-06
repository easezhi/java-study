package easezhi.study.lang.bean;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
public class FieldSpec {
    String fieldName;

    Class fieldType;

    Field field;

    Method getter;

    Method setter;
}
