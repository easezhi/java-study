package study.cnbm.clrp.model;

import easezhi.study.data.excel.ExcelColumnMapType;
import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExcelEntity(columnMapType = ExcelColumnMapType.COLUMN_NAME)
@Data
public class PurchaseOrderExcel implements ExcelParseEntity {

    // 采购订单号
    @ExcelColumn(value = "EBELN")
    private String orderNo;

    // 供应商订单号
    @ExcelColumn(value = "ZZPO")
    private String supplierOrder;

    // 供应商编号
    @ExcelColumn(value = "LIFNR")
    private String supplierId;

    // 供应商名称
    private String supplier;

    // 所属公司
    @ExcelColumn(value = "BUKRS")
    private String corp;

    // 采购组织
    @ExcelColumn(value = "EKORG")
    private String purchaseOrg;

    // 采购组
    @ExcelColumn(value = "EKGRP")
    private String purchaseGroup;

    // 利润中心
    @ExcelColumn(value = "ZZCP")
    private String profitCenter;

    // 产品线
    @ExcelColumn(value = "ZSPL")
    private String productLine;

    // 采购合同类型
    @ExcelColumn(value = "BSART")
    private String orderType;

    // 合同金额
    @ExcelColumn(value = "ZZZJY")
    private BigDecimal orderAmt;

    private BigDecimal exRebateAmt;

    public BigDecimal getExRebateAmt() {
        return orderAmt.subtract(getRebateAmt());
    }

    // 设备采购金额
    @ExcelColumn(value = "ZSBZJE")
    private BigDecimal deviceAmt;

    // 服务采购金额
    @ExcelColumn(value = "ZFWZJE")
    private BigDecimal serviceAmt;

    // 使用返点金额
    private BigDecimal rebateAmt;

    public BigDecimal getRebateAmt() {
        return deviceRebateAmt.add(serviceRebateAmt);
    }

    // 设备返点金额
    @ExcelColumn(value = "ZZSBFD")
    private BigDecimal deviceRebateAmt;

    // 服务返点金额
    @ExcelColumn(value = "ZZFWFD")
    private BigDecimal serviceRebateAmt;

    // 币种
    @ExcelColumn(value = "WAERS")
    private String currencyType;

    // 运输方式
    @ExcelColumn(value = "ZZYS")
    private String transportType;

    // 是否标准合同
    @ExcelColumn(value = "ZSTANDARD")
    private String isStandard;

    // 销售员姓名
    @ExcelColumn(value = "ZZSQR")
    private String salesman;

    // 二级经销商
    @ExcelColumn(value = "ZZEJ")
    private String dealer;

    // 最终客户
    @ExcelColumn(value = "ZZKH")
    private String finalCustomer;

    // 商务人员
    @ExcelColumn(value = "ZSWRY")
    private String businessMan;

    // 创建人
    @ExcelColumn(value = "ERNAM")
    private String creatorName;

    // 创建时间
    @ExcelColumn(value = "BEDAT")
    private LocalDateTime createTime;

    // 项目名称
    @ExcelColumn(value = "ZZXM")
    private String projectName;
}
