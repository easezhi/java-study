package study.cnbm.sap.viewquery;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;

import java.util.List;

/**
 * 接受视图查询的相应结果。用 fastjson 反序列号
 * @param <E> 视图对应的实体类
 */
@Data
public class ViewQueryResult<E> {
    @JSONField(name = "STATUS")
    String status;

    // 单据列表
    @JSONField(name = "RESULT")
    List<E> result;
}
