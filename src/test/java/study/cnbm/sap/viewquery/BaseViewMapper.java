package study.cnbm.sap.viewquery;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.TypeUtil;
import com.alibaba.fastjson2.annotation.JSONField;
import easezhi.study.lang.SFunction;
import easezhi.study.lang.bean.BeanUtil;
import lombok.Getter;
import study.cnbm.sap.annotation.SapField;
import study.cnbm.sap.annotation.SapView;

import java.beans.Introspector;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 视图实体类的封装类，提供了视图查询的通用、辅助功能：
 *   缓存了通过实体类和注解解析到的视图字段映射信息
 *   提供了简单的视图查询功能
 * @param <V> 声明视图字段结构的实体类
 */
public class BaseViewMapper<V> {
    // 实体类
    private final Class<V> viewClazz;

    // SAP 视图名
    @Getter
    private final String viewName;

    // SAP 集团号
    @Getter
    private final String sapClient = "550";

    // 视图 Bean 定义的属性、字段，包括从父类继承的字段，用户获取注解信息
    private final List<Field> fields;

    // 解析以后缓存的 SAP 视图字段映射信息
    @Getter
    private List<SapFieldSpec> fieldSpecList;

    protected BaseViewMapper() {
        // 通过泛型参数获取实体类型
        viewClazz = (Class) TypeUtil.getTypeArgument(this.getClass());
        fields = BeanUtil.getAllFields(viewClazz);
        cacheFieldList();

        var viewAnno = viewClazz.getAnnotation(SapView.class);
        Assert.notNull(viewAnno, viewClazz.getSimpleName() + "缺少视图注解");
        viewName = viewAnno.value();
    }

    // 用实体类的 getter 方法引用声明要查询的字段
    public List<SapFieldSpec> getFieldSpecList(List<SFunction<V, Object>> getters) {
        List<SapFieldSpec> select = new ArrayList<>();
        for (var getter: getters) {
            // 获取方法引用对用的属性名
            var propName = BeanUtil.getPropertyName(getter);
            var fieldSpec = CollUtil.findOne(fieldSpecList, spec -> spec.getPropertyName().equals(propName));
            Assert.notNull(fieldSpec, "字段" + propName + "在视图中不存在");
            select.add(fieldSpec);
        }
        return select;
    }

    // 解析实体类的属性和字段上加的注解，获取 SAP 视图的字段信息
    // mapper 类一般是单实例，这些信息适合缓存下来
    void cacheFieldList() {
        fieldSpecList = new ArrayList<>();
        try {
            var propertyArr = Introspector.getBeanInfo(viewClazz, Object.class).getPropertyDescriptors();
            for (var prop: propertyArr) {
                var propName = prop.getName();
                // 字段映射的注解都加在字段上，所以需要找到对应的字段，包括父类定义的私有字段
                var field = CollUtil.findOne(fields, f -> f.getName().equals(propName));
                if (field == null) {
                    continue;
                }
                var fieldAnno = field.getAnnotation(SapField.class);
                // 只有加注解，才视为视图字段
                if (fieldAnno == null) {
                    continue;
                }
                // SAP 视图字段名取值优先级，SapField > JSONField > PropertyDescriptor
                String fieldType = fieldAnno.fieldType();
                String fieldName = fieldAnno.fieldName();
                if (fieldName.length() == 0) {
                    var jsonAnno = field.getAnnotation(JSONField.class);
                    if (jsonAnno != null) {
                        fieldName = jsonAnno.name();
                    }
                }
                if (fieldName.length() == 0){
                    fieldName = propName;
                }
                if (fieldType.length() == 0){
                    fieldType = fieldName;
                }
                var fieldSpec = new SapFieldSpec();
                fieldSpec.setFieldName(fieldName);
                fieldSpec.setFieldType(fieldType);
                fieldSpec.setPropertyName(propName);
                fieldSpecList.add(fieldSpec);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
