package easezhi.study.lang.bean;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@Getter
@NoArgsConstructor
public class FieldSpec {
    String name;

    Class type;

    Field field;

    Method getter;

    Method setter;

    public FieldSpec(String name) {
        this.name = name;
    }

    public FieldSpec(Field field) {
        this.field = field;
        this.name = field.getName();
        this.type = field.getType();
    }
}
