package easezhi.study.lang.bean;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class BeanUtil {

    public static String getterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static String setterName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static List<Object> getFieldSpecOfBean(Class clazz) {
        return null;
    }

    // 从子类到各级父类，一次获取声明的字段，同名字段取用子类中定义的
    public static List<Field> getAllFields(Class clazz) {
        var fieldSet = new HashSet<String>(); // 对继承的同名字段去重
        var allFields = new ArrayList<Field>();
        var curClazz = clazz;
        while (curClazz != Object.class) {
            var fields = curClazz.getDeclaredFields();
            for (var field: fields) {
                if (!fieldSet.contains(field.getName())) {
                    allFields.add(field);
                    fieldSet.add(field.getName());
                }
            }
            curClazz = curClazz.getSuperclass();
        }
        return allFields;
    }

    public static List<Method> getAllAccessors(Class clazz) {
        var accessors = new ArrayList<Method>();
        var methods = clazz.getMethods();
        for (var method: methods) {
            var name = method.getName();
            var params = method.getParameterTypes();
            var returnType = method.getReturnType();
            if (name.startsWith("set") && params.length == 1) {
                accessors.add(method);
            } else if (name.startsWith("get") && params.length == 0 && returnType != void.class) {
                accessors.add(method);
            }
        }
        return accessors;
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
