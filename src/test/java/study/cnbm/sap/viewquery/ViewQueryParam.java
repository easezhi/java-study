package study.cnbm.sap.viewquery;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 视图查询接口的参数。使用 fastjson 序列号
 */
@Data
public class ViewQueryParam {
    @JSONField(name = "SQL")
    private String sql;

    @JSONField(name = "FIELDS")
    private List<FieldQueryParam> fields;

    public void setFields(List<SapFieldSpec> fieldSpecs) {
        fields = fieldSpecs.stream().map(spec -> {
            var field = new FieldQueryParam();
            field.setFieldName(spec.getFieldName());
            field.setFieldType(spec.getFieldType());
            return field;
        }).toList();
    }
}
