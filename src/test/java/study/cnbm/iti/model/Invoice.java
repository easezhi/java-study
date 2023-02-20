package study.cnbm.iti.model;

import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@ExcelEntity(title = {
    "发票类型","发票代码","发票号",
    "客户订单号","华为订单号","订单名称","签约经销商","华为签约主体",
    "发票开具日期","贷项发票的原发票号","发票金额","折扣金额（含税）","折扣金额（未税）","总税金","备注",
    "申请编号","申请类型","申请单来源","申请单标识","申请时间","发票申请状态",
    "快递单号","寄送日期","签收日期","齐套标识","签收单号",
    "创建时间","税率"
},
    dateFormat = "yyyy/MM/dd", dateTimeFormat = "yyyy/MM/dd HH:mm:ss"
//    dateFormat = "MM/dd/yyyy", dateTimeFormat = "MM/dd/yyyy HH:mm:ss"
)
public class Invoice implements ExcelParseEntity {

    @ExcelColumn(value = "发票类型")
    private String invoiceTypeName;

    @ExcelColumn(value = "发票代码")
    private String invoiceCode;

    @ExcelColumn(value = "发票号")
    private String invoiceNo;

    @ExcelColumn(value = "客户订单号")
    private String customerPoNo;

    // 供应商订单号
    @ExcelColumn(value = "华为订单号")
    private String supplierOrder;

    @ExcelColumn(value = "订单名称")
    private String contractName;

    private String companyCode;

    // 公司主体
    @ExcelColumn(value = "签约经销商")
    private String companyName;

    private String supplierCode;

    // 供应商
    @ExcelColumn(value = "华为签约主体")
    private String supplierName;

    @ExcelColumn(value = "发票开具日期")
    private LocalDate invoiceDate;

    @ExcelColumn(value = "贷项发票的原发票号")
    private String originInvoiceNo;

    @ExcelColumn(value = "发票金额")
    private BigDecimal invoiceAmt;

    @ExcelColumn(value = "折扣金额（含税）")
    private BigDecimal discountAmt;

    @ExcelColumn(value = "折扣金额（未税）")
    private BigDecimal netDiscountAmt;

    @ExcelColumn(value = "总税金")
    private BigDecimal taxAmt;

    @ExcelColumn(value = "税率")
    private String taxRate;

    @ExcelColumn(value = "备注")
    private String remark;

    @ExcelColumn(value = "申请编号")
    private String applyCode;

    @ExcelColumn(value = "申请类型")
    private String applyType;

    // 蓝票、红票
    private String invoiceFinanceType;

    @ExcelColumn(value = "申请单来源")
    private String applySource;

    @ExcelColumn(value = "申请单标识")
    private String applyFlag;

    @ExcelColumn(value = "申请时间")
    private LocalDateTime applyTime;

    @ExcelColumn(value = "发票申请状态")
    private String applyStatus;

    @ExcelColumn(value = "快递单号")
    private String expressNo;

    @ExcelColumn(value = "寄送日期")
    private LocalDate sendDate;

    @ExcelColumn(value = "签收日期")
    private LocalDate signDate;

    @ExcelColumn(value = "齐套标识")
    private String fullShipment;

    @ExcelColumn(value = "签收单号")
    private String signNo;

    // 采购订单号
    private String poNo;

    // 商务人员
    private String businessName;

    // 发票状态
    private String invoiceStatus = "1";

    // 过账状态
    private String postStatus = "1";

    @ExcelColumn(value = "创建时间")
    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
