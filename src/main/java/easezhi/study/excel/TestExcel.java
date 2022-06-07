package easezhi.study.excel;

import com.alibaba.fastjson.JSON;
import org.apache.poi.ss.util.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestExcel {
    public static void main( String[] args ) throws Exception {
        importPerson();
    }

    static void importPerson() throws Exception {
        Map<ExcelColumnError.ErrorType, String> errorMap = new HashMap<>();
        errorMap.put(ExcelColumnError.ErrorType.REQUIRE, "必输");
        var file = "C:\\Users\\easezhi\\Downloads\\员工导入.xlsx";
        var parser = new ExcelParser<Person>().init(Person.class).setErrorMap(errorMap);
        var pers = parser.parse(new FileInputStream(file));
        var json = JSON.toJSON(pers);
        System.out.println(json);
    }

    static void exportPerson() throws Exception {
        List<Person> pers = new ArrayList<>();
        pers.add(new Person());
        String outPath = "C:\\Users\\easezhi\\Downloads\\导出.xlsx";
        var os = new FileOutputStream(outPath);
        new ExcelBuilder<Person>().init(Person.class).build(os, pers);
        os.close();
        System.out.println("ok");
    }
}
