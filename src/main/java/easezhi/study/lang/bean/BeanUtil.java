package easezhi.study.lang.bean;

import org.springframework.beans.BeanUtils;

public class BeanUtil {

    public static String getterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static String setterName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static <E> E cloneBean(E obj) {
        if (obj == null) {
            return null;
        }
        Class clazz = obj.getClass();
        var newObj = BeanUtils.instantiateClass(clazz);
        BeanUtils.copyProperties(obj, newObj);
        return (E) newObj;
    }
}
