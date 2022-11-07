package easezhi.study.lang.bean;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

public class BeanUtil {

    public static String getterName(String fieldName) {
        return "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static String setterName(String fieldName) {
        return "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
    }

    public static String fieldName(String accessorName) {
        if (accessorName.length() <= 3) {
            return "";
        }
        return accessorName.substring(3, 4).toLowerCase() + accessorName.substring(4);
    }

    /*
     * 获取所有的属性
     * 如果有字段定义，则至少需要有一个公共访问器函数（getter 或者 setter）
     * 只有访问器函数，没有字段定义的也会获取到
     * 如果字段类型与访问器类型不一致，最终属性类型不明确
     */
    public static Map<String, FieldSpec> getFieldSpecMap(Class clazz) {
        var fields = getAllFields(clazz);
        var accessors = getAllAccessors(clazz);
        Map<String, FieldSpec> fieldMap = new HashMap<>();
        for (var field: fields) {
            var spec = new FieldSpec(field);
            fieldMap.put(field.getName(), spec);
        }
        // 访问器函数与对应名字的字段关联在一起
        for (var accessor: accessors) {
            var methodName = accessor.getName();
            var fieldName = BeanUtil.fieldName(methodName);
            var spec = fieldMap.computeIfAbsent(fieldName, FieldSpec::new);
            if (methodName.startsWith("get")) {
                spec.getter = accessor;
                spec.type = accessor.getReturnType();
            } else {
                spec.setter = accessor;
                spec.type = accessor.getParameterTypes()[0];
            }
        }
        // 过滤掉只有字段定义，没有对应访问器函数的
        var iter = fieldMap.entrySet().iterator();
        while (iter.hasNext()) {
            var entry = iter.next();
            var spec = entry.getValue();
            if (spec.getGetter() == null && spec.getSetter() == null) {
                iter.remove();
            }
        }
        return fieldMap;
    }

    public static List<FieldSpec> getFieldSpecs(Class clazz) {
        return new ArrayList<>(getFieldSpecMap(clazz).values());
    }

    // 从子类到各级父类，递归获取所有声明的字段，同名字段取用子类中定义的。不包括继承自 Object 类的字段
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

    /*
     * 获取所有公共的访问器函数。不包括继承自 Object 的(getClass)
     * 名字以 get 开头、返回类型非 void、无参的函数，视为 getter 函数
     * 名字以 set 开头、只有一个参数的函数，视为 setter 函数
     */
    public static List<Method> getAllAccessors(Class clazz) {
        var accessors = new ArrayList<Method>();
        var methods = clazz.getMethods();
        for (var method: methods) {
            var name = method.getName();
            var params = method.getParameterTypes();
            var returnType = method.getReturnType();
            if (method.getDeclaringClass() == Object.class) {
                continue;
            }
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
