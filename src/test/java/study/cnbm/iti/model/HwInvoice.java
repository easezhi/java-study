package study.cnbm.iti.model;

import com.alibaba.fastjson2.annotation.JSONField;
import easezhi.study.data.excel.ExcelColumnMapType;
import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
@ExcelEntity(columnMapType = ExcelColumnMapType.COLUMN_NAME)
public class HwInvoice implements ExcelParseEntity {

    @ExcelColumn(value = "发票类型")
    private String invoiceTypeName;

    @ExcelColumn(value = "发票代码", required = true)
    private String fillInvoiceCode;

    @ExcelColumn(value = "发票号码", required = true)
    private String invoiceNumber;

    @ExcelColumn(value = "客户订单号")
    private String customerPoNumber;

    // 供应商订单号
    @ExcelColumn(value = "华为订单号", required = true)
    private String contractNumber;

    @ExcelColumn(value = "订单名称")
    private String contractName;

    // 公司主体
    @ExcelColumn(value = "签约经销商", required = true)
    private String fullName;

    // 供应商
    @ExcelColumn(value = "华为签约主体")
    private String companyName;

    @ExcelColumn(value = "发票开具日期")
    private String taxInvoiceDateStr;

//    @ExcelColumn(value = "贷项发票的原发票号")
//    private String originInvoiceNo;

    @ExcelColumn(value = "发票金额", required = true)
    private String totalAmount;

    @ExcelColumn(value = "折扣金额")
    private String disAmount;

//    @ExcelColumn(value = "总税金")
    private String vatAmount = "0";

//    @ExcelColumn(value = "备注")
//    private String remark;

    @JSONField(serialize = false)
    @ExcelColumn(value = "服务税率")
    private String taxRate;

    @ExcelColumn(value = "申请编号")
    private String applicationCode;

    @ExcelColumn(value = "申请类型", required = true)
    private String applicationType;

    @ExcelColumn(value = "申请单来源")
    private String applicationSourceName;

    @ExcelColumn(value = "申请单标识")
    private String applicationFlagName;

    @ExcelColumn(value = "申请时间")
    private String applicationDateStr;

//    @ExcelColumn(value = "发票申请状态")
    private String statusName = "待签收";

    @ExcelColumn(value = "快递单号", required = true)
    private String dhlNo;

    @ExcelColumn(value = "寄送日期")
    private String sendDateStr;

    @ExcelColumn(value = "签收日期")
    private String signDateStr;

//    @ExcelColumn(value = "齐套标识")
//    private String fullShipment;
//
//    @ExcelColumn(value = "签收单号")
//    private String signNo;

    List<HwInvoiceItem> invoiceItem;
}
