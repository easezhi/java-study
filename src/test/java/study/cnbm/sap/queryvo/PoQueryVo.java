package study.cnbm.sap.queryvo;

import lombok.Data;
import study.cnbm.sap.annotation.SapQuery;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PoQueryVo {

    @SapQuery
    private String poNo;

    @SapQuery(type = SapQuery.Type.IN, propName = "poNo", wideChar = true)
    private List<String> poNos;

    @SapQuery(type = SapQuery.Type.LIKE, wideChar = true)
    private String supplierOrder;

    @SapQuery(type = SapQuery.Type.NOT_NULL, propName = "businessName")
    private Boolean hasBusinessMan;

    @SapQuery(type = SapQuery.Type.GE)
    private BigDecimal orderAmt;

    @SapQuery(type = SapQuery.Type.IS_NULL)
    private String companyCode;
}
