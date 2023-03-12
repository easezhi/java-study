package study.cnbm.sap.viewquery;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

/**
 * 向 SAP 查询接口传输的 dto 类，使用 fastjson 序列号
 */
@Data
public class FieldQueryParam {
    @JSONField(name = "FIELDNAME")
    String fieldName;

    @JSONField(name = "FIELDTYPE")
    String fieldType;
}
