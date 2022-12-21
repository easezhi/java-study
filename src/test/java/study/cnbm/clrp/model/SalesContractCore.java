package study.cnbm.clrp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SalesContractCore {

    private String contractNo;

    // 销售合同原始版本号
    private String contractGroup;

    // 销售订单号
    private String salesOrderNo;

    // 客户编号
    private String customerId;

    // 客户名称
    private String customer;

    // 开票客户编号
    private String invoiceCustomerId;

    // 开票客户
    private String invoiceCustomer;

    // 最终客户名称
    private String finalCustomer;

    // 所属公司
    private String corp;

    // 事业部部门id
    private String salesDeptKid;

    // 事业部
    private String salesDept;

    // 销售部门id
    private String salesOrgKid;

    // 销售部门
    private String salesOrg;

    // 利润中心
    private String profitCenter;

    // 产品线
    private String productLine;

    // 合同类型
    private String contractType;

    // 合同大类
    private String contractClass;

    // 是否集成服务合同
    private Boolean isService;

    // 项目名称
    private String projectName;

    // 开票税率
    private Integer taxRate;

    // 应收创建方式
    private String receivableType;

    // 合同模板
    private String template;

    // 签订日期
    private LocalDate signDate;

    // 合同金额
    private BigDecimal contractAmt;

    // 返点金额
    private BigDecimal rebateAmt;

    // 返点后合同金额
    private BigDecimal exRebateAmt;

    // 币种
    private String currencyType;

    // 商务账号
    private String businessManLogin;

    // 商务
    private String businessMan;

    // 销售人员账号
    private String salesmanLogin;

    // 销售人员
    private String salesman;

    // 创建人账号
    private String createBy;

    // 创建人
    private String creatorName;

    // 创建时间
    private LocalDateTime createTime;

    // 生效时间
    private LocalDateTime effectTime;

    // 审批状态
    private Integer approvalStatus;

    // 单据状态
    private Integer orderStatus;

    private Integer isLast;

    private Integer isLastValid;

    // 关联合同编号
    private String relationContract;

    // 是否是关联合同
    private Integer isRelation;

    // 电商订单编号
    private String ecommerceOrder;
}
