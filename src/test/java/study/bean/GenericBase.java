package study.bean;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Data
public class GenericBase<E> {
    String name;

    InnerBean inner;

    InnerStatic<E> innerStatic;

    Class<E> innerClazz;

    public GenericBase() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        innerClazz = (Class) actualTypeArguments[0];
    }

    @Data
    public static class InnerStatic<T> {
        T order;
    }

    @Data
    public class InnerBean {
        E order;
    }

    public E parseInnerJson(String json) {
        var data = JSON.parseObject(json, InnerBean.class);
        return (E)data.getOrder();
    }

//    public E parseStaticInnerJson(String json) {
//        var data = JSON.parseObject(json, E.class);
//    }
}
