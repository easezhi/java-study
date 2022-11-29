package study.cnbm.clrp.model;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PostLetter {

    // 合同编号，带版本号的
    private String salesContractNo;

    // 原始版本合同编号
    private String originalContractNo;

    // 单据类型（来源）
    private String source;

    // 关联业务编号
    private String relatedOrder;

    // 签约主体，所属公司
    private String signSubject;

    // 客户名称
    private String contractCustomerName;

    // 合同商务人员
    private String contractBusinessMan;

    // 合同销售人员
    private String contractSalesMan;

    // 创建人
    private String contractCreateName;

    // 合同创建时间
    private LocalDateTime contractCreateTime;

    // 申请部门
    private String salesApplyDept;

    // 返点后合同金额
    private BigDecimal rebatedContractAmt;

    // 合同模板
    private String contractTemplate;

    // 合同类型
    private String salesContractType;

    // 项目名称
    private String entryName;

    // 审批状态
    private String approvalStatus;
}
