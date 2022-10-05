package study.data.excel;

import com.alibaba.fastjson2.JSON;
import easezhi.study.data.excel.ExcelBuilder;
import easezhi.study.data.excel.ExcelColumnError;
import easezhi.study.data.excel.ExcelParser;
import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestExcel {

    @Test
    public void testParse() throws Exception {
        Map<ExcelColumnError.ErrorType, String> errorMap = new HashMap<>();
        errorMap.put(ExcelColumnError.ErrorType.REQUIRE, "必输");
        var file = "C:\\Users\\easezhi\\Downloads\\员工导入.xlsx";
        var parser = ExcelParser.parser(PersonExcel.class).setErrorMap(errorMap);
        var pers = parser.parse(new FileInputStream(file));
        var json = JSON.toJSON(pers);
        System.out.println(json);
    }

    @Test
    public void testExport() throws Exception {
        int total = 10000;
        List<PersonExcel> pers = new ArrayList<>(total);
        LocalDate d = LocalDate.now();
        LocalDateTime dt = LocalDateTime.now();
        for (int i = 0; i < total; i++) {
            if (i % 200 == 0) {
                d = LocalDate.now();
                dt = LocalDateTime.now();
            }
            var per = new PersonExcel();
            per.setName("张三" + i)
                .setSex(i % 2 == 0 ? "男" : "女")
                .setAge(i)
                .setLongN((long)(i * 2))
                .setDashu(new BigDecimal(i + "123.12"))
                .setBirth(d)
                .setTime(dt)
            ;
            pers.add(per);
        }
        String outPath = "C:\\Users\\easezhi\\Downloads\\导出.xlsx";
        var os = new FileOutputStream(outPath);
        System.out.println(LocalDateTime.now());
        new ExcelBuilder<PersonExcel>().init(PersonExcel.class).build(os, pers);
        os.close();
        System.out.println(LocalDateTime.now());
        System.out.println("ok");
    }
}

