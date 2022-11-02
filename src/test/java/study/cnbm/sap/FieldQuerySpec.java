package study.cnbm.sap;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

@Data
public class FieldQuerySpec {
    @JSONField(name = "FIELDNAME")
    String fieldName;

    @JSONField(name = "FIELDTYPE")
    String fieldType;
}
