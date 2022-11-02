package study.bean;

import com.alibaba.fastjson2.JSON;
import lombok.Data;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

@Data
public class GenericOuter<M extends GenericInner<E>, E> {

    Class modelClazz;

    Class dataClazz;

    public GenericOuter() {
        Type genericSuperclass = this.getClass().getGenericSuperclass();
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
//        modelClazz = (Class) ((ParameterizedType) actualTypeArguments[0]).getRawType();
        modelClazz = (Class) actualTypeArguments[0];
        dataClazz = (Class) actualTypeArguments[1];
    }

    public E parseGenericJson(String json) {
        var model = (M) JSON.parseObject(json, modelClazz);
        var data = model.getData();
        return data;
    }
}
