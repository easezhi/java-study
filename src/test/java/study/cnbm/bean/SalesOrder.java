package study.cnbm.bean;

import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

import java.time.LocalDate;

@Data
@ExcelEntity(title = {"销售合同号", "合同类型", "签订日期"})
public class SalesOrder implements ExcelParseEntity {
    @ExcelColumn("销售合同号")
    String contractNo;

    @ExcelColumn("合同类型")
    String orderType;

    @ExcelColumn("签订日期")
    private LocalDate signingDate;
}
