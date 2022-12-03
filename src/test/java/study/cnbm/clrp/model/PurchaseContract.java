package study.cnbm.clrp.model;

import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExcelEntity(title = {
    "采购合同号","采购合同原始版本号","采购订单号",
    "供应商订单号","供应商编号","供应商名称",
    "所属公司","采购组织","采购组","事业部","利润中心组","利润中心","产品线",
    "采购合同类型","合同金额","返点后合同金额","设备采购金额","服务采购金额","使用返点金额","币种","运输方式","是否标准合同",
    "销售员账号","销售员姓名","销售部门id","销售部门","销售小组编号","二级经销商","最终客户","商务人员",
    "创建人部门id","创建人部门","创建人","创建人账号","创建时间","审批生效时间",
    "是否需要归档",
    "审批状态", "单据状态", "是否最新版本",
    "项目名称"
})
@Data
public class PurchaseContract implements ExcelParseEntity {

    @ExcelColumn(value = "采购合同号")
    private String contractNo;

    @ExcelColumn(value = "采购合同原始版本号")
    private String contractGroup;

    @ExcelColumn(value = "采购订单号")
    private String orderNo;

    private Integer orderSrc = 1;

    @ExcelColumn(value = "供应商订单号")
    private String supplierOrder;

    @ExcelColumn(value = "供应商编号")
    private String supplierId;

    @ExcelColumn(value = "供应商名称")
    private String supplier;

    @ExcelColumn(value = "所属公司")
    private String corp;

    @ExcelColumn(value = "采购组织")
    private String purchaseOrg;

    @ExcelColumn(value = "采购组")
    private String purchaseGroup;

    @ExcelColumn(value = "事业部")
    private String division;

    @ExcelColumn(value = "利润中心组")
    private String profitGroup;

    @ExcelColumn(value = "利润中心")
    private String profitCenter;

    @ExcelColumn(value = "产品线")
    private String productLine;

    @ExcelColumn(value = "采购合同类型")
    private String orderType;

    @ExcelColumn(value = "合同金额")
    private BigDecimal orderAmt;

    @ExcelColumn(value = "返点后合同金额")
    private BigDecimal exRebateAmt;

    @ExcelColumn(value = "设备采购金额")
    private BigDecimal deviceAmt;

    @ExcelColumn(value = "服务采购金额")
    private BigDecimal serviceAmt;

    @ExcelColumn(value = "使用返点金额")
    private BigDecimal rebateAmt;

    @ExcelColumn(value = "币种")
    private String currencyType;

    @ExcelColumn(value = "运输方式")
    private String transportType;

    @ExcelColumn(value = "是否标准合同")
    private String isStandard;

    @ExcelColumn(value = "销售员账号")
    private String salesmanLogin;

    @ExcelColumn(value = "销售员姓名")
    private String salesman;

    @ExcelColumn(value = "销售部门id")
    private String salesOrgKid;

    @ExcelColumn(value = "销售部门")
    private String salesOrg;

    @ExcelColumn(value = "销售小组编号")
    private String salesGroup;

    @ExcelColumn(value = "二级经销商")
    private String dealer;

    @ExcelColumn(value = "最终客户")
    private String finalCustomer;

    @ExcelColumn(value = "商务人员")
    private String businessMan;

    private String businessManLogin;

    @ExcelColumn(value = "创建人部门id")
    private String creatorOrgKid;

    @ExcelColumn(value = "创建人部门")
    private String creatorOrg;

    @ExcelColumn(value = "创建人")
    private String creatorName;

    @ExcelColumn(value = "创建人账号")
    private String createBy;

    @ExcelColumn(value = "创建时间")
    private LocalDateTime createTime;

    @ExcelColumn(value = "审批状态")
    private Integer approvalStatus;

    @ExcelColumn(value = "单据状态")
    private Integer orderStatus;

    @ExcelColumn(value = "是否最新版本")
    private Integer isLast;

    private Integer versionSn;

    @ExcelColumn(value = "审批生效时间")
    private LocalDateTime effectTime;

    @ExcelColumn(value = "是否需要归档")
    private String needArchive;

    @ExcelColumn(value = "项目名称")
    private String projectName;
}
