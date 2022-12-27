package study.cnbm;

import easezhi.study.data.db.SqlBuilder;
import easezhi.study.data.excel.ExcelParser;
import easezhi.study.datastructure.CollectionUtil;
import easezhi.study.io.FileUtil;
import org.junit.Test;
import study.cnbm.bean.SalesOrder;
import study.cnbm.bean.SapReceivable;
import study.cnbm.bean.User;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// 2022-09 超期应收管理导入历史应收数据
public class ReceivableImport {

    String dir = "D:\\cnbm-work\\超期应收\\810数据\\";
    @Test
    public void parseSql() throws Exception {
        var companyMap = new HashMap<String, String>();
        companyMap.put("1000", "中建材信息技术股份有限公司");
        companyMap.put("1100", "中建材信云智联科技有限公司");
        companyMap.put("1200", "中建材信云智联科技有限公司北京分公司");
        companyMap.put("9100", "中建材信息技术（香港）有限公司");
        companyMap.put("2100", "博瑞夏信息技术（北京）有限公司");

        var arFile = dir + "业务应收.XLSX";
        var arList = ExcelParser.parser(SapReceivable.class).parse(new FileInputStream(arFile));
        arList.forEach(ar -> ar.setCompanyName(companyMap.get(ar.getCompanyCode())));

        var soFile = dir + "销售合同.xlsx";
        var soList = ExcelParser.parser(SalesOrder.class).parse(new FileInputStream(soFile));
        var soMap = new HashMap<String, SalesOrder>(soList.size());
        soList.forEach(so -> soMap.put(so.getContractNo(), so));
        arList.forEach(ar -> {
            var so = soMap.get(ar.getContractNo());
            if (so != null) {
                ar.setContractType(so.getOrderType());
                ar.setSigningDate(so.getSigningDate());
            }

            if (ar.getReceivableType() == null) {
                ar.setReceivableType("0"); // 普通应收
            }
        });

        var userFile = dir + "员工账号.xlsx";
        List<User> userList = ExcelParser.parser(User.class).parse(new FileInputStream(userFile));
        Map<String, String> userNameMap = CollectionUtil.toMap(userList, User::getName, User::getLogin);
        arList.forEach(ar -> {
            var login = userNameMap.get(ar.getSaleName());
            if (login != null) {
                ar.setSaleAccount(login);
                ar.setUpdateBy(login);
                ar.setCreateBy(login);
            }
        });

        var outFile = dir + "历史应收数据.sql";
        var sql = SqlBuilder.builder(SapReceivable.class, "receivable_order").buildBatchInsertSql(arList, 5000);
        FileUtil.writeStringToFile(outFile, sql.toString());
        System.out.printf("写入%d行\n", arList.size());
    }
}

