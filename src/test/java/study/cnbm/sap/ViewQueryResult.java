package study.cnbm.sap;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

@Data
public class ViewQueryResult<E> {
    @JSONField(name = "STATUS")
    String status;

    @JSONField(name = "RESULT")
    List<E> result;
}
