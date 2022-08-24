package study.sede.model;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import study.bean.Person;

import java.util.List;

@Data
public class PersonListDto {
    private String title;

    @JSONField(name = "persons")
    private List<Person> personList;
}
