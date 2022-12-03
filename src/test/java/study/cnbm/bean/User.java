package study.cnbm.bean;

import easezhi.study.data.excel.ExcelColumnMapType;
import easezhi.study.data.excel.ExcelParseEntity;
import easezhi.study.data.excel.annotation.ExcelColumn;
import easezhi.study.data.excel.annotation.ExcelEntity;
import lombok.Data;

@ExcelEntity(columnMapType = ExcelColumnMapType.COLUMN_NAME)
@Data
public class User implements ExcelParseEntity {

    @ExcelColumn("id")
    String kid;

    @ExcelColumn("账号")
    String login;

    @ExcelColumn("姓名")
    String name;
}
