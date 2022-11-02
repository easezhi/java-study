package study.cnbm.sap;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class ViewQueryParam {
    @JSONField(name = "SQL")
    String sql;

    @JSONField(name = "FIELDS")
    List<FieldQuerySpec> fields;
}
