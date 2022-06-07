package easezhi.study.resource.pojo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Data
public class PersonDto implements Serializable {
    String name;
    Integer age;
    String sex;
}
