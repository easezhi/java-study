package study.cnbm.clrp;

import easezhi.study.data.db.SqlBuilder;
import easezhi.study.data.excel.ExcelParser;
import easezhi.study.io.FileUtil;
import org.junit.Test;
import study.cnbm.clrp.model.*;

import java.io.FileInputStream;
import java.util.List;

public class ClrpImport {
    int sqlBatch = 5000;

    @Test
    public void importPCSql() throws Exception {
        var inDir = "D:\\cnbm-work\\基石存储核心业务单据\\原始单据\\";
        var outDir = "D:\\cnbm-work\\基石存储核心业务单据\\360环境100条\\";
        var pcFile = inDir + "采购合同-360-100条.xlsx";
        List<PurchaseContract> pcList = ExcelParser.parser(PurchaseContract.class).parse(new FileInputStream(pcFile));
        pcList = pcList.subList(0, 10);

        // 采购合同核心表
        var sqlFile = outDir + "采购合同核心表.sql";
        var sql = SqlBuilder.builder(PurchaseContract.class, "public.purchase_contract").buildBatchInsertSql(pcList, sqlBatch);
        FileUtil.writeStringToFile(sqlFile, sql.toString());
        System.out.printf("写入采购合同%d行\n", pcList.size());

        List<ContractOrder> contractOrderList = pcList.stream().map(ContractMap::fromPurchaseContract).toList();
        buildClrpSql(contractOrderList, outDir, 3);
    }

    void buildClrpSql(List<ContractOrder> contractOrderList, String dir, int source) throws Exception {
        String orderName;
        if (source == 3) {
            orderName = "采购合同";
        } else {
            orderName = "销售合同";
        }

        // 寄出表
        List<PostLetter> plList = ContractMapper.INSTANCE.toPostLetter(contractOrderList);
        var plSqlFile = dir + orderName + "导入寄出.sql";
        var plSql = SqlBuilder.builder(PostLetter.class, "public.dailyoffice_post_letter")
            .buildBatchInsertSql(plList, sqlBatch);
        FileUtil.writeStringToFile(plSqlFile, plSql.toString());
        System.out.printf("%s写入寄出%d行\n", orderName, plList.size());

        // 收取表
        var rlSqlFile = dir + orderName + "导入收取.sql";
        var rlSql = SqlBuilder.builder(PostLetter.class, "public.dailyoffice_receive_letter")
            .buildBatchInsertSql(plList, sqlBatch);
        FileUtil.writeStringToFile(rlSqlFile, rlSql.toString());
        System.out.printf("%s写入收取%d行\n", orderName, plList.size());

        // 归档表
        List<FileArchive> faList = contractOrderList.stream()
            .map(ContractMap::contractToArchive).toList();
        var arSqlFile = dir + orderName + "导入归档.sql";
        var arSql = SqlBuilder.builder(FileArchive.class, "public.dailyoffice_file_archived")
            .buildBatchInsertSql(faList, sqlBatch);
        FileUtil.writeStringToFile(arSqlFile, arSql.toString());
        System.out.printf("%s写入归档%d行\n", orderName, plList.size());
    }
}
