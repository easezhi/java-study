package study.sede.model;

import lombok.Data;

import java.util.List;

@Data
public class GenericDto<T> {
    private T one;

    private List<T> beanList;
}
