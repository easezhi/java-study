package study.cnbm.sap.viewquery;

import lombok.Data;

/**
 * 实体类与 SAP 视图字段的映射信息。
 * 作用类似 PropertyDescriptor，只保留针对 SAP 查询需要的字段信息。
 */
@Data
public class SapFieldSpec {

    // SAP 视图字段名
    private String fieldName;

    // SAP 字段类型
    private String fieldType;

    // 实体类属性名。（现在注解都加在字段上，所以属性必须有对应的字段）
    private String propertyName;
}
