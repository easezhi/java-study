package study.cnbm.clrp.model;

import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@ExcelEntity(title = {
    "协议编号","原始版本编号","签约主体",
    "协议金额","币种","协议类型",
    "商务人员","销售员账号","销售人员","创建人账号","创建人","创建时间",
    "申请部门id","申请部门","审批完成时间",
    "签约单位","合作方协议编号","协议名称"
})
@Data
public class Protocol implements ExcelParseEntity {

    @ExcelColumn("协议编号")
    private String agreementNo;

    @ExcelColumn("原始版本编号")
    private String agreementGroup;

    @ExcelColumn("协议类型")
    private String agreementType;

    @ExcelColumn("签约主体")
    private String corp;

    @ExcelColumn("签约单位")
    private String signParty;

    @ExcelColumn("合作方协议编号")
    private String partyAgreementNo;

    @ExcelColumn("协议名称")
    private String agreementName;

    @ExcelColumn("协议金额")
    private BigDecimal agreementAmt;

    @ExcelColumn("币种")
    private String currencyType;

    @ExcelColumn("商务人员账号")
    private String businessManLogin;

    @ExcelColumn("商务人员")
    private String businessMan;

    @ExcelColumn("销售员账号")
    private String salesmanLogin;

    @ExcelColumn("销售人员")
    private String salesman;

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

    @ExcelColumn("审批完成时间")
    private LocalDateTime effectTime;
}
