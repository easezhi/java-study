package easezhi.study.data.db;

import cn.hutool.core.util.StrUtil;
import easezhi.study.datastructure.ListUtil;
import easezhi.study.lang.bean.BeanUtil;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SqlBuilder<E> {
    String tableName;

    Class<E> beanClazz;

    String[] fieldNames;

    Method[] getters;

    public static <E> SqlBuilder<E> builder(Class<E> clazz, String tableName) throws Exception {
        var builder = new SqlBuilder<E>();
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

        builder.tableName = tableName;
        builder.beanClazz = clazz;
        builder.fieldNames = fieldNames;
        builder.getters = getters;
        return builder;
    }

    public StringBuilder buildBatchInsertSql(List<?> beanList, int batch) {
        var sqlBu = new StringBuilder();
        var batchList = ListUtil.partition(beanList, batch);
        batchList.forEach(bat -> {
            buildBatchInsertSql(bat, sqlBu);
            sqlBu.append("\n--\n");
        });
        return sqlBu;
    }

    public StringBuilder buildBatchInsertSql(List<?> beanList) {
        var sqlBu = new StringBuilder();
        buildBatchInsertSql(beanList, sqlBu);
        return sqlBu;
    }

    public void buildBatchInsertSql(List<?> beanList, StringBuilder sqlBu) {
        sqlBu.append(String.format("INSERT INTO %s (%s) VALUES \n", tableName, String.join(",", fieldNames)));
        int total = beanList.size();
        for (var i = 0; i < total; i++) {
            var bean = beanList.get(i);
            var valueSql = Arrays.stream(getters).map(getter -> {
                try {
                    return getter.invoke(bean);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }).map(SqlUtil::valueToSql).collect(Collectors.joining(","));
            sqlBu.append("(")
                .append(valueSql)
                .append(")")
                .append(i < (total - 1) ? ",\n" : ";");
        }
    }
}
