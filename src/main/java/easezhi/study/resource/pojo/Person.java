package easezhi.study.resource.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class Person {
    String name;
    Integer age;
    String gender;
}
