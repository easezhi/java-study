package study.cnbm;

import easezhi.study.data.excel.ExcelParser;
import easezhi.study.io.FileUtil;
import easezhi.study.lang.StrUtil;
import org.junit.Test;
import study.cnbm.dict.DictDetail;

import java.util.ArrayList;

public class GenSqlTest {
    // 生成字典SQL
    @Test
    public void testDictSql() {
        var dict = new DictDetail("LAWSUIT_BIZ_TYPE", "案件细分类型");
        var details = new ArrayList<DictDetail>();
        details.add(new DictDetail("01", "民事诉讼"));

        System.out.println(CnbmUtil.genDictSql(dict, details));
    }

    // 通过Excel生成字典SQL
    @Test
    public void testDictSqlFromExcel() throws Exception {
        var file = "D:\\cnbm-work\\公共\\生成字典SQL.xlsx";
        var data = ExcelParser.parser(DictDetail.class).parseExcelFile(file);
        var dict = data.get(0);
        var details = data.subList(1, data.size());
        System.out.println(CnbmUtil.genDictSql(dict, details));
    }

    // 修改表结构时，把字段注释写在行尾，然后生成pgsql的字段注释语句
    @Test
    public void genPgsqlCommentTest() {
        var file = "D:\\easezhi\\document\\sql.sql";
        var lines = FileUtil.readLinesFromFile(file);
        var start = false;
        var tableName = "public.purchase_contract";
        var sql = "COMMENT ON COLUMN tableName.field IS 'comment';";
        for (var line: lines) {
            if (line.length() == 0) continue;
            var first = StrUtil.firstWord(line);
            if (!start) {
                if (first == null) continue;
                if (first.equals("CREATE")) {
                    start = true;
                }
                continue;
            } else {
                if (line.startsWith(")")) {
                    break;
                } else {
                    if (first == null || !StrUtil.isLowerCase(first)) continue;
                }
            }

            var segs = line.split("--");
            if (segs.length == 1) continue;
            var comment = segs[1].trim();
            System.out.println(sql.replace("tableName", tableName).replace("field", first).replace("comment", comment));
        }

    }
}
