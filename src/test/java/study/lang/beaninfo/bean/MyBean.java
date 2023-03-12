package study.lang.beaninfo.bean;

import lombok.Getter;

public class MyBean {

    @Getter
    private String privateWithGetter;

    @Getter
    public String publicWithGetter;
}
