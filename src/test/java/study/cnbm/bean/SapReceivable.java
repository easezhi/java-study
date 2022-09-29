package study.cnbm.bean;

import easezhi.study.data.db.SqlFunNameEnum;
import easezhi.study.excel.ExcelParseEntity;
import easezhi.study.excel.annotation.ExcelColumn;
import easezhi.study.excel.annotation.ExcelEntity;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;


@Data
@ExcelEntity(title = {"业务应收编号", "客户编号", "客户名称", "销售合同编号", "销售订单号", "所属公司编码", "所属部门名称", "销售员账号",
    "销售员姓名", "应收类型", "应收金额", "未核销金额", "币种", "到期时间", "销售合同类型", "销售合同签订日期"})
public class SapReceivable implements ExcelParseEntity {

    @ExcelColumn("业务应收编号")
    private String receivableNo;

    @ExcelColumn("客户编号")
    private String customerNo;

    @ExcelColumn("客户名称")
    private String customerName;

    @ExcelColumn("销售合同编号")
    private String contractNo;

    @ExcelColumn("销售订单号")
    private String orderId;

    @ExcelColumn("所属公司编码")
    private String companyCode;

    private String companyName;

    @ExcelColumn("所属部门名称")
    private String deptName;

    @ExcelColumn("销售员账号")
    private String saleAccount;

    @ExcelColumn("销售员姓名")
    private String saleName;

    @ExcelColumn("应收类型")
    private String receivableType;

    @ExcelColumn("应收金额")
    private BigDecimal receivableAmt;

    @ExcelColumn("未核销金额")
    private BigDecimal unwrittenOffAmt;

    @ExcelColumn("币种")
    private String currency;

    @ExcelColumn("到期时间")
    private LocalDate dueDate;

    @ExcelColumn("销售合同签订日期")
    private LocalDate signingDate;

    @ExcelColumn("销售合同类型")
    private String contractType;

    private SqlFunNameEnum createTime = SqlFunNameEnum.CURRENT_TIMESTAMP;

    private SqlFunNameEnum updateTime = SqlFunNameEnum.CURRENT_TIMESTAMP;
}
