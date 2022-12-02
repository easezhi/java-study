package easezhi.study.data.db;

import cn.hutool.core.util.StrUtil;
import easezhi.study.lang.bean.BeanUtil;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SqlUtil {
    public static String valueToSql(Object val) {
        if (val == null) {
            return "null";
        }
        if (val instanceof SqlFunNameEnum) {
            return ((SqlFunNameEnum) val).getValue();
        } else if (val instanceof Number) {
            return val.toString();
        } else if (val instanceof Boolean) {
            return val.toString();
        } else {
            return "'" + val + "'";
        }
    }

    public static <E> List<String> buildInsertSql(List<E> beanList, String tableName) throws Exception {
        var clazz = beanList.get(0).getClass();
        var fields = clazz.getDeclaredFields();
        var fieldNames = new String[fields.length];
        var getters = new Method[fields.length];
        for (var i = 0; i < fields.length; i++) {
            var field = fields[i];
            var fieldName = field.getName();
            fieldNames[i] = StrUtil.toUnderlineCase(fieldName);
            var getter = clazz.getMethod(BeanUtil.getterName(fieldName));
            getters[i] = getter;
        }
        var fieldSql = String.join(",", fieldNames);
        var sql = String.format("INSERT INTO %s (%s) VALUES (", tableName, fieldSql);
        List<String> sqlList = new ArrayList<>(beanList.size());
        beanList.forEach(bean -> {
            var valueSql = Arrays.stream(getters).map(getter -> {
                try {
                    return getter.invoke(bean);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).map(SqlUtil::valueToSql).collect(Collectors.joining(","));
            sqlList.add(sql + valueSql + ");");
        });
        return sqlList;
    }
}
