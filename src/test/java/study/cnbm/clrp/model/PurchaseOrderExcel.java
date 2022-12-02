package study.cnbm.clrp.model;

import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PurchaseOrderExcel implements ExcelParseEntity {

    @ExcelColumn(value = "采购订单号")
    private String orderNo;

    @ExcelColumn(value = "供应商订单号")
    private String supplierOrder;

    @ExcelColumn(value = "供应商编号")
    private String supplierId;

    @ExcelColumn(value = "供应商名称")
    private String supplier;

    @ExcelColumn("LIFNR_NAME")
    String supplier1;

    @ExcelColumn("LIFNR_NAME2")
    String supplier2;

    @ExcelColumn(value = "所属公司")
    private String corp;

    @ExcelColumn(value = "采购组织")
    private String purchaseOrg;

    @ExcelColumn(value = "采购组")
    private String purchaseGroup;

    @ExcelColumn(value = "利润中心")
    private String profitCenter;

    @ExcelColumn(value = "产品线")
    private String productLine;

    @ExcelColumn(value = "采购合同类型")
    private String orderType;

    @ExcelColumn(value = "合同金额")
    private BigDecimal orderAmt;

    private BigDecimal exRebateAmt;

    @ExcelColumn(value = "设备采购金额")
    private BigDecimal deviceAmt;

    @ExcelColumn(value = "服务采购金额")
    private BigDecimal serviceAmt;

    @ExcelColumn(value = "使用返点金额")
    private BigDecimal rebateAmt;

    private BigDecimal deviceRebate;

    private BigDecimal serviceRebate;

    @ExcelColumn(value = "币种")
    private String currencyType;

    @ExcelColumn(value = "运输方式")
    private String transportType;

    @ExcelColumn(value = "是否标准合同")
    private String isStandard;

    @ExcelColumn(value = "销售员姓名")
    private String salesman;

    @ExcelColumn(value = "二级经销商")
    private String dealer;

    @ExcelColumn(value = "最终客户")
    private String finalCustomer;

    @ExcelColumn(value = "商务人员")
    private String businessMan;

    @ExcelColumn(value = "创建人账号")
    private String createBy;

    @ExcelColumn(value = "创建时间")
    private LocalDateTime createTime;

    @ExcelColumn(value = "项目名称")
    private String projectName;
}
