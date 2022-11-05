package study.bean;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Person implements Cloneable {
    private String name;
    private String sex;
    private Integer age;
    private LocalDate birth;

    public Person() {}

    public Person(String name) {
        this.name = name;
    }

    public Person(String name, Integer age) {
        this(name);
        this.age = age;
    }

    public Person clone() throws CloneNotSupportedException {
        return (Person) super.clone();
    }
}
