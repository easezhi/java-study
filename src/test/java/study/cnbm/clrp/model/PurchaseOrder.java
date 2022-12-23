package study.cnbm.clrp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PurchaseOrder {

    private String contractGroup;

    public String getContractGroup() {
        return getOrderNo();
    }

    // 采购订单号
    private String orderNo;

    private Integer orderSrc = 2;

    // 供应商订单号
    private String supplierOrder;

    // 供应商编号
    private String supplierId;

    // 供应商名称
    private String supplier;

    // 所属公司
    private String corp;

    // 采购组织
    private String purchaseOrg;

    // 采购组
    private String purchaseGroup;

    // 利润中心
    private String profitCenter;

    // 产品线
    private String productLine;

    // 采购合同类型
    private String orderType;

    // 合同金额
    private BigDecimal orderAmt;

    private BigDecimal exRebateAmt;

    // 设备采购金额
    private BigDecimal deviceAmt;

    // 服务采购金额
    private BigDecimal serviceAmt;

    // 使用返点金额
    private BigDecimal rebateAmt;

    // 币种
    private String currencyType;

    // 运输方式
    private String transportType;

    // 是否标准合同
    private String isStandard;

    // 销售员姓名
    private String salesman;

    private String salesmanLogin;

    // 二级经销商
    private String dealer;

    // 最终客户
    private String finalCustomer;

    // 商务人员
    private String businessMan;

    private String businessManLogin;

    // 创建人账号
    private String createBy;

    // 创建人
    private String creatorName;

    // 创建时间
    private LocalDateTime createTime;

    private Integer approvalStatus = 2;

    private Integer orderStatus = 1;

    private Integer isLast = 1;

    private Integer isLastValid = 1;

    private LocalDateTime effectTime;

    // 项目名称
    private String projectName;

    private String needArchive;
}
