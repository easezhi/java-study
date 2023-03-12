package study.cnbm.sap.view;

import com.alibaba.fastjson2.annotation.JSONField;
import lombok.Data;
import study.cnbm.sap.annotation.SapField;
import study.cnbm.sap.annotation.SapView;

import java.math.BigDecimal;

@Data
@SapView("ZVMM_PO_HEADER")
public class PoHeaderView {
    // 采购订单号
    @SapField(fieldType = "EBELN")
    @JSONField(name = "EBELN")
    private String poNo;

    // 供应商订单号
    @SapField(fieldType = "ZEPO")
    @JSONField(name = "ZZPO")
    private String supplierOrder;

    // 所属公司
    @SapField(fieldType = "BUKRS")
    @JSONField(name = "BUKRS")
    private String companyCode;

    // 订单总金额
    @SapField(fieldType = "ZEZJY")
    @JSONField(name = "ZZZJY")
    private BigDecimal orderAmt;

    // 币种
    @SapField(fieldType = "WAERS")
    @JSONField(name = "WAERS")
    private String currency;

    // 商务人员姓名
    @SapField(fieldType = "ZESWRY")
    @JSONField(name = "ZSWRY")
    private String businessName;

}
