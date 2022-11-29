package study.cnbm.clrp.model;

import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExcelEntity(title = {
    "返点冲抵编号","销售合同号","客户","客户编号","所属公司编号",
    "商务人员账号","商务人员","销售人员","销售员账号","销售部门id","销售部门",
    "创建人账号","创建人","创建时间","申请部门id","申请部门",
    "销售合同金额","销售合同返点后金额","合同使用返点金额","返点冲抵金额","币种"
})
@Data
public class RebateOffset implements ExcelParseEntity {

    @ExcelColumn("返点冲抵编号")
    private String credenceNo;

    @ExcelColumn("销售合同号")
    private String salesContractNo;

    @ExcelColumn("客户")
    private String customer;

    @ExcelColumn("客户编号")
    private String customerId;

    @ExcelColumn("所属公司编号")
    private String corp;

    @ExcelColumn("商务人员账号")
    private String businessManLogin;

    @ExcelColumn("商务人员")
    private String businessMan;

    @ExcelColumn("销售人员")
    private String salesman;

    @ExcelColumn("销售员账号")
    private String salesmanLogin;

    @ExcelColumn("销售部门id")
    private String salesOrgKid;

    @ExcelColumn("销售部门")
    private String salesOrg;

    @ExcelColumn("创建人账号")
    private String createBy;

    @ExcelColumn("创建人")
    private String creatorName;

    @ExcelColumn("创建时间")
    private LocalDateTime createTime;

    @ExcelColumn("申请部门id")
    private String creatorOrgKid;

    @ExcelColumn("申请部门")
    private String creatorOrg;

    @ExcelColumn("销售合同金额")
    private BigDecimal contractAmt;

    @ExcelColumn("销售合同返点后金额")
    private BigDecimal contractRebatedAmt;

    @ExcelColumn("合同使用返点金额")
    private BigDecimal rebateAmt;

    @ExcelColumn("返点冲抵金额")
    private BigDecimal rebateOffsetAmt;

    @ExcelColumn("币种")
    private String currencyType;
}
