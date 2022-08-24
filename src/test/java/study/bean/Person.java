package study.bean;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Person {
    private String name;
    private String sex;
    private Integer age;
    private LocalDate birth;
}
