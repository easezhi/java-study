package study.bean;

import lombok.Data;

@Data
public class GenericBase<E> {
    String name;

    InnerBean inner;

    @Data
    public class InnerBean {
        E order;
    }
}
