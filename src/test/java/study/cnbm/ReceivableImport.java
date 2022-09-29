package study.cnbm;

import easezhi.study.data.db.SqlUtil;
import easezhi.study.excel.ExcelParser;
import easezhi.study.io.FileUtil;
import org.junit.Test;
import study.cnbm.bean.SapReceivable;

import java.io.FileInputStream;
import java.util.HashMap;

// 2022-09 超期应收管理导入历史应收数据
public class ReceivableImport {
    @Test
    public void parseSql() throws Exception {
        var inFile = "D:\\cnbm-work\\诉讼管理\\历史应收数据-开发.xlsx";
        var companyMap = new HashMap<String, String>();
        companyMap.put("1000", "中建材信息技术股份有限公司");
        companyMap.put("1100", "中建材信云智联科技有限公司");
        companyMap.put("1200", "中建材信云智联科技有限公司北京分公司");
        companyMap.put("9100", "中建材信息技术（香港）有限公司");
        companyMap.put("2100", "博瑞夏信息技术（北京）有限公司");
        var arList = ExcelParser.parser(SapReceivable.class).parse(new FileInputStream(inFile));
        arList.forEach(ar -> ar.setCompanyName(companyMap.get(ar.getCompanyCode())));

        var sqlList = SqlUtil.buildInsertSql(arList, "receivable_order");
        var outFile = "D:\\cnbm-work\\诉讼管理\\历史应收数据-开发.sql";
        FileUtil.writeLinesToFile(outFile, sqlList);
        System.out.printf("写入%d行\n", sqlList.size());
//        System.out.println(sqlList);
    }
}

