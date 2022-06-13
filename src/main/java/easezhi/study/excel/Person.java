package easezhi.study.excel;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;
import lombok.experimental.Accessors;

import easezhi.study.excel.annotation.*;

@Data
@Accessors(chain = true)
//@ExcelEntity(title = {"姓名","年龄","性别","次数","小数","实数","大数","生日","时间","短日期"}, sheet = "员工",
//    dateTimeFormat = "yyyy-MM-dd HH:mm:ss")
@ExcelEntity(title = {"姓名","年龄","性别","次数","小数","实数","大数","生日","时间"},
    sheet = "员工", freezeTitle = true,
    dateTimeFormat = "yyyy-MM-dd HH:mm:ss", simplifyErrorMsg = true)
public class Person implements ExcelParseEntity {
    @ExcelColumn(value = "姓名", required = true, max = 5)
    private String name;

    @ExcelColumn("性别")
    private String sex;

    @ExcelColumn(value = "年龄", required = true, precision = 2)
    private Integer age;

    @ExcelColumn(value = "次数", required = true, precision = 10)
    Long longN;

    @ExcelColumn(value = "小数", scale = 2)
    Float xiaoshu;

    @ExcelColumn(value = "实数", required = true, precision = 6, scale = 2)
    Double shishu;

    @ExcelColumn(value = "大数", required = true, precision = 16, scale = 4)
    BigDecimal dashu;

    @ExcelColumn(value = "生日", width = -1, format = "M月d日")
    private LocalDate birth;

    @ExcelColumn(value = "时间", width = 40, align = "center")
    private LocalDateTime time;

    public Person() {
//        dashu = new BigDecimal("89.12345");
//        birth = LocalDate.now();
//        time = LocalDateTime.now();
//        st = LocalDateTime.now();
    }

    Integer excelRow;
//    public Person setExcelRow(int row) {
//        return this;
//    }

    private String errMsg;
    public void setExcelBeanError(String msg) {
        if (errMsg == null) errMsg = msg;
    }
}
