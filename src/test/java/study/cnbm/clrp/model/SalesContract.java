package study.cnbm.clrp.model;

import easezhi.study.data.excel.ExcelParseEntityImpl;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@ExcelEntity(title = {
    "销售合同号","销售合同原始版本号","销售订单号",
    "客户编号","客户名称","开票客户编号","开票客户","最终客户名称",
    "所属公司","事业部部门id","事业部","销售部门id","销售部门","利润中心","产品线",
    "合同类型","合同大类","是否集成服务合同","项目名称","开票税率","应收创建方式","合同模板","签订日期",
    "合同金额","返点金额","币种",
    "商务账号","商务","销售人员账号","销售人员","创建人账号","创建人",
    "创建时间","生效时间","审批状态","单据状态","关联合同编号","是否是关联合同",
    "电商订单编号","是否需要归档",
    "寄出拉取","收取拉取","归档拉取"
})
@Data
public class SalesContract extends ExcelParseEntityImpl {
    @ExcelColumn(value = "销售合同号")
    private String contractNo;

    @ExcelColumn(value = "销售合同原始版本号")
    private String contractGroup;

    @ExcelColumn(value = "销售订单号")
    private String salesOrderNo;

    @ExcelColumn(value = "客户编号")
    private String customerId;

    @ExcelColumn(value = "客户名称")
    private String customer;

    @ExcelColumn(value = "开票客户编号")
    private String invoiceCustomerId;

    @ExcelColumn(value = "开票客户")
    private String invoiceCustomer;

    @ExcelColumn(value = "最终客户名称", escapeCsvDelimiter = true, max = 500)
    private String finalCustomer;

    @ExcelColumn(value = "所属公司")
    private String corp;

    @ExcelColumn(value = "事业部部门id")
    private String salesDeptKid;

    @ExcelColumn(value = "事业部")
    private String salesDept;

    @ExcelColumn(value = "销售部门id")
    private String salesOrgKid;

    @ExcelColumn(value = "销售部门")
    private String salesOrg;

    @ExcelColumn(value = "利润中心")
    private String profitCenter;

    @ExcelColumn(value = "产品线")
    private String productLine;

    @ExcelColumn(value = "合同类型")
    private String contractType;

    @ExcelColumn(value = "合同大类")
    private String contractClass;

    @ExcelColumn(value = "是否集成服务合同")
    private Boolean isService;

    @ExcelColumn(value = "项目名称", escapeCsvDelimiter = true, max = 200)
    private String projectName;

    @ExcelColumn(value = "开票税率")
    private Integer taxRate;

    @ExcelColumn(value = "应收创建方式")
    private String receivableType;

    @ExcelColumn(value = "合同模板", max = 30)
    private String template;

    @ExcelColumn(value = "签订日期")
    private LocalDate signDate;

    @ExcelColumn(value = "合同金额")
    private BigDecimal contractAmt;

    @ExcelColumn(value = "返点金额")
    private BigDecimal rebateAmt;

    // 返点后合同金额
    private BigDecimal exRebateAmt;

    public BigDecimal getExRebateAmt() {
        return (contractAmt == null || rebateAmt == null) ? contractAmt : contractAmt.subtract(rebateAmt);
    }

    @ExcelColumn(value = "币种")
    private String currencyType;

    @ExcelColumn(value = "商务账号")
    private String businessManLogin;

    @ExcelColumn(value = "商务")
    private String businessMan;

    @ExcelColumn(value = "销售人员账号")
    private String salesmanLogin;

    @ExcelColumn(value = "销售人员")
    private String salesman;

    @ExcelColumn(value = "创建人账号")
    private String createBy;

    @ExcelColumn(value = "创建人")
    private String creatorName;

    @ExcelColumn(value = "创建时间")
    private LocalDateTime createTime;

    @ExcelColumn(value = "生效时间")
    private LocalDateTime effectTime;

    @ExcelColumn(value = "审批状态")
    private Integer approvalStatus;

    @ExcelColumn(value = "单据状态")
    private Integer orderStatus;

    private Integer isLast;

    private Integer isLastValid;

    @ExcelColumn(value = "关联合同编号")
    private String relationContract;

    @ExcelColumn(value = "是否是关联合同")
    private Integer isRelation;

    @ExcelColumn(value = "电商订单编号")
    private String ecommerceOrder;

    @ExcelColumn(value = "是否需要归档")
    private String needArchive;

    @ExcelColumn(value = "寄出拉取")
    private String sendPulled;

    @ExcelColumn(value = "收取拉取")
    private String receivePulled;

    @ExcelColumn(value = "归档拉取")
    private String archivePulled;
}
