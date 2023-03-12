package study.cnbm.sap.viewquery;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.StrUtil;
import easezhi.study.lang.SFunction;
import easezhi.study.lang.bean.BeanUtil;
import study.cnbm.sap.annotation.SapQuery;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.List;

/**
 * SAP 视图查询的工具类
 */
public class QueryUtil {

    /**
     * 根据查询器数据，构造视图简单查询的参数。查询视图的所有字段
     * @param view 视图 Mapper
     * @param query 保护查询字段的查询器
     * @return SAP 视图查询接口的参数
     * @param <V> 视图对应的实体类
     */
    public static <V> ViewQueryParam buildSimpleQuery(BaseViewMapper<V> view, Object query) {
        return buildQueryParam(view, query, view.getFieldSpecList());
    }

    /**
     * 根据查询器数据，构造视图简单查询的参数。查询指定的字段
     * @param view 视图 Mapper
     * @param select 待查询的字段
     * @param query 保护查询字段的查询器
     * @return SAP 视图查询接口的参数
     * @param <V> 视图对应的实体类
     */
    public static <V> ViewQueryParam buildQueryWithSelect(BaseViewMapper<V> view, List<SFunction<V, Object>> select, Object query) {
        return buildQueryParam(view, query, view.getFieldSpecList(select));
    }

    static <V> ViewQueryParam buildQueryParam(BaseViewMapper<V> view, Object query, List<SapFieldSpec> fieldSpecs) {
        var queryParam = new ViewQueryParam();
        var fields = fieldSpecs.stream().map(SapFieldSpec::getFieldName).toList();
        List<String> wheres = query == null ? null : buildWhere(view, query);
        queryParam.setSql(buildSql(view.getViewName(), fields, wheres, view.getSapClient()));
        queryParam.setFields(fieldSpecs);
        return queryParam;
    }

    /**
     * 构造 SQL 查询语句
     * @param viewName 视图名
     * @param fields 查询的字段
     * @param wheres 查询条件 field = 'value'
     * @param sapClient SAP 集团号
     * @return SQL select 语句
     */
    public static String buildSql(String viewName, List<String> fields, List<String> wheres, String sapClient) {
        var sql = new StringBuilder("SELECT ");
        sql.append(String.join(",", fields))
            .append(" FROM ")
            .append(viewName)
            .append(" WHERE MANDT = '")
            .append(sapClient)
            .append("'");
        if (CollUtil.isNotEmpty(wheres)) {
            sql.append(" AND ")
                .append(String.join(" AND ", wheres));
        }
        return sql.toString();
    }

    /**
     * 解析查询器对象，构造查询条件
     * @param view 视图 Mapper
     * @param query 查询器对象
     * @return 查询条件列表
     * @param <V> 实体类
     */
    public static <V> List<String> buildWhere(BaseViewMapper<V> view, Object query) {
        var where = new ArrayList<String>();
        var queryFields = getQueryProperty(query.getClass());
        var viewFields = view.getFieldSpecList();
        queryFields.forEach(queryField -> {
            var value = getQueryValue(query, queryField);
            if (value == null) {
                return;
            }
            if (value instanceof CharSequence && StrUtil.isBlank((CharSequence) value)) {
                return;
            }
            var queryFieldName = queryField.getPropName();
            var viewFieldSpec = CollUtil.findOne(viewFields, spec -> spec.getPropertyName().equals(queryFieldName));
            Assert.notNull(viewFieldSpec, "查询的字段" + queryFieldName + "在视图中不存在");

            where.add(buildCondition(viewFieldSpec.getFieldName(), queryField.getType(), value, queryField.getWideChar()));
        });
        return where;
    }

    // 构造单字段的查询条件
    static String buildCondition(String fieldName, SapQuery.Type op, Object value, boolean wideChar) {
        String whereStr = "";
        switch (op) {
            case IN -> {
                if (value instanceof Iterable) {
                    var first = true;
                    var valBuilder = new StringBuilder(fieldName);
                    valBuilder.append(" IN (");
                    for (var val: (Iterable)value) {
                        if (!first) {
                            valBuilder.append(",");
                        } else {
                            first = false;
                        }
                        valBuilder.append(wrapValue(val, wideChar));
                    }
                    valBuilder.append(")");
                    whereStr = valBuilder.toString();
                } else {
                    throw new RuntimeException("字段" + fieldName + "的IN查询参数值不合法");
                }
            }
            case LIKE -> {
                var valStr = "'%" + value.toString() + "%'";
                if (wideChar) {
                    valStr = "N" + valStr;
                }
                whereStr = fieldName + " LIKE " + valStr;
            }
            case IS_NULL, NOT_NULL -> whereStr = fieldName + " " + op.getOperator();
            default -> whereStr = fieldName + " " + op.getOperator() + " " + wrapValue(value, wideChar);
        }
        return whereStr;
    }

    // 把字段值转换成 SQL 语句需要的格式
    static String wrapValue(Object value, boolean wideChar) {
        if (value instanceof Number) {
            return value.toString();
        } else {
            var valueStr = "'" + value.toString() + "'";
            return wideChar ? ("N" + valueStr) : valueStr;
        }
    }

    // 用 getter 函数从查询器对象获取查询字段的值
    static Object getQueryValue(Object query, QueryFieldSpec fieldSpec) {
        try {
            return fieldSpec.getGetter().invoke(query);
        } catch (Exception e) {
            throw new RuntimeException("字段" + fieldSpec.getPropName() + "取值出错", e);
        }
    }

    // 解析查询器和注解，获取所有查询条件字段的信息
    static List<QueryFieldSpec> getQueryProperty(Class queryClazz) {
        try {
            var fields = BeanUtil.getAllFields(queryClazz);
            var props = Introspector.getBeanInfo(queryClazz, Object.class).getPropertyDescriptors();
            List<QueryFieldSpec> queryFieldSpecs = new ArrayList<>();
            for (var prop: props) {
                if (prop.getReadMethod() == null) {
                    continue;
                }
                var propName = prop.getName();
                var field = CollUtil.findOne(fields, f -> f.getName().equals(propName));
                if (field == null) {
                    continue;
                }
                var queryAnno = field.getAnnotation(SapQuery.class);
                if (queryAnno == null) {
                    continue;
                }
                String queryName = queryAnno.propName().length() == 0 ? propName : queryAnno.propName();
                QueryFieldSpec queryFieldSpec = new QueryFieldSpec();
                queryFieldSpec.setPropName(queryName);
                queryFieldSpec.setGetter(prop.getReadMethod());
                queryFieldSpec.setType(queryAnno.type());
                queryFieldSpec.setWideChar(queryAnno.wideChar());
                queryFieldSpecs.add(queryFieldSpec);
            }
            return queryFieldSpecs;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
