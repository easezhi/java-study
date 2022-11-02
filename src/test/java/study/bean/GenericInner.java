package study.bean;

import lombok.Data;

@Data
public class GenericInner<E> {
    String name;

    E data;
}
