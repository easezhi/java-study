package study.cnbm;

import easezhi.study.data.excel.ExcelParser;
import org.junit.Test;
import study.cnbm.dict.DictDetail;

import java.util.ArrayList;

public class GenSqlTest {
    @Test
    public void testDictSql() {
        var dict = new DictDetail("LAWSUIT_BIZ_TYPE", "案件细分类型");
        var details = new ArrayList<DictDetail>();
        details.add(new DictDetail("01", "民事诉讼"));

        System.out.println(CnbmUtil.genDictSql(dict, details));
    }

    @Test
    public void testDictSqlFromExcel() throws Exception {
        var file = "D:\\cnbm-work\\公共\\生成字典SQL.xlsx";
        var data = ExcelParser.parser(DictDetail.class).parseExcelFile(file);
        var dict = data.get(0);
        var details = data.subList(1, data.size());
        System.out.println(CnbmUtil.genDictSql(dict, details));
    }
}
